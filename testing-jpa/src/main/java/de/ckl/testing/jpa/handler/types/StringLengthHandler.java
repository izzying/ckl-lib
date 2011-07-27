package de.ckl.testing.jpa.handler.types;

import java.lang.reflect.Field;

import javax.persistence.Column;

import de.ckl.testing.handler.types.StringHandler;

/**
 * Supports attribute length of {@link Column} annotation
 * 
 * @author ckl
 * 
 */
public class StringLengthHandler extends StringHandler {
	public boolean supports(Field _field) {
		if (_field.getType().isAssignableFrom(String.class)) {
			long maxLength = 0;

			if (_field.isAnnotationPresent(Column.class)) {
				maxLength = _field.getAnnotation(Column.class).length();
			}

			if (maxLength != 0) {
				setMaxLength(maxLength);
				return true;
			}
		}

		return false;
	}

	public int getPriority() {
		return PRIORITY_NORMAL;
	}
}
