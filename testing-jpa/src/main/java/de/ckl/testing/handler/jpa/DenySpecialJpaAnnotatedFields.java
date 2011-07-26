package de.ckl.testing.handler.jpa;

import java.lang.reflect.Field;

import javax.persistence.Id;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

import de.ckl.testing.handler.FieldHandlerAdapter;
import de.ckl.testing.handler.types.DenyFinalStaticFields;

/**
 * Deny all field generators if field is annotated by Id or Transient
 * 
 * @author ckl
 * 
 */
public class DenySpecialJpaAnnotatedFields extends FieldHandlerAdapter {
	Logger log = Logger.getLogger(DenyFinalStaticFields.class);

	@Override
	public boolean supports(Field _field) {
		return true;
	}

	@Override
	public Veto getVeto(Field _field) {
		if (_field.isAnnotationPresent(Id.class)
				|| _field.isAnnotationPresent(Transient.class)) {
			log.info("Not creating data for field ["
					+ _field.getName()
					+ "] because it is annotated by JPA supported @Id or @Transient");
			return Veto.DENY_ALL_HANDLERS;
		}

		return Veto.NO_VETO;
	}

	@Override
	public int getPriority() {
		return PRIORITY_HIGHEST;
	}

}
