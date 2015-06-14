package com.xxs.sdk.util;

import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.os.SystemClock;

import com.xxs.sdk.app.AppContext;

/**
 * 这是用于获取系统当前时间，日期等的一个工具包
 * 
 * @time 2013-10-08
 * @author 熊小松
 * 
 */
public class DateUtil {
	private static String LOG_TAG = DateUtil.class.getName();

	/**
	 * 获取当前时间
	 * 
	 * @return long型时间
	 */
	public static long getcurrentTimeMillis() {
		long currenttime = AppContext.netTime > 0 ? AppContext.netTime > 0 ? AppContext.netTime
				+ (SystemClock.elapsedRealtime() - AppContext.localTime)
				: System.currentTimeMillis()
				: System.currentTimeMillis();
		return currenttime;
	}

	/**
	 * 获取系统当前的日期
	 * 
	 * @return YYYY-MM-DD
	 */
	public static String getDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",
				Locale.getDefault());
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getcurrentTimeMillis());
		String date = format.format(c.getTime());
		return date;
	}

	/**
	 * 获取系统当前的日期
	 * 
	 * @return YYYY/MM/DD
	 */
	public static String getDate2() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd",
				Locale.getDefault());
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getcurrentTimeMillis());
		String date = format.format(c.getTime());
		return date;
	}

	/**
	 * 获取系统当前的时间
	 * 
	 * @return XX:XX:XX
	 */
	public static String getTime() {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss",
				Locale.getDefault());
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getcurrentTimeMillis());
		String time = format.format(c.getTime());
		return time;
	}

	/**
	 * 获取系统当前的小时和分
	 * 
	 * @return XX:XX
	 */
	public static String getHourAndMinute() {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm",
				Locale.getDefault());
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getcurrentTimeMillis());
		String time = format.format(c.getTime());
		return time;
	}

	/**
	 * 获取系统当前的日期和时间
	 * 
	 * @return YYYY-MM-DD HH:MM:SS
	 */
	public static String getDateAndTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getcurrentTimeMillis());
		String dateandtime = format.format(c.getTime());
		return dateandtime;
	}

	/**
	 * 获取系统当前的日期和时间
	 * 
	 * @return YYYYMMDDHHMMSS
	 */
	public static String getTimeString() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss",
				Locale.getDefault());
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getcurrentTimeMillis());
		String timestring = format.format(c.getTime());
		return timestring;
	}

	/**
	 * 将小于十的数字前面加零
	 * 
	 * @return 转换后的字符串
	 */
	public static String JiaLing(int i) {
		String s;
		if (i < 10) {
			s = "0" + String.valueOf(i);
		} else {
			s = String.valueOf(i);
		}
		return s;
	}

	/**
	 * 将两个日期时间字符串转换为对应的时间比较大小
	 * 
	 * @param s1
	 *            传入时间1格式：yyyy-MM-dd HH:mm:ss
	 * @param s2
	 *            传入时间2格式：yyyy-MM-dd HH:mm:ss
	 * @return s1>s2 返回true，否则返回false
	 */
	public static boolean dateAndTimeCompare(String s1, String s2) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		try {
			Date d1 = sdf.parse(s1.replaceAll("/", "-"));
			Date d2 = sdf.parse(s2.replaceAll("/", "-"));
			if (d1.getTime() > d2.getTime()) {
				return true;
			}
			return false;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.d(LOG_TAG, "日期时间格式转换出错");
			LogUtil.e(LOG_TAG, e);
		}
		return false;
	}

	/**
	 * 获取周几的方法
	 * 
	 * @return 0 周日 1周一 2周二 3周三 4周四 5周五 6周六
	 */
	public static int getDayOfWeek() {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getcurrentTimeMillis());
		return c.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 获取最近七天日期的方法
	 */
	public static ArrayList<String> getSevendays() {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getcurrentTimeMillis());
		int dd = c.get(Calendar.DAY_OF_MONTH) - 7;
		c.set(Calendar.DAY_OF_MONTH, dd);
		ArrayList<String> datelist = new ArrayList<String>();
		for (int i = 1; i <= 7; i++) {
			int ddd = c.get(Calendar.DAY_OF_MONTH) + 1;
			c.set(Calendar.DAY_OF_MONTH, ddd);
			datelist.add(c.get(Calendar.YEAR) + "/"
					+ JiaLing(c.get(Calendar.MONTH) + 1) + "/"
					+ JiaLing(c.get(Calendar.DAY_OF_MONTH)));
		}
		return datelist;
	}

	/**
	 * 获取最近三十天日期的方法
	 */
	public static ArrayList<String> getThirtydays() {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getcurrentTimeMillis());
		int dd = c.get(Calendar.DAY_OF_MONTH) - 30;
		c.set(Calendar.DAY_OF_MONTH, dd);
		ArrayList<String> datelist = new ArrayList<String>();
		for (int i = 1; i <= 30; i++) {
			int ddd = c.get(Calendar.DAY_OF_MONTH) + 1;
			c.set(Calendar.DAY_OF_MONTH, ddd);
			datelist.add(c.get(Calendar.YEAR) + "/"
					+ JiaLing(c.get(Calendar.MONTH) + 1) + "/"
					+ JiaLing(c.get(Calendar.DAY_OF_MONTH)));
		}
		return datelist;
	}

	/**
	 * 获取指定日期区间集合的方法
	 * 
	 * @param startdate
	 *            开始日期
	 * @param enddate
	 *            结束日期
	 * @return 区间的日期集合
	 */
	public static ArrayList<String> getOwndays(String startdate, String enddate) {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd",
				Locale.getDefault());
		ArrayList<String> datelist = new ArrayList<String>();
		try {
			Date start = sdf.parse(startdate);
			Date end = sdf.parse(enddate);
			long cha = end.getTime() - start.getTime();
			long days = cha / (24 * 60 * 60 * 1000);
			c.setTime(start);
			datelist.add(c.get(Calendar.YEAR) + "/"
					+ JiaLing(c.get(Calendar.MONTH) + 1) + "/"
					+ JiaLing(c.get(Calendar.DAY_OF_MONTH)));
			for (int i = 0; i < days; i++) {
				int ddd = c.get(Calendar.DAY_OF_MONTH) + 1;
				c.set(Calendar.DAY_OF_MONTH, ddd);
				datelist.add(c.get(Calendar.YEAR) + "/"
						+ JiaLing(c.get(Calendar.MONTH) + 1) + "/"
						+ JiaLing(c.get(Calendar.DAY_OF_MONTH)));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datelist;
	}

	/**
	 * 获取两个日期之间差值的方法
	 * 
	 * @param startdate
	 *            开始日期
	 * @param enddate
	 *            结束日期
	 * @return 两个日期之间的差值
	 */
	public static long betweenTwodates(String startdate, String enddate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd",
				Locale.getDefault());
		long days = 0;
		try {
			Date start = sdf.parse(startdate);
			Date end = sdf.parse(enddate);
			long cha = end.getTime() - start.getTime();
			days = cha / (24 * 60 * 60 * 1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return days;
	}

	/**
	 * 获取指定日期前一天日期
	 * 
	 * @param olddate
	 *            指定日期
	 * @return 得到的新日期
	 */
	public static String getOnedayBefore(String olddate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
				Locale.getDefault());
		Calendar c = Calendar.getInstance();
		try {
			Date date = sdf.parse(olddate);
			c.setTime(date);
			c.add(Calendar.DAY_OF_MONTH, -1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sdf.format(c.getTime());
	}

	/**
	 * 获取指定日期后一天日期
	 * 
	 * @param olddate
	 *            指定日期
	 * @return 得到的新日期
	 */
	public static String getOnedayAfter(String olddate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
				Locale.getDefault());
		Calendar c = Calendar.getInstance();
		try {
			Date date = sdf.parse(olddate);
			c.setTime(date);
			c.add(Calendar.DAY_OF_MONTH, 1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sdf.format(c.getTime());
	}

	/**
	 * 获取两个日期相差是否在三个月以内的方法
	 * 
	 * @return 在三个月以内返回true 否则返回false
	 */
	public static boolean ifInthreeMonth(String startdate, String enddate) {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd",
				Locale.getDefault());
		try {
			Date start = sdf.parse(startdate);
			Date end = sdf.parse(enddate);
			c.setTime(end);
			c.add(Calendar.MONTH, -3);
			System.out.println("==:" + sdf.format(c.getTime()));
			if (start.getTime() - ((c.getTime()).getTime()) >= 0) {
				return true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	/** 将时间转换为指定格式的日期形式 */
	public static String long2Date(long ltime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm",
				Locale.getDefault());
		Date date = new Date(ltime);
		return sdf.format(date);
	}

	/**
	 * 将日期时间字符串转换为指定格式的日期形式
	 * 
	 * @param stringdate
	 *            日期时间字符串 yyyyMMddHHmm
	 * @return yyyy-MM-dd HH:mm
	 */
	public static String stong2Date(String stringdate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm",
				Locale.getDefault());
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm",
				Locale.getDefault());
		Date date;
		try {
			date = sdf.parse(stringdate);
			return sdf2.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 日期格式转换
	 * 
	 * @param transdate
	 *            带转换日期
	 * @param type01
	 *            原始格式
	 * @param type02
	 *            目标格式
	 */
	public static String transFormDate(String transdate, String type01,
			String type02) {
		SimpleDateFormat sdf = new SimpleDateFormat(type01, Locale.getDefault());
		SimpleDateFormat sdf2 = new SimpleDateFormat(type02,
				Locale.getDefault());
		Date date;
		try {
			date = sdf.parse(transdate);
			return sdf2.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取系统当前的年
	 * 
	 * @return YYYY
	 */
	public static String getYear() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy",
				Locale.getDefault());
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getcurrentTimeMillis());
		String dateandtime = format.format(c.getTime());
		return dateandtime;
	}

	/**
	 * 获取系统当前的月
	 * 
	 * @return MM
	 */
	public static String getMounth() {
		SimpleDateFormat format = new SimpleDateFormat("MM",
				Locale.getDefault());
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getcurrentTimeMillis());
		String dateandtime = format.format(c.getTime());
		return dateandtime;
	}

	/**
	 * 获取系统当前的日
	 * 
	 * @return DD
	 */
	public static String getDay() {
		SimpleDateFormat format = new SimpleDateFormat("dd",
				Locale.getDefault());
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getcurrentTimeMillis());
		String dateandtime = format.format(c.getTime());
		return dateandtime;
	}

	/*** 获取当前小时 */
	public static String getHour() {
		SimpleDateFormat format = new SimpleDateFormat("HH",
				Locale.getDefault());
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getcurrentTimeMillis());
		return format.format(c.getTime());
	}

	/** 获取当前分钟 */
	public static String getMinute() {
		SimpleDateFormat format = new SimpleDateFormat("mm",
				Locale.getDefault());
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getcurrentTimeMillis());
		return format.format(c.getTime());
	}

	/** 获取系统当前的秒 */
	public static String getSecond() {
		SimpleDateFormat format = new SimpleDateFormat("HH",
				Locale.getDefault());
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getcurrentTimeMillis());
		return format.format(c.getTime());
	}

	/** 获取系统当前的秒 */
	public static int getMonthLength(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",
				Locale.getDefault());
		Date date1 = null;
		try {
			date1 = format.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);
		Calendar c2 = Calendar.getInstance();
		c2.setTimeInMillis(getcurrentTimeMillis());
		return (c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR)) * 12
				+ c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
	}

	/**
	 * 获取当前时间为基准的任意时间
	 * 
	 * @param file
	 *            1年 2月 3周 5天
	 * @param value
	 *            >0添加file <0减少file
	 * @return yyyy-MM-dd
	 */
	public static String getEveryDate(int file, int value) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(new Date(System.currentTimeMillis()));
		gc.add(file, value);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
				Locale.getDefault());
		return sdf.format(gc.getTime());
	}

	/**
	 * 将日期转为Long型的方法
	 * 
	 * @param date
	 *            传入日期数据
	 * @return 得到的long时间
	 */
	public static Long date2Long(String date) {
		Long longtime = 0L;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
				Locale.getDefault());
		try {
			Date date2 = sdf.parse(date);
			longtime = date2.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return longtime;
	}

	/**
	 * 具体获取网络时间的方法
	 */
	private static void GetTimeMethod() {
		long ld = 0;
		try {
			URL url = new URL("http://www.baidu.com");
			URLConnection uc = url.openConnection();// 生成连接对象
			uc.connect(); // 发出连接
			ld = uc.getDate(); // 取得网站日期时间
			AppContext.initTimeMethod(ld, SystemClock.elapsedRealtime());
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.e(LOG_TAG, e);
		}
	}

	/** 同步时间的方法 */
	public static void synchronizeTime() {
		/** 同步网络时间线程 */
		Thread thread = new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				GetTimeMethod();
			}
		};
		thread.start();
	}
}
