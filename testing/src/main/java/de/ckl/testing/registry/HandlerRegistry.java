package de.ckl.testing.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.ckl.testing.handler.IFieldHandler;

public class HandlerRegistry {
	private List<IFieldHandler> globalFieldHandlers = new ArrayList<IFieldHandler>();
	private Map<Class, List<IFieldHandler>> classSpecificFieldHandlers = new HashMap<Class, List<IFieldHandler>>();
	private INewHandlerRegistered onNewHandlerRegistered = null;

	public void register(IFieldHandler _fieldHandler) {
		globalFieldHandlers.add(_fieldHandler);

		if (onNewHandlerRegistered != null) {
			onNewHandlerRegistered.onRegister(_fieldHandler, null);
		}

	}

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

	public void setOnNewHandlerRegistered(
			INewHandlerRegistered onNewHandlerRegistered) {
		this.onNewHandlerRegistered = onNewHandlerRegistered;
	}
}
