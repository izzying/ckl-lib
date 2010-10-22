package de.ckl.report.service;

import java.io.File;

import de.ckl.report.model.IReportModel;

public class ReportRepository extends ReportServiceDelegator implements
		IReportRepository
{

	@Override
	public File findReport(IReportModel _reportModel)
	{
		File r = null;
		for (IReportService service : getReportServices())
		{
			if (service instanceof IReportRepository)
			{
				if (service.supports(_reportModel))
				{
					r = ((IReportRepository) service).findReport(_reportModel);

					if (r != null)
					{
						break;
					}
				}
			}
		}

		return r;
	}
}
