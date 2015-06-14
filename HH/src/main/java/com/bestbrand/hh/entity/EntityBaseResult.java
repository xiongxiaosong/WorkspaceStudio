package com.bestbrand.hh.entity;

/**
 * 网络请求返回数据对象基类
 * 
 * @author xiongxs
 * @date 2015-05-14
 * @introduce 本工程中所有封装网络请求返回数据实体对象的类都将继承此类
 */
public class EntityBaseResult {
	/** 状态码 */
	protected int code;
	/** 错误信息 */
	protected String msg;

	/** 获取状态码 */
	public int getCode() {
		return code;
	}

	/** 设置状态码 */
	public void setCode(int code) {
		this.code = code;
	}

	/** 获取错误信息 */
	public String getMsg() {
		return msg;
	}

	/** 设置错误信息 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
