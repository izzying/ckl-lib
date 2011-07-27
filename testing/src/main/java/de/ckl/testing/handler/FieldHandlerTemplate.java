package de.ckl.testing.handler;

import java.lang.reflect.Field;

/**
 * Can be used as simple template inside your unit tests
 * 
 * @author ckl
 * 
 */
public class FieldHandlerTemplate implements IFieldHandler, Cloneable {
	/**
	 * Default priority
	 */
	private int priority = PRIORITY_NORMAL;

	/**
	 * Default veto
	 */
	private Veto veto = Veto.NO_VETO;

	/**
	 * Callback for creation of new data
	 */
	private ICreateValue createValueCallback = null;

	/**
	 * Callback for supporting classes
	 */
	private ISupports supportsCallback = null;

	/**
	 * Internal class which implements {@link ISupports} for a default class
	 * 
	 * @author ckl
	 * 
	 */
	private class ClassOnlyCallback implements ISupports {
		private Class clazz;

		public ClassOnlyCallback(Class _clazz) {
			clazz = _clazz;
		}

		@Override
		public boolean supports(Field _field) {
			return _field.getType().isAssignableFrom(clazz);
		}
	}

	/**
	 * Creates a new field handler with {@link ICreateValue} and
	 * {@link IFieldHandler} callbacks
	 * 
	 * @param _createValueCallback
	 * @param _supportsCallback
	 */
	public FieldHandlerTemplate(ICreateValue _createValueCallback,
			ISupports _supportsCallback) {
		supportsCallback = _supportsCallback;
		createValueCallback = _createValueCallback;
	}

	/**
	 * Creates a new field handler with {@link ICreateValue} callback. This
	 * field supports automatically given _supportedClazz. Should be enough in
	 * most reasons.
	 * 
	 * @param _supportedClazz
	 * @param _createValueCallback
	 */
	public FieldHandlerTemplate(Class _supportedClazz,
			ICreateValue _createValueCallback) {
		createValueCallback = _createValueCallback;

		// create class support callback
		supportsCallback = new ClassOnlyCallback(_supportedClazz);
	}

	@Override
	public Object createValue(Field _field) {
		if (createValueCallback != null) {
			return createValueCallback.createValue(_field);
		}

		return null;
	}

	@Override
	public boolean supports(Field _field) {
		if (supportsCallback != null) {
			return supportsCallback.supports(_field);
		}
		return false;
	}

	@Override
	public Veto getVeto(Field _field) {
		return veto;
	}

	@Override
	public int getPriority() {
		return priority;
	}

}
