package com.bestbrand.hh.fragment;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.bestbrand.hh.R;
import com.bestbrand.hh.dialog.NetworkCongestionDialog;
import com.xxs.sdk.manage.XFragmentManager;
import com.xxs.sdk.myinterface.XHttpCallBack;

/**
 * Fragment基类
 * 
 * @author xiongxs
 * @date 2015-03-31
 * @introduce 本工程中所有Fragment的公共父类
 * @interface <ul>
 *            <li>OnClickListener view点击事件监听</li>
 *            <li>OnItemClickListener adapter item点击事件监听</li>
 *            <li>XHttpCallBack 自定义网络请求回调接口 {@link com.xxs.sdk.myinterface.XHttpCallBack}</li>
 *            </ul>
 */
public class FragmentBase extends Fragment implements OnClickListener,
		OnItemClickListener, XHttpCallBack {
	/** 当做上下文使用 */
	protected Activity mActivity;
	/** 数据加载等待提示 */
	protected NetworkCongestionDialog loding;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		XFragmentManager.addFragment(this);
		mActivity = getActivity();
		intLoadDialog();
	}
	/** 初始化等待提示框的方法 */
	private void intLoadDialog() {
		loding = new NetworkCongestionDialog(mActivity);
		// loding.setTitle(getResources().getString(R.string.prompt));
		// loding.setMessage(getResources().getString(R.string.data_loading));
	}
	@Override
	public ViewGroup onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LinearLayout rootlaLayout = (LinearLayout) inflater.inflate(
				R.layout.fragment_base, container, false);
		return rootlaLayout;
	}
	/**
	 * 获取公共请求参数的方法(例如:deviceId)
	 * 
	 * @return 得到的请求参数
	 * @introduce 公共请求参数统一放到这里,调用接口是先获取公共参数然后再后面追加剩余参数,下面公共参数根据项目自行添加
	 */
	protected Map<String, String> getHttpBaseParamsMethod() {
		Map<String, String> parameters = new HashMap<String, String>();
		// JSONObject obj = new JSONObject();
		// try {
		// obj.put("deviceId", InfoUtil.getDeviceId());
		// obj.put("token", SharedUtil.readSharedMethod(
		// SharedStatic.UserInfos.SHARED_NAME,
		// SharedStatic.UserInfos.USER_TOKEN, ""));
		// parameters.put("result", obj.toString());
		// } catch (JSONException e) {
		// e.printStackTrace();
		// LogUtil.e(LOG_TAG, e);
		// }
		return parameters;
	}
	@Override
	public void preExecuteHttp(String threadId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void succedExecuteHttp(String threadId, String result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void failExecuteHttp(String threadId,int code, Exception e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void cancleExecuteHttp(String threadId) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
