package de.ckl.report.service;

import de.ckl.report.exception.ReportException;
import de.ckl.report.model.IReportModel;

public interface IReportService
{
	/**
	 * Creates a new report
	 * 
	 * @param _report
	 * @throws ReportException
	 */
	public void create(IReportModel _report) throws ReportException;

	/**
	 * returns true if report service can handle this model
	 * 
	 * @param _report
	 * @return
	 */
	public boolean supports(IReportModel _report);
}
