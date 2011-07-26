package de.ckl.testing.handler.types;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

import de.ckl.testing.handler.FieldHandlerAdapter;

/**
 * Returns a new date object
 * 
 * @author ckl
 */
public class DateHandler extends FieldHandlerAdapter {

	private long milisecondsToAdd = 0;

	public long getMilisecondsToAdd() {
		return milisecondsToAdd;
	}

	public void setMilisecondsToAdd(long milisecondsToAdd) {
		this.milisecondsToAdd = milisecondsToAdd;
	}

	@Override
	public boolean supports(Field _field) {
		return _field.getType().isAssignableFrom(Date.class);
	}

	/**
	 * Which date type should be generated. Only {@link DateOption#CURRENT}
	 * currently available.
	 * 
	 */
	public static enum DateOption {
		CURRENT, PAST, FUTURE
	}

	@Override
	public Object createValue(Field _field) {
		long milis = Calendar.getInstance().getTimeInMillis();
		milis += getMilisecondsToAdd();
		Calendar cal = Calendar.getInstance();

		cal.setTimeInMillis(milis);

		return cal.getTime();
	}

}
