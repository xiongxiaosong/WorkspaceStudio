package com.xxs.sdk.myinterface;

/**
 * 自定义计时器回调接口
 * 
 * @author xiongxs
 * 
 */
public interface MyTimerInterCallback {
	/** 剩余计时 */
	public void timerLeft(long lefttime);

	/** 计时结束 */
	public void timerFinish();
}
