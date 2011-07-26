package de.ckl.testing.registry;

import de.ckl.testing.handler.IFieldHandler;

public interface INewHandlerRegistered {
	public void onRegister(IFieldHandler fieldHandler, Class _clazz);
}
