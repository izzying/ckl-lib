package de.ckl.report.service;

import java.io.File;

import de.ckl.report.model.IReportModel;

public interface IReportRepository
{
	public File findReport(IReportModel _reportModel);
}
