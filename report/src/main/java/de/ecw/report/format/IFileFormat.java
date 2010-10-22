package de.ecw.report.format;

public interface IFileFormat
{
	/**
	 * Return extension of file without "."
	 * 
	 * @return
	 */
	public String getExtension();

	/**
	 * Returns Mime type of format
	 * 
	 * @return
	 */
	public String getMimeType();

}