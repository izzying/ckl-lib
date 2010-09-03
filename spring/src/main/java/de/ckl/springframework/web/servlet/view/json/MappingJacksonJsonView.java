package de.ckl.springframework.web.servlet.view.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;

public class MappingJacksonJsonView extends
		org.springframework.web.servlet.view.json.MappingJacksonJsonView
{
	private List<String> ignoreAttributes = new ArrayList<String>();

	/**
	 * Filters out undesired attributes from the given model. The return value
	 * can be either another {@link Map}, or a single value object.
	 * 
	 * <p>
	 * Default implementation removes {@link BindingResult} instances and
	 * entries not included in the {@link #setRenderedAttributes(Set)
	 * renderedAttributes} property.
	 * 
	 * @param model
	 *            the model, as passed on to {@link #renderMergedOutputModel}
	 * @return the object to be rendered
	 */
	protected Object filterModel(Map<String, Object> model)
	{
		Map<String, Object> result = new HashMap<String, Object>(model.size());
		Set<String> renderedAttributes = !CollectionUtils
				.isEmpty(getRenderedAttributes()) ? getRenderedAttributes()
				: model.keySet();
		for (Map.Entry<String, Object> entry : model.entrySet())
		{
			if (!(entry.getValue() instanceof BindingResult)
					&& renderedAttributes.contains(entry.getKey())
					&& !ignoreAttributes.contains(entry.getKey()))
			{
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}

	public void setIgnoreAttributes(List<String> ignoreAttributes)
	{
		this.ignoreAttributes = ignoreAttributes;
	}

	public List<String> getIgnoreAttributes()
	{
		return ignoreAttributes;
	}
}
