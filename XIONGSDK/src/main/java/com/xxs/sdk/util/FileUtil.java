package com.xxs.sdk.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import android.os.Environment;
import android.util.Log;

import com.xxs.sdk.app.AppContext;

/**
 * 文件操作工具类
 * 
 * @author xiongxs
 * @date 2014-10-08
 * @introduce 包括文件的创建、删除、读写等方法操作
 */
public class FileUtil {
	private static Object lock = new Object();
	private static boolean isWaiting = false;
	private static int mRunCount = 0;
	private static String mFormatingPath = null;
	public static final String[] EXTERNAL_SD_PATHS = {
			"/mnt/sdcard/external_sd", "/mnt/emmc", "/mnt/extSdCard",
			"/mnt/sdcard/extstorages/sdcard", "/mnt/sdcard/extStorages/SdCard",
			"/mnt/sdcard/Removable/MicroSD", "/mnt/sdcard-ext", "/mnt/sdcard1",
			"/mnt/sdcard/sdcard1", "/mnt/sdcard/_ExternalSD",
			"/storage/sdcard1", "/storage/extSdCard", "/mnt/sdcard/ext_sd",
			"/mnt/ext_sdcard", "/mnt/external_sd", "/mnt/external1",
			"/mnt/sdcard2", "/Removable/MicroSD", "/mnt/extsd" };
	private static String TAG = FileUtil.class.getName();

	/**
	 * 创建指定名称根目录的方法
	 * 
	 * @param dirname
	 *            根目录名称
	 */
	public static String creatRootLogFile(String dirname) {
		String rootDir = null;
		if (ProveUtil.ifSDCardEnable()) {
			rootDir = Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ "/Android/data/"
					+ AppContext.mMainContext.getPackageName()
					+ File.separator
					+ dirname;

			// 创建文件目录
			File file = new File(rootDir);
			if (!file.exists()) // 创建目录
			{
				file.mkdirs();
			}
		} else {
			rootDir = AppContext.mMainContext.getCacheDir() + File.separator
					+ dirname;
			// 创建文件目录
			File file = new File(rootDir);
			if (!file.exists()) // 创建目录
			{
				file.mkdirs();
			}
		}
		return rootDir;
	}
	/**
	 * 获得内置SD卡路径
	 * 
	 * @author anjx
	 * @return
	 */
	public static final String getSDCardPath() {
		String sdPath = Environment.getExternalStorageDirectory().getPath();
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return sdPath;
		} else {
			return null;
		}
	}

	/**
	 * 获得外置SD卡路径
	 * 
	 * @author anjx
	 * @return
	 */
	public static final String getExtSDCardPath() {
		List<String> mountList = FileUtil.getExternalMounts();
		List<String> sdPathList = Arrays.asList(FileUtil.EXTERNAL_SD_PATHS);
		for (String path : sdPathList) {
			if (mountList.contains(path)) {
				return path;
			}
		}
		return null;
	}
	/**
	 * 格式化所有正在挂载的SD卡。
	 * 
	 * @return
	 */
	public synchronized static void formatAllSdCards() {
		// anjx 0411 按照系统信息里的展示逻辑调整了获得sd卡路径的方式
		// 目前存在的一个问题是，有的sd卡的确是外置的，但我们识别不出来，所以在这里把内置和外置都进行删除，避免遗漏。
		// 缺点是内置SD卡会在系统wipe的时候自行格式化，这里多了一步步需要的操作。
		List<String> mountList = new ArrayList<String>();
		String extPath = getExtSDCardPath();
		if (extPath != null) {
			mountList.add(extPath);
		}
		String sdPath = getSDCardPath();
		if (sdPath != null) {
			mountList.add(sdPath);
		}

		for (String path : mountList) {
			deleteAllData(path);
			LogUtil.d(TAG, "已删除SD卡上文件：" + path);
		}
	}

	public static final void deleteAllData(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		try {
			File[] filenames = file.listFiles();
			if (filenames != null && filenames.length > 0) {
				for (File tempFile : filenames) {
					if (tempFile.isDirectory()) {
						wipeDirectory(tempFile.toString());
						tempFile.delete();
					} else {
						tempFile.delete();
					}
				}
			} else {
				file.delete();
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}

	private static class DaemonThread extends Thread {
		private int mLocalRunCount;

		public DaemonThread(int runCount) {
			this.mLocalRunCount = runCount;
		}

		public void run() {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synchronized (lock) {
				if (isWaiting && mLocalRunCount == mRunCount) {
					Log.d(TAG, "等待卸载广播超时！");
					lock.notify();
				}
			}
		}
	}

	public static final void notifySdUnmounted(String path) {
		Log.d(TAG, "监测到SD卡卸载：" + path);
		synchronized (lock) {
			if (mFormatingPath != null && mFormatingPath.equals(path)
					&& isWaiting) {
				Log.d(TAG, "进行唤醒");
				lock.notify();
			} else {
				Log.d(TAG, "不匹配:" + mFormatingPath + "," + isWaiting);
			}
		}
	}

	private static void wipeDirectory(String name) {
		File directoryFile = new File(name);
		File[] filenames = directoryFile.listFiles();
		if (filenames != null && filenames.length > 0) {
			for (File tempFile : filenames) {
				if (tempFile.isDirectory()) {
					wipeDirectory(tempFile.toString());
					tempFile.delete();
				} else {
					tempFile.delete();
				}
			}
		} else {
			directoryFile.delete();
		}
	}

	/**
	 * 获得系统内置及外置SD卡
	 * 
	 * @return
	 */
	public static List<String> getExternalMounts() {
		final List<String> out = new ArrayList<String>();
		File file = new File("/proc/mounts");
		if (file.exists()) {
			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader(file));

				String line = null;
				String reg = "(?i).*vold.*(vfat|ntfs|exfat|fat32|ext3|ext4).*rw.*";
				while ((line = br.readLine()) != null) {
					if (!line.toLowerCase(Locale.US).contains("asec")) {
						if (line.matches(reg)) {
							String[] parts = line.split(" ");
							for (String part : parts) {
								if (part.startsWith("/"))
									if (!part.toLowerCase(Locale.US).contains(
											"vold"))
										out.add(part);
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		if (out.size() == 0
				&& Environment.MEDIA_MOUNTED.equals(Environment
						.getExternalStorageState())) {
			LogUtil.d(TAG, "查找SD卡失败，使用系统API");
			out.add(Environment.getExternalStorageDirectory().getAbsolutePath());
		}
		LogUtil.d(TAG, "getExternalMounts:" + out);
		return out;
	}

	/**
	 * 获得SD存储个数
	 * 
	 * @return
	 */
	public int getSDCardNum() {
		File file = new File("//sys/block");
		if (file.exists()) {
			FilenameFilter filter = new FilenameFilter() {
				@Override
				public boolean accept(File file, String s) {
					return file.isDirectory() && s.startsWith("mmcblk");
				}
			};
			File[] files = file.listFiles(filter);
			return files.length;
		}
		return 0;
	}
}
