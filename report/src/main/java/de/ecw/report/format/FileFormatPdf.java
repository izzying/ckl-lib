package de.ecw.report.format;

import java.util.Map;

import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;

import de.ecw.report.task.IBirtMimeTypeInjector;

public class FileFormatPdf extends FileFormatAdapter implements
		IBirtMimeTypeInjector
{
	public FileFormatPdf()
	{
		super("application/pdf", "pdf");
	}

	public void updateSettings(Map<Object, Object> contextMap,
			RenderOption _renderOptions)
	{
		contextMap.put(EngineConstants.APPCONTEXT_PDF_RENDER_CONTEXT,
				_renderOptions);
		_renderOptions.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_PDF);
	}
}
