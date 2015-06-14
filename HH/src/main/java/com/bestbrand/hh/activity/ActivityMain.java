package com.bestbrand.hh.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.bestbrand.hh.R;
import com.bestbrand.hh.fragment.FragmentFind;
import com.bestbrand.hh.fragment.FragmentHh;
import com.bestbrand.hh.fragment.FragmentLeftMenu;
import com.xxs.sdk.activity.XSlidingFragmentActivity;
import com.xxs.sdk.manage.XActivityManager;
import com.xxs.sdk.menu.XSlidingMenu;

/**
 * 主Activity
 * 
 * @author xiaongxs
 * @date 2015-03-31
 * @introduce 程序启动完成后进入的主界面，实现侧边栏效果
 */
public class ActivityMain extends XSlidingFragmentActivity implements
		OnClickListener, OnCheckedChangeListener {
	/** 侧边菜单 */
	public XSlidingMenu slidingMenu;
	/** 侧边菜单片段 */
	private FragmentLeftMenu leftMenu;
	/** 单选组件 */
	private RadioGroup radiogroup;
	/** 新增互友按钮 */
	private Button buttonAdd;
	/** 当前加载的片段 */
	private Fragment mFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		XActivityManager.addActivity(this);
		setContentView(R.layout.activity_main_top);
		setBehindContentView(R.layout.activity_main_behind);
		if (savedInstanceState == null) {
			FragmentTransaction t = this.getSupportFragmentManager()
					.beginTransaction();
			leftMenu = new FragmentLeftMenu();
			t.replace(R.id.activitymain_behindcontent, leftMenu);
			t.commit();
		} else {
			leftMenu = (FragmentLeftMenu) this.getSupportFragmentManager()
					.findFragmentById(R.id.activitymain_behindcontent);
		}
		intSlidingMenu();// 调用初始化侧边菜单的方法
		intViewMethod();// 调用实例化控件的方法
		replaceFragmentMethod(FragmentFind.getInstense());// 调用加载Fragment片段的方法
	}

	/** 初始化侧边菜单 */
	private void intSlidingMenu() {
		slidingMenu = getSlidingMenu();// 得到侧边菜单
		slidingMenu.setBehindScrollScale(0.0f);
		slidingMenu.setMode(XSlidingMenu.LEFT);
		slidingMenu.setTouchModeAbove(XSlidingMenu.TOUCHMODE_FULLSCREEN);
		// 需要更换为设计的背景图
		// slidingMenu.setBackgroundColor(getResources().getColor(R.color.danlanse));
		slidingMenu.setFadeDegree(0.6f);
		slidingMenu.setBehindOffsetRes(R.dimen.setbehindOffsetres);
		// setQQanimation();// 设置类似QQ的滑出效果
	}

	/** 实例化控件的方法 */
	private void intViewMethod() {
		radiogroup = (RadioGroup) findViewById(R.id.activitymain_radiogroup);
		buttonAdd = (Button) findViewById(R.id.activitymain_button_add);
		radiogroup.setOnCheckedChangeListener(this);
		buttonAdd.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		System.out.println("============");
		switch (v.getId()) {
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (group.getId()) {
		case R.id.activitymain_radiogroup:
			switch (checkedId) {
			case R.id.activitymain_radiobutton_find:
				replaceFragmentMethod(FragmentFind.getInstense());
				break;
			case R.id.activitymain_radiobutton_hh:
				replaceFragmentMethod(FragmentHh.getInstense());
				break;
			default:
				break;
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 替换Fragment片段的方法
	 * 
	 * @param fragment
	 *            待替换的片段 注意：该片段需要使用单例模式进行实例化
	 */
	private void replaceFragmentMethod(Fragment fragment) {
		FragmentTransaction transaction = this.getSupportFragmentManager()
				.beginTransaction();
		if (fragment.isAdded()) {
			if (mFragment != fragment) {
				transaction.hide(mFragment).show(fragment).commit(); // 隐藏当前的fragment，显示下一个
			}
		} else {
			if (mFragment != null) {
				transaction.hide(mFragment)
						.add(R.id.activitymain_topcontent, fragment).commit();
			} else {
				transaction.add(R.id.activitymain_topcontent, fragment)
						.commit();
			}
		}
		mFragment = fragment;
	}

}
