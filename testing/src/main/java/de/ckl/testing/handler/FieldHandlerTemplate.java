package de.ckl.testing.handler;

import java.lang.reflect.Field;

/**
 * Can be used as simple template inside your unit test code
 * 
 * @author ckl
 * 
 */
public class FieldHandlerTemplate implements IFieldHandler, Cloneable {
	private int priority = PRIORITY_NORMAL;
	private Veto veto = Veto.NO_VETO;
	private ICreateValue createValueCallback = null;
	private ISupports supportsCallback = null;

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

	public FieldHandlerTemplate(ICreateValue _createValueCallback,
			ISupports _supportsCallback) {
		supportsCallback = _supportsCallback;
		createValueCallback = _createValueCallback;
	}

	public FieldHandlerTemplate(Class _supportedClazz,
			ICreateValue _createValueCallback) {
		createValueCallback = _createValueCallback;

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
