package com.datasection.utils;

public class DateUtil {

	public static String getLastTime(String string) {

		if (string.endsWith("_00")) {

			if (isFirstDayOfMonth(string)) {

				return (getLastTimeOfLastDay(string, true) + ".txt");

			} else {

				return (getLastTimeOfLastDay(string, false) + ".txt");

			}

		}

		int time = Integer.parseInt(string.substring(string.lastIndexOf("_") + 1, string.length()));
		int lastTime;

		if (time == 0)
			lastTime = 23;
		else
			lastTime = time - 1;

		String strLastTime = lastTime + "";
		if (strLastTime.length() < 2)
			strLastTime = "0" + strLastTime;
		String s1 = string.substring(0, string.lastIndexOf("_"));

		return s1 + "_" + strLastTime + ".txt";

	}

	private static boolean isFirstDayOfMonth(String date) {

		if (date.endsWith("01_00"))
			return true;

		return false;
	}

	private static String getLastTimeOfLastDay(String date, boolean isNewMonth) {

		String lastTime = "";

		if (isNewMonth) {

			String[] tokens = date.split("_");
			String strYear = tokens[0];
			String strMonth = tokens[1];
			int month = Integer.parseInt(strMonth);
			int year = Integer.parseInt(strYear);
			if (month == 1) {
				strMonth = "12";
				strYear = (year - 1) + "";
			} else
				strMonth = (Integer.parseInt(strMonth) - 1) + "";
			if (strMonth.length() == 1) {
				strMonth = "0" + strMonth;
			}

			lastTime = strYear + "_" + strMonth + "_" + getLastDayOfLastMonth(date) + "_" + "23";

		} else {

			String[] tokens = date.split("_");
			String strDay = tokens[2];
			int lastDay = Integer.parseInt(strDay) - 1;
			strDay = lastDay + "";
			if (strDay.length() == 1) {
				strDay = "0" + strDay;
			}
			lastTime = tokens[0] + "_" + tokens[1] + "_" + strDay + "_" + "23";

		}

		return lastTime;
	}

	private static boolean isLeapYear(String date) {

		String srtYear = date.substring(0, date.indexOf("_"));
		int year = Integer.parseInt(srtYear);

		if ((year % 400 == 0) || (year % 4 == 0 && year % 100 != 0))
			return true;

		return false;

	}

	private static String getLastDayOfLastMonth(String date) {

		String[] tokens = date.split("_");
		int month = Integer.parseInt(tokens[1]);
		if (month == 1)
			month = 12;
		else
			month -= 1;

		if (month == 4 || month == 6 || month == 9 || month == 11)
			return "30";
		else if (month == 2) {
			if (isLeapYear(date))
				return "29";
			return "28";
		}

		return "31";
	}

	public static boolean isMiddleNight(String time) {

		int hour = Integer.parseInt(time.substring(time.lastIndexOf("_") + 1, time.length()));

		if (0 <= hour && hour <= 6)
			return true;

		return false;
	}

}
