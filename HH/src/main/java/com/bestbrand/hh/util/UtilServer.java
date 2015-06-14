package com.bestbrand.hh.util;

/**
 * 保存服务接口地址常量的类
 * 
 * @author xiongxs
 * @date 2015-05-09
 */
public class UtilServer {
	/**网络请求根地址*/
	public final static String ROOTURL = "http://192.168.0.104:8080/api";
	/**用户登录接口*/
	public static final String LOGINUSER = ROOTURL+"/clientUser/loginUsers";
	/**用户注册接口*/
	public static final String REGISTERUSER = ROOTURL+"/clientUser/registeredUsers";
	/**获取用户详细信息接口*/
	public static final String GETUSERINFO = ROOTURL+"/clientUser/getUsersInfo";
	/**重置密码接口*/
	public static final String RESTPASSWORD = ROOTURL+"/clientUser/restPassword";
	/**修改昵称接口*/
	public static final String RESETNICKNAME = ROOTURL+"/clientUser/restNickname";
	/**修改个人资料接口*/
	public static final String UPDATEUSERS = ROOTURL+"/clientUser/updateUsers";
	/**添加好友接口*/
	public static final String ADDFRIEND = ROOTURL+"/friend/addFriend";
	/**添加好友接口*/
	public static final String DELETEFRIEND = ROOTURL+"/friend/deleteFriend";
	/**查看好友接口*/
	public static final String GETFRIEND = ROOTURL+"/friend/getFriend";
}
