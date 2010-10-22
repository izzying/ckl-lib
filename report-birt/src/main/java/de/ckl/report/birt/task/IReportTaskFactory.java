package de.ckl.report.birt.task;

import org.eclipse.birt.report.engine.api.IReportEngine;

import de.ckl.report.model.IReportModel;

public interface IReportTaskFactory
{
	/**
	 * Creates a new report task
	 * 
	 * @param _reportEngine
	 * @param _report
	 * @param _targetReportPath
	 * @param _reportDesignPath
	 */
	public ReportTask create(IReportEngine _reportEngine, IReportModel _report,
			String _targetReportPath, String _reportDesignPath);

}
