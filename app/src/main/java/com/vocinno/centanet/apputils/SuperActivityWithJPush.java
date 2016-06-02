package com.vocinno.centanet.apputils;

import com.vocinno.centanet.apputils.utils.MethodsJni;

import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

public abstract class SuperActivityWithJPush extends InstrumentedActivity
		implements OnClickListener {
	public static String TAG = null;
	public Context mContext = null;
	public Handler mHander = null;
	public static View mRootView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppInstance.mListActivitys.add(this);
		mContext = this;
		TAG = this.getClass().getName();
		mRootView = LayoutInflater.from(this).inflate(setContentLayoutId(),
				null);
		setContentView(mRootView);
		mHander = setHandler();
		initView();
		setListener();
		initData();
	}

	public abstract int setContentLayoutId();

	public abstract Handler setHandler();

	public abstract void initView();

	public abstract void setListener();

	public abstract void initData();

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
		Log.d(TAG, "输出Context 类：" + ((Activity) mContext).getClass().getName());
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	@Override
	protected void onDestroy() {
		Activity activity = AppInstance.mListActivitys
				.get(AppInstance.mListActivitys.size() - 1);
		if (activity.getClass() == this.getClass()) {
			AppInstance.mListActivitys
					.remove(AppInstance.mListActivitys.size() - 1);
		}
		MethodsJni.removeAllNotifications(TAG);
		System.gc();
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		onBack();
	}

	public abstract void onBack();

}
