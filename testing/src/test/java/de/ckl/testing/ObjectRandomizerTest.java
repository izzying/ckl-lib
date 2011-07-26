package de.ckl.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.ConsoleMockitoLogger;

import de.ckl.testing.ObjectRandomizer;
import de.ckl.testing.handler.IFieldHandler;
import de.ckl.testing.handler.IFieldHandler.Veto;
import de.ckl.testing.handler.types.DenyFinalStaticFields;
import de.ckl.testing.handler.types.IntegerHandler;
import de.ckl.testing.handler.types.StringHandler;
import de.ckl.testing.stub.ObjectStub;

public class ObjectRandomizerTest {

	class ObjectRandomizerStub<E> extends ObjectRandomizer<E> {
		public ObjectRandomizerStub(Class _clazz) {
			super(_clazz);
		}

		public List<IFieldHandler> filterAvailableHandlers(Field _field,
				List<IFieldHandler> _fieldHandlers) {
			return super.filterAvailableHandlers(_field, _fieldHandlers);
		}

		public List<IFieldHandler> filterHandlersAfterSkipFollowing(
				Field _field, List<IFieldHandler> _fieldHandlersForField) {
			return super.filterHandlersAfterSkipFollowing(_field,
					_fieldHandlersForField);
		}
	}

	@Test
	public void filterAvailableHandlers() throws Exception {
		ObjectStub objectMock = new ObjectStub();

		IFieldHandler nonDeny = Mockito.mock(IFieldHandler.class);
		IFieldHandler denyHandler = Mockito.mock(IFieldHandler.class);

		when(nonDeny.getVeto(any(Field.class))).thenReturn(Veto.NO_VETO);
		when(nonDeny.toString()).thenReturn("nonDeny");
		when(nonDeny.supports(any(Field.class))).thenReturn(true);
		when(denyHandler.getVeto(any(Field.class))).thenReturn(
				Veto.DENY_ALL_HANDLERS);
		when(denyHandler.supports(any(Field.class))).thenReturn(true);
		when(denyHandler.toString()).thenReturn("denyHandler");

		ObjectRandomizerStub<ObjectStub> randomizer = new ObjectRandomizerStub(
				ObjectStub.class);

		// Basic support for deny veto
		List<IFieldHandler> handlers = new ArrayList<IFieldHandler>();
		handlers.add(nonDeny);
		handlers.add(denyHandler);

		List<IFieldHandler> fields = randomizer.filterAvailableHandlers(
				any(Field.class), handlers);
		assertNotNull(fields);
		assertEquals(0, fields.size());

		handlers.remove(denyHandler);
		fields = randomizer.filterAvailableHandlers(any(Field.class), handlers);
		assertNotNull(fields);
		assertEquals(1, fields.size());
		assertEquals(nonDeny, fields.get(0));

		// Support for object cloning
		StringHandler stringHandler = new StringHandler();
		Field field = objectMock.getClass().getDeclaredField("string");
		handlers.clear();
		handlers.add(stringHandler);

		fields = randomizer.filterAvailableHandlers(field, handlers);
		assertNotNull(fields);
		assertEquals(1, fields.size());
		assertTrue(fields.get(0).getClass()
				.isAssignableFrom(StringHandler.class));
		// must be false, cause we need a copy
		assertFalse(fields.get(0).equals(stringHandler));
	}

	@Test
	public void filterHandlersAfterSkipFollowing() {
		IFieldHandler highestPriority = Mockito.mock(IFieldHandler.class);
		IFieldHandler highPriority = Mockito.mock(IFieldHandler.class);
		IFieldHandler normalPriority = Mockito.mock(IFieldHandler.class);
		IFieldHandler lowPriority = Mockito.mock(IFieldHandler.class);
		IFieldHandler lowestPriority = Mockito.mock(IFieldHandler.class);

		when(highPriority.getVeto(any(Field.class))).thenReturn(
				Veto.SKIP_FOLLOWING);
		when(highPriority.supports(any(Field.class))).thenReturn(true);
		when(highPriority.getPriority())
				.thenReturn(IFieldHandler.PRIORITY_HIGH);

		when(lowPriority.getVeto(any(Field.class))).thenReturn(Veto.NO_VETO);
		when(lowPriority.getPriority()).thenReturn(IFieldHandler.PRIORITY_LOW);
		when(lowPriority.supports(any(Field.class))).thenReturn(true);

		ObjectRandomizerStub<ObjectStub> randomizer = new ObjectRandomizerStub(
				ObjectStub.class);

		List<IFieldHandler> handlers = new ArrayList<IFieldHandler>();
		handlers.add(highPriority);
		handlers.add(lowPriority);

		Collections.sort(handlers, randomizer.createComparator());

		List<IFieldHandler> r = randomizer.filterHandlersAfterSkipFollowing(
				any(Field.class), handlers);
		assertEquals(1, r.size());
		assertEquals(highPriority, r.get(0));

		when(highestPriority.getVeto(any(Field.class)))
				.thenReturn(Veto.NO_VETO);
		when(highestPriority.supports(any(Field.class))).thenReturn(true);
		when(highestPriority.getPriority()).thenReturn(
				IFieldHandler.PRIORITY_HIGHEST);

		handlers.add(highestPriority);
		Collections.sort(handlers, randomizer.createComparator());

		r = randomizer.filterHandlersAfterSkipFollowing(any(Field.class),
				handlers);
		assertEquals(2, r.size());
		assertEquals(highestPriority, r.get(0));
		assertEquals(highPriority, r.get(1));
	}

	@Test
	public void update() {
		BasicConfigurator.configure();
		DenyFinalStaticFields denyFinalStaticFields = new DenyFinalStaticFields();
		StringHandler stringHandler = new StringHandler();
		IntegerHandler integerHandler = new IntegerHandler();
		ObjectRandomizer<ObjectStub> randomizer = new ObjectRandomizer<ObjectStub>(
				ObjectStub.class);
		randomizer.getFieldHandlers().add(denyFinalStaticFields);
		randomizer.getFieldHandlers().add(stringHandler);
		randomizer.getFieldHandlers().add(integerHandler);

		ObjectStub object = randomizer.createNewInstance();
		assertNotNull(object.getString());
		assertTrue(object.getString().length() > 1);
		assertTrue(object.getInteger() != 0);
		assertEquals("isStatic", object.getStaticString());
		assertEquals("isFinal", object.getFinalString());
	}
}
