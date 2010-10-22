package de.ckl.report.service;

import javax.servlet.ServletContext;

import org.eclipse.birt.core.framework.PlatformServletContext;
import org.springframework.web.context.ServletContextAware;

import de.ecw.report.service.ReportService;

/**
 * This class must be used if running BIRT subsystem in a servlet container like
 * Tomcat. <br />
 * This class is Springified and depends on spring-mvc.jar.<br />
 * You can use the following bean configuration:
 * 
 * <pre>
 * 	<bean name="reportService" id="reportService"
 * 		class="de.ckl.report.service.WebReportService" init-method="init">
 * 		<property name="dataDir" value="c:/temp/reports" />
 * 		<property name="engineHome" value="WEB-INF/platform" />
 * 		<property name="reportDesignDir" value="WEB-INF/reports" />
 * 		<property name="connection" ref="jdbcConnection" />
 * 	</bean>
 * </pre>
 * 
 * @author ckl
 * 
 */
public class WebReportService extends ReportService implements
		ServletContextAware
{
	private ServletContext servletContext = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ecw.report.service.ReportServiceAdapter#initPlatformContext()
	 */
	protected void initPlatformContext()
	{
		if (servletContext != null)
		{
			setPlatformContext(new PlatformServletContext(servletContext));
		}
		else
		{
			super.initPlatformContext();
		}

	}

	/**
	 * Initalizes pathes; set the absolute path to
	 * {@link #setEngineHome(String)} and {@link #setReportDesignDir(String)}.
	 */
	protected void initPathes()
	{
		if (!isUseAbsolutePathForBirt() && (servletContext != null))
		{
			setEngineHome(servletContext.getRealPath(getEngineHome()));
			setReportDesignDir(servletContext.getRealPath(getReportDesignDir()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.context.ServletContextAware#setServletContext
	 * (javax.servlet.ServletContext)
	 */
	public void setServletContext(ServletContext _sc)
	{
		servletContext = _sc;
	}
}
