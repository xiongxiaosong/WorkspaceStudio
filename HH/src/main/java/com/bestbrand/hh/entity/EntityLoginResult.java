package com.bestbrand.hh.entity;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.bestbrand.hh.bean.BeanLoginData;
import com.xxs.sdk.util.LogUtil;

/**
 * 封装登录返回信息的实体对象
 * 
 * @author xiongxs
 * @date 2015-05-14
 */
public class EntityLoginResult extends EntityBaseResult {
	private static String LOG_TAG = EntityLoginResult.class.getName();
	/** 用户信息数据 */
	private BeanLoginData data;

	/** 获取用户信息实体对象 */
	public BeanLoginData getData() {
		return data;
	}

	/** 设置用户信息实体对象 */
	public void setData(BeanLoginData data) {
		this.data = data;
	}
	/**
	 * 将Json解析为EntityLoginResult实体对象的方法
	 * 
	 * @param params
	 *            JSON字符串
	 * @return 得到的EntityLoginResult实体对象
	 */
	public static EntityLoginResult paramsJson(String params) {
		EntityLoginResult bean = null;
		try {
			bean = JSONObject.parseObject(params, EntityLoginResult.class);
		} catch (JSONException e) {
			// TODO: handle exception
			LogUtil.e(LOG_TAG, "JSON数据解析异常");
		}
		return bean;
	}
}
