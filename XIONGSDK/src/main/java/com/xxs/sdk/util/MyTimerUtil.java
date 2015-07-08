package com.xxs.sdk.util;


import com.xxs.sdk.myinterface.MyTimerInterCallback;

/**
 * 自定义计时器
 * 
 * @author 熊小松
 * @time 2014-01-07
 * 
 */
public class MyTimerUtil extends CountDownTimer {
	/** 计时器回调接口 */
	private MyTimerInterCallback callback;

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            上下文
	 * @param millisInFuture
	 *            倒计时间总数（单位毫秒）
	 * @param countDownInterval
	 *            每次计时间隔时间（单位毫秒）
	 * @param myInterFace
	 *            自定义计时器回调接口
	 */
	public MyTimerUtil(long millisInFuture, long countDownInterval,
			MyTimerInterCallback callback) {
		super(millisInFuture, countDownInterval);
		// TODO Auto-generated constructor stub
		this.callback = callback;
	}

	/**
	 * 每次计时都会调用的方法
	 */
	@Override
	public void onTick(long millisUntilFinished) {
		// TODO Auto-generated method stub
		callback.timerLeft(millisUntilFinished/1000);
	}

	/**
	 * 计时完成后将要自动调用的方法
	 */
	@Override
	public void onFinish() {
		callback.timerFinish();
	}
	public void onCancle(){
		callback.timerFinish();
	}
}
