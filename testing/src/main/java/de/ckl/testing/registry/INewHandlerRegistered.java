package de.ckl.testing.registry;

import de.ckl.testing.handler.IFieldHandler;

public interface INewHandlerRegistered {
	/**
	 * Will be executed if a new field handler is registered
	 * 
	 * @param fieldHandler
	 * @param _clazz
	 *            class for which the field handler was registered. Is null if
	 *            field handler was registered for all classes (global)
	 */
	public void onRegister(IFieldHandler fieldHandler, Class _clazz);
}
