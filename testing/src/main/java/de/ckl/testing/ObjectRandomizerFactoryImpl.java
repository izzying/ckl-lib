package de.ckl.testing;

/**
 * Default implementation for {@link ObjectRandomizerTemplate}
 * 
 * @author ckl
 * 
 */
public class ObjectRandomizerFactoryImpl implements ObjectRandomizerFactory {
	public ObjectRandomizer create(Class _clazz) {
		return new ObjectRandomizerTemplate(_clazz);
	}
}
