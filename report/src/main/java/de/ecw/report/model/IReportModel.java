package de.ecw.report.model;

import java.util.Map;

import de.ecw.report.format.IFileFormat;
import de.ecw.report.type.IReportType;

/**
 * Interface for models from which a report will be generated
 * 
 * @author ckl
 * 
 */
public interface IReportModel
{
	/**
	 * Return report format (CSV, PDF etc.) report to generate
	 * 
	 * @return
	 */
	public IFileFormat getReportFormat();

	/**
	 * Return type of report (e.g. bilancing, statistic)
	 * 
	 * @return
	 */
	public IReportType getReportType();

	/**
	 * Return specific options which will be passed to the report and can be
	 * accessed inside BIRT.
	 * 
	 * @return
	 */
	public Map<String, String> getOptions();

	/**
	 * Returns an UID under which the report can be unique identified.
	 * 
	 * @return
	 */
	public String getReportUid();

	/**
	 * Sets whether the report is already created or not
	 * 
	 * @param _isCreated
	 */
	public void setIsCreated(boolean _isCreated);
}
