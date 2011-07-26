package de.ckl.testing;

public class ObjectRandomizerFactory {
	public ObjectRandomizer create(Class _clazz) {
		return new ObjectRandomizerTemplate(_clazz);
	}
}
