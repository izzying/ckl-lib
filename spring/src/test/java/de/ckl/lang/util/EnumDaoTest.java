package de.ckl.lang.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class EnumDaoTest
{
	class TestNoEnum
	{

	}

	enum ValidEnum
	{
		FIRST, SECOND
	};

	@Test
	public void enumerationFailed()
	{
		EnumerationDao enumDao = new EnumerationDao(null);

		try
		{
			enumDao.afterPropertiesSet();
			fail("No IllegalArgumentException thrown");
		}
		catch (Exception e)
		{

		}

		try
		{
			enumDao.setEnumReflection(TestNoEnum.class);
			enumDao.afterPropertiesSet();
			fail("No IllegalArgumentException thrown");
		}
		catch (Exception e)
		{

		}
	}

	@Test
	public void enumerationValid()
	{
		EnumerationDao enumDao = new EnumerationDao(ValidEnum.class);
		try
		{
			enumDao.afterPropertiesSet();
		}
		catch (Exception e)
		{
			fail("Failed to initialze properties");
		}

		assertTrue(enumDao.isKey("first"));
		assertTrue(enumDao.isKey("FIRST"));
		assertFalse(enumDao.isKey("invalid"));
		assertFalse(enumDao.isKey("INVALID"));
		assertEquals(0, enumDao.getValue("first"));
		assertEquals(0, enumDao.getValue("FIRST"));
		assertEquals(-1, enumDao.getValue("INVALID"));

		enumDao = new EnumerationDao(ValidEnum.class);
		enumDao.setIgnoreCase(false);
		try
		{
			enumDao.afterPropertiesSet();
		}
		catch (Exception e)
		{
			fail("Failed to initialze properties");
		}

		assertFalse(enumDao.isKey("first"));
		assertTrue(enumDao.isKey("FIRST"));
		assertFalse(enumDao.isKey("invalid"));
		assertFalse(enumDao.isKey("INVALID"));
		assertEquals(-1, enumDao.getValue("first"));
		assertEquals(0, enumDao.getValue("FIRST"));
		assertEquals(-1, enumDao.getValue("INVALID"));
	}
}
