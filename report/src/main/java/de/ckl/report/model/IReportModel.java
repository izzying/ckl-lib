package de.ckl.report.model;

import java.util.Map;

import de.ckl.report.format.IFileFormat;
import de.ckl.report.type.IReportType;

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
	 * accessed inside BIRT or Apache FO
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

	enum CREATION_STATUS
	{
		NOT_STARTED, STARTED, FINISHED, FAILED
	}

	/**
	 * Sets status of reoprt
	 * 
	 * @param _status
	 */
	public void setCreationStatus(CREATION_STATUS _status);

	public CREATION_STATUS getCreationStatus();
}
