package de.ckl.report.birt.service;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.birt.core.framework.IPlatformConfig;
import org.eclipse.birt.core.framework.IPlatformContext;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.core.framework.PlatformFileContext;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;

import de.ckl.report.birt.model.IBirtReportModel;
import de.ckl.report.birt.task.IReportTaskFactory;
import de.ckl.report.birt.task.ReportTask;
import de.ckl.report.exception.ReportException;
import de.ckl.report.model.IReportModel;
import de.ckl.report.service.IReportRepository;
import de.ckl.report.service.IReportService;

/**
 * Adapter class which capsulates the interaction between BIRT and the rest of
 * the world
 * 
 * @author ckl
 * 
 */
public class BirtReportService implements IReportService, IReportRepository
{
	private final static Logger log = Logger.getLogger(BirtReportService.class);

	public static final String RPTDESIGN_EXTENSION = ".rptdesign";

	/**
	 * Home directory of BIRT
	 */
	private String engineHome;

	/**
	 * Home directory of report design files (
	 */
	private String reportDesignDir;

	/**
	 * Home directory where new reports wilbe stored
	 */
	private String dataDir;

	/**
	 * Use absolute path for BIRT
	 */
	private boolean useAbsolutePathForBirt = false;

	/**
	 * BIRT engine configuration
	 */
	private final EngineConfig config = new EngineConfig();

	/**
	 * BIRT engine
	 */
	private IReportEngine birtEngine = null;

	/**
	 * platform context
	 */
	private IPlatformContext platformContext = null;

	/**
	 * Logging directory
	 */
	private String logDir;

	private IReportTaskFactory reportTaskFactory;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ecw.zabos.report.IReportService#init()
	 */
	public void init() throws ReportException
	{
		try
		{
			if (getReportTaskFactory() == null)
			{
				throw new ReportException(
						"ReportService [taskFactory]: no task factory specified");
			}

			log.debug("ReportService [engineHome]: " + engineHome);
			log.debug("ReportService [designDir]: " + reportDesignDir);
			log.debug("ReportService [dataDir]: " + dataDir);
			log.debug("ReportService [logDir]: " + getLogDir());

			if (!new File(dataDir).exists())
			{
				throw new ReportException(
						"ReportService [dataDir]: directory does not exist");
			}

			log.debug("ReportService [use absolute path]: "
					+ useAbsolutePathForBirt);

			initPathes();

			log.debug("ReportService [engineHome]: " + engineHome);

			if (getLogDir() != null)
			{
				config.setLogConfig(getLogDir(), java.util.logging.Level.ALL);
			}

			if (!new File(engineHome).exists())
			{
				throw new ReportException(
						"ReportService [engineHome]: directory does not exist");
			}

			log.debug("ReportService [designDir]: " + reportDesignDir);

			if (!(new File(reportDesignDir).exists()))
			{
				throw new ReportException(
						"ReportService [designDir]: directory does not exist");
			}

			initPlatformContext();

			config.setPlatformContext(getPlatformContext());
			Platform.startup(config);
			final IReportEngineFactory factory = (IReportEngineFactory) Platform
					.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);

			if (factory == null)
			{
				throw new ReportException(
						"ReportEngine: could not create factory");
			}

			birtEngine = factory.createReportEngine(config);
		}
		catch (Exception e)
		{
			throw new ReportException("ReportEngine: " + e.getMessage());
		}

		log.info("ReportService started");
	}

	/**
	 * Initialzes platform configuration
	 */
	protected void initPlatformContext()
	{
		System.setProperty(IPlatformConfig.BIRT_HOME, engineHome);
		setPlatformContext(new PlatformFileContext());
	}

	/**
	 * initalizes path
	 */
	protected void initPathes()
	{

	}

	public File findReport(IReportModel _report)
	{
		return new File(getDataDir() + "/" + _report.getReportUid() + "."
				+ _report.getReportFormat().getExtension());
	}

	public void setEngineHome(String engineHome)
	{
		this.engineHome = engineHome;
	}

	public String getEngineHome()
	{
		return engineHome;
	}

	public void setReportDesignDir(String designDir)
	{
		this.reportDesignDir = designDir;
	}

	public String getReportDesignDir()
	{
		return reportDesignDir;
	}

	public void setDataDir(String dataDir)
	{
		this.dataDir = dataDir;
	}

	public String getDataDir()
	{
		return dataDir;
	}

	public void setUseAbsolutePathForBirt(boolean useAbsolutePathForBirt)
	{
		this.useAbsolutePathForBirt = useAbsolutePathForBirt;
	}

	public synchronized void destroy()
	{
		birtEngine.destroy();
		Platform.shutdown();

		birtEngine = null;

		log.debug("ReportService stopped");
	}

	public boolean isUseAbsolutePathForBirt()
	{
		return useAbsolutePathForBirt;
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}

	public boolean supports(IReportModel _report)
	{
		return (_report instanceof IBirtReportModel);
	}

	public void create(IReportModel _report) throws ReportException
	{
		// UID muss gesetzt werden, da darüber der Report-Name definiert wird
		if (_report.getReportUid() == null || _report.getReportUid().equals(""))
		{
			throw new ReportException("no UID for report available");
		}

		try
		{
			log.debug("Creating new report: " + _report.toString());

			ReportTask task = getReportTaskFactory().create(birtEngine,
					_report, findReport(_report).getAbsolutePath(),
					resolveReportDesignPath(_report));

			// TODO Threading not implemented yet
			task.run();

			log.debug("report '" + _report.toString() + "' was created");
		}
		catch (ReportException e)
		{
			throw e;
		}
	}

	/**
	 * Set platform context
	 * 
	 * @param platformContext
	 */
	public void setPlatformContext(IPlatformContext platformContext)
	{
		this.platformContext = platformContext;
	}

	/**
	 * Get platform context
	 * 
	 * @return
	 */
	public IPlatformContext getPlatformContext()
	{
		return platformContext;
	}

	/**
	 * Resolves the complete path of a {@link IReportModel} object
	 * 
	 * @param the
	 *            reportModel
	 */
	public String resolveReportDesignPath(IReportModel _report)
	{
		return getReportDesignDir() + "/" + _report.getReportType().getHash()
				+ RPTDESIGN_EXTENSION;
	}

	/**
	 * Set logging directory
	 * 
	 * @param logDir
	 */
	public void setLogDir(String logDir)
	{
		this.logDir = logDir;
	}

	/**
	 * Get logging directory
	 * 
	 * @return
	 */
	public String getLogDir()
	{
		return logDir;
	}

	public void setReportTaskFactory(IReportTaskFactory reportTaskFactory)
	{
		this.reportTaskFactory = reportTaskFactory;
	}

	public IReportTaskFactory getReportTaskFactory()
	{
		return reportTaskFactory;
	}
}
