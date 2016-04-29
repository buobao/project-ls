package com.os.activity.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;


/**
 * fragment基类
 * 
 * @author admin
 * 
 */
public abstract class BaseFragment extends Fragment {

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	
	public abstract void tabNextRecharge();
	public abstract void refreshUserData();
	/**
	 * 添加引导图片
	 */
	public void addGuideImage(int guideResourceId) {
	
	}
}
