package com.xxs.sdk.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

//需要权限:
//android.permission.ACCESS_COARSE_LOCATION
//android.permission.ACCESS_FINE_LOCATION
//模拟器还需要的权限:android.permission.ACCESS_MOCK_LOCATION
//保证这个类只存在一个实例,因为GPS设备只有一个，如果多个类的话手机会出问题
public class GPSInfoProvider {
	LocationManager manager;
	private static GPSInfoProvider mGPSInfoProvider;
	private static Context context;
	private static MyLoactionListener listener;
    //1.私有化构造方法
	private GPSInfoProvider(){};
	
    //2. 提供一个静态的方法 可以返回他的一个实例，同步块就会保证里面所有的内容都会做完不会被打断
	public static synchronized GPSInfoProvider getInstance(Context context){
		if(mGPSInfoProvider==null){
			mGPSInfoProvider = new GPSInfoProvider();
			GPSInfoProvider.context = context;
		}
		return mGPSInfoProvider;
	}
	// 获取gps信息 
	public String getLocation(){
		manager =(LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		//manager.getAllProviders(); gps,wifi返回手机所有的定位方式都返回回来
		String provider = getProvider(manager);
		// 注册位置的监听器，当位置发生改变的时候就会调用这个回调方法
		//第一参数为位置提供着，第二个参数每隔多少毫秒更新位置的信息最小值都是1分钟，
//		第三个参数位置改变多少去获取gps的信息，第四个参数位置改变的时候对应的回调方法
		manager.requestLocationUpdates(provider,60000, 50, getListener());
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		String location = sp.getString("location", "");
		return location;
	}
	/**获取经度、纬度的方法*/
	public double[] getLatitudeAndLongitude(){
		double[] dd=new double[]{};
		return dd;
	}
	 //@param manager 位置管理服务
	 //@return 最好的位置提供者
	private String getProvider(LocationManager manager){
		Criteria criteria = new Criteria();//设置最好的位置提供者的一些查询条件
		criteria.setAccuracy(Criteria.ACCURACY_FINE);//设置定位精度，为精准的位置
		criteria.setAltitudeRequired(false);//是否对海拔信息敏感
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);//设置对电力的要求，要求电力为中等
		criteria.setSpeedRequired(true);//对速度是否敏感
		criteria.setCostAllowed(true);//是否产生一些必要的开销
		return  manager.getBestProvider(criteria, true);//true只返回打开的设备
	}
	
	// 停止gps监听
	public void stopGPSListener(){
		manager.removeUpdates(getListener());//这句代码就是停止gps的监听
	}
	
	private synchronized MyLoactionListener getListener(){//得到一个LocationListener实例
		if(listener==null){
			listener = new MyLoactionListener();
		}
		return listener;
	}
	
	private class MyLoactionListener implements LocationListener{

		//当手机位置发生改变的时候 调用的方法
		public void onLocationChanged(Location location) {
			String latitude ="latitude "+ location.getLatitude(); //纬度 
			String longtitude = "longtitude "+ location.getLongitude(); //精度
			SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putString("location", latitude+" - "+ longtitude);
			editor.commit(); //最后一次获取到的位置信息 存放到sharedpreference里面
		}

		//某一个设备的状态发生改变的时候 调用 可用->不可用  不可用->可用，对应参数，为那个位置提供者，状态，额外的参数
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		//provider设备被打开调用的方法
		public void onProviderEnabled(String provider) {
		}

		//provider个设备被禁用调用的方法
		public void onProviderDisabled(String provider) {
		}
	}
}
