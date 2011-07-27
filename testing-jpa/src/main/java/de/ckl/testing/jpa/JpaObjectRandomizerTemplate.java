package de.ckl.testing.jpa;
import de.ckl.testing.ObjectRandomizer;
import de.ckl.testing.ObjectRandomizerTemplate;
import de.ckl.testing.jpa.handler.types.DenySpecialJpaAnnotatedFields;
import de.ckl.testing.jpa.handler.types.StringLengthHandler;

public class JpaObjectRandomizerTemplate<E> extends ObjectRandomizerTemplate<E> {
	public static <E> ObjectRandomizer<E> getInstance(Class<E> _e) {
		return new JpaObjectRandomizerTemplate<E>(_e);
	}

	public JpaObjectRandomizerTemplate(Class<E> _e) {
		super(_e);
		getFieldHandlers().add(new DenySpecialJpaAnnotatedFields());
		getFieldHandlers().add(new StringLengthHandler());
	}
}
