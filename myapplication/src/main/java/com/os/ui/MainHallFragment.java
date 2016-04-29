package com.os.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.os.activity.MainActivity;
import com.os.activity.base.BaseSlidingFragment;
import com.os.adapter.PagerAdapter;
import com.os.slidingmenu.R;
import com.os.slidingmenu.SlidingMenu;
import com.os.ui.ui2.FirstFragment;
import com.os.ui.ui2.SecondFragment;
import com.os.ui.ui2.ThirdFragment;




public class MainHallFragment extends BaseSlidingFragment implements OnClickListener {
	public static final int LEVEL_ALL = -1;// 全部
	public static final int LEVEL_HEART = 0;// 红心
	public static final int LEVEL_STAR = 1;// 星级
	public static final int LEVEL_DIAMOND = 2;// 钻石
	public static final int LEVEL_CROWN = 3;// 皇冠
	private ViewPager mViewPager;
	private TextView mHomePage;
	private TextView mFocus;
	private TextView mMine;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.x_hall);
	}
	@Override
	public void initViews() {
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mHomePage = (TextView) findViewById(R.id.tv_home);
		mFocus = (TextView) findViewById(R.id.tv_focus);
		mMine = (TextView) findViewById(R.id.tv_my);

		PagerAdapter mPagerAdapter = new PagerAdapter(getActivity());

	
		mViewPager.setCurrentItem(0);
		mPagerAdapter.addTab(FirstFragment.class, null);
		mPagerAdapter.addTab(SecondFragment.class, null);
		mPagerAdapter.addTab(ThirdFragment.class, null);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOffscreenPageLimit(2);
	}
	
	@Override
	public void addListener() {
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case 0:
					mHomePage.setTextColor(getResources().getColor(R.color.text_pink));
					mFocus.setTextColor(getResources().getColor(R.color.text_gray));
					mMine.setTextColor(getResources().getColor(R.color.text_gray));
					break;
				case 1:
					mHomePage.setTextColor(getResources().getColor(R.color.text_gray));
					mFocus.setTextColor(getResources().getColor(R.color.text_pink));
					mMine.setTextColor(getResources().getColor(R.color.text_gray));
					break;
				case 2:
					mHomePage.setTextColor(getResources().getColor(R.color.text_gray));
					mFocus.setTextColor(getResources().getColor(R.color.text_gray));
					mMine.setTextColor(getResources().getColor(R.color.text_pink));
					break;
				default:
					MainActivity.mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
					break;
				}
			}

		});
		
		mHomePage.setOnClickListener(this);
		mFocus.setOnClickListener(this);
		mMine.setOnClickListener(this);
	}
	
	
	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		case R.id.tv_home:
			mViewPager.setCurrentItem(0);
			break;
		case R.id.tv_focus:
			mViewPager.setCurrentItem(1);
			break;
		case R.id.tv_my:
			mViewPager.setCurrentItem(2);
			break;
		}
		
	}
	
	
	
	
	public void postScrollTop() {
	
	}
}
