package de.ckl.testing.jpa.handler.types;

import java.lang.reflect.Field;

import javax.persistence.Id;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

import de.ckl.testing.handler.FieldHandlerAdapter;
import de.ckl.testing.handler.types.DenyFinalStaticFields;

/**
 * Denies all field generators if field is annotated by {@link Id} or
 * {@link Transient} or is one of a JPA generated field (starting with
 * _persistence_).
 * 
 * @author ckl
 * 
 */
public class DenySpecialJpaAnnotatedFields extends FieldHandlerAdapter {
	Logger log = Logger.getLogger(DenyFinalStaticFields.class);

	@Override
	public boolean supports(Field _field) {
		if (_field.isAnnotationPresent(Id.class)
				|| _field.isAnnotationPresent(Transient.class)
				|| _field.getName().startsWith("_persistence_")) {
			return true;
		}

		return false;
	}

	@Override
	public Veto getVeto(Field _field) {
		log.info("Denying all handlers for field ["
				+ _field.getName()
				+ "] because it is annotated by JPA @Id or @Transient or starts with _persistence_");

		return Veto.DENY_ALL_HANDLERS;
	}

	@Override
	public int getPriority() {
		return PRIORITY_HIGHEST;
	}

}
