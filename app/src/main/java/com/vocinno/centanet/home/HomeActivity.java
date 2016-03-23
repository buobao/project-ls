package com.vocinno.centanet.home;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.SuperActivity;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.customermanage.CustomerManageActivity;
import com.vocinno.centanet.housemanage.HouseManageActivity;
import com.vocinno.centanet.housemanage.HouseType;
import com.vocinno.centanet.keymanage.KeyGetInActivity;
import com.vocinno.centanet.keymanage.KeyManageActivity;
import com.vocinno.centanet.remind.MessageListActivity;
import com.vocinno.utils.MethodsData;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsFile;
import com.zbar.lib.CaptureActivity;

/**
 * 主页
 * 
 * @author Administrator
 * 
 */
public class HomeActivity extends SuperActivity {

	private TextView mTvHouseManage, mTvCustomerManage, mTvKeyManage,
			mTvGrabHouse, mTvGrabCustomer, mTvRemind;
	private View mViewBlur, mViewBlurBorder;

	private LinearLayout mLlytScan, mLlytInputPassword;
	// 背景图片
	private ImageView mImgViewBackground;
	private Bitmap mBitmap = null;
	// 屏幕宽度和高度
	private int[] mIntScreenWithHeight;

	@Override
	public Handler setHandler() {
		return new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case R.id.doRequest:
					mImgViewBackground.setImageBitmap(mBitmap);
					MethodsFile.doBlurForViewBkgBitmap(mContext, mBitmap, 20,
							mViewBlur, 1f);
					MethodsFile.doBlurForViewBkgBitmap(mContext, mBitmap, 14,
							mViewBlurBorder, 1f);
					setAlpha(mViewBlurBorder, 0.8f);
					break;
				default:
					break;
				}

			}
		};
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	void setAlpha(View view, float alpha) {
		view.setAlpha(alpha);
	}

	@Override
	public int setContentLayoutId() {
		return R.layout.activity_home_page;
	}

	@Override
	public void initView() {
		mTvHouseManage = (TextView) findViewById(R.id.tv_home_HomeActivity);
		mTvCustomerManage = (TextView) findViewById(R.id.tv_customerManage_HomeActivity);
		mTvKeyManage = (TextView) findViewById(R.id.tv_keyManage_HomeActivity);
		mTvGrabHouse = (TextView) findViewById(R.id.tv_grabHouse_HomeActivity);
		mTvGrabCustomer = (TextView) findViewById(R.id.tv_grabCustomer_HomeActivity);
		mTvRemind = (TextView) findViewById(R.id.tv_myRemind_HomeActivity);
		mLlytInputPassword = (LinearLayout) findViewById(R.id.llyt_password_HomeActivity);
		mLlytScan = (LinearLayout) findViewById(R.id.llyt_scan_HomeActivity);
		MethodsExtra.findHeadTitle1(mContext, mRootView, 0, "地产三级平台");
		mImgViewBackground = (ImageView) findViewById(R.id.imgView_background_HomeActivity);
		mViewBlur = findViewById(R.id.tv_blur_modelLayerGradit);
		mViewBlurBorder = findViewById(R.id.tv_blurBorder_modelLayerGradit);
	}

	@Override
	public void setListener() {
		mTvHouseManage.setOnClickListener(this);
		mTvCustomerManage.setOnClickListener(this);
		mTvKeyManage.setOnClickListener(this);
		mTvGrabHouse.setOnClickListener(this);
		mTvGrabCustomer.setOnClickListener(this);
		mTvRemind.setOnClickListener(this);
		mLlytInputPassword.setOnClickListener(this);
		mLlytScan.setOnClickListener(this);
	}

	@Override
	public void initData() {
		mIntScreenWithHeight = MethodsData.getScreenWidthHeight(mContext);
		Bitmap bp = MethodsFile
				.getScaledBitmap(BitmapFactory.decodeResource(getResources(),
						R.drawable.test01), mIntScreenWithHeight[0],
						(mIntScreenWithHeight[1] - MethodsData.dip2px(mContext,
								25)) / 2, 0, 0);

		mBitmap = MethodsFile.getBitmapWithVerticalMirror(bp);
		mHander.sendEmptyMessageDelayed(R.id.doRequest, 50);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_home_HomeActivity:
			MethodsDeliverData.mIntHouseType = HouseType.WO_DE;
			CST_JS.setZOrS("s");
			HouseManageActivity.zOrS=true;
			MethodsExtra.startActivity(mContext, HouseManageActivity.class);
			break;
		case R.id.tv_customerManage_HomeActivity:
			MethodsDeliverData.isMyCustomer = true;
			MethodsExtra.startActivity(mContext, CustomerManageActivity.class);
			break;
		case R.id.tv_keyManage_HomeActivity:
			MethodsExtra.startActivity(mContext, KeyManageActivity.class);
			break;
		case R.id.tv_grabHouse_HomeActivity:
			MethodsDeliverData.flag = 1;
			MethodsDeliverData.mIntHouseType = HouseType.GONG_FANG;
			MethodsExtra.startActivity(mContext, HouseManageActivity.class);
			break;
		case R.id.tv_grabCustomer_HomeActivity:
			MethodsDeliverData.flag = 1;
			MethodsDeliverData.isMyCustomer = false;
			MethodsExtra.startActivity(mContext, CustomerManageActivity.class);
			break;
		case R.id.tv_myRemind_HomeActivity:
			MethodsDeliverData.flag = -1;
			MethodsExtra.startActivity(mContext, MessageListActivity.class);
			break;
		case R.id.llyt_password_HomeActivity:
			MethodsExtra.startActivity(mContext, KeyGetInActivity.class);
			break;
		case R.id.llyt_scan_HomeActivity:
			MethodsExtra.startActivity(mContext, CaptureActivity.class);
			break;
		}
	}

	@Override
	public void onBack() {
		finish();
	}

	@Override
	public void notifCallBack(String name, String className, Object data) {

	}

}
