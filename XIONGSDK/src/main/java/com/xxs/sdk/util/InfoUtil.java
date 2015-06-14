package com.xxs.sdk.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.text.format.Formatter;

import com.xxs.sdk.app.AppContext;

/**
 * 获取信息的工具类
 * 
 * @author xiongxs
 * @date 2014-10-08
 * @introduce 包含获取一些应用信息的方法例如包名，手机号码
 */
public class InfoUtil {
	private static String LOG_TAG = InfoUtil.class.getName();

	/**
	 * 用于获取状态栏的高度
	 * 
	 * @return 返回状态栏高度的像素值
	 */
	public static final  int getStatusBarHeight() {
		int statusBarHeight = 0;
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object o = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = (Integer) field.get(o);
			statusBarHeight = AppContext.mMainContext.getResources()
					.getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusBarHeight;
	}

	/**
	 * 获取安装应用版本名
	 *
	 */
	public static final String getVersionName() {
		String version = "";
		PackageManager pm = AppContext.mMainContext.getPackageManager();
		PackageInfo info;
		try {
			info = pm.getPackageInfo(AppContext.mMainContext.getPackageName(),
					0);
			if (info != null) {
				version = String.valueOf(info.versionName);
			}
		} catch (Exception e) {
			LogUtil.e(LOG_TAG, e);
			e.printStackTrace();
		}

		return version;
	}

	/**
	 * 获取安装应用版本号
	 * 
	 */
	public static final String getVersionCode() {
		String versionCode = "";
		try {
			PackageManager pm = AppContext.mMainContext.getPackageManager();
			PackageInfo info = pm.getPackageInfo(
					AppContext.mMainContext.getPackageName(), 0);
			if (info != null) {
				versionCode = String.valueOf(info.versionCode);
			}
		} catch (Exception e) {
			LogUtil.e(LOG_TAG, e);
			// e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * 获取本机电话号码的方法
	 * 
	 * @return 得到的本机电话号码
	 */
	public static String getPhoneNumber() {
		TelephonyManager tm = (TelephonyManager) AppContext.mMainContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getLine1Number();
	}

	/**
	 * 获取设备唯一ID的方法
	 */
	public static String getDeviceId() {
		TelephonyManager manager = (TelephonyManager) AppContext.mMainContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		String deviceId = "";
		if (!TextUtils.isEmpty(manager.getDeviceId())) {
			deviceId = manager.getDeviceId();
		} else if (!TextUtils.isEmpty(getSerialNum())) {
			deviceId = getSerialNum();
		} else if (!TextUtils.isEmpty(getIMEI())) {
			deviceId = getIMEI();
		} else if (!TextUtils.isEmpty(getIMSI())) {
			deviceId = getIMSI();
		} else if (!TextUtils.isEmpty(getInstalltionId())) {
			deviceId = getInstalltionId();
		} else {
			deviceId = "null";
		}
		return deviceId;
	}

	/**
	 * Android系统2.3版本以上可以通过下面的方法得到Serial Number，且非手机设备也可以通过该接口获取。
	 * 
	 * @return
	 */
	public static String getSerialNum() {
		return android.os.Build.SERIAL;
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

	/** 获取IMSI */
	public static String getIMSI() {
		TelephonyManager tm = (TelephonyManager) AppContext.mMainContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = tm.getSubscriberId();
		return imsi == null ? "" : imsi;
	}

	/** 获取IMEI */
	public static String getIMEI() {
		String android_id = "";
		try {
			TelephonyManager telManager = (TelephonyManager) AppContext.mMainContext
					.getSystemService(Context.TELEPHONY_SERVICE);
			android_id = telManager.getDeviceId();
			if (android_id == null) {
				android_id = android.provider.Settings.System.getString(
						AppContext.mMainContext.getContentResolver(),
						"android_id");
			}
		} catch (Exception e) {
		}
		return android_id;
	}

	/** 获取当前是否有SIM卡 */
	public static boolean getSimState() {
		TelephonyManager tm = (TelephonyManager) AppContext.mMainContext
				.getSystemService(Context.TELEPHONY_SERVICE);// 取得相关系统服务
		// StringBuffer sb = new StringBuffer();
		String tag = "SimState";
		boolean result = true;
		switch (tm.getSimState()) { // getSimState()取得sim的状态 有下面6中状态
		case TelephonyManager.SIM_STATE_ABSENT:
			LogUtil.i(tag, "无卡");
			break;
		// 目前测试，一般只有平板是这个状态
		case TelephonyManager.SIM_STATE_UNKNOWN:
			LogUtil.i(tag, "未知状态");
			result = false;
			break;
		case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
			LogUtil.i(tag, "需要NetworkPIN解锁");
			break;
		case TelephonyManager.SIM_STATE_PIN_REQUIRED:
			LogUtil.i(tag, "需要PIN解锁");
			break;
		case TelephonyManager.SIM_STATE_PUK_REQUIRED:
			LogUtil.i(tag, "需要PUK解锁");
			break;
		case TelephonyManager.SIM_STATE_READY:
			LogUtil.i(tag, "良好");
			LogUtil.i(tag, "IMSI:" + tm.getSubscriberId());
			break;
		}
		return result;
	}

	/**
	 * 获取手机型号
	 * 
	 * @return 得到的手机型号 即设备名称
	 */
	public static String getModel() {
		return android.os.Build.MODEL;
	}

	/**
	 * 获取手机厂商
	 * 
	 * @return
	 */
	public static String getManufacturer() {
		return android.os.Build.MANUFACTURER;
	}

	/***
	 * 获取CPU信息
	 * 
	 * @return
	 */
	public static String getCpu() {
		return android.os.Build.CPU_ABI;
	}

	/**
	 * 获取屏幕高度
	 * 
	 * @return 屏幕高度的像素值
	 */
	public static int getScreenHeight() {
		return AppContext.mMainContext.getResources().getDisplayMetrics().heightPixels;
	}

	/**
	 * 获取屏幕宽度
	 * 
	 * @return 屏幕宽度的像素值
	 */
	public static int getScreenWidth() {
		return AppContext.mMainContext.getResources().getDisplayMetrics().widthPixels;
	}

	/**
	 * 获取手机号
	 */
	public static String getMobileNumber(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String num = tm.getLine1Number();
		return num == null ? "" : num;
	}

	/**
	 * 是否有root权限
	 * 
	 * @return 0:非root,1:root
	 */
	public static int getRootAhth() {
		return SuExecUtil.getInstance().isMobileRoot() ? 1 : 0;
		// return 0;
	}

	/**
	 * 获取国家
	 */
	public static String getCountry() {
		Locale locale = Locale.getDefault();
		return locale.getCountry();
	}

	/**
	 * 获取CellID
	 */
	public static int getCellID(Context context) {
		GsmCellLocation location = null;
		try {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			location = (GsmCellLocation) tm.getCellLocation();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (location != null)
			return location.getCid();
		else
			return -1;
	}

	/**
	 * 获取当前系统的APN信息
	 */
	public static String getAPNInfo(Context context) {
		try {
			ConnectivityManager conManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (conManager != null) {
				NetworkInfo info = conManager
						.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				return info.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取WIFI MAC地址
	 */
	public static String getWifiMac() {
		WifiInfo wifiInfo = null;
		try {
			WifiManager wifiManager = (WifiManager) AppContext.mMainContext
					.getSystemService(Context.WIFI_SERVICE);
			wifiInfo = wifiManager.getConnectionInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (wifiInfo != null && wifiInfo.getMacAddress() != null)
			return wifiInfo.getMacAddress(); // 获取WIFI_MAC地址
		else
			return "";
	}

	/**
	 * 获取wifi名称
	 * 
	 * @param context
	 * @return
	 */
	public static String getSSID(Context context) {
		WifiInfo wifiInfo = null;
		try {
			WifiManager wifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			wifiInfo = wifiManager.getConnectionInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (wifiInfo != null && wifiInfo.getSSID() != null)
			return wifiInfo.getSSID(); // 获取WIFI_MAC名称
		else
			return "";
	}

	/**
	 * 蓝牙Mac地址
	 */
	public static String getBluetoothAddress() {
		try {
			BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
			if (btAdapter != null) {
				String address = btAdapter.getAddress();// 获取蓝牙地址
				return address;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取SD序列号
	 */
	public static String getSdcardId() {
		// 判断SD卡是否存在，如果不存在返回null
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			int maxId = -1;
			try {
				// 经过调研，SD序列号就是"/sys/block/mmcblk+n/device/cid"
				// n为/sys/block/mmcblk中，取mmcblk中下标最大的值
				Process proc = Runtime.getRuntime().exec("ls /sys/block/");
				InputStreamReader ir = new InputStreamReader(
						proc.getInputStream());
				LineNumberReader input = new LineNumberReader(ir);
				String str = input.readLine();
				Pattern p = Pattern.compile("mmcblk[0-9]$");
				while (str != null) {
					Matcher m = p.matcher(str);
					if (m.matches()) {
						int index = Integer.parseInt(str.substring(6));
						if (index > maxId)
							maxId = index;
					}
					str = input.readLine();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			String path = "/sys/block/mmcblk" + maxId + "/device/cid";
			File file = new File(path);
			if (file.exists()) {
				try {
					BufferedReader CID = new BufferedReader(
							new FileReader(path));
					String sd_cid = CID.readLine();
					CID.close();
					return sd_cid;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return "";
		} else
			// sd卡不存在
			return "";
	}

	/**
	 * 获取运营商
	 */
	public static String getOperator(Context context) {
		String ProvidersName = "";
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		// 返回唯一的用户ID
		String IMSI = tm.getSubscriberId();
		// IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信
		if (IMSI != null) {
			if (IMSI.startsWith("46000") || IMSI.startsWith("46002")
					|| IMSI.startsWith("46007")) {
				ProvidersName = "中国移动";
			} else if (IMSI.startsWith("46001")) {
				ProvidersName = "中国联通";
			} else if (IMSI.startsWith("46003")) {
				ProvidersName = "中国电信";
			}
			LogUtil.d(LOG_TAG, "operator: " + IMSI);
		} else {
			LogUtil.d(LOG_TAG, "operator is null");
		}

		return ProvidersName;
	}

	/**
	 * 获取SD已用空间
	 */
	public static long getSDUsedSize(Context context) {
		String path = FileUtil.getExtSDCardPath();
		if (path == null) {
			path = FileUtil.getSDCardPath();
		}
		if (path == null) {
			return 0;
		} else {
			return getSDCardTotalSize(path) - getSDCardAvailableSize(path);
		}
	}

	/**
	 * 获取SD可用空间
	 */
	public static String getSDAvailableSize(Context context) {
		// anjx 0305修改，优先读取外置SD卡容量，读取不到时再读内置SD卡容量
		String path = FileUtil.getExtSDCardPath();
		if (path == null) {
			path = FileUtil.getSDCardPath();
		}
		if (path == null) {
			return "";
		} else {
			return Formatter.formatFileSize(context,
					getSDCardAvailableSize(path));
		}

	}

	/**
	 * 获取SD总容量
	 */
	public static long getSDTotalSize(Context context) {
		// anjx 0305修改，优先读取外置SD卡容量，读取不到时再读内置SD卡容量
		String path = FileUtil.getExtSDCardPath();
		if (path == null) {
			path = FileUtil.getSDCardPath();
		}
		if (path == null) {
			return 0;
		} else {
			return getSDCardTotalSize(path);
		}

	}

	/**
	 * 获取SD总容量(格式化)
	 */
	public static String getSDTotalSizeString(Context context) {
		// anjx 0305修改，优先读取外置SD卡容量，读取不到时再读内置SD卡容量
		String path = FileUtil.getExtSDCardPath();
		if (path == null) {
			path = FileUtil.getSDCardPath();
		}
		if (path == null) {
			return "";
		} else {
			return Formatter.formatFileSize(context, getSDCardTotalSize(path));
		}

	}

	/**
	 * 获得SD卡可用空间
	 * 
	 * @author anjx
	 * @return
	 */
	public static long getSDCardAvailableSize(String pathStr) {
		// 取得sdcard文件路径

		if (pathStr == null) {
			return 0;
		}
		StatFs stat = new StatFs(pathStr);

		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	/**
	 * 获得SD卡总容量
	 * 
	 * @author anjx
	 * @return
	 */
	public static long getSDCardTotalSize(String pathStr) {
		if (pathStr == null) {
			return 0;
		}
		StatFs stat = new StatFs(pathStr);
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getBlockCount();
		return availableBlocks * blockSize;
	}

	/**
	 * 获取当前系统的可用内存
	 */
	public static long getRamAvailableSize(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);// mi.availMem; 当前系统的可用内存
		return mi.availMem;

	}

	/**
	 * 获取当前系统的可用内存(格式化)
	 */
	public static String getRamAvailableSizeString(Context context) {
		long mem = getRamAvailableSize(context);
		String rs = Formatter.formatFileSize(context, mem);// 将获取的内存大小规格化
		return rs;
	}

	/**
	 * 获取当前系统的内存总大小
	 */
	public static long getRamTotalSize(Context context) {
		String file_path = "/proc/meminfo";// 系统内存信息文件
		String ram_info;
		String[] arrayOfRam;
		long initial_memory = 0;
		try {
			FileReader fr = new FileReader(file_path);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			// 读取meminfo第一行，系统总内存大小
			ram_info = localBufferedReader.readLine();
			arrayOfRam = ram_info.split("\\s+");// 实现多个空格切割的效果
			initial_memory = Integer.valueOf(arrayOfRam[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
			localBufferedReader.close();
		} catch (IOException e) {
		}
		return initial_memory;
	}

	/**
	 * 获取当前系统的内存总大小(格式化)
	 */
	public static String getRamTotalSizeString(Context context) {
		long size = getRamTotalSize(context);
		return Formatter.formatFileSize(context, size);
	}

	/**
	 * 获取系统ROM可用空间
	 */
	public static long getRomAvailableSize(Context context) {
		File root = Environment.getDataDirectory();
		StatFs sf = new StatFs(root.getPath());
		long blockSize = sf.getBlockSize();
		long availCount = sf.getAvailableBlocks();
		return availCount * blockSize;
	}

	/**
	 * 获取系统ROM可用空间(格式化)
	 */
	public static String getRomAvailableSizeString(Context context) {
		long size = getRomAvailableSize(context);
		return Formatter.formatFileSize(context, size);
	}

	/**
	 * 获取系统ROM总容量
	 */
	public static long getRomTotalSize(Context context) {
		File root = Environment.getDataDirectory();
		StatFs sf = new StatFs(root.getPath());
		long blockSize = sf.getBlockSize();
		long blockCount = sf.getBlockCount();
		return blockCount * blockSize;
	}

	/**
	 * 获取系统ROM总容量(格式化)
	 */
	public static String getRomTotalSizeString(Context context) {
		long size = getRomTotalSize(context);
		return Formatter.formatFileSize(context, size);
	}

	/**
	 * 获取系统已经使用的总容量
	 * 
	 * @param context
	 * @return
	 */
	public static long getRomUsedSize(Context context) {
		File root = Environment.getDataDirectory();
		StatFs sf = new StatFs(root.getPath());
		long blockSize = sf.getBlockSize();
		long blockCount = sf.getBlockCount() - sf.getAvailableBlocks();
		return blockCount * blockSize;
	}

	/**
	 * 获取系统已经使用的总容量(格式化)
	 * 
	 * @param context
	 * @return
	 */
	public static String getRomUsedSizeString(Context context) {
		long size = getRomUsedSize(context);
		return Formatter.formatFileSize(context, size);
	}

	/**
	 * 获取基带版本
	 */
	public static String getBasebandVer() {
		Object result = null;
		try {
			Class<?> cl = Class.forName("android.os.SystemProperties");
			Object invoker = cl.newInstance();
			Method m = cl.getMethod("get", new Class[] { String.class,
					String.class });
			result = m.invoke(invoker, new Object[] { "gsm.version.baseband",
					"no message" });
		} catch (Exception e) {
		}
		return result.toString();
		// return android.os.Build.VERSION.INCREMENTAL;
	}

	/**
	 * 获取内核版本
	 */
	public static String getKernelVer() {
		String path = "/proc/version";
		File file = new File(path);
		String kernelVersion = null;
		if (file.exists()) {
			try {
				BufferedReader version = new BufferedReader(
						new FileReader(path));
				kernelVersion = version.readLine();
				String[] subStrs = kernelVersion.split(" ");
				StringBuffer buffer = new StringBuffer();
				buffer.append(subStrs[2] + " ");
				buffer.append(subStrs[3] + " ");
				buffer.append(subStrs[9]);
				kernelVersion = buffer.toString();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return kernelVersion;
	}

	/**
	 * 得到当前IP
	 */
	public static String getIP() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& inetAddress instanceof Inet4Address) {
						return inetAddress.getHostAddress().toString();

					}
				}
			}
		} catch (SocketException ex) {
			LogUtil.e("WifiPreference IpAddress", ex.toString());
		}
		return "";
	}

	/**
	 * 电池当前电量百分比
	 */
	public static double getBatteryLevel(Context context) {

		try {
			IntentFilter ifilter = new IntentFilter(
					Intent.ACTION_BATTERY_CHANGED);
			Intent battery = context.getApplicationContext().registerReceiver(
					null, ifilter);
			int level = battery.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
			int scale = battery.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
			// System.out.println("level=" + level + ",scale=" + scale);
			double batteryPct = (double) level / scale;
			return batteryPct;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 电池是否在充电
	 * 
	 * @return 0：未充电，1：在充电
	 */
	public static String getBatteryState(Context context) {
		IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent battery = context.getApplicationContext().registerReceiver(null,
				ifilter);
		int status = battery.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
		boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING
				|| status == BatteryManager.BATTERY_STATUS_FULL;
		return isCharging ? "1" : "0";
	}

	/**
	 * 获取系统运行时间
	 */
	public static String getBootUp() {
		InputStreamReader ir = null;
		LineNumberReader input = null;
		try {
			Process proc = Runtime.getRuntime().exec("cat /proc/uptime");
			ir = new InputStreamReader(proc.getInputStream());
			input = new LineNumberReader(ir);
			String str = input.readLine();
			if (str != null) {
				// System.out.println(str);
				String s[] = str.split(" ");
				if (s.length > 1)
					return s[0];
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if (input != null)
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (ir != null)
				try {
					ir.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return "-1";
	}

	/**
	 * 取得数据漫游是否打开
	 * 
	 * @return 1:打开，0:未打开
	 */
	public static String getRoaming(Context context) {
		ContentResolver cv = context.getContentResolver();
		return android.provider.Settings.System.getString(cv,
				android.provider.Settings.System.DATA_ROAMING);
	}

	public static String getAllowUnkownSource(Context context) {
		ContentResolver cv = context.getContentResolver();
		return android.provider.Settings.System.getString(cv,
				android.provider.Settings.System.INSTALL_NON_MARKET_APPS);
	}

	public static final void printNetworkState(Context context) {
		WifiManager wm = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		if (wm != null) {
			LogUtil.d(LOG_TAG, "wifi state:" + wm.getWifiState()
					+ ",is enable:" + wm.isWifiEnabled());
		} else {
			LogUtil.d(LOG_TAG, "WifiManager is null");
		}
		ConnectivityManager cManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mi = cManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mi != null) {
			LogUtil.d(LOG_TAG, "mobile network state:" + mi.getState()
					+ ",is available:" + mi.isAvailable());
		} else {
			LogUtil.d(LOG_TAG, "NetworkInfo is null");
		}
	}

	/**
	 * 比较系统版本号
	 * 
	 * @param v1
	 * @param v2
	 * @return 0--等；-1--小；1--大
	 */
	public static int compareVersion(String v1, String v2) {
		if (v1 == null) {
			return -1;
		}

		if (v2 == null) {
			return 1;
		}

		if (v1.equals(v2)) {
			return 0;
		}

		String[] arrayStr1 = v1.split("\\.");
		String[] arrayStr2 = v2.split("\\.");

		int leng1 = arrayStr1.length;
		int leng2 = arrayStr2.length;
		int leng = (leng1 > leng2) ? leng2 : leng1;

		int result = 0;
		for (int i = 0; i < leng; i++) {
			result = arrayStr1[i].compareTo(arrayStr2[i]);
			if (result != 0) {
				return result > 0 ? 1 : -1;
			}
		}

		if (result == 0) {
			if (leng1 > leng2) {
				result = 1;
			} else if (leng1 < leng2) {
				result = -1;
			}
		}

		return result;
	}

	/**
	 * 生成设备Udid
	 * 
	 * @return
	 */
	public static String getUdid() {
		try {
			String imei = getIMEI();
			String mac = getWifiMac();
			String content = imei + mac;
			MessageDigest md;
			md = MessageDigest.getInstance("MD5");
			md.update(content.getBytes("UTF-8"));
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取系统版本号
	 * 
	 * @return
	 */
	public static String getOsVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * 获取 Installtion ID 即UUID
	 * 
	 * @return
	 */
	public static String getInstalltionId() {
		String uuid;
		if (TextUtils.isEmpty((String) SharedUtil.readSharedMethod(
				"deviceInfo", "deviceid", ""))) {
			uuid = UUID.randomUUID().toString();
			SharedUtil.writeSharedMethod("deviceInfo", "deviceid", uuid);
		} else {
			uuid = (String) SharedUtil.readSharedMethod("deviceInfo",
					"deviceid", "");
		}
		return uuid;
	}

}
