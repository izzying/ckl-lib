package de.ckl.testing.handler.types;

import java.lang.reflect.Field;

import de.ckl.testing.handler.FieldHandlerAdapter;
import de.ckl.testing.handler.annotation.CreatesUniqueValues;

/**
 * Creates a new integer value. Supports int and {@link Integer}
 * 
 * @author ckl
 * 
 */
@CreatesUniqueValues
public class IntegerHandler extends FieldHandlerAdapter {
	private int minNumber = Integer.MIN_VALUE;
	private int maxNumber = Integer.MAX_VALUE;

	@Override
	public boolean supports(Field _field) {
		return (_field.getType().isAssignableFrom(int.class) || _field
				.getType().isAssignableFrom(Integer.class));
	}

	@Override
	public Object createValue(Field _field) {
		return getMinNumber()
				+ (int) (Math.random() * (getMaxNumber() - getMinNumber()) + 0.5);
	}

	public int getMaxNumber() {
		return maxNumber;
	}

	public void setMaxNumber(int maxNumber) {
		this.maxNumber = maxNumber;
	}

	public int getMinNumber() {
		return minNumber;
	}

	public void setMinNumber(int minNumber) {
		this.minNumber = minNumber;
	}

}
