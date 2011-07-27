package de.ckl.springframework.web.servlet.helper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import de.ckl.lang.util.MapBuilder;

/**
 * This helper class defines a layout file which can be used for defining a
 * standard HTML layout.
 * 
 * @author ckl
 */
public class LayoutHelper {
	Logger log = Logger.getLogger(LayoutHelper.class);

	/**
	 * Prefix path to content JSP files
	 */
	private String prefix = null;

	/**
	 * Suffix for the physical content file
	 */
	private String suffix = ".jsp";

	/**
	 * This key will automatically put into the model
	 */
	private String contentKey = "body";

	/**
	 * Path to layout file
	 */
	private String layoutView = "layout";

	/**
	 * HTTP Parameter for disabling layout
	 */
	private String disableLayoutRequestParameter = "noLayout";

	/**
	 * allow disable of layout integration per request
	 */
	private boolean allowDisableLayoutForRequest = true;

	/**
	 * If {@link #xmlMarshallingModelKey} is not null, the object in _models
	 * will be automatically mapped to this key.
	 */
	private String xmlMarshallingModelKey;

	/**
	 * Builds a new {@link ModelAndView} object
	 * 
	 * @param _contentView
	 *            The logical path of given view. If {{@link #getPrefix()} is
	 *            not null, the prefix will be put BEFORE the _contentView path;
	 *            if {@link #getSuffix()} is not null, the suffix will be put
	 *            AFTER the _contentView path
	 * @param _models
	 *            The models will be injected into the view (delegates to
	 *            {@link ModelAndView#addAllObjects(Map)})
	 * @param _mainModel
	 *            If _mainModel and {@link #xmlMarshallingModelKey} is not null,
	 *            a new entry for _models parameter will be added. Key is
	 *            {@link #xmlMarshallingModelKey} and value _mainModel.
	 * @param _ommitPrefix
	 *            If true, the prefix will be skipped
	 * @param _ommitSuffix
	 *            If true, the suffix will be skipped
	 * @return
	 */
	public ModelAndView buildModelAndView(String _contentView,
			Map<String, Object> _models, Object _mainModel,
			boolean _ommitPrefix, boolean _ommitSuffix) {
		if (_models == null) {
			_models = new HashMap<String, Object>();
		}

		if (!_ommitPrefix && getPrefix() != null) {
			_contentView = getPrefix() + _contentView;
		}

		String jspFileToServe = null;

		if (isAllowDisableLayoutForRequest()) {
			RequestAttributes requestAttributes = RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = null;

			if (requestAttributes instanceof ServletRequestAttributes) {
				request = ((ServletRequestAttributes) requestAttributes)
						.getRequest();

				if (request.getParameter(getDisableLayoutRequestParameter()) != null) {
					jspFileToServe = _contentView;
					log.debug("Skipping layout and serving main view ["
							+ jspFileToServe + "]");
				}
			}
		}

		if (jspFileToServe == null) {
			if (!_ommitSuffix && getSuffix() != null) {
				_contentView += getSuffix();
			}

			jspFileToServe = getLayoutView();

			log.debug("Adding content key [" + contentKey
					+ "] with physical view [" + _contentView + "]");

			_models.put(contentKey, _contentView);
		}

		if (_mainModel != null
				&& (getXmlMarshallingModelKey() != null && getXmlMarshallingModelKey()
						.length() > 0)) {
			log.debug("Adding XML marshalling key ["
					+ getXmlMarshallingModelKey() + "] to model [" + _mainModel
					+ "]");
			_models.put(getXmlMarshallingModelKey(), _mainModel);
		}

		ModelAndView r = new ModelAndView(jspFileToServe, _models);

		return r;
	}

	/**
	 * Delegates to
	 * {@link #buildModelAndView(String, Map, Object, boolean, boolean)} with
	 * empty parameters
	 * 
	 * @param _contentView
	 * @return
	 */
	public ModelAndView buildModelAndView(String _contentView) {
		return buildModelAndView(_contentView, null, null, false, false);
	}

	public ModelAndView buildModelAndView(String _contentView,
			MapBuilder<String> _mapBuilder) {
		return buildModelAndView(_contentView, _mapBuilder.map());
	}

	/**
	 * Delegates to
	 * {@link #buildModelAndView(String, Map, Object, boolean, boolean)} if
	 * _models is not null. The first entry of _models is passed as
	 * {@link Object} parameter.<br />
	 * Otherwise it will delegate to {@link #buildModelAndView(String)}
	 * 
	 * @param _contentView
	 * @param _models
	 * @return
	 */
	public ModelAndView buildModelAndView(String _contentView,
			Map<String, Object> _models) {
		Collection<Object> cObjects = _models.values();
		Object[] objects = cObjects.toArray();

		if (objects != null && objects.length > 0) {
			return buildModelAndView(_contentView, _models, objects[0]);
		}

		return buildModelAndView(_contentView);
	}

	public ModelAndView buildModelAndView(String _contentView,
			Map<String, Object> _models, Object _mainModel) {
		return buildModelAndView(_contentView, _models, _mainModel, false,
				false);
	}

	public ModelAndView buildModelAndView(String _contentView,
			MapBuilder<String> _mapBuilder, Object _mainModel) {
		return buildModelAndView(_contentView, _mapBuilder.map(), _mainModel);
	}

	public void setContentKey(String contentKey) {
		this.contentKey = contentKey;
	}

	public String getContentKey() {
		return contentKey;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setLayoutView(String layoutView) {
		this.layoutView = layoutView;
	}

	public String getLayoutView() {
		return layoutView;
	}

	public void setXmlMarshallingModelKey(String xmlMarshallingModelKey) {
		this.xmlMarshallingModelKey = xmlMarshallingModelKey;
	}

	public String getXmlMarshallingModelKey() {
		return xmlMarshallingModelKey;
	}

	public void setAllowDisableLayoutForRequest(
			boolean allowDisableLayoutForRequest) {
		this.allowDisableLayoutForRequest = allowDisableLayoutForRequest;
	}

	public boolean isAllowDisableLayoutForRequest() {
		return allowDisableLayoutForRequest;
	}

	public void setDisableLayoutRequestParameter(
			String disableLayoutRequestParameter) {
		this.disableLayoutRequestParameter = disableLayoutRequestParameter;
	}

	public String getDisableLayoutRequestParameter() {
		return disableLayoutRequestParameter;
	}

}
