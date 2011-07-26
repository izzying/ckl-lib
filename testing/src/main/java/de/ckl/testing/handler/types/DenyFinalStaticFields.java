package de.ckl.testing.handler.types;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.log4j.Logger;

import de.ckl.testing.handler.FieldHandlerAdapter;
import de.ckl.testing.handler.IFieldHandler;
import de.ckl.testing.handler.IFieldHandler.Veto;

/**
 * Protect all final or static fields
 * 
 * @author ckl
 */
public class DenyFinalStaticFields extends FieldHandlerAdapter {
	Logger log = Logger.getLogger(DenyFinalStaticFields.class);

	@Override
	public boolean supports(Field _field) {
		return (Modifier.isFinal(_field.getModifiers())
				|| Modifier.isStatic(_field.getModifiers()));
	}

	@Override
	public Veto getVeto(Field _field) {
		if (Modifier.isFinal(_field.getModifiers())
				|| Modifier.isStatic(_field.getModifiers())) {
			log.info("Not creating data for field [" + _field.getName()
					+ "] because it is final/static");
			return Veto.DENY_ALL_HANDLERS;
		}
		return Veto.NO_VETO;
	}

	@Override
	public int getPriority() {
		return PRIORITY_HIGHEST;
	}
}
