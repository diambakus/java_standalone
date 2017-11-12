package com.ef.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Class responsible for building a text date
 * @author diambakus
 *
 */
public class DateBuilder {

	/**
	 * This method converts file inputs into date
	 * @param stringDateTime
	 * @return returns the date according to parameter and date format
	 */
	public Date getStringBuildDate(String stringDateTime) {
		Date pureDateTime = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		
		try {
			pureDateTime = simpleDateFormat.parse(stringDateTime);
		} catch (ParseException e) {
			System.out.println("Date parsing has failed: "+e.getMessage());
		}
		
		return pureDateTime;
	}
	
	/**
	 * 
	 * @param date - date in the format yyyy-MM-dd HH:mm:ss.SSSS
	 * @param format - string that represents date without time pattern
	 * @return returns a string that represents date in the specific format
	 */
	public String getDateWithoutTime(Date date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}
	
	/**
	 * 
	 * @param date - date in the format yyyy-MM-dd HH:mm:ss.SSSS
	 * @param format - a string that represents only time pattern
	 * @return returns a string that represents only time in the specific format
	 */
	public String getDateOnlyTime(Date date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}
	
	/**
	 * this method converts arguments input into date
	 * @param stringDateTime
	 * @return returns date according to string parameter
	 */
	public Date completeWithMillisecond(String stringDateTime) {
		Date pureDateTime = null;
		Calendar calendar = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			pureDateTime = simpleDateFormat.parse(stringDateTime);
		} catch (ParseException e) {
			System.out.println("Date parsing has failed: "+e.getMessage());
		}
		if (pureDateTime != null) {
			calendar = Calendar.getInstance();
			calendar.setTime(pureDateTime);
			calendar.add(Calendar.MILLISECOND, 0);
		}
		
		return calendar.getTime();
	}
}
