package de.ckl.testing.handler;

import java.lang.reflect.Field;

public interface ICreateValue {

	/**
	 * Create a new value for field
	 * 
	 * @param _field
	 * @return
	 */
	public Object createValue(Field _field);
}
