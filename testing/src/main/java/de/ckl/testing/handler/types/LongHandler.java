package de.ckl.testing.handler.types;

import java.lang.reflect.Field;

import de.ckl.testing.handler.annotation.CreatesUniqueValues;

/**
 * Creates a new long, supports long and {@link Long}
 * 
 * @author ckl
 * 
 */
@CreatesUniqueValues
public class LongHandler extends IntegerHandler {
	@Override
	public boolean supports(Field _field) {
		return (_field.getType().isAssignableFrom(long.class) || _field
				.getType().isAssignableFrom(Long.class));
	}
}
