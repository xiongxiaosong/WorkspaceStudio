package com.xxs.sdk.thread;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.apache.http.HttpStatus;

import android.os.Handler;
import android.os.Message;

import com.xxs.sdk.R;
import com.xxs.sdk.app.AppContext;
import com.xxs.sdk.manage.ThreadManage;
import com.xxs.sdk.myinterface.XDownLoadCallback;
import com.xxs.sdk.util.FileUtil;
import com.xxs.sdk.util.LogUtil;
import com.xxs.sdk.util.ProveUtil;

/**
 * 网络请求线程
 *
 * @author xiongxs
 * @date 2014-10-08
 * @introduce 实现网络求数据请求的线程，得到请求结果并且发送回调消息
 */
public class XDownLoadThread extends Thread {
	private static String LOG_TAG = XDownLoadThread.class.getName();
	private XDownLoadCallback httpCallBack;
	private String path;
	private String threadId;
	private String filename;
	private boolean iscancle;
	private HttpURLConnection httpUrlConn;
	/** 总长度 */
	private long totlelength;
	/** 当前长度 */
	private long nowlength;
	/** 已经下载的长度 */
	private long hasdownlength;

	/** 构造函数 */
	public XDownLoadThread() {

	}

	/**
	 * 构造函数
	 *
	 * @param threadId
	 *            线程ID,通过程序控制生成唯一的ID序列
	 * @param httpCallBack
	 *            下载请求回调接口
	 * @param path
	 *            请求路径
	 * @param filename
	 *            下载保存名称
	 */
	public XDownLoadThread(String threadId, XDownLoadCallback httpCallBack,
						   String path, String filename) {
		this.httpCallBack = httpCallBack;
		this.path = path;
		this.threadId = threadId;
		this.filename = filename;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		handler.sendEmptyMessage(5);
		Message msg = new Message();
		try {
			if (ProveUtil.IfHasNet()) {
				File savefile = new File(FileUtil.creatRootLogFile("Download"),
						filename);
				File tempfile = new File(FileUtil.creatRootLogFile("Download"),
						filename.substring(0, filename.lastIndexOf("."))
								+ ".tmp");
				if (savefile.exists()) {
					savefile.delete();
				}
				FileOutputStream fos;
				if (tempfile.exists()) {
					hasdownlength = tempfile.length();
					nowlength = hasdownlength;
					fos = new FileOutputStream(tempfile, true);
				} else {
					fos = new FileOutputStream(tempfile);
				}
				URL url = new URL(path);
				httpUrlConn = (HttpURLConnection) url.openConnection();
				httpUrlConn.setRequestProperty("RANGE", "bytes="
						+ hasdownlength + "-");//特别设置断点续传
				totlelength = httpUrlConn.getContentLength();
				if (totlelength < 0) {// 如果读取出来为-1重新设置一下对Gzip不支持再获取
					httpUrlConn.disconnect();
					httpUrlConn = (HttpURLConnection) url.openConnection();
					httpUrlConn.setRequestProperty("Accept-Encoding",
							"identity");
					httpUrlConn.setRequestProperty("RANGE", "bytes="
							+ hasdownlength + "-"); //特别设置断点续传
					totlelength = httpUrlConn.getContentLength();
				}
				totlelength = totlelength + hasdownlength;
				if (!(httpUrlConn.getResponseCode() == HttpStatus.SC_OK||httpUrlConn.getResponseCode()==HttpStatus.SC_PARTIAL_CONTENT)) {
					handler.sendEmptyMessage(7);
					fos.close();
					return;
				}
				InputStream inputStream = new BufferedInputStream(
						httpUrlConn.getInputStream());
				// 缓存
				byte buf[] = new byte[1024 * 1024];
				int numread = inputStream.read(buf);
				while (numread != -1 && !iscancle) {
					nowlength += numread;
					fos.write(buf, 0, numread);
					fos.flush();
					numread = inputStream.read(buf);
					handler.sendEmptyMessage(8);
				}
				fos.close();
				inputStream.close();
				if (!iscancle)
					tempfile.renameTo(savefile);
				handler.sendEmptyMessage(4);
			} else {
				msg.what = 0;
				handler.sendEmptyMessage(0);
			}
		} catch (UnsupportedEncodingException e) {
			msg.what = 1;
			handler.sendMessage(msg);
			LogUtil.e(LOG_TAG, e);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			msg.what = 2;
			handler.sendMessage(msg);
			LogUtil.e(LOG_TAG, e);
		} catch (SocketTimeoutException e) {
			msg.what = 6;
			handler.sendMessage(msg);
			LogUtil.e(LOG_TAG, e);
		} catch (IOException e) {
			msg.what = 3;
			handler.sendMessage(msg);
			LogUtil.e(LOG_TAG, e);
		}
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (!iscancle) {
				if (!(msg.what == 5 || msg.what == 8)) {
					ThreadManage.getMethod().removeDownloadThread(threadId);
				}
				switch (msg.what) {
					case 0:// 无网络
						httpCallBack
								.onXDownLoadFailed(
										threadId,
										new Exception(
												AppContext.mMainContext
														.getResources()
														.getString(
																R.string.nonetwork_please_checknet)));
						break;
					case 1:// 不支持编码
						httpCallBack
								.onXDownLoadFailed(
										threadId,
										new Exception(
												AppContext.mMainContext
														.getResources()
														.getString(
																R.string.notsurport_encodtransform)));
						break;
					case 2:// 请求地址错误
						httpCallBack.onXDownLoadFailed(
								threadId,
								new Exception(AppContext.mMainContext
										.getResources().getString(
												R.string.net_adress_error)));
						break;
					case 3:// 数据请求失败
						httpCallBack
								.onXDownLoadFailed(
										threadId,
										new Exception(
												AppContext.mMainContext
														.getResources()
														.getString(
																R.string.falsegetdata_please_checknet)));
						break;
					case 4:// 下载完成
						httpCallBack.onXDownLoadFinish(threadId);
						break;
					case 5:// 开始请求网络
						httpCallBack.onXDownLoadStart(threadId);
						break;
					case 6:// 请求超时
						httpCallBack.onXDownLoadFailed(
								threadId,
								new Exception(AppContext.mMainContext
										.getResources().getString(
												R.string.netsocket_timeout)));
						break;
					case 7:// 网络错误
						httpCallBack.onXDownLoadFailed(
								threadId,
								new Exception(AppContext.mMainContext
										.getResources().getString(
												R.string.network_error)));
						break;
					case 8:// 下载中
						httpCallBack.onXDownLoadLoad(threadId, totlelength,
								nowlength);
						break;
					default:
						break;
				}
			}
		}
	};

	/** 断开Http连接的方法 */
	public void disConnectDownloadMethod() throws Exception {
		if (httpUrlConn != null)
			httpUrlConn.disconnect();
	}

	/** 取消Http连接的方法 */
	public void cancleDownloadMethod() {
		iscancle = true;
		httpCallBack.onXDownLoadCancle(threadId);
	}
}
