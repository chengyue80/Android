package com.yue.demo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.iflytek.android.framework.util.StringUtils;

/**
 * @author chengyue 处理时间的类
 */
@SuppressLint("SimpleDateFormat")
public class DateUtils {

	/**
	 * 时间基础格式化
	 */
	public static final String DEAFULTFORMAT = "yyyyMMddHHmmss";

	/**
	 * @return 得到当前时间
	 */
	public static String getNowDate() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DEAFULTFORMAT);
		return simpleDateFormat.format(Calendar.getInstance().getTime());
	}
	/**
	 * @return 得到当前时间
	 */
	public static String getNowDate(String sdf) {
		
		if (TextUtils.isEmpty(sdf)) {
			return getNowDate();
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sdf);
		return simpleDateFormat.format(Calendar.getInstance().getTime());
	}

	/**
	 * 
	 * 时间格式化
	 * 
	 * @param date
	 *            日期时间
	 * @param formate
	 *            格式化
	 * @return 字符串类型时间
	 * @history
	 */
	public static String getDate(Date date, String formate) {
		if (StringUtils.isBlank(formate)) {
			SimpleDateFormat df = new SimpleDateFormat(DEAFULTFORMAT);
			return df.format(date);
		} else {
			SimpleDateFormat df = new SimpleDateFormat(formate);
			return df.format(date);
		}

	}

	/**
	 * 当前时期增加天数
	 * 
	 * @param format
	 * @param days
	 *            增加的天数
	 * @return
	 */
	public static String getDate(String format, Integer days) {
		if (format == null || "".equals(format.trim())) {
			return getNowDate();
		} else {
			Calendar calendar = new GregorianCalendar();
			// 把日期往后增加一天.整数往后推,负数往前移动
			calendar.add(Calendar.DATE, days);
			// 这个时间就是日期往后推一天的结果
			Date date = calendar.getTime();
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			String dateString = formatter.format(date);
			return dateString;
		}
	}

	/**
	 * 
	 * 日期格式转换
	 * 
	 * @param date
	 * @param formateFrom
	 * @param formateTo
	 * @author nanhuang
	 * @lastModified
	 * @history
	 */
	public static String getDate(String date, String formateFrom, String formateTo) {
		try {
			return new SimpleDateFormat(formateTo).format(new SimpleDateFormat(formateFrom).parse(date));
		} catch (ParseException e) {
			return date;
		}
	}

	/**
	 * 得到时间差
	 * 
	 * @param oldDate
	 * @param newDate
	 * @return
	 */
	public static long getDateDiff(String oldTime, String newTime) {
		try {
			// 根据字符串得到时间
			Date oldDate = new SimpleDateFormat(DEAFULTFORMAT).parse(oldTime);
			Date newDate = new SimpleDateFormat(DEAFULTFORMAT).parse(newTime);
			// 得到时间差
			long timeDiff = Math.abs(newDate.getTime() - oldDate.getTime());
			return timeDiff;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}


	/**
	 * 得到当前日期是星期几
	 * 
	 * @return
	 */
	public static String getWeek() {
		Date date = new Date();
		SimpleDateFormat dateFm = new SimpleDateFormat("E");
		return dateFm.format(date);
	}

}
