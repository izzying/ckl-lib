package de.ckl.testing.handler;

import java.lang.reflect.Field;

/**
 * Adapter class for the most field handlers which will be wirtten Returns in
 * this implementation:
 * <ul>
 * <li> {@link #getVeto(Field)}: {@link Veto#NO_VETO}</li>
 * <li> {@link #getPriority()}: {@link IFieldHandler#PRIORITY_NORMAL}</li>
 * <li> {@link #supports(Field)}: false</li>
 * </ul>
 * 
 * @author ckl
 * 
 */
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
