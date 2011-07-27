package de.ckl.testing.validation.handler.types;

import java.lang.reflect.Field;

import javax.validation.constraints.Size;

import de.ckl.testing.handler.types.StringHandler;

/**
 * This handlers extracts the min/max parameters from {@link Size} annotation
 * 
 * @author ckl
 * 
 */
public class SizeAnnotationHandler extends StringHandler {
	public boolean supports(Field _field) {
		if (_field.isAnnotationPresent(Size.class)) {
			int max = _field.getAnnotation(Size.class).max();

			if (max != Integer.MAX_VALUE) {
				setMaxLength(max);
			}

			setMinLength(_field.getAnnotation(Size.class).min());

			return true;
		}

		return false;
	}

	public int getPriority() {
		return PRIORITY_HIGH;
	}
}
