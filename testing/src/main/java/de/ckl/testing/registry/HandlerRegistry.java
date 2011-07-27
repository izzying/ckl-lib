package de.ckl.testing.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.ckl.testing.ObjectRandomizer;
import de.ckl.testing.handler.IFieldHandler;

/**
 * This registry contains all registered field handlers. instances of
 * {@link ObjectRandomizer} can be informed by callback.
 * 
 * @author ckl
 * 
 */
public class HandlerRegistry {
	/**
	 * contains field handlers which should be available to all classes
	 */
	private List<IFieldHandler> globalFieldHandlers = new ArrayList<IFieldHandler>();

	/**
	 * contains field handlers which are only available to specific classes
	 */
	private Map<Class, List<IFieldHandler>> classSpecificFieldHandlers = new HashMap<Class, List<IFieldHandler>>();

	/**
	 * callback if a new field handler is registered
	 */
	private INewHandlerRegistered onNewHandlerRegistered = null;

	/**
	 * Register a new field handler which is available to all target classes.
	 * 
	 * @param _fieldHandler
	 */
	public void register(IFieldHandler _fieldHandler) {
		globalFieldHandlers.add(_fieldHandler);

		if (onNewHandlerRegistered != null) {
			onNewHandlerRegistered.onRegister(_fieldHandler, null);
		}

	}

	/**
	 * Register a new field handler for given class. The field handler is only
	 * available for this class. There can be more than one field handler for
	 * one target class.
	 * 
	 * @param _clazz
	 * @param _fieldHandler
	 */
	public void register(Class _clazz, IFieldHandler _fieldHandler) {
		if (!classSpecificFieldHandlers.containsKey(_clazz)) {
			classSpecificFieldHandlers.put(_clazz,
					new ArrayList<IFieldHandler>());
		}

		classSpecificFieldHandlers.get(_clazz).add(_fieldHandler);

		if (onNewHandlerRegistered != null) {
			onNewHandlerRegistered.onRegister(_fieldHandler, _clazz);
		}
	}

	/**
	 * Returns all field handlers for given class. Merges global handlers and
	 * specific handlers!
	 * 
	 * @param _clazz
	 * @return
	 */
	public List<IFieldHandler> getFieldHandlersForClass(Class _clazz) {
		List<IFieldHandler> r = new ArrayList<IFieldHandler>();
		r.addAll(globalFieldHandlers);

		if (classSpecificFieldHandlers.containsKey(_clazz)) {
			r.addAll(classSpecificFieldHandlers.get(_clazz));
		}

		return r;
	}

	public INewHandlerRegistered getOnNewHandlerRegistered() {
		return onNewHandlerRegistered;
	}

	/**
	 * Set callback which should be executed if a new field handler is
	 * registered
	 * 
	 * @param onNewHandlerRegistered
	 */
	public void setOnNewHandlerRegistered(
			INewHandlerRegistered onNewHandlerRegistered) {
		this.onNewHandlerRegistered = onNewHandlerRegistered;
	}
}
