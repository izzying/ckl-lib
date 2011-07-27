package de.ckl.testing;

import de.ckl.testing.handler.types.DateHandler;
import de.ckl.testing.handler.types.DenyFinalStaticFields;
import de.ckl.testing.handler.types.IntegerHandler;
import de.ckl.testing.handler.types.LongHandler;
import de.ckl.testing.handler.types.StringHandler;

/**
 * Template class with registered default handlers {@link DenyFinalStaticFields}
 * , {@link StringHandler}, {@link IntegerHandler}, {@link LongHandler},
 * {@link DateHandler}
 * 
 * @author ckl
 * 
 * @param <E>
 */
public class ObjectRandomizerTemplate<E> extends ObjectRandomizer<E> {
	/**
	 * Creates a new instance
	 * 
	 * @param _e
	 * @return
	 */
	public static <E> ObjectRandomizer<E> newInstance(Class<E> _e) {
		return new ObjectRandomizerTemplate<E>(_e);
	}

	public ObjectRandomizerTemplate(Class<E> _e) {
		super(_e);
		getFieldHandlers().add(new DenyFinalStaticFields());
		getFieldHandlers().add(new StringHandler());
		getFieldHandlers().add(new IntegerHandler());
		getFieldHandlers().add(new DateHandler());
		getFieldHandlers().add(new LongHandler());
	}
}
