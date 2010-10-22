package de.ecw.report.task;

import java.sql.Connection;
import java.util.HashMap;

import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.RenderOption;

import de.ecw.report.exception.ReportException;
import de.ecw.report.model.IReportModel;

/**
 * 
 * @author ckl
 */
public class ReportTask
{
	/**
	 * report engine reference
	 */
	private IReportEngine reportEngine;

	/**
	 * report model reference
	 */
	private IReportModel report;

	/**
	 * source path of report template
	 */
	private String reportDesignPath;

	private HashMap<Object, Object> contextMap = new HashMap<Object, Object>();

	private RenderOption renderOptions = new RenderOption();

	private String targetReportPath;

	/**
	 * JDBC connection
	 */
	protected Connection connection;

	public void run() throws ReportException
	{
		IReportRunnable design;

		try
		{
			// Open report design
			design = reportEngine.openReportDesign(reportDesignPath);
			// create task to run and render report
			IRunAndRenderTask task = reportEngine
					.createRunAndRenderTask(design);
			task.setAppContext(contextMap);
			// Read parameter values from report model and pass to BIRT task
			ReportTaskUtil.setParameterValues(task, report.getOptions());
			task.setRenderOption(renderOptions);

			// run report
			task.run();
			task.close();

			// set as created
			getReportModel().setIsCreated(true);
		}
		catch (Exception e)
		{
			// Falls Parameter fehlen, wird eine ParameterValidationException
			// geworfen, die ich aber nicht auffangen kann.
			throw new ReportException(
					"Could not create report. Please check for availability of report"
							+ reportDesignPath
							+ " and check all needed parameters inside the report.");
		}
	}

	public void setReportEngine(IReportEngine _reportEngine)
	{
		reportEngine = _reportEngine;
	}

	public IReportModel getReportModel()
	{
		return report;
	}

	public void setReportModel(IReportModel _report)
	{
		report = _report;
	}

	public void setReportDesignPath(String _reportDesignPath)
	{
		reportDesignPath = _reportDesignPath;
	}

	public void setConnection(Connection _conn)
	{
		connection = _conn;
	}

	public void setContextMap(HashMap<Object, Object> contextMap)
	{
		this.contextMap = contextMap;
	}

	public HashMap<Object, Object> getContextMap()
	{
		return contextMap;
	}

	public void setRenderOptions(RenderOption renderOptions)
	{
		this.renderOptions = renderOptions;
	}

	public RenderOption getRenderOptions()
	{
		return renderOptions;
	}

	public void setTargetReportPath(String targetReportPath)
	{
		this.targetReportPath = targetReportPath;
	}

	public String getTargetReportPath()
	{
		return targetReportPath;
	}
}
