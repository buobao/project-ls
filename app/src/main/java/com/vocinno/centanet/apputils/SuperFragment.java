package com.vocinno.centanet.apputils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

public abstract class SuperFragment extends Fragment implements OnClickListener {
	public static String TAG = null;
	public Activity mContext = null;
	public Handler mHander = null;
	public static View mRootView = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		TAG = this.getClass().getName();
		mRootView = LayoutInflater.from(mContext).inflate(setContentLayoutId(),
				null);
		mHander = setHandler();
		initView();
		setListener();
		initData();
		return mRootView;
	}

	public abstract int setContentLayoutId();

	public abstract Handler setHandler();

	public abstract void initView();

	public abstract void setListener();

	public abstract void initData();

	@Override
	public void onDestroy() {
		System.gc();
		super.onDestroy();
	}

}
