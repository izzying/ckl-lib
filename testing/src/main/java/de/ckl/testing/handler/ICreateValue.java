package de.ckl.testing.handler;

import java.lang.reflect.Field;

public interface ICreateValue {

	/**
	 * Creates a new value for give field
	 * 
	 * @param _field
	 * @return
	 */
	public Object createValue(Field _field);
}
