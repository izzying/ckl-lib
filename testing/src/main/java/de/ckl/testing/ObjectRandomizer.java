package de.ckl.testing;

import java.lang.reflect.Field;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import de.ckl.testing.generator.DataGeneratorDelegator;
import de.ckl.testing.handler.CloneableFieldHandler;
import de.ckl.testing.handler.IFieldHandler;
import de.ckl.testing.handler.IFieldHandler.Veto;

/**
 * {@link ObjectRandomizer} is the main factory for creating and updating test
 * data for objects of given type.
 * 
 * @author ckl
 * 
 * @param <E>
 */
public class ObjectRandomizer<E> {
	Logger log = Logger.getLogger(ObjectRandomizer.class);

	private boolean isInitialized = false;

	/**
	 * Reference to class for objects under test
	 */
	private Class<E> assignedClass;

	private List<IFieldHandler> fieldHandlers = new ArrayList<IFieldHandler>();

	private Map<String, List<IFieldHandler>> assignedFieldHandlers = new HashMap<String, List<IFieldHandler>>();

	private DataGeneratorDelegator dataGeneratorStrategy = new DataGeneratorDelegator();

	public ObjectRandomizer(Class<E> _class) {
		setAssignedClass(_class);
	}

	/**
	 * Comparator for priority ordering
	 * 
	 * @author ckl
	 * 
	 */
	public class FieldHandlerComparatorByPriority implements
			Comparator<IFieldHandler> {
		@Override
		public int compare(IFieldHandler o1, IFieldHandler o2) {
			return o1.getPriority() - o2.getPriority();
		}
	}

	/**
	 * Initalizes the assigned field handler for class under test.
	 * 
	 * @return
	 */
	public Map<String, List<IFieldHandler>> init() {
		if (!isInitialized) {
			Field[] fields = getAssignedClass().getDeclaredFields();

			log.info("Initalizing field handlers for class "
					+ getAssignedClass().getName());

			// iterate over all fields and assign field handlers
			for (Field field : fields) {
				log.info("  Initalizing field handlers for field "
						+ field.getName());
				// remove unsupported or denied handlers
				List<IFieldHandler> fieldHandlersForField = filterAvailableHandlers(
						field, getFieldHandlers());

				log.info("    " + fieldHandlersForField.size()
						+ " field handler(s) available");

				// ignore empty set
				if (fieldHandlersForField.size() == 0) {
					continue;
				}

				// field can be modified by one or more handlers.
				// Sort handlers by priority and remove all items after
				// SKIP_FOLLOWING (if exists)
				Collections.sort(fieldHandlersForField, createComparator());

				fieldHandlersForField = filterHandlersAfterSkipFollowing(field,
						fieldHandlersForField);

				// Reorder entites, so high priority is last and can overwrite
				// ever other value
				Collections.reverse(fieldHandlersForField);

				// register all valid field handlers
				assignedFieldHandlers.put(field.getName(),
						fieldHandlersForField);
			}

			isInitialized = true;
		}

		return assignedFieldHandlers;
	}

	/**
	 * Signals that context is dirty and has to be rebuild
	 */
	public void dirtyContext() {
		isInitialized = false;
		init();
	}

	/**
	 * Returns a new comparator instance
	 * 
	 * @return
	 */
	public Comparator<IFieldHandler> createComparator() {
		return new FieldHandlerComparatorByPriority();
	}

	/**
	 * Checks every registered {@link IFieldHandler} if it has support the
	 * field. If one the registered fields returns
	 * {@link IFieldHandler#getVeto(Field)} == {@link Veto#DENY_ALL_HANDLERS}
	 * all handlers are removed for this field and the field is protected for
	 * changes.
	 * 
	 * @param _field
	 * @param _fieldHandlers
	 * @return
	 */
	protected List<IFieldHandler> filterAvailableHandlers(Field _field,
			List<IFieldHandler> _fieldHandlers) {
		List<IFieldHandler> r = new ArrayList<IFieldHandler>();

		for (IFieldHandler fieldHandler : _fieldHandlers) {
			log.info("    Testing field handler");

			// field handler can work with given field
			if (fieldHandler.supports(_field)) {
				log.info("      Field handler can support this field");
				// this field handler denies ALL field handler, so skip all
				if (IFieldHandler.Veto.DENY_ALL_HANDLERS.equals(fieldHandler
						.getVeto(_field))) {
					log.warn("      Field handler DENIES ALL handlers. Removing all handlers and returning");
					r.clear();
					break;
				}

				boolean fieldHandlerCloned = false;

				// copy field handler for dynamic settings of field
				if (fieldHandler instanceof CloneableFieldHandler) {
					try {
						log.info("      Field handler supports cloning, create a new copy");
						r.add((IFieldHandler) ((CloneableFieldHandler) fieldHandler)
								.clone());
						fieldHandlerCloned = true;
					} catch (CloneNotSupportedException e) {
						log.error("      Failed to clone field handler for field "
								+ _field.getName() + ": " + e.getMessage());
					}
				}

				if (!fieldHandlerCloned) {
					log.warn("      Field handler does *NOT* support cloning, making a reference to original field handler");
					r.add(fieldHandler);
				}
			}
		}

		return r;
	}

	/**
	 * Removes all field handlers after first occurence of
	 * {@link Veto#SKIP_FOLLOWING} is returned by
	 * {@link IFieldHandler#getVeto(Field)}
	 * 
	 * @param _field
	 * @param _fieldHandlersForField
	 * @return
	 */
	protected List<IFieldHandler> filterHandlersAfterSkipFollowing(
			Field _field, List<IFieldHandler> _fieldHandlersForField) {

		List<IFieldHandler> r = new ArrayList<IFieldHandler>();

		boolean bSkipFollowingFieldHandler = false;
		int idxMax = (_fieldHandlersForField.size() - 1);

		for (int i = 0, m = idxMax; i <= m; i++) {
			IFieldHandler fieldHandler = _fieldHandlersForField.get(i);

			boolean bHasSkipFollowingVeto = IFieldHandler.Veto.SKIP_FOLLOWING
					.equals(fieldHandler.getVeto(_field));

			if (bSkipFollowingFieldHandler) {
				idxMax = i;

				if (bHasSkipFollowingVeto) {
					log.warn("    You have more than one field handler with veto SKIP_FOLLOWING with same priority. Taking first one.");
				}
				break;
			}

			if (IFieldHandler.Veto.SKIP_FOLLOWING.equals(fieldHandler
					.getVeto(_field))) {
				log.info("    Field handler has veto SKIP_FOLLOWING enabled. No more field handlers allowed for this field");
				bSkipFollowingFieldHandler = true;
			}

			r.add(fieldHandler);
		}

		// remove skiped handlers
		return r;
	}

	/**
	 * Creates a new list with _count random instances.
	 * 
	 * @param _clazz
	 * @param _count
	 * @return
	 */
	public List<E> factory(int _count) {
		List<E> r = new ArrayList<E>();

		log.info("Creating [" + _count + "] new instances of class ["
				+ getAssignedClass().getName() + "]");

		for (int i = 0; i < _count; i++) {
			r.add(createNewInstance());
		}

		return r;
	}

	/**
	 * Creates a new instance. If {@link Class#newInstance()} throws any
	 * execption, the exception is packed and will be thrown as
	 * {@link InvalidParameterException}
	 * 
	 * @return
	 */
	public E createNewInstance() {
		E instance = null;

		try {
			instance = getAssignedClass().newInstance();
		} catch (Exception e) {
			throw new InvalidParameterException(
					"Could not instantiate new object of class ["
							+ getAssignedClass().getName()
							+ "]. Check class for constructor without arguments");
		}

		return update(instance);

	}

	/**
	 * Creates new random data for all declared fields of class.
	 * 
	 * @param _clazz
	 * @return
	 */
	public E update(E instance) {
		if (!isInitialized) {
			init();
		}

		Field[] fields = instance.getClass().getDeclaredFields();

		for (Field field : fields) {
			handleField(field, instance);
		}

		return instance;
	}

	/**
	 * Internal method for update a given field of a given instance
	 * 
	 * @param field
	 * @param instance
	 */
	protected void handleField(Field field, E instance) {
		String fieldName = field.getName();

		if (!assignedFieldHandlers.containsKey(fieldName)) {
			log.debug("No field handlers for field " + fieldName
					+ " registered");
			return;
		}

		// find available field handlers
		List<IFieldHandler> availableFieldHandlers = assignedFieldHandlers
				.get(fieldName);
		log.debug("Field handlers for field " + fieldName + ": "
				+ availableFieldHandlers.size());

		// make field accessible
		boolean access = field.isAccessible();
		field.setAccessible(true);

		// iterate over every field handler
		for (IFieldHandler fieldHandler : availableFieldHandlers) {
			
			// generate random data
			Object valueToSet = dataGeneratorStrategy.generate(field,
					fieldHandler);

			if (valueToSet == null) {
				log.warn("Value for [" + fieldName + "] is null");
			} else {
				try {
					field.set(instance, valueToSet);
				} catch (Exception e) {
					log.error("Failed to set value for field [" + fieldName
							+ "]", e);
				}
			}
		}
		
		// set access level back to original value
		field.setAccessible(access);
	}

	public Class<E> getAssignedClass() {
		return assignedClass;
	}

	public void setAssignedClass(Class<E> assignedClass) {
		this.assignedClass = assignedClass;
	}

	public List<IFieldHandler> getFieldHandlers() {
		return fieldHandlers;
	}

	public void setFieldHandlers(List<IFieldHandler> fieldHandlers) {
		this.fieldHandlers = fieldHandlers;
	}
}
