package com.os.activity.sliding;

import com.os.activity.MainActivity;
import com.os.activity.base.BaseSlidingFragment;
import com.os.slidingmenu.R;
import com.os.ui.MainHallFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
public class RightFragment extends BaseSlidingFragment {

	private ImageButton mSearchBtn;
	private TextView mOnlineCountText;
	private TextView[] mMenuTexts;
	private int[] HOST_LEVELS = { MainHallFragment.LEVEL_ALL, MainHallFragment.LEVEL_CROWN, MainHallFragment.LEVEL_DIAMOND, MainHallFragment.LEVEL_STAR,
			MainHallFragment.LEVEL_HEART };
//	private int[] HOST_LEVELS = { MainHallFragment.LEVEL_ALL, MainHallFragment.LEVEL_CROWN, MainHallFragment.LEVEL_DIAMOND, MainHallFragment.LEVEL_STAR,
//			MainHallFragment.LEVEL_HEART };
	private String[] HOST_LEVELS_NAME={"全部","皇冠","钻石","星级","红心"};
	private int mCurrentIndex = -1;
	private boolean isLoading = false;//是否正在加载

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sliding_right);
		setMenuChecked(0);
	}

	@Override
	public void initViews() {

		mMenuTexts = new TextView[] { (TextView) findViewById(R.id.menu_host_level_all), (TextView) findViewById(R.id.menu_host_level_1),
				(TextView) findViewById(R.id.menu_host_level_2), (TextView) findViewById(R.id.menu_host_level_3), (TextView) findViewById(R.id.menu_host_level_4) };
	}

	@Override
	public void addListener() {

		for (int i = 0; i < mMenuTexts.length; i++) {
			mMenuTexts[i].setTag(i);
			mMenuTexts[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int index = (Integer) v.getTag();
					changeMenu(index);
				}
			});
		}
	}

	
	public void changeMenu(int index) {
		if (mCurrentIndex != index) {
			clearMenu();
			setMenuChecked(index);
		}
//		MobclickAgent.onEvent(getActivity(), IConfig.ToRightMenu, HOST_LEVELS_NAME[index]);
		getFragmentActivity(MainActivity.class).filterCenterFragment(MainHallFragment.class, HOST_LEVELS[index]);
		mCurrentIndex = index;
	}

	private void clearMenu() {
		for (int i = 0; i < mMenuTexts.length; i++) {
			if (i != 0) {
				mMenuTexts[i].setBackgroundDrawable(null);
			}
			mMenuTexts[i].setTextColor(getResources().getColor(R.color.gray7));
		}
	}

	private void setMenuChecked(int index) {
		if (index != 0) {
			mMenuTexts[index].setBackgroundResource(R.drawable.sliding_menu_checked_bg);
		}
		mMenuTexts[index].setTextColor(getResources().getColor(R.color.white));
	}

	
	

}
