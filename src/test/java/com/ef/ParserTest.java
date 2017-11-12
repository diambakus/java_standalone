package com.ef;

import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import org.junit.Test;

import com.ef.utils.CommandLineInputParser;
import com.ef.utils.DateBuilder;

public class ParserTest {

	DateBuilder dateBuilder;
	CommandLineInputParser commandLineInputParser;
	String[] arguments = { "--accesslog=/path/to/file", "--startDate=2017-01-01.13:00:00", "--duration=hourly",
			"--threshold=100" };

	public ParserTest() {
		dateBuilder = new DateBuilder();
		commandLineInputParser = new CommandLineInputParser(arguments);
	}

	@Test
	public void parsingLineTest() {
		String line = "2017-01-01 00:00:11.763|192.168.234.82|\"GET / HTTP/1.1\"|200|\"swcd (unknown version) CFNetwork/808.2.16 Darwin/15.6.0";
		String[] parseredLine;
		int parseredLineLength = -1;

		parseredLine = line.trim().split(Pattern.quote("|"));
		parseredLineLength = parseredLine.length;

		assertTrue(parseredLineLength == 5);
	}

	@Test
	public void endDateTest() {
		String startDateString = commandLineInputParser.getStartDate();
		Date startDate = dateBuilder.completeWithMillisecond(startDateString);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.HOUR, 1);
        Date endDate = calendar.getTime();
        
        long differenceInMilliseconds = endDate.getTime() - startDate.getTime();
        assertTrue(differenceInMilliseconds/(60*60*1000)%24 == 1);
	}
}
