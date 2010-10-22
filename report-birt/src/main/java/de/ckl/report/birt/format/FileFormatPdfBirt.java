package de.ckl.report.birt.format;

import java.util.Map;

import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;

import de.ckl.report.birt.model.IBirtReportModel;

public class FileFormatPdfBirt extends de.ckl.report.format.FileFormatPdf
		implements IBirtReportModel
{
	public FileFormatPdfBirt()
	{
		super();
	}

	public void updateSettings(Map<Object, Object> contextMap,
			RenderOption _renderOptions)
	{
		contextMap.put(EngineConstants.APPCONTEXT_PDF_RENDER_CONTEXT,
				_renderOptions);
		_renderOptions.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_PDF);
	}
}
