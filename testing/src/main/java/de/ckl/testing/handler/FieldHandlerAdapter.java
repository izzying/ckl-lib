package de.ckl.testing.handler;

import java.lang.reflect.Field;

public class FieldHandlerAdapter extends CloneableFieldHandler implements
		IFieldHandler {
	@Override
	public Veto getVeto(Field _field) {
		return Veto.NO_VETO;
	}

	@Override
	public int getPriority() {
		return PRIORITY_NORMAL;
	}

	@Override
	public boolean supports(Field _field) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object createValue(Field _field) {
		// TODO Auto-generated method stub
		return null;
	}

}
