package de.ckl.testing.jpa;
import de.ckl.testing.ObjectRandomizer;
import de.ckl.testing.ObjectRandomizerFactory;


public class JpaObjectRandomizerFactoryImpl implements ObjectRandomizerFactory {
	public ObjectRandomizer create(Class _clazz) {
		return new JpaObjectRandomizerTemplate(_clazz);
	}
}
