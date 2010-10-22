package de.ckl.report.service;

import java.util.List;

import de.ckl.report.exception.ReportException;
import de.ckl.report.model.IReportModel;

public class ReportServiceDelegator implements IReportService
{
	private List<IReportService> reportServices;

	@Override
	public void create(IReportModel _report) throws ReportException
	{
		for (IReportService service : getReportServices())
		{
			if (service.supports(_report))
			{
				service.create(_report);
				return;
			}
		}
	}

	@Override
	public boolean supports(IReportModel _report)
	{
		for (IReportService service : getReportServices())
		{
			if (service.supports(_report))
			{
				return true;
			}
		}

		return false;
	}

	public void setReportServices(List<IReportService> reportServices)
	{
		this.reportServices = reportServices;
	}

	public List<IReportService> getReportServices()
	{
		return reportServices;
	}
}
