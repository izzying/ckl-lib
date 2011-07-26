package de.ckl.testing.handler.types;

import java.lang.reflect.Field;

import org.apache.commons.lang.RandomStringUtils;

import de.ckl.testing.handler.FieldHandlerAdapter;
import de.ckl.testing.handler.annotation.CreatesUniqueValues;

/**
 * Creates a random string.
 * 
 * @author ckl
 */
@CreatesUniqueValues
public class StringHandler extends FieldHandlerAdapter {
	private long minLength = 0;
	private long maxLength = 1000;

	public boolean supports(Field _field) {
		return _field.getType().isAssignableFrom(String.class);
	}

	public int getPriority() {
		return PRIORITY_LOWEST;
	}

	@Override
	public Object createValue(Field _field) {
		long length = getMinLength()
				+ (long) (Math.random() * (getMaxLength() - getMinLength()) + 0.5);

		return RandomStringUtils.randomAlphanumeric((int) length);
	}

	public long getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(long maxLength) {
		this.maxLength = maxLength;
	}

	public long getMinLength() {
		return minLength;
	}

	public void setMinLength(long minLength) {
		this.minLength = minLength;
	}
}
