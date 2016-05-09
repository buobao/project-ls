package com.vocinno.centanet.home;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vocinno.centanet.R;
import com.vocinno.centanet.tools.MyUtils;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.baseactivity.HomeBaseActivity;
import com.vocinno.centanet.customermanage.CustomerManageActivity;
import com.vocinno.centanet.housemanage.HouseManageActivity;
import com.vocinno.centanet.housemanage.HouseManageActivity2;
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
public class HomeActivity extends HomeBaseActivity {
	private RelativeLayout rl_leftView;
	private ImageView menuView;
	private TextView /*mTvHouseManage,*/ /*mTvCustomerManage*/ mTvKeyManage,
			mTvGrabHouse, mTvGrabCustomer, mTvRemind;
	private ImageView iv_fangyuan,iv_keyuan,iv_yaoshi,iv_qianggongfang,iv_qianggongke,iv_tixing;
	private LinearLayout ll_pinma,ll_saoyisao;
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
		setMenu();
		iv_fangyuan = (ImageView) findViewById(R.id.iv_fangyuan);
		iv_keyuan = (ImageView) findViewById(R.id.iv_keyuan);
		iv_yaoshi = (ImageView) findViewById(R.id.iv_yaoshi);
		iv_qianggongfang = (ImageView) findViewById(R.id.iv_qianggongfang);
		iv_qianggongke = (ImageView) findViewById(R.id.iv_qianggongke);
		iv_tixing = (ImageView) findViewById(R.id.iv_tixing);
		ll_pinma = (LinearLayout) findViewById(R.id.ll_pinma);
		ll_saoyisao = (LinearLayout) findViewById(R.id.ll_saoyisao);
		MethodsExtra.findHeadTitle1(mContext, baseView, 0,getString(R.string.app_name));
		mImgViewBackground = (ImageView) findViewById(R.id.imgView_background_HomeActivity);
		mImgViewBackground.setBackgroundResource(R.drawable.homeimg);
//		mViewBlurBorder = findViewById(R.id.tv_blurBorder_modelLayerGradit);

		iv_fangyuan.setOnClickListener(this);
		iv_keyuan.setOnClickListener(this);
		iv_yaoshi.setOnClickListener(this);
		iv_qianggongfang.setOnClickListener(this);
		iv_qianggongke.setOnClickListener(this);
		iv_tixing.setOnClickListener(this);
		ll_pinma.setOnClickListener(this);
		ll_saoyisao.setOnClickListener(this);
	}
	private void setMenu() {
		rl_leftView = (RelativeLayout) baseView.findViewById(R.id.rlyt_left_mhead1);
		rl_leftView.setVisibility(View.VISIBLE);
		menuView=(ImageView)baseView.findViewById(R.id.img_left_mhead1);
		menuView.setVisibility(View.VISIBLE);
		menuView.setImageResource(R.drawable.home_menu);
		menuView.setOnClickListener(this);
	}
	@Override
	public void initData() {
		mIntScreenWithHeight = MethodsData.getScreenWidthHeight(mContext);
		Bitmap bp = MethodsFile
				.getScaledBitmap(BitmapFactory.decodeResource(getResources(),
								R.drawable.homeimg), mIntScreenWithHeight[0],
						(mIntScreenWithHeight[1] - MethodsData.dip2px(mContext,
								25)) / 2, 0, 0);

		mBitmap = MethodsFile.getBitmapWithVerticalMirror(bp);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.img_left_mhead1:
				if(drawer_layout.isDrawerOpen(leftMenuView)){
					drawer_layout.closeDrawer(leftMenuView);
				}else{
					drawer_layout.openDrawer(leftMenuView);
				}
				break;
			case R.id.iv_fangyuan:
//			MethodsDeliverData.mIntHouseType = HouseType.WO_DE;
				/*MethodsDeliverData.mIntHouseType = HouseType.CHU_SHOU;
				CST_JS.setZOrS("s");
				HouseManageActivity.zOrS=true;
				MethodsExtra.startActivity(mContext, HouseManageActivity.class);*/
				MethodsDeliverData.mIntHouseType = HouseType.CHU_SHOU;
				CST_JS.setZOrS("s");
				HouseManageActivity.zOrS=true;
//				MethodsExtra.startActivity(mContext, HouseManageActivity2.class);
				Intent intent=new Intent();
				intent.setClass(mContext, HouseManageActivity2.class);
				intent.putExtra(MyUtils.ROB_GONG_FANG,false);
				intent.putExtra("viewPageIndex", 0);
				startActivity(intent);

				break;
			case R.id.iv_keyuan:
				MethodsDeliverData.isMyCustomer = true;
				MethodsDeliverData.keYuanOrGongKe=1;
				MethodsExtra.startActivity(mContext, CustomerManageActivity.class);
				break;
			case R.id.iv_yaoshi:
				MethodsExtra.startActivity(mContext, KeyManageActivity.class);
				break;
			case R.id.iv_qianggongfang:
				MethodsDeliverData.flag = 1;
				MethodsDeliverData.mIntHouseType = HouseType.GONG_FANG;
				intent=new Intent(mContext, HouseManageActivity2.class);
				intent.putExtra(MyUtils.ROB_GONG_FANG,true);
				intent.putExtra("viewPageIndex",0);
				//viewPageIndex =getIntent().getIntExtra("viewPageIndex", 0);
				//isGongFang=getIntent().getBooleanExtra(MyUtils.ROB_GONG_FANG,false);
				startActivity(intent);
				break;
			case R.id.iv_qianggongke:
				MethodsDeliverData.flag = 1;
				MethodsDeliverData.isMyCustomer = false;
				MethodsDeliverData.keYuanOrGongKe=0;
				this.intent.setClass(mContext,CustomerManageActivity.class);
				this.intent.putExtra("isGongKe", true);
				startActivity(this.intent);
				break;
			case R.id.iv_tixing:
				MethodsDeliverData.flag = -1;
				MethodsExtra.startActivity(mContext, MessageListActivity.class);
				break;
			case R.id.ll_pinma:
				MethodsExtra.startActivity(mContext, KeyGetInActivity.class);
				break;
			case R.id.ll_saoyisao:
				MethodsExtra.startActivity(mContext, CaptureActivity.class);
				break;
		}
	}

	@Override
	public void notifCallBack(String name, String className, Object data) {

	}
	private long exitTime = 0;
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
			if((System.currentTimeMillis()-exitTime) > 2000){
				Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
