package de.ckl.report.birt.task;

import java.sql.Connection;
import java.util.Map;

import org.eclipse.birt.report.data.oda.jdbc.IConnectionFactory;
import org.eclipse.birt.report.engine.api.IReportEngine;

import de.ckl.report.birt.model.IBirtReportModel;
import de.ckl.report.model.IReportModel;

/**
 * Default factory for new report task
 * 
 * @author ckl
 */
public class ReportTaskFactory implements IReportTaskFactory
{
	private Connection connection;

	private boolean closePassInConnection = false;

	private Map<Object, Object> mapDefaultContextMap = null;

	public ReportTask create(IReportEngine _reportEngine, IReportModel _report,
			String _targetReportPath, String _reportDesignPath)
	{
		ReportTask task = new ReportTask();

		passInDefaultContextMap(task.getContextMap());

		task.setReportDesignPath(_reportDesignPath);
		task.setReportEngine(_reportEngine);
		task.setReportModel(_report);
		task.setConnection(getConnection());

		task.getContextMap().put(IConnectionFactory.PASS_IN_CONNECTION,
				connection);
		task.getContextMap().put(IConnectionFactory.CLOSE_PASS_IN_CONNECTION,
				isClosePassInConnection());

		// set output options
		task.getRenderOptions().setOutputFileName(_targetReportPath);
		task.getRenderOptions().closeOutputStreamOnExit(true);

		if (_report.getReportFormat() instanceof IBirtReportModel)
		{
			((IBirtReportModel) _report.getReportFormat()).updateSettings(
					task.getContextMap(), task.getRenderOptions());
		}

		return task;
	}

	/**
	 * Copies all references from {@link #mapDefaultContextMap} to task context
	 * map
	 * 
	 * @param _contextMapTask
	 */
	private void passInDefaultContextMap(Map<Object, Object> _contextMapTask)
	{
		if (getMapDefaultContextMap() != null)
		{
			for (Object o : getMapDefaultContextMap().keySet())
			{
				_contextMapTask.put(o, getMapDefaultContextMap().get(o));
			}
		}
	}

	/**
	 * Sets a JDBC connection. If given, this connection will be injected into
	 * report
	 * 
	 * @param connection
	 */
	public void setConnection(Connection connection)
	{
		this.connection = connection;
	}

	public Connection getConnection()
	{
		return connection;
	}

	/**
	 * Sets parameter for closind passed in connection. Default value is false
	 * 
	 * @param closePassInConnection
	 */
	public void setClosePassInConnection(boolean closePassInConnection)
	{
		this.closePassInConnection = closePassInConnection;
	}

	public boolean isClosePassInConnection()
	{
		return closePassInConnection;
	}

	public void setMapDefaultContextMap(Map<Object, Object> mapDefaultContextMap)
	{
		this.mapDefaultContextMap = mapDefaultContextMap;
	}

	public Map<Object, Object> getMapDefaultContextMap()
	{
		return mapDefaultContextMap;
	}
}
