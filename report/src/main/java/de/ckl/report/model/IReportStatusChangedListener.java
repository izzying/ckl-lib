package de.ckl.report.model;



public interface IReportStatusChangedListener
{
	public void onChange(IReportModel reportModel, IReportModel.CREATION_STATUS _status);
}
