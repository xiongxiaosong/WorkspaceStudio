package com.xxs.sdk.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import com.xxs.sdk.app.AppContext;

import android.os.Build;
import android.os.Environment;
import android.util.Log;

/**
 * 对android自带日志的一个简单封装，方便调用
 * 
 * @author yue
 * 
 */
public class LogUtil {

	public static int DEBUG_LEVEL = 1;

	private static boolean isDebug(int level) {
		if (level >= DEBUG_LEVEL) {
			return true;
		} else {
			return false;
		}
	}

	/**冗余信息输出*/ 
	public static void v(String tag, String msg) {
		if (isDebug(0)) {
			Log.v(tag, msg);
			writeLog("v", tag, msg);
		}
	}

	/**调试信息输出*/ 
	public static void d(String tag, String msg) {
		if (isDebug(1)) {
			Log.d(tag, msg);
			writeLog("d", tag, msg);
		}
	}

	/**提示信息输出*/ 
	public static void i(String tag, String msg) {
		if (isDebug(2)) {
			Log.i(tag, msg);
			writeLog("i", tag, msg);
		}
	}

	/**警告信息输出*/ 
	public static void w(String tag, String msg) {
		if (isDebug(3)) {
			Log.w(tag, msg);
			writeLog("w", tag, msg);
		}
	}

	/**错误信息输出*/ 
	public static void e(String tag, String msg) {
		if (isDebug(4)) {
			Log.e(tag, msg);
			writeLog("e", tag, msg);
		}
	}

	public static void e(String tag, Exception e) {
		if (isDebug(4) && e != null) {
			StringWriter mExceptionMsg = new StringWriter();
			e.printStackTrace(new PrintWriter(mExceptionMsg));
			String msg = mExceptionMsg.toString();
			Log.e(tag, msg);
			writeLog("e", tag, msg);
		}
	}

	/**日志记录到文件中*/ 
	private static void writeLog(String flag, String tag, String msg) {
		try {
			if (Build.VERSION.SDK_INT > Build.VERSION_CODES.DONUT
					&& ProveUtil.ifSDCardEnable()) {
				String DATE = DateUtil.getDate();
				String filePath = FileUtil.creatRootLogFile("LOG");
				File mFile = new File(filePath,DATE + ".txt");
				if(!mFile.exists()){
					mFile.createNewFile();
				}
				FileWriter out = new FileWriter(mFile, true);
				out.write("[" + DateUtil.getDateAndTime() + "] [" + flag + "]:"
						+ tag + " : " + msg + "\n\r");
				out.close();
			}
		} catch (IOException e) {
			
		}
	}
}
