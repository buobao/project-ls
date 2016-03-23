package com.vocinno.centanet.apputils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import cn.jpush.android.api.JPushInterface;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.dialog.ModelDialog;
import com.vocinno.centanet.apputils.dialog.MyDialog;
import com.vocinno.centanet.apputils.selfdefineview.SlideMenu;
import com.vocinno.centanet.customermanage.CustomerManageActivity;
import com.vocinno.centanet.home.HomeActivity;
import com.vocinno.centanet.housemanage.HouseManageActivity;
import com.vocinno.centanet.housemanage.HouseType;
import com.vocinno.centanet.keymanage.KeyGetInActivity;
import com.vocinno.centanet.keymanage.KeyManageActivity;
import com.vocinno.centanet.remind.MessageListActivity;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJni;
import com.zbar.lib.CaptureActivity;

public abstract class SuperSlideMenuActivity extends Activity implements
		OnClickListener {
	public static String TAG = null;
	public Activity mContext = null;
	public Handler mHander = null;
	public static View mRootView = null;
	public MyDialog.Builder myDialog;
	private RelativeLayout mRyltSellHouse, mRyltRentHouse, mRyltSeeHouse,
			mRyltMyHouse, mRyltKeyHouse, mRyltRemind, mRyltMyCustomer,
			mRyltGrabHouse, mRyltGrabCustomer, mRyltInputPassword, mRyltScan;

	public SlideMenu mSlidMenu;
	public LinearLayout mLlytContainer;
	public ModelDialog modelDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppInstance.mListActivitys.add(this);
		mContext = this;
		TAG = this.getClass().getName();
		mRootView = LayoutInflater.from(this).inflate(
				R.layout.activity_super_slide_menu, null);
		setContentView(mRootView);
		mSlidMenu = (SlideMenu) findViewById(R.id.slide_menu_superSlideMenuActivity);
		mLlytContainer = (LinearLayout) findViewById(R.id.llyt_superSlideMenuActivity);

		mRyltSellHouse = (RelativeLayout) findViewById(R.id.rlyt_sell_house_main_page_slid_menus);
		mRyltRentHouse = (RelativeLayout) findViewById(R.id.rlyt_rent_house_main_page_slid_menus);
		mRyltSeeHouse = (RelativeLayout) findViewById(R.id.rlyt_see_house_main_page_slid_menus);
		mRyltMyHouse = (RelativeLayout) findViewById(R.id.rlyt_my_house_main_page_slid_menus);
		mRyltKeyHouse = (RelativeLayout) findViewById(R.id.rlyt_key_house_main_page_slid_menus);
		mRyltMyCustomer = (RelativeLayout) findViewById(R.id.rlyt_my_customer_main_page_slid_menus);
		mRyltGrabHouse = (RelativeLayout) findViewById(R.id.rlyt_grab_house_main_page_slid_menus);
		mRyltGrabCustomer = (RelativeLayout) findViewById(R.id.rlyt_grab_customer_main_page_slid_menus);
		mRyltInputPassword = (RelativeLayout) findViewById(R.id.rlyt_password_main_page_slid_menus);
		mRyltScan = (RelativeLayout) findViewById(R.id.rlyt_sacn_customer_main_page_slid_menus);
		mRyltRemind = (RelativeLayout) findViewById(R.id.rlyt_remind_customer_main_page_slid_menus);
		mRyltSellHouse.setOnClickListener(this);
		mRyltRentHouse.setOnClickListener(this);
		mRyltSeeHouse.setOnClickListener(this);
		mRyltMyHouse.setOnClickListener(this);
		mRyltKeyHouse.setOnClickListener(this);
		mRyltMyCustomer.setOnClickListener(this);
		mRyltGrabHouse.setOnClickListener(this);
		mRyltGrabCustomer.setOnClickListener(this);
		mRyltInputPassword.setOnClickListener(this);
		mRyltScan.setOnClickListener(this);
		mRyltRemind.setOnClickListener(this);

		View layoutView = LayoutInflater.from(this).inflate(
				setContentLayoutId(), null);
		mLlytContainer.addView(layoutView);

		mHander = setHandler();
		initView();
		setListener();
		initData();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rlyt_sell_house_main_page_slid_menus:
			if ((MethodsDeliverData.mIntHouseType == HouseType.CHU_SHOU
					|| MethodsDeliverData.mIntHouseType == HouseType.CHU_ZU
					|| MethodsDeliverData.mIntHouseType == HouseType.YUE_KAN || MethodsDeliverData.mIntHouseType == HouseType.WO_DE)
					&& AppInstance.mListActivitys
							.get(AppInstance.mListActivitys.size() - 1) instanceof HouseManageActivity) {
				MethodsDeliverData.mIntHouseType = HouseType.CHU_SHOU;
				AppInstance.mHouseManageActivity
						.switchHouseType(HouseType.CHU_SHOU);
			} else if (AppInstance.mListActivitys
					.get(AppInstance.mListActivitys.size() - 1) instanceof HouseManageActivity) {
				MethodsDeliverData.mIntHouseType = HouseType.CHU_SHOU;
				AppInstance.mListActivitys.get(
						AppInstance.mListActivitys.size() - 1).finish();
				startActivity(new Intent(mContext, HouseManageActivity.class));
			} else {
				MethodsDeliverData.mIntHouseType = HouseType.CHU_SHOU;
				MethodsExtra.startActivity(mContext, HouseManageActivity.class);
				clearActivity();
			}
			sendMessageCloseMenu();
			break;
		case R.id.rlyt_rent_house_main_page_slid_menus:
			if ((MethodsDeliverData.mIntHouseType == HouseType.CHU_SHOU
					|| MethodsDeliverData.mIntHouseType == HouseType.CHU_ZU
					|| MethodsDeliverData.mIntHouseType == HouseType.YUE_KAN || MethodsDeliverData.mIntHouseType == HouseType.WO_DE)
					&& AppInstance.mListActivitys
							.get(AppInstance.mListActivitys.size() - 1) instanceof HouseManageActivity) {
				MethodsDeliverData.mIntHouseType = HouseType.CHU_ZU;
				AppInstance.mHouseManageActivity
						.switchHouseType(HouseType.CHU_ZU);
			} else if (AppInstance.mListActivitys
					.get(AppInstance.mListActivitys.size() - 1) instanceof HouseManageActivity) {
				MethodsDeliverData.mIntHouseType = HouseType.CHU_ZU;
				AppInstance.mListActivitys.get(
						AppInstance.mListActivitys.size() - 1).finish();
				startActivity(new Intent(mContext, HouseManageActivity.class));
			} else {
				MethodsDeliverData.mIntHouseType = HouseType.CHU_ZU;
				MethodsExtra.startActivity(mContext, HouseManageActivity.class);
				clearActivity();
			}
			sendMessageCloseMenu();
			break;
		case R.id.rlyt_see_house_main_page_slid_menus:
			if ((MethodsDeliverData.mIntHouseType == HouseType.CHU_SHOU
					|| MethodsDeliverData.mIntHouseType == HouseType.CHU_ZU
					|| MethodsDeliverData.mIntHouseType == HouseType.YUE_KAN || MethodsDeliverData.mIntHouseType == HouseType.WO_DE)
					&& AppInstance.mListActivitys
							.get(AppInstance.mListActivitys.size() - 1) instanceof HouseManageActivity) {
				MethodsDeliverData.mIntHouseType = HouseType.YUE_KAN;
				AppInstance.mHouseManageActivity
						.switchHouseType(HouseType.YUE_KAN);
			} else if (AppInstance.mListActivitys
					.get(AppInstance.mListActivitys.size() - 1) instanceof HouseManageActivity) {
				MethodsDeliverData.mIntHouseType = HouseType.YUE_KAN;
				AppInstance.mListActivitys.get(
						AppInstance.mListActivitys.size() - 1).finish();
				startActivity(new Intent(mContext, HouseManageActivity.class));

			} else {
				MethodsDeliverData.mIntHouseType = HouseType.YUE_KAN;
				MethodsExtra.startActivity(mContext, HouseManageActivity.class);
				clearActivity();
			}
			sendMessageCloseMenu();
			break;
		case R.id.rlyt_my_house_main_page_slid_menus:
			if ((MethodsDeliverData.mIntHouseType == HouseType.CHU_SHOU
					|| MethodsDeliverData.mIntHouseType == HouseType.CHU_ZU
					|| MethodsDeliverData.mIntHouseType == HouseType.YUE_KAN || MethodsDeliverData.mIntHouseType == HouseType.WO_DE)
					&& AppInstance.mListActivitys
							.get(AppInstance.mListActivitys.size() - 1) instanceof HouseManageActivity) {
				MethodsDeliverData.mIntHouseType = HouseType.WO_DE;
				AppInstance.mHouseManageActivity
						.switchHouseType(HouseType.WO_DE);
			} else if (AppInstance.mListActivitys
					.get(AppInstance.mListActivitys.size() - 1) instanceof HouseManageActivity) {
				MethodsDeliverData.mIntHouseType = HouseType.WO_DE;
				AppInstance.mListActivitys.get(
						AppInstance.mListActivitys.size() - 1).finish();
				startActivity(new Intent(mContext, HouseManageActivity.class));
			} else {
				MethodsDeliverData.mIntHouseType = HouseType.WO_DE;
				MethodsExtra.startActivity(mContext, HouseManageActivity.class);
				clearActivity();
			}
			sendMessageCloseMenu();
			break;
		case R.id.rlyt_key_house_main_page_slid_menus:
			if (!(AppInstance.mListActivitys.get(AppInstance.mListActivitys
					.size() - 1) instanceof KeyManageActivity)) {
				MethodsExtra.startActivity(mContext, KeyManageActivity.class);
				clearActivity();
			}
			sendMessageCloseMenu();
			break;
		case R.id.rlyt_my_customer_main_page_slid_menus:
			if (MethodsDeliverData.isMyCustomer
					&& AppInstance.mListActivitys
							.get(AppInstance.mListActivitys.size() - 1) instanceof CustomerManageActivity) {
			} else if (AppInstance.mListActivitys
					.get(AppInstance.mListActivitys.size() - 1) instanceof CustomerManageActivity) {
				MethodsDeliverData.isMyCustomer = true;
				AppInstance.mListActivitys.get(
						AppInstance.mListActivitys.size() - 1).recreate();
			} else {
				MethodsDeliverData.isMyCustomer = true;
				MethodsExtra.startActivity(mContext,
						CustomerManageActivity.class);
				clearActivity();
			}
			sendMessageCloseMenu();
			break;
		case R.id.rlyt_grab_house_main_page_slid_menus:
			if (MethodsDeliverData.mIntHouseType == HouseType.GONG_FANG
					&& AppInstance.mListActivitys
							.get(AppInstance.mListActivitys.size() - 1) instanceof HouseManageActivity) {
			} else if (AppInstance.mListActivitys
					.get(AppInstance.mListActivitys.size() - 1) instanceof HouseManageActivity) {
				MethodsDeliverData.flag = 1;
				MethodsDeliverData.mIntHouseType = HouseType.GONG_FANG;
				AppInstance.mListActivitys.get(
						AppInstance.mListActivitys.size() - 1).finish();
				startActivity(new Intent(mContext, HouseManageActivity.class));
			} else {
				MethodsDeliverData.flag = 1;
				MethodsDeliverData.mIntHouseType = HouseType.GONG_FANG;
				MethodsExtra.startActivity(mContext, HouseManageActivity.class);
				clearActivity();
			}
			sendMessageCloseMenu();
			break;
		case R.id.rlyt_grab_customer_main_page_slid_menus:
			if (!MethodsDeliverData.isMyCustomer
					&& AppInstance.mListActivitys
							.get(AppInstance.mListActivitys.size() - 1) instanceof CustomerManageActivity) {
			} else if (AppInstance.mListActivitys
					.get(AppInstance.mListActivitys.size() - 1) instanceof CustomerManageActivity) {
				MethodsDeliverData.flag = 1;
				MethodsDeliverData.isMyCustomer = false;
				AppInstance.mListActivitys.get(
						AppInstance.mListActivitys.size() - 1).recreate();
			} else {
				MethodsDeliverData.flag = 1;
				MethodsDeliverData.isMyCustomer = false;
				MethodsExtra.startActivity(mContext,
						CustomerManageActivity.class);
				clearActivity();
			}
			sendMessageCloseMenu();
			break;
		case R.id.rlyt_password_main_page_slid_menus:
			if (!(AppInstance.mListActivitys.get(AppInstance.mListActivitys
					.size() - 1) instanceof KeyGetInActivity)) {
				MethodsExtra.startActivity(mContext, KeyGetInActivity.class);
				clearActivity();
			}
			sendMessageCloseMenu();
			break;
		case R.id.rlyt_sacn_customer_main_page_slid_menus:
			MethodsExtra.startActivity(mContext, CaptureActivity.class);
			sendMessageCloseMenu();
			break;
		case R.id.rlyt_remind_customer_main_page_slid_menus:
			if (!(AppInstance.mListActivitys.get(AppInstance.mListActivitys
					.size() - 1) instanceof KeyGetInActivity)) {
				MethodsDeliverData.flag = -1;
				MethodsExtra.startActivity(mContext, MessageListActivity.class);
				clearActivity();
			}
			sendMessageCloseMenu();
			break;
		}

	}

	public abstract int setContentLayoutId();

	public abstract void initView();

	public abstract void setListener();

	public abstract void initData();

	public abstract Handler setHandler();

	private void sendMessageCloseMenu() {
		if (mHander != null) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					mHander.sendEmptyMessageDelayed(R.id.closeSlideMenu, 1000);
				}
			}).start();
		}
	}

	/**
	 * 关闭侧栏
	 * 
	 * @param msg
	 */
	public void closeMenu(Message msg) {
		switch (msg.what) {
		case R.id.closeSlideMenu:
			mSlidMenu.closeMenu();
			clearActivity();
			break;
		}
	}

	void clearActivity() {
		for (int i = AppInstance.mListActivitys.size() - 2; i > 0; i--) {
			Activity activity = AppInstance.mListActivitys.get(i);
			if (!(activity instanceof HomeActivity)) {
				activity.finish();
			} else {
				break;
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
		Log.d(TAG, "输出Context 类：" + mContext.getClass().getName());
	}

	@Override
	protected void onStart() {
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

	// 数据回调函数(来自通知)
	public abstract void notifCallBack(final String name,
			final String className, final Object data);

}
