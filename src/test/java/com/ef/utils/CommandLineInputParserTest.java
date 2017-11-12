package com.ef.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CommandLineInputParserTest {

	/**
	 * TODO: test accesslog
	 */
	private String[] arguments = { "--accesslog=/path/to/file", "--startDate=2017-01-01.13:00:00", "--duration=hourly",
			"--threshold=100" };
	private CommandLineInputParser commandLineInputParser;

	public CommandLineInputParserTest() {
		commandLineInputParser = new CommandLineInputParser(arguments);
	}

	@Test
	public void getStartDateTest() {
		String startDateFormat = "2017-01-01 13:00:00";
		assertTrue(commandLineInputParser.getStartDate().equals(startDateFormat));
	}

	@Test
	public void getDurationTest() {
		String durationFormat = "hourly";
		assertEquals(commandLineInputParser.getDuration(), durationFormat);
	}

	@Test
	public void getThresholdTest() {
		int threshold = 100;
		assertTrue(commandLineInputParser.getThreshold() == threshold);
	}

	@Test
	public void getAccesslog() {
		/**
		 * TODO
		 */
	}

}
