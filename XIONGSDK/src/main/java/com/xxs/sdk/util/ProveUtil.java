package com.xxs.sdk.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.telephony.TelephonyManager;

import com.xxs.sdk.app.AppContext;

/**
 * 验证格式或者状态的工具类
 * 
 * @author xiongxs
 * @date 2014-10-08
 */
public class ProveUtil {
	private static String LOG_TAG = ProveUtil.class.getName();

	/**
	 * SD是否可用
	 * 
	 * @return SD卡可用返回true否则返回false
	 */
	public static boolean ifSDCardEnable() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	/**
	 * 验证邮箱格式是否正确的方法
	 * 
	 * @param strEmail
	 *            传入的邮箱地址字符串
	 * @return 是邮箱地址返回true，否则返回false
	 */
	public static boolean isEmail(String strEmail) {
		String strPattern = "^[a-zA-Z0-9_-]+@([a-zA-Z0-9_-])+(\\.([a-zA-Z0-9_-])+)+$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail);
		return m.matches();
	}

	/**
	 * 验证手机号码格式是否正确
	 * 
	 * @param num
	 *            传入的手机号码
	 * @return 是电话返回true，否则返回false
	 */
	public static boolean isMobile(String num) {
		boolean isValid = false;
		String expre = "(^(13|15|18|14)[0-9]{9}$)";
		// String expre = "^\\(?(\\d{3})\\)?[-]?(\\d{3})[-]?(\\d{5})$";
		// CharSequence inputStr = num;
		Pattern pattern = Pattern.compile(expre);
		Matcher matcher = pattern.matcher(num);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	/**
	 * 验证电话号码格式是否正确
	 * 
	 * @param num
	 *            传入的电话号码字符串
	 * @return 是电话返回true，否则返回false
	 */
	public static boolean isPhone(String num) {
		boolean isValid = false;
		// String expre = "^\\(?(\\d{3})\\)?[-]?(\\d{3})[-]?(\\d{5})$";
		String expre = "((^(13|15|18|14)[0-9]{9}$)|(^0[0-9]{1}\\d{1}[-]?\\d{8}$)|(^0[0-9]{1}\\d{2}[-]?\\d{7,8}$)|(^0[0-9]{1}\\d{1}[-]?\\d{8}-(\\d{1,4})$)|(^0[0-9]{1}\\d{2}[-]?\\d{7,8}-(\\d{1,4})$))";
		// CharSequence inputStr = num;
		Pattern pattern = Pattern.compile(expre);
		Matcher matcher = pattern.matcher(num);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	/**
	 * 判断是否含有中文字符
	 * 
	 * @param string
	 *            传入的字符串
	 * @return 含有返回true 否则返回false
	 */
	public static boolean ifhaschinese(String string) {
		boolean haschinese = false;
		String expre = "[\u4e00-\u9fa5]";
		CharSequence inputStr = string;
		Pattern pattern = Pattern.compile(expre);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.find()) {
			haschinese = true;
		}
		return haschinese;
	}

	/**
	 * 判断是否是平板
	 * 
	 * @return 是平板返回true 否则返回false
	 */
	public static boolean isTabletDevice() {
		int screenLayout = AppContext.mMainContext.getResources()
				.getConfiguration().screenLayout;
		int cur = screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
		if (cur == Configuration.SCREENLAYOUT_SIZE_UNDEFINED)
			return false;
		return cur >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	/**
	 * 验证url的合法性
	 * 
	 * @param url
	 * @return 合法返回true 否则返回false
	 */
	public static boolean isUrl(String url) {
		return url.matches("^[a-zA-z]+://[^\\s]*$");
	}

	/***
	 * 是否只包含数字
	 * 
	 * @param str
	 *            待验证的字符串
	 * @return 只包含数字返回true 否则返回false
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]+");
		return pattern.matcher(str).matches();
	}

	/**
	 * 是否只包含字母
	 * 
	 * @param str
	 *            待验证的字符串
	 * @return 只包含字母返回true 否则返回false
	 */
	public static boolean isAlpha(String str) {
		String strPattern = "[a-zA-Z]+$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(str);
		return m.matches();
	}

	/**
	 * 是否只包含字母和中文
	 * 
	 * @param str
	 *            待验证的字符串
	 * @return 只包含字母和中文返回true 否则返回false
	 */
	public static boolean ifHasOnlyStr(String str) {
		String strPattern = "[a-zA-Z\u4e00-\u9fa5]+$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(str);
		return m.matches();
	}

	/**
	 * 是否只包含数字和字母
	 * 
	 * @param str
	 *            待验证的字符串
	 * @return 验证通过返回true 否则返回false
	 */
	public static boolean isAlphaNumeric(String str) {
		String strPattern = "[0-9a-zA-Z]+$";
		// String strPattern = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{2,}$";?
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(str);
		return m.matches();
	}

	/**
	 * 判断GPS是否打开
	 * 
	 * @return 已经打开返回true false返回false
	 */
	public static final boolean isGPSOPen() {
		LocationManager locationManager = (LocationManager) AppContext.mMainContext
				.getSystemService(Context.LOCATION_SERVICE);
		// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
		boolean gps = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
		// boolean network = locationManager
		// .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (gps) {
			return true;
		}
		return false;
	}

	/**
	 * 判断当前网络是否可用
	 * 
	 * @param context
	 *            上下文
	 * @return 有可以使用的网络返回true 否则返回false
	 */
	public static boolean IfHasNet() {
		ConnectivityManager cm = (ConnectivityManager) AppContext.mMainContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo network = cm.getActiveNetworkInfo();
		if (network != null && network.getState() != null) {
			if (network.getState() == State.CONNECTED
					|| network.getState() == State.CONNECTING) {
				return true;
			}
			// return network.isAvailable();
		}
		return false;
	}

	/**
	 * 判断是否打开WIFI的方法
	 * 
	 * @param context
	 *            上下文
	 * @return 是则返回true 否则返回false
	 */
	public static boolean isWifiOpen() {
		ConnectivityManager connectivityManager = (ConnectivityManager) AppContext.mMainContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	/**
	 * 判断当前网络是否是3G网络
	 * 
	 * @return 是返回true 否则返回false
	 */
	public static boolean is3G() {
		ConnectivityManager connectivityManager = (ConnectivityManager) AppContext.mMainContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			return true;
		}
		return false;
	}

	/**
	 * 判断网络是否为漫游
	 */
	public static boolean isNetworkRoaming() {
		ConnectivityManager connectivityManager = (ConnectivityManager) AppContext.mMainContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager == null) {
			LogUtil.e(LOG_TAG, "couldn't get connectivity manager");
		} else {
			NetworkInfo info = connectivityManager.getActiveNetworkInfo();
			if (info != null
					&& info.getType() == ConnectivityManager.TYPE_MOBILE) {
				TelephonyManager tm = (TelephonyManager) AppContext.mMainContext
						.getSystemService(Context.TELEPHONY_SERVICE);
				if (tm != null && tm.isNetworkRoaming()) {
					LogUtil.e(LOG_TAG, "network is roaming");
					return true;
				} else {
					LogUtil.e(LOG_TAG, "network is not roaming");
				}
			} else {
				LogUtil.e(LOG_TAG, "not using mobile network");
			}
		}
		return false;
	}

	/**
	 * 判断手机网络是否可用
	 * 
	 * @return 是返回true 否则返回false
	 * @throws Exception
	 */
	public static boolean isMobileDataEnable() throws Exception {
		ConnectivityManager connectivityManager = (ConnectivityManager) AppContext.mMainContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean isMobileDataEnable = false;

		isMobileDataEnable = connectivityManager.getNetworkInfo(
				ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();

		return isMobileDataEnable;
	}

	/**
	 * 判断日期时间格式是否正确
	 * 
	 * @param time
	 *            传入的日期时间字符串 yyyy-MM-dd HH:mm
	 * @return 正确返回true，否则返回false
	 */
	public static boolean isDateIsRight(String time) {
		try {
			SimpleDateFormat sss = new SimpleDateFormat("yyyy-MM-dd HH:mm",
					Locale.getDefault());
			sss.parse(time);
			String[] ss = time.split("[-: ]");
			int year = Integer.valueOf(ss[0]);
			int month = Integer.valueOf(ss[1]);
			int day = Integer.valueOf(ss[2]);
			int hour = Integer.valueOf(ss[3]);
			int minute = Integer.valueOf(ss[4]);
			if (year < 1 || year > 2099 || month < 1 || month > 12) {// 判断年月
				return false;
			}
			int[] monthLengths = new int[] { 0, 31, -1, 31, 30, 31, 30, 31, 31,
					30, 31, 30, 31 };
			if (isLeapYear(year)) {// 判断是否闰年
				monthLengths[2] = 29;
			} else {
				monthLengths[2] = 28;
			}
			int monthLength = monthLengths[month];
			if (day < 1 || day > monthLength) {// 判断每月的天数
				return false;
			}
			if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {// 判断时分
				return false;
			}
			return true;
		} catch (ParseException e) {// 日期时间格式错误
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (NumberFormatException e) {// 数据格式转换错误
			e.printStackTrace();
			return false;
		}
	}

	/** 是否是闰年 */
	private static boolean isLeapYear(int year) {
		return ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0);
	}

	/**
	 * 判断是否是在桌面
	 * 
	 * @return 在桌面true 不在桌面false
	 */
	@SuppressWarnings("deprecation")
	public static boolean isHome() {
		ActivityManager mActivityManager = (ActivityManager) AppContext.mMainContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
		List<String> homeList = new ArrayList<String>();
		PackageManager packageManager = AppContext.mMainContext
				.getPackageManager();
		// 属性
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(
				intent, PackageManager.MATCH_DEFAULT_ONLY);
		for (ResolveInfo ri : resolveInfo) {
			homeList.add(ri.activityInfo.packageName);
		}
		return homeList.contains(rti.get(0).topActivity.getPackageName());
	}
	/**得到运行在最上层App的包名*/
	@SuppressWarnings("deprecation")
	public static String getTopAppName() {
		ActivityManager mActivityManager = (ActivityManager) AppContext.mMainContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
		return rti.get(0).topActivity.getPackageName();
	}
}
