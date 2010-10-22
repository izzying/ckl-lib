package de.ckl.report.format;


public class FileFormatAdapter implements IFileFormat
{
	public FileFormatAdapter(String mimeType, String extension)
	{
		setMimeType(mimeType);
		setExtension(extension);
	}

	/**
	 * mime type
	 */
	private String mimeType = "text/unknown";

	/**
	 * extension
	 */
	private String extension = "txt";

	public void setExtension(String extension)
	{
		this.extension = extension;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ecw.report.format.IReportFormat#getExtension()
	 */
	public String getExtension()
	{
		return extension;
	}

	public void setMimeType(String mimeType)
	{
		this.mimeType = mimeType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ecw.report.format.IReportFormat#getMimeType()
	 */
	public String getMimeType()
	{
		return mimeType;
	}

	public String toString()
	{
		return getExtension();
	}
}
