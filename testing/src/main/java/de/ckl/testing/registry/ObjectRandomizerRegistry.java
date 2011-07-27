package de.ckl.testing.registry;

import java.util.HashMap;
import java.util.Map;

import de.ckl.testing.ObjectRandomizer;
import de.ckl.testing.ObjectRandomizerFactory;
import de.ckl.testing.ObjectRandomizerFactoryImpl;
import de.ckl.testing.handler.IFieldHandler;

/**
 * Registry for every object randomizer.<br />
 * Every class for which new objects should be automatically generated is
 * registered in this registry. There can be only one {@link ObjectRandomizer}
 * for a given target class.
 * 
 * @author ckl
 * 
 */
public class ObjectRandomizerRegistry implements INewHandlerRegistered {
	/**
	 * target class to object randomizer mapping
	 */
	private Map<Class, ObjectRandomizer> mapClassToObjectRandomizer = new HashMap<Class, ObjectRandomizer>();

	/**
	 * registry with handlers
	 */
	private HandlerRegistry handlerRegistry = new HandlerRegistry();

	/**
	 * factory for new {@link ObjectRandomizer} instances
	 */
	private ObjectRandomizerFactory factory = new ObjectRandomizerFactoryImpl();

	/**
	 * Create a new {@link ObjectRandomizerRegistry}. This class is
	 * automatically registered for {@link HandlerRegistry} as
	 * {@link INewHandlerRegistered} callback.
	 */
	public ObjectRandomizerRegistry() {
		getHandlerRegistry().setOnNewHandlerRegistered(this);
	}

	/**
	 * Returns a new randomizer for a given target class. If no randomizer
	 * exists, a new one is created by {@link getFactory()}.
	 * 
	 * @param _clazz
	 * @return
	 */
	public ObjectRandomizer getRandomizer(Class _clazz) {
		if (!mapClassToObjectRandomizer.containsKey(_clazz)) {
			// create new object randomizer instance
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
		// target class specific field handler
		if (_clazz != null) {
			if (mapClassToObjectRandomizer.containsKey(_clazz)) {
				mapClassToObjectRandomizer.get(_clazz).getFieldHandlers()
						.add(fieldHandler);
				mapClassToObjectRandomizer.get(_clazz).dirtyContext();
			}
		} else {
			// global field handler
			for (ObjectRandomizer or : mapClassToObjectRandomizer.values()) {
				or.getFieldHandlers().add(fieldHandler);
				or.dirtyContext();
			}
		}
	}

	public ObjectRandomizerFactory getFactory() {
		return factory;
	}

	/**
	 * Sets a new {@link ObjectRandomizerRegistry}.
	 * {@link ObjectRandomizerFactoryImpl} is used as default.
	 * 
	 * @param factory
	 */
	public void setFactory(ObjectRandomizerFactory factory) {
		this.factory = factory;
	}

	public HandlerRegistry getHandlerRegistry() {
		return handlerRegistry;
	}

	/**
	 * Sets a new handler registry. {@link HandlerRegistry} is used as default.
	 * 
	 * @param handlerRegistry
	 */
	public void setHandlerRegistry(HandlerRegistry handlerRegistry) {
		this.handlerRegistry = handlerRegistry;
	}
}
