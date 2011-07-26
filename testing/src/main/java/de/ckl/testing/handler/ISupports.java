package de.ckl.testing.handler;

import java.lang.reflect.Field;

public interface ISupports {

	/**
	 * Returns true if field handler has support for given field type
	 * 
	 * @param _class
	 * @return
	 */
	public boolean supports(Field _field);
}
