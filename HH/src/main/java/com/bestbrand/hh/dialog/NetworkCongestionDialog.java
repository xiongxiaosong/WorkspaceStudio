package com.bestbrand.hh.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.LayoutInflater;
import android.view.View;

import com.bestbrand.hh.R;
import com.xxs.sdk.manage.ThreadManage;
import com.xxs.sdk.util.LogUtil;

/**
 * 网络阻塞Dialog
 * 
 * @author xiongxs
 * @date 2015-03-31
 */
public class NetworkCongestionDialog extends Dialog implements OnCancelListener {
	protected NetworkCongestionDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	/** 网络请求Id */
	private String threadId;
	/** 上下文 */
	private Context context;

	/**
	 * 构造函数
	 * 
	 * @param activity
	 */
	public NetworkCongestionDialog(Activity activity) {
		super(activity, R.style.customDialog);
		View view = LayoutInflater.from(activity).inflate(
				R.layout.dialog_networkcongestion, null);
		setContentView(view);
		// setContentView(R.layout.dialog_networkcongestion);
		this.context = activity;
		setOnCancelListener(this);
		setCanceledOnTouchOutside(false);
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		ThreadManage.getMethod().cancleHttpThread(threadId);
		LogUtil.e("移除网络请求线程:", threadId);
	}

	/**
	 * 显示网络阻塞弹窗的方法
	 * 
	 * @param threadId
	 *            网络请求ID
	 */
	public void showDialog(String threadId) {
		this.threadId = threadId;
		if (!this.isShowing()) {
			this.show();
		}
	}

	/**
	 * 隐藏网络阻塞弹窗的方法
	 */
	public void hidDialog() {
		if (this.isShowing()) {
			this.dismiss();
		}
	}
}
