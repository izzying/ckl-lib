package de.ckl.testing.registry;

import java.util.HashMap;
import java.util.Map;

import de.ckl.testing.ObjectRandomizer;
import de.ckl.testing.ObjectRandomizerFactory;
import de.ckl.testing.handler.IFieldHandler;

public class ObjectRandomizerRegistry implements INewHandlerRegistered {
	private Map<Class, ObjectRandomizer> mapClassToObjectRandomizer = new HashMap<Class, ObjectRandomizer>();
	private HandlerRegistry handlerRegistry = new HandlerRegistry();
	private ObjectRandomizerFactory factory = new ObjectRandomizerFactory();

	public ObjectRandomizerRegistry() {
		getHandlerRegistry().setOnNewHandlerRegistered(this);
	}

	/**
	 * Returns a new randomizer for given class. If no randomizer exists, a new
	 * one is created
	 * 
	 * @param _clazz
	 * @return
	 */
	public ObjectRandomizer getRandomizer(Class _clazz) {
		if (!mapClassToObjectRandomizer.containsKey(_clazz)) {
			ObjectRandomizer objectRandomizer = factory.create(_clazz);
			// assign all registered field handlers to new created object
			// randomizer
			objectRandomizer.getFieldHandlers().addAll(
					getHandlerRegistry().getFieldHandlersForClass(_clazz));
			mapClassToObjectRandomizer.put(_clazz, objectRandomizer);
		}

		return mapClassToObjectRandomizer.get(_clazz);
	}

	@Override
	public void onRegister(IFieldHandler fieldHandler, Class _clazz) {
		if (_clazz != null) {
			if (mapClassToObjectRandomizer.containsKey(_clazz)) {
				mapClassToObjectRandomizer.get(_clazz).getFieldHandlers()
						.add(fieldHandler);
				mapClassToObjectRandomizer.get(_clazz).dirtyContext();
			}
		} else {
			for (ObjectRandomizer or : mapClassToObjectRandomizer.values()) {
				or.getFieldHandlers().add(fieldHandler);
				or.dirtyContext();
			}
		}
	}

	public ObjectRandomizerFactory getFactory() {
		return factory;
	}

	public void setFactory(ObjectRandomizerFactory factory) {
		this.factory = factory;
	}

	public HandlerRegistry getHandlerRegistry() {
		return handlerRegistry;
	}

	public void setHandlerRegistry(HandlerRegistry handlerRegistry) {
		this.handlerRegistry = handlerRegistry;
	}
}
