package com.ef.utils;

/**
 * That class parses command line input. Those inputs values can be
 * 
 * @author diambakus
 *
 */
public class CommandLineInputParser {

	private String startDate;
	private String duration;
	private int threshold;
	private String accesslog;
	//command line input argument delimiter
	private final String DELIMITER = "=";

	public CommandLineInputParser(String[] arguments) {
		accesslog = splitArgumentGetValue(arguments, 0);
		//If user type time with milliseconds it will fail
		startDate = splitArgumentGetValue(arguments, 1).replace('.', ' ');
		duration = splitArgumentGetValue(arguments, 2);
		threshold = Integer.parseInt(splitArgumentGetValue(arguments, 3));
	}

	private String splitArgumentGetValue(String[] arguments, int index) {

		/*
		 * TODO validate arguments here
		 */

		String[] splittedArgument = arguments[index].trim().split(DELIMITER);

		return splittedArgument[1];
	}

	public String getStartDate() {
		return startDate;
	}

	public String getDuration() {
		return duration;
	}

	public int getThreshold() {
		return threshold;
	}

	public String getAccesslog() {
		return accesslog;
	}
}
