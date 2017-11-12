package com.ef.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class DateBuilderTest {

	private DateBuilder dateBuilder;
	private SimpleDateFormat simpleDateFormat;

	public DateBuilderTest() {
		dateBuilder = new DateBuilder();
		simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	}

	/**
	 * Lets test whether text to date conversion has worked.
	 */
	@Test
	public void stringToDate() {
		String text = "2017-01-01 00:00:21.164";
		String textWithoutMilliseconds = "2017-01-01 00:00:21";

		Date date = dateBuilder.getStringBuildDate(text);
		
		assertTrue(simpleDateFormat.format(date).equals(text));
		assertFalse(simpleDateFormat.format(date).equals(textWithoutMilliseconds));
	}
}
