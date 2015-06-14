package com.xxs.sdk.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SuExecUtil {

	private static final String TAG = "SuExecUtil";
	private DataOutputStream mDataOutputStream = null;
	private DataInputStream mDataInputStream = null;
	private DataInputStream mDataErrStream = null;
	private Process mProcess = null;
	private static SuExecUtil mInstance = new SuExecUtil();
	private boolean bRet = false;
	private Thread mThreadGetSu = null;
	private Object installLock = new Object();
	private Object uninstallLock = new Object();

	public static SuExecUtil getInstance() {
		return mInstance;
	}

	// 判断手机是否被ROOT
	public boolean isMobileRoot() {

		String rootpath = xGetCmdPath("su");
		LogUtil.d("SuExecUtil", "rootpath = " + rootpath);
		return (rootpath != null);
	}

	Runnable mRunGetSu = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (mProcess == null || mDataOutputStream == null) {
				boolean bRootOk = false;
				// String cmd = getCmdPath("su");
				Process process = null;
				DataOutputStream dataOutputStream = null;
				DataInputStream dataInputStream = null;
				DataInputStream dataErrStream = null;
				try {
					ProcessBuilder builder = new ProcessBuilder("su");
					builder.redirectErrorStream(true);
					process = builder.start();
					dataOutputStream = new DataOutputStream(process.getOutputStream());
					dataInputStream = new DataInputStream(process.getInputStream());
					dataErrStream = new DataInputStream(process.getErrorStream());
					for (int i = 0; i < 5; i++) {
						if (isRootOk(dataOutputStream, dataInputStream, dataErrStream)) {
							bRootOk = true;
							break;
						}
						Thread.sleep(100);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (process == null || dataInputStream == null || dataOutputStream == null || !bRootOk) {

					if (dataInputStream != null) {
						try {
							dataInputStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						dataInputStream = null;
					}
					if (dataOutputStream != null) {
						try {
							dataOutputStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						dataOutputStream = null;
					}
					if (process != null) {
						process.destroy();
						process = null;
					}
					bRet = false;
				} else {
					bRet = true;
				}

				synchronized (SuExecUtil.this) {
					SuExecUtil.this.mProcess = process;
					SuExecUtil.this.mDataInputStream = dataInputStream;
					SuExecUtil.this.mDataOutputStream = dataOutputStream;
					SuExecUtil.this.mDataErrStream = dataErrStream;
					SuExecUtil.this.mThreadGetSu = null;
				}
			}
		}
	};

	public synchronized void enterRoot() {
		if (mThreadGetSu == null) {
			mThreadGetSu = new Thread(mRunGetSu);
			mThreadGetSu.start();
		}
	}

	private static boolean isRootOk(DataOutputStream output, DataInputStream input, final DataInputStream errInput) {
		if (output != null) {
			try {
				output.writeBytes("id\n");
				output.flush();
				String a = input.readLine();
				if (a.contains("root") || a.contains("uid=0")) {
					return true;
				}
				new Thread() {
					@Override
					public void run() {
						try {
							errInput.read();
						} catch (IOException e) {
							e.printStackTrace();
						}
					};
				}.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean disableAutoBoot(String packageName, List<String> receiverList) {

		StringBuilder sb = new StringBuilder();
		String cmd = getCmdPath("pm");
		for (int i = 0; i < receiverList.size(); i++) {
			sb.append(cmd);
			sb.append(" disable ");
			sb.append(packageName + "/" + receiverList.get(i));
			if (i == receiverList.size() - 1)
				sb.append("\n");
			else
				sb.append(";");
		}
		return execCmd(sb.toString().replace("$", "\\$"));
	}

	public boolean enableAutoBoot(String packageName, List<String> receiverList) {

		StringBuilder sb = new StringBuilder();
		String cmd = getCmdPath("pm");
		for (int i = 0; i < receiverList.size(); i++) {
			sb.append(cmd);
			sb.append(" enable ");
			sb.append(packageName + "/" + receiverList.get(i));
			if (i == receiverList.size() - 1)
				sb.append("\n");
			else
				sb.append(";");
		}
		return execCmd(sb.toString().replace("$", "\\$"));
	}

	private void leaveRoot() {

		if (mDataOutputStream != null) {
			try {
				mDataOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mDataOutputStream = null;
		}
		if (mDataInputStream != null) {
			try {
				mDataInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mDataInputStream = null;
		}
		if (mProcess != null) {
			mProcess.destroy();
			mProcess = null;
		}
	}

	public boolean checkRoot() {
		return (mProcess != null) && (mDataOutputStream != null);
	}

	public boolean install(String filePath) {
		String cmd = getCmdPath("pm");
		StringBuilder sb = new StringBuilder(cmd);
		sb.append(" install -r ");
		sb.append(filePath);
		sb.append("\n");
		return execCmd(sb.toString());
	}

	/**
	 * anjx add 12-03.安装程序并返回，注意返回值为true并不代表安装成功，而是代表命令执行成功
	 * 
	 * @param filePath
	 * @return
	 */
	public synchronized boolean installWithReturn(String filePath) {
		if (mDataOutputStream != null) {
			String cmd = getCmdPath("pm");
			StringBuilder sb = new StringBuilder(cmd);
			sb.append(" install -r ");
			sb.append(filePath);
			sb.append("\n");
			LogUtil.d(TAG, "安装命令：" + sb);
			try {
				mDataOutputStream.writeBytes(sb.toString());
				mDataOutputStream.flush();

				new Thread() {
					public void run() {
						try {
							byte[] data = new byte[256];
							//int len = mDataInputStream.read(data);
							//String status = new String(data, 0, len);
							String status = mDataInputStream.readLine();
							LogUtil.d(TAG, "安装结果：1," + status);
							int n = 2;
							while (!status.contains("Failure") && !status.contains("Success")) {
								//len = mDataInputStream.read(data);
								//status = new String(data, 0, len);
								status = mDataInputStream.readLine();
								LogUtil.d(TAG, "安装结果：" + (n++) + "," + status);
							}

						} catch (IOException e) {
							e.printStackTrace();
						}
						synchronized (installLock) {
							installLock.notify();
						}

					};
				}.start();

				synchronized (installLock) {
					installLock.wait();
				}

				return true;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean uninstall(String name) {

		String cmd = getCmdPath("pm");
		StringBuilder sb = new StringBuilder(cmd);
		sb.append(" uninstall ");
		sb.append(name);
		sb.append("\n");
		return execCmd(sb.toString());
	}

	/**
	 * anjx add 12-03.卸载程序并返回，注意返回值为true并不代表卸载成功，而是代表命令执行成功
	 * 
	 * @param name
	 * @return
	 */
	public synchronized boolean uninstallWithReturn(String name) {

		String cmd = getCmdPath("pm");
		StringBuilder sb = new StringBuilder(cmd);
		sb.append(" uninstall ");
		sb.append(name);
		sb.append("\n");
		LogUtil.d(TAG, "卸载命令：" + sb);
		try {
			mDataOutputStream.writeBytes(sb.toString());
			mDataOutputStream.flush();

			new Thread() {
				public void run() {
					try {
						byte[] data = new byte[256];
						//int len = mDataInputStream.read(data);
						//String status = new String(data, 0, len);
						String status = mDataInputStream.readLine();
						LogUtil.d(TAG, "卸载结果：1," + status);
						int n = 2;
						while (!status.contains("Failure") && !status.contains("Success")) {
							//len = mDataInputStream.read(data);
							//status = new String(data, 0, len);
							status = mDataInputStream.readLine();
							LogUtil.d(TAG, "卸载结果：" + (n++) + "," + status);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					synchronized (uninstallLock) {
						uninstallLock.notify();
					}
				};
			}.start();

			synchronized (uninstallLock) {
				uninstallLock.wait();
			}

			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean rm(String path) {

		String cmd = getCmdPath("rm");
		StringBuilder sb = new StringBuilder(cmd);
		sb.append(" ");
		sb.append(path);
		sb.append("\n");
		return execCmd(sb.toString());
	}

	public boolean rm_dir(String path) {

		String cmd = getCmdPath("rm");
		StringBuilder sb = new StringBuilder(cmd);
		sb.append(" -r ");
		sb.append(path);
		sb.append("\n");
		return execCmd(sb.toString());
	}

	public boolean cp(String path1, String path2) {

		String cmd = getCmdPath("cat");
		StringBuilder sb = new StringBuilder(cmd);
		sb.append(" ");
		sb.append(path1);
		sb.append(" > ");
		sb.append(path2);
		sb.append("\n");
		return execCmd(sb.toString());
	}

	public boolean cp_dir(String path1, String path2) {

		String cmd = getCmdPath("cp");
		StringBuilder sb = new StringBuilder(cmd);
		sb.append(" ");
		sb.append("-r");
		sb.append(" ");
		sb.append(path1);
		sb.append(" ");
		sb.append(path2);
		sb.append("\n");
		return execCmd(sb.toString());
	}

	public String getSystemMountPoint() {

		Process process = null;
		DataInputStream dataInputStream = null;
		String result = null;
		String resultback = null;
		try {
			String cmd = getCmdPath("mount");
			process = Runtime.getRuntime().exec(cmd);
			dataInputStream = new DataInputStream(process.getInputStream());
			process.waitFor();
			while (true) {
				String line = dataInputStream.readLine();
				if (line == null) {
					break;
				}
				if (line.startsWith("/dev") && line.contains("/system")) {
					int j = line.indexOf(' ');
					if (j != -1) {
						resultback = line.substring(0, j);
					}
				}
				if (line.startsWith("/dev/block/") && line.contains("/system")) {
					int j = line.indexOf(' ');
					if (j != -1) {
						result = line.substring(0, j);
					}
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (dataInputStream != null) {
			try {
				dataInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			dataInputStream = null;
		}
		if (process != null) {
			process.destroy();
			process = null;
		}
		return result == null ? resultback : result;
	}

	public boolean mountSystemRW() {

		String point = getSystemMountPoint();
		if (point == null) {
			return false;
		}
		String cmd = getCmdPath("mount");
		StringBuilder sb = new StringBuilder(cmd);
		sb.append(" -o remount,rw ");
		sb.append(point);
		sb.append(" /system\n");
		return execCmd(sb.toString());
	}

	public boolean mountRW(String path, String arg) {

		if (path == null) {
			return false;
		}
		String cmd = getCmdPath("mount");
		StringBuilder sb = new StringBuilder(cmd);
		sb.append(" -o remount,rw ");
		sb.append(path);
		sb.append(" ");
		sb.append(arg + "\n");
		return execCmd(sb.toString());
	}

	public boolean mountRO(String path, String arg) {

		if (path == null) {
			return false;
		}
		String cmd = getCmdPath("mount");
		StringBuilder sb = new StringBuilder(cmd);
		sb.append(" -o remount,ro ");
		sb.append(path);
		sb.append(" ");
		sb.append(arg + "\n");
		return execCmd(sb.toString());
	}

	public boolean mountSystemRO() {

		String point = getSystemMountPoint();
		if (point == null) {
			return false;
		}
		String cmd = getCmdPath("mount");
		StringBuilder sb = new StringBuilder(cmd);
		sb.append(" -o remount,ro ");
		sb.append(point);
		sb.append(" /system\n");
		return execCmd(sb.toString());
	}

	public boolean execCmd(String strCmd) // 需要回车符号
	{

		boolean result = false;
		if (mDataOutputStream != null) {
			try {
				mDataOutputStream.writeBytes(strCmd);
				new StreamGobble(mDataInputStream, "INFO").start();
				new StreamGobble(mDataErrStream, "ERR").start();
				mDataOutputStream.flush();
				result = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	class StreamGobble extends Thread {
		InputStream is;
		String type;

		StreamGobble(InputStream is, String type) {
			this.is = is;
			this.type = type;
		}

		public void run() {
			try {
				byte[] b = new byte[1024];
				while (is.read(b) != -1)
					;
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	private static final String[] bin_dirs = { "/system/sbin/", "/system/xbin/", "/system/bin/" };

	private String xGetCmdPath(String cmd) {

		if (cmd.indexOf('/') != -1) {
			return cmd;
		}
		for (int i = 0; i < bin_dirs.length; i++) {
			try {
				String path = bin_dirs[i] + cmd;
				if (new File(path).exists()) {
					return path;
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		return null;
	}

	private String getCmdPath(String cmd) {
		String s = xGetCmdPath(cmd);
		if (s == null) {
			s = xGetCmdPath("busybox");
			if (s != null) {
				s += " ";
				s += cmd;
			} else {
				s = xGetCmdPath("toolbox");
				if (s != null) {
					s += " ";
					s += cmd;
				}
			}
		}
		String str = (s == null) ? cmd : s;
		return str;
	}
	
	public synchronized static final boolean installAppSilent(String filePath) {
		return installOrUninstallApk(filePath, "install", "-r");
	}

	public synchronized static final boolean uninstallAppSilent(String packageName) {
		return installOrUninstallApk(packageName, "uninstall", "-k");
	}

	private static boolean installOrUninstallApk(String apkPath, String installOruninstall, String rOrP) {
		Process process = null;
		DataOutputStream os = null;
		String command = null;
		try {
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			command = "pm " + installOruninstall + " " + rOrP + " "+ apkPath
					+ " \n";
			os.writeBytes(command);
			os.flush();
			os.close();
			process.waitFor();
			process.destroy();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}
}
