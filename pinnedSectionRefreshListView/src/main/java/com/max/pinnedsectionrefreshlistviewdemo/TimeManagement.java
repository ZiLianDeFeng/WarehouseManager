package com.max.pinnedsectionrefreshlistviewdemo;

import java.text.ParseException;

public class TimeManagement {
	public static String exchangeStringDate(String date) throws ParseException {
		if (date != null && date.length() > 10) {
			String result = date.substring(0, 10);
			return result;
		}else{
			return null;
		}

	}

	/**
	 *
	 * @throws ParseException
	 */
	public static String exchangeStringTime(String date) throws ParseException {
		if (date != null && date.length() > 10) {
			String result = date.substring(11, date.length());
			return result;
		}else{
			return null;
		}
	}


}
