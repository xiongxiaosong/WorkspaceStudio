package com.xxs.sdk.manage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.xxs.sdk.app.AppContext;

import android.app.Service;
import android.content.Intent;

/**
 * 管理本工程中后台服务的类
 * 
 * @author xiongxs
 * @date 2014-11-03
 */
public class ServiceManager {
	/** 服务队列 */
	private final Map<String, Service> serviceQueens;
	public static ServiceManager serviceManage;

	/** 单利模式获取唯一实体对象 */
	public static ServiceManager getMethod() {
		if (serviceManage == null) {
			serviceManage = new ServiceManager();
		}
		return serviceManage;
	}

	/** 构造函数,可以进行一些初始化的操作 */
	public ServiceManager() {
		serviceQueens = Collections
				.synchronizedMap(new HashMap<String, Service>());
	}

	/** 启动Service的方法 */
	public void startServiceMethod(Service service) {
		if (!serviceQueens.containsKey(service.getClass().getName())) {
			AppContext.mMainContext.startService(new Intent(
					AppContext.mMainContext, service.getClass()));
			serviceQueens.put(service.getClass().getName(), service);
		}
	}

	/** 结束Service的方法 */
	public void stopServiceMethod(Service service) {
		if (serviceQueens.containsKey(service.getClass().getName())) {
			Service servi = serviceQueens.get(service.getClass().getName());
			if (servi != null) {
				servi.stopSelf();
			}
			serviceQueens.remove(service.getClass().getName());
		}
	}

	/** 移除Service的方法 */
	public void removeServiceMethod(Service service) {
		if (serviceQueens.containsKey(service.getClass().getName())) {
			serviceQueens.remove(service.getClass().getName());
		}
	}

	/** 停止所有服务的方法 */
	public void stopAllServiceMethod() {
		for (String key : serviceQueens.keySet()) {
			Service servi = serviceQueens.get(key);
			if (servi != null) {
				servi.stopSelf();
			}
		}
		serviceQueens.clear();
	}
}
