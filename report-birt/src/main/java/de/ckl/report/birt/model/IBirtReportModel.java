package de.ckl.report.birt.model;

import java.util.Map;

import org.eclipse.birt.report.engine.api.RenderOption;

public interface IBirtReportModel
{
	/**
	 * Update report creation setting depending on given report format
	 * 
	 * @param contextMap
	 * @param _renderOptions
	 */
	public void updateSettings(Map<Object, Object> contextMap,
			RenderOption _renderOptions);
}
