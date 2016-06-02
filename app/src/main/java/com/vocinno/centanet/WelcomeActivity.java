package com.vocinno.centanet;

import java.util.ArrayList;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.SharedPreferencesUtils;
import com.vocinno.centanet.apputils.SuperActivity;
import com.vocinno.centanet.apputils.adapter.MyPagerAdapter;
import com.vocinno.centanet.apputils.adapter.MyPagerAdapter.MType;
import com.vocinno.centanet.user.UserLoginActivity;
import com.vocinno.utils.MethodsExtra;

import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

//第一次运行的引导页代码
public class WelcomeActivity extends SuperActivity implements
		OnPageChangeListener, OnClickListener {
	private Context mContext;
	private ViewPager mViewPager;
	private PagerAdapter mPagerAdapter;
	private Button mStartButton;
	private LinearLayout mIndicatorLayout;
	private ArrayList<View> mViews;
	private ImageView[] mIndicators = null;
	private int[] mImages;

	@Override
	public Handler setHandler() {
		return null;
	}

	@Override
	public int setContentLayoutId() {
		return R.layout.activity_welcome;
	}

	// 初始化视图
	@Override
	public void initView() {
		// 实例化视图控件
		mContext = this;
		// 创建桌面快捷方式
		MethodsExtra.CreateShortCut(this);
		// 设置引导图片
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 仅需在这设置图片 指示器和page自动添加
		mImages = new int[] { R.drawable.welcome_01, R.drawable.welcome_02,
				R.drawable.welcome_03 };
		mViewPager = (ViewPager) findViewById(R.id.viewpage);
		mStartButton = (Button) findViewById(R.id.btn_start_WelcomeActivity);
		mStartButton.setOnClickListener(this);
		mIndicatorLayout = (LinearLayout) findViewById(R.id.llyt_indicator_WelcomeActivity);
		mViews = new ArrayList<View>();
		mIndicators = new ImageView[mImages.length]; // 定义指示器数组大小
		for (int i = 0; i < mImages.length; i++) {
			// 循环加入图片
			ImageView imageView = new ImageView(mContext);
			imageView.setBackgroundResource(mImages[i]);
			mViews.add(imageView);
			// 循环加入指示器
			mIndicators[i] = new ImageView(mContext);
			mIndicators[i].setBackgroundResource(R.drawable.indicators_default);
			if (i == 0) {
				mIndicators[i].setBackgroundResource(R.drawable.indicators_now);
			}
			mIndicatorLayout.addView(mIndicators[i]);
		}
		mPagerAdapter = new MyPagerAdapter((Activity) mContext, mViews,
				MType.Normal);
		mViewPager.setAdapter(mPagerAdapter); // 设置适配器
		mViewPager.setOnPageChangeListener(this);
	}

	@Override
	public void setListener() {
	}

	@Override
	public void initData() {
	}

	@Override
	public void onBack() {
		MethodsExtra.backdailog(mContext);
	}

	// 按钮的点击事件
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_start_WelcomeActivity) {
			SharedPreferencesUtils.setIsFirstStartFinish(this, true);
			startActivity(new Intent(WelcomeActivity.this,
					UserLoginActivity.class));
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			this.finish();
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	// 监听viewpage
	@Override
	public void onPageSelected(int arg0) {
		// 显示最后一个图片时显示按钮
		if (arg0 == mIndicators.length - 1) {
			mStartButton.setVisibility(View.VISIBLE);
		} else {
			mStartButton.setVisibility(View.INVISIBLE);
		}
		// 更改指示器图片
		for (int i = 0; i < mIndicators.length; i++) {
			mIndicators[arg0].setBackgroundResource(R.drawable.indicators_now);
			if (arg0 != i) {
				mIndicators[i]
						.setBackgroundResource(R.drawable.indicators_default);
			}
		}
	}

	@Override
	public void notifCallBack(String name, String className, Object data) {

	}
}
