package de.ckl.testing.handler.springmodules.validation;

import java.lang.reflect.Field;

import org.springmodules.validation.bean.conf.loader.annotation.handler.Length;
import org.springmodules.validation.bean.conf.loader.annotation.handler.MaxLength;
import org.springmodules.validation.bean.conf.loader.annotation.handler.MinLength;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;

import de.ckl.testing.handler.types.StringHandler;

/**
 * Has support for Strings annotated with {@link Length}, {@link MaxLength},
 * {@link MinLength} and {@link NotBlank}
 * 
 * @author ckl
 * 
 */
public class StringLengthHandler extends StringHandler {
	public boolean supports(Field _field) {
		if (_field.getType().isAssignableFrom(String.class)) {
			long maxLength = 0, minLength = 0;

			if (_field.isAnnotationPresent(NotBlank.class)) {
				minLength = 1;
			}

			if (_field.isAnnotationPresent(Length.class)) {
				maxLength = _field.getAnnotation(Length.class).max();
				minLength = _field.getAnnotation(Length.class).min();
			}

			if (_field.isAnnotationPresent(MinLength.class)) {
				minLength = _field.getAnnotation(MinLength.class).value();
			}

			if (_field.isAnnotationPresent(MaxLength.class)) {
				maxLength = _field.getAnnotation(MaxLength.class).value();
			}

			if (maxLength != 0 || minLength != 0) {
				setMaxLength(maxLength);
				setMinLength(minLength);
				return true;
			}
		}

		return false;
	}

	public int getPriority() {
		return PRIORITY_NORMAL;
	}
}
