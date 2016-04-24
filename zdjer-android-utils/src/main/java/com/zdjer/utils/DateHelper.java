package com.zdjer.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间通用类
 */
@SuppressLint("SimpleDateFormat")
public class DateHelper {
	
	/**
	 * 获取当前日历
	 * 
	 * @return
	 */
	public static Calendar getCurrCalendar() {
		return Calendar.getInstance();
	}

	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	public static Date getCurrDate() {
		return Calendar.getInstance().getTime();
	}

	/**
	 * 获取当前时间的字符串
	 */
	public static String getCurrDateString() {
		SimpleDateFormat sdfCurrDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date currDate = Calendar.getInstance().getTime();
		return sdfCurrDateFormat.format(currDate);
	}

	/**
	 * 获取当前时间的字符串（扩展）
	 */
	public static String getCurrDateStringEx() {
		SimpleDateFormat sdfCurrDateFormat = new SimpleDateFormat(
				"yyyy/MM/ddHH:mm:ss");
		Date currDate = Calendar.getInstance().getTime();
		return sdfCurrDateFormat.format(currDate);
	}

	/**
	 * 获取当前月日
	 */
	public static String getCurrYearMonthDayString() {
		SimpleDateFormat sdfCurrMonthDayFormat = new SimpleDateFormat("yyyyMMdd");
		Date currData = Calendar.getInstance().getTime();
		return sdfCurrMonthDayFormat.format(currData);
	}

	/**
	 * 通过字符串获得时间
	 */
	public static Date getDate(String dateString) throws ParseException {
		SimpleDateFormat sdfDateFormat = new SimpleDateFormat(
				"yyyy-MM-ddHH:mm:ss");
		return sdfDateFormat.parse(dateString);
	}

	/**
	 * 通过字符串获得时间（扩展）
	 */
	public static Date getDateEx(String dateString) throws ParseException {
		SimpleDateFormat sdfDateFormat = new SimpleDateFormat(
				"yyyy/MM/ddHH:mm:ss");
		return sdfDateFormat.parse(dateString);
	}

	/**
	 * 通过时间获取字符串
	 */
	public static String getDateString(Date date) {
		if(date==null){
			return null;
		}
		SimpleDateFormat sdfCurrDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return sdfCurrDateFormat.format(date);
	}

	/**
	 * 通过时间获取字符串（扩展）
	 */
	public static String getDateStringEx(Date date) {
		SimpleDateFormat sdfCurrDateFormat = new SimpleDateFormat(
				"yyyy/MM/ddHH:mm:ss");
		return sdfCurrDateFormat.format(date);
	}
	
	/**
	 * 通过时间获取字符串（扩展）
	 */
	public static String getDateStringEx(Date date,String format) {
		SimpleDateFormat sdfCurrDateFormat = new SimpleDateFormat(
				format);
		return sdfCurrDateFormat.format(date);
	}
}
