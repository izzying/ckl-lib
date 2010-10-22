package de.ckl.report.type;


/**
 * Defines a type of report
 * 
 * @author ckl
 */
public class ReportType implements IReportType
{
    /**
     * unique hash of report
     */
    private String hash;

    /**
     * description of report
     */
    private String description;

    /**
     * Constructor
     * 
     * @param _hash
     * @param _description
     */
    public ReportType(String _hash, String _description)
    {
        setHash(_hash);
        setDescription(_description);
    }

    /**
     * Set description
     * 
     * @param description
     */
    public void setDescription(String _description)
    {
        this.description = _description;
    }

    /* (non-Javadoc)
	 * @see de.ecw.report.types.IReportType#getDescription()
	 */
    public String getDescription()
    {
        return description;
    }

    /**
     * Set unique hash
     * 
     * @param hash
     */
    public void setHash(String _hash)
    {
        this.hash = _hash;
    }

    /* (non-Javadoc)
	 * @see de.ecw.report.types.IReportType#getHash()
	 */
    public String getHash()
    {
        return hash;
    }
}
