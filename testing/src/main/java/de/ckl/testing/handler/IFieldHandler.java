package de.ckl.testing.handler;

import java.lang.reflect.Field;

public interface IFieldHandler extends Cloneable, ICreateValue, ISupports {
	public final static int PRIORITY_HIGHEST = 0;
	public final static int PRIORITY_HIGH = 25;
	public final static int PRIORITY_NORMAL = 50;
	public final static int PRIORITY_LOWEST = 100;
	public final static int PRIORITY_LOW = 75;

	public enum Veto {
		NO_VETO, SKIP_FOLLOWING, DENY_ALL_HANDLERS
	}

	/**
	 * Returns veto for field handler
	 * 
	 * @return
	 */
	public Veto getVeto(Field _field);

	/**
	 * Return priority of field handler
	 * 
	 * @return
	 */
	public int getPriority();

}
