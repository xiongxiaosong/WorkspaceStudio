package com.xxs.sdk.manage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.xxs.sdk.app.AppContext;
import com.xxs.sdk.util.DateUtil;

/**
 * 管理本地通知的类
 * 
 * @author xiongxs
 * @date 2014-12-22
 */
public class NotifyManager {
	/** 通知管理器 */
	private NotificationManager notificationManager;
	/** 服务队列 */
	private final Map<String, Notification> notifiQueens;
	/** 统计通知队列 */
	private final Map<String, Integer> notifinumQueens;
	/** 单利对象 */
	public static NotifyManager notifyManager;
	private int notifinum;

	/** 单例模式获取唯一对象 */
	public static NotifyManager getMethod() {
		if (notifyManager == null) {
			notifyManager = new NotifyManager();
		}
		return notifyManager;
	}

	/**
	 * 构造函数
	 */
	public NotifyManager() {
		notificationManager = (NotificationManager) AppContext.mMainContext
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notifiQueens = Collections
				.synchronizedMap(new HashMap<String, Notification>());
		notifinumQueens = Collections
				.synchronizedMap(new HashMap<String, Integer>());
	}

	/**
	 * 执行通知提醒的方法
	 * 
	 * @param drawableId
	 *            通知图标Id
	 * @param ticker
	 *            提示信息
	 * @param title
	 *            通知标题
	 * @param message
	 *            通知内容
	 * @param cls
	 *            点击通知过后跳转到的Activity
	 * @param notifiId
	 *            自定义的通知ID
	 * @param singletop
	 *            是否保持在最顶部
	 */
	@SuppressWarnings("deprecation")
	public void notifiMethod(int drawableId, String ticker, String title,
			String message, Class<?> cls, String notifiId, boolean singletop) {
		if (notifiQueens.containsKey(notifiId)) {
			Notification notification = notifiQueens.get(notifiId);
			Intent intent = new Intent(AppContext.mMainContext, cls);
			if(singletop){
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
			}else{
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			}
			PendingIntent pendintent = PendingIntent.getActivity(
					AppContext.mMainContext, 0, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			notification.icon = drawableId;
			notification.tickerText = ticker;
			notification.when = DateUtil.getcurrentTimeMillis();
			notification.contentIntent = pendintent;
			notification.setLatestEventInfo(AppContext.mMainContext, title,
					message, pendintent);
			notificationManager.notify(notifinumQueens.get(notifiId),
					notification);
		} else {
			Intent intent = new Intent(AppContext.mMainContext, cls);
			if(singletop){
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
			}else{
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			}
			PendingIntent pendintent = PendingIntent.getActivity(
					AppContext.mMainContext, 0, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			Notification notification = new Notification();
			notification.icon = drawableId;
			notification.tickerText = ticker;
			notification.when = DateUtil.getcurrentTimeMillis();
			notification.defaults = Notification.DEFAULT_ALL;
			notification.contentIntent = pendintent;
			notification.setLatestEventInfo(AppContext.mMainContext, title,
					message, pendintent);
			notification.flags |= Notification.FLAG_AUTO_CANCEL;// 点击之后自动消失
			notificationManager.notify(notifinum, notification);
			notifiQueens.put(notifiId, notification);
			notifinumQueens.put(notifiId, notifinum);
			notifinum++;
		}
	}

	/** 清空所有通知信息的方法 */
	public void clearAllNotifiMethod() {
		notificationManager.cancelAll();
		notifinumQueens.clear();
	}

	/** 清除指定通知内容的方法 */
	public void cancleNotifiMethod(String notifiId) {
		if (notifinumQueens.containsKey(notifiId)) {
			int id = notifinumQueens.get(notifiId);
			notificationManager.cancel(id);
			notifinumQueens.remove(notifiId);
		}
	}
}
