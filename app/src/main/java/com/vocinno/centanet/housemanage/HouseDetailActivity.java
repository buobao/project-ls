package com.vocinno.centanet.housemanage;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.AppInstance;
import com.vocinno.centanet.apputils.SharedPreferencesUtils;
import com.vocinno.centanet.apputils.adapter.MyPagerAdapter;
import com.vocinno.centanet.apputils.adapter.MyPagerAdapter.MType;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.apputils.dialog.MyDialog;
import com.vocinno.centanet.apputils.selfdefineview.ListViewNeedResetHeight;
import com.vocinno.centanet.baseactivity.OtherBaseActivity;
import com.vocinno.centanet.customermanage.CustomerManageActivity;
import com.vocinno.centanet.customermanage.adapter.CustormerPhoneAdapter;
import com.vocinno.centanet.keymanage.KeyGetInActivity;
import com.vocinno.centanet.keymanage.KeyManageActivity;
import com.vocinno.centanet.model.BorrowKey;
import com.vocinno.centanet.model.ContactDetail;
import com.vocinno.centanet.model.ContactItem;
import com.vocinno.centanet.model.Exclude;
import com.vocinno.centanet.model.HouseDetail;
import com.vocinno.centanet.model.HouseItem;
import com.vocinno.centanet.model.Image;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.model.Track;
import com.vocinno.centanet.myinterface.HttpInterface;
import com.vocinno.centanet.remind.MessageListActivity;
import com.vocinno.centanet.tools.MyUtils;
import com.vocinno.centanet.tools.constant.MyConstant;
import com.vocinno.centanet.user.UserLoginActivity;
import com.vocinno.utils.MethodsData;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsFile;
import com.vocinno.utils.MethodsJni;
import com.vocinno.utils.MethodsJson;
import com.zbar.lib.CaptureActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 房源详情
 * 
 * @author Administrator
 * 
 */
public class HouseDetailActivity extends OtherBaseActivity {
	private HouseDetail mHouseDetail = null;
	private ScrollView mScrollView = null;
	private String shareImgUrl=null;
	private int shareTag;
	private View mBackView, mMoreView, mTitleView;
	private ListViewNeedResetHeight mLvTracks, mLvSigns;
	private TextView mTvName, mTvPrice, mTvDetail, mTvLouceng, mTvDistanst,tv_shihao_houseDetailActivity,tv_lookshihao_houseDetailActivity,
			mTvYear, mTvBankuai, mTvSaleTime, mTvPriceUnit;
	private Dialog mMenuDialog;
	private Dialog mCallCustormerDialog;
	private ViewPager mViewPager;
	private MyPagerAdapter myPagerAdapter;
	private List<Image> mListImages = null;
	private LinearLayout mllytDataContainer;
	private RelativeLayout mKeysNotHere; // 钥匙不在的提示栏
	private LinearLayout mKeysUsingFinish, mEnsureUser; // 归还传递部分
	private RelativeLayout mReturnKey; // 归还钥匙
	private RelativeLayout mPassKey; // 传递钥匙
	private FrameLayout mGetKeyAction; // 传递钥匙动作
	private RelativeLayout mBorrowKey;
	private RelativeLayout mGrabHouse;
	private TextView mTvPagerIndicator, mTvQiang;
	private RelativeLayout mQiang;// 抢
	private int mKeyCountTest = 1;
	private int[] mIntScreenWithHeight;
	private ArrayList<View> mArrayListViews = new ArrayList<View>();
	private ArrayList<String> mArrayListBitmapPaths = new ArrayList<String>();
	private int isFirstDataCall = 0;
	private RelativeLayout mRlView;
	private Drawable drawable;
	private static final int Scroll_to_Top = 100001;
	private String LouCeng;//防止刷新页面楼层号消失用来保存变量
	private HttpInterface hif=(HttpInterface)this;
	private boolean isIntoForList;//判断是否从列表进入详情
	private LinearLayout ll_houes_detail;
	private TextView tv_houes_detail;
	private RelativeLayout relt_house_detail_layout;
	@Override
	@SuppressLint("HandlerLeak")
	public Handler setHandler() {
		return new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case R.id.doRequest:
					if (myPagerAdapter != null) {
						myPagerAdapter.destory();
					}
					myPagerAdapter = new MyPagerAdapter(mContext,
							mArrayListViews, MType.HouseDetail);
					mViewPager.setAdapter(myPagerAdapter);
					Log.d(TAG,
							"wanggsxHouseImage new mypageradapter adapter数据回调创建adapter");
					mTvPagerIndicator.setText("1/" + mArrayListViews.size());
					mHander.sendEmptyMessageDelayed(Scroll_to_Top, 500);
					break;
				case R.id.doGetImg:
					Bitmap bitmap=(Bitmap)msg.obj;
					wechatShare(zoomImage(bitmap, 100,100));
					break;
				case R.id.doGetImgError:
					Bitmap map=BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
					wechatShare(map);
					break;
				case R.id.doUpdate:
					// 初始化viewpage
					ViewGroup.LayoutParams mLayoutParams = mViewPager
							.getLayoutParams();
					if (mLayoutParams == null) {
						mLayoutParams = new LinearLayout.LayoutParams(
								android.view.ViewGroup.LayoutParams.MATCH_PARENT,
								mIntScreenWithHeight[1]);
					} else {
						mLayoutParams.height = mIntScreenWithHeight[1];
					}
					mViewPager.setLayoutParams(mLayoutParams);
					// 初始化数据layout
					mllytDataContainer.setPadding(
							MethodsData.dip2px(mContext, 10),
							mIntScreenWithHeight[1]
									- MethodsData.dip2px(mContext, 362), 0, 0);
					break;
				case Scroll_to_Top:
					Log.d(TAG, "wanggsxHouseImage mScrollView to top");
					mScrollView.scrollTo(0, 0);
					break;
				default:
					break;
				}
			}
		};
	}
	@Override
	public int setContentLayoutId() {
		return R.layout.activity_house_detail;
	}

	@Override
	public void initView() {
		MethodsExtra.findHeadTitle1(mContext, baseView, R.string.housedecribe,
				null);
		mBackView = MethodsExtra.findHeadLeftView1(mContext, baseView, 0, 0);
		mMoreView = MethodsExtra.findHeadRightView1(mContext, baseView, 0, 0);
		mTitleView = MethodsExtra
				.findHeadTitle1(mContext, baseView, 0, "房源详情");
		mViewPager = (ViewPager) findViewById(R.id.vPager_houseDetailActivity);
		mBorrowKey = (RelativeLayout) findViewById(R.id.rlyt_borrowKey_houseDetailActivity);
		mKeysNotHere = (RelativeLayout) findViewById(R.id.rylt_borrowKeynothere_houseDetailActivity);
		mKeysUsingFinish = (LinearLayout) findViewById(R.id.llyt_finishUsingKey_houseDetailActivity);
		mReturnKey = (RelativeLayout) findViewById(R.id.rlyt_returnKey_houseDetailActivity);
		mPassKey = (RelativeLayout) findViewById(R.id.rlyt_passKey_houseDetailActivity);
		mGetKeyAction = (FrameLayout) findViewById(R.id.touchFlyt_giveKey_houseDetailActivity);
		mGrabHouse = (RelativeLayout) findViewById(R.id.rlyt_seize_houseDetailActivity);
		mQiang = (RelativeLayout) findViewById(R.id.rlyt_qiang_houseDetailActivity);
		mTvPagerIndicator = (TextView) findViewById(R.id.tv_pagerIndicator_houseDetailActivity);
		mllytDataContainer = (LinearLayout) findViewById(R.id.llyt_dataContainer_houseDetailActivity);
		mLvTracks = (ListViewNeedResetHeight) findViewById(R.id.lv_track_houseDetailActivity);
		mEnsureUser = (LinearLayout) findViewById(R.id.llyt_qianpei_houseDetailActivity);
		mRlView = (RelativeLayout) findViewById(R.id.rlimg_genjin_houseDetailActivity);
		mTvQiang = (TextView) findViewById(R.id.tv_seize_houseSoueceDetailActivity);
		mTvName = (TextView) findViewById(R.id.tv_name_houseDetailActivity);
		mTvPrice = (TextView) findViewById(R.id.tv_price_houseDetailActivity);
		mTvPriceUnit = (TextView) findViewById(R.id.tv_priceUnit_houseDetailActivity);
		mTvDetail = (TextView) findViewById(R.id.tv_details_houseDetailActivity);
		mTvLouceng = (TextView) findViewById(R.id.tv_louceng_houseDetailActivity);
		mTvDistanst = (TextView) findViewById(R.id.tv_distance_houseDetailActivity);
		tv_shihao_houseDetailActivity = (TextView) findViewById(R.id.tv_shihao_houseDetailActivity);
		tv_lookshihao_houseDetailActivity = (TextView) findViewById(R.id.tv_lookshihao_houseDetailActivity);
		tv_lookshihao_houseDetailActivity.setOnClickListener(this);
		mTvYear = (TextView) findViewById(R.id.tv_year_houseDetailActivity);
		mTvBankuai = (TextView) findViewById(R.id.tv_bankuai_houseDetailActivity);
		mTvSaleTime = (TextView) findViewById(R.id.tv_saleTime_houseDetailActivity);
		mLvSigns = (ListViewNeedResetHeight) findViewById(R.id.lv_sign_houseDetailActivity);
		mScrollView = (ScrollView) findViewById(R.id.scroller_houseDetailActivity);

		relt_house_detail_layout= (RelativeLayout) baseView.findViewById(R.id.relt_house_detail_layout);
		relt_house_detail_layout.setVisibility(View.GONE);
		ll_houes_detail= (LinearLayout) baseView.findViewById(R.id.ll_houes_detail);
		WindowManager wm = this.getWindowManager();
		int height = wm.getDefaultDisplay().getHeight();
		LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) ll_houes_detail.getLayoutParams(); //取控件textView当前的布局参数
		linearParams.height = height-48;
		ll_houes_detail.setLayoutParams(linearParams);
		tv_houes_detail= (TextView) baseView.findViewById(R.id.tv_houes_detail);
		tv_houes_detail.setOnClickListener(this);
		tv_houes_detail.setOnClickListener(new NoDoubleClickListener() {
			@Override
			protected void onNoDoubleClick(View v) {
				getHouesData();
			}
		});
		// 根据数据显示房源的具体详情
//		mHouseUi.setVisibility(View.GONE);

		mBackView.setOnClickListener(this);
		mMoreView.setOnClickListener(this);
		mBorrowKey.setOnClickListener(this);
		mReturnKey.setOnClickListener(this);
		mPassKey.setOnClickListener(this);
		mGrabHouse.setOnClickListener(this);
		mQiang.setOnClickListener(this);
		mRlView.setOnClickListener(this);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				String strLastText = mTvPagerIndicator.getText().toString();
				String str = strLastText.substring(strLastText.indexOf("/") + 1);
				mTvPagerIndicator.setText((arg0 + 1) + "/" + str);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}
	@Override
	public void initData() {
		 isIntoForList=getIntent().getBooleanExtra(MyUtils.INTO_FROM_LIST,false);
		methodsJni=new MethodsJni();
		methodsJni.setMethodsJni((HttpInterface) this);
		mIntScreenWithHeight = MethodsData.getScreenWidthHeight(mContext);
		// 初始化ViewPager adapter
		mHander.sendEmptyMessageDelayed(R.id.doUpdate, 50);

		if (MethodsDeliverData.flag == 1) {
			MethodsDeliverData.flag = -1;
			mGrabHouse.setBackgroundColor(Color.RED);
			mTvQiang.setText("抢");
			drawable = getResources().getDrawable(R.drawable.icon_grab);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			mTvQiang.setCompoundDrawables(drawable, null, null, null);
			mQiang.setVisibility(View.VISIBLE);
			mGrabHouse.setVisibility(View.GONE);
		}
		showBorrowKey();
		// 注册通知
		MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_DOROOMVIEW_RESULT, TAG);
		MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_CONTACT_LIST_RESULT, TAG);
		MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_HOU_DETAIL_RESULT, TAG);
		MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_BORROW_KEY_FROM_SHOP_RESULT, TAG);
		MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_CLAIM_HOUSE_RESULT, TAG);
		getHouesData();
	}
	public void getHouesData() {
		showDialog();
		// 调用初始化数据
		MethodsJni
				.callProxyFun(hif,
						CST_JS.JS_ProxyName_HouseResource,
						CST_JS.JS_Function_HouseResource_getHouseDetail,
						CST_JS.getJsonStringForHouseListGetHouseDetail(MethodsDeliverData.mDelCode));
	}
	@Override
	protected void onResume() {
		super.onResume();
		isFirstDataCall = 0;
		if(getIntent().getBooleanExtra("key",false)){
			modelDialog.show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode){
			case 101:
				String  roomNo=data.getStringExtra("roomNo");
				String  buiding=data.getStringExtra("buiding");
				tv_shihao_houseDetailActivity.setText("室号："+roomNo);
				mTvLouceng.setText("楼层：" + mHouseDetail.getFloor());
				tv_lookshihao_houseDetailActivity.setVisibility(View.GONE);

				mTvName.setText(mHouseDetail.getAddr() + " "+buiding+" " + mHouseDetail.getDelCode());
				break;
			case MyConstant.REFRESH:
				showDialog();
				// 调用初始化数据
				MethodsJni
						.callProxyFun(hif,
								CST_JS.JS_ProxyName_HouseResource,
								CST_JS.JS_Function_HouseResource_getHouseDetail,
								CST_JS.getJsonStringForHouseListGetHouseDetail(MethodsDeliverData.mDelCode));
				break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_lookshihao_houseDetailActivity:
			if(!mHouseDetail.isRequireReason()){//false不需要原因，true需要
				showDialog();
				MethodsJni.callProxyFun(hif,CST_JS.JS_ProxyName_HouseResource,
						CST_JS.JS_Function_HouseResource_getShiHao, CST_JS
								.getJsonStringForLookShiHao("查看室号", mHouseDetail.getDelCode(), mHouseDetail.getHouseId(), "10080001"));
				/*String roomNo=mHouseDetail.roomNo;
				tv_lookshihao_houseDetailActivity.setVisibility(View.INVISIBLE);
				mTvLouceng.setText("楼层：" + mHouseDetail.getFloor());
				tv_shihao_houseDetailActivity.setText("室号："+roomNo);*/
			}else{
				String delCode=mHouseDetail.getDelCode();
				String houseId=mHouseDetail.getHouseId();
				Intent intent = new Intent(mContext,
						HouseReasonActivity.class);
				intent.putExtra("delCode",delCode);
				intent.putExtra("houseId",houseId);
				MethodsExtra.startActivityForResult(mContext, 100, intent);
			}

			break;
		case R.id.img_left_mhead1:
			finish();
			break;
		case R.id.img_right_mhead1:
			showMenuDialog();
			break;
		case R.id.llyt_borrowKey_HouseDetailActivity:
			mMenuDialog.dismiss();
			MethodsDeliverData.mKeyType = 1;
			showBorrowKey();
			break;
		case R.id.llyt_connectOwner_HouseDetailActivity:
			mMenuDialog.dismiss();
			showCallCosturmerDialog();
			break;
		case R.id.llyt_shareHouse_HouseDetailActivity:
			// 这里是友盟分享的dialog
			mMenuDialog.dismiss();
			shareTag=0;
//			wechatShare(0);
			showDialog();
			returnBitmap(shareImgUrl);
			break;
		case R.id.llyt_sharequan_HouseDetailActivity:
			// 这里是友盟分享的dialog
			mMenuDialog.dismiss();
			shareTag=1;
			showDialog();
			returnBitmap(shareImgUrl);
//			wechatShare(1);
			break;
		case R.id.llyt_addPic_HouseDetailActivity:
			mMenuDialog.dismiss();
			if (mHouseDetail != null && mHouseDetail.getDelCode() != null) {
				MethodsDeliverData.mDelCode = mHouseDetail.getDelCode();
			} else {
				MethodsExtra.toast(mContext, "mHouseDetail不能为空");
			}
			mArrayListViews.clear();
			mViewPager.setAdapter(null);
			mViewPager.removeAllViews();
			if (myPagerAdapter != null) {
				myPagerAdapter.destory();
			}
			mTvPagerIndicator.setText("0/0");
			if(mHouseDetail!=null&&mHouseDetail.getDelCode()!=null){
				Intent addHousePictureIntent=new Intent(this,AddHousePictureActivity.class);
				addHousePictureIntent.putExtra("delCode",mHouseDetail.getDelCode());
				addHousePictureIntent.putExtra("explmsg",mHouseDetail.getExplmsg());
				this.startActivity(addHousePictureIntent);
			}else{
				MethodsExtra.toast(this,"房源编号为空无法增加实勘");
			}
			break;
		case R.id.llyt_seeFollowUp_HouseDetailActivity:
			mMenuDialog.dismiss();
			if (mHouseDetail != null && mHouseDetail.getDelCode() != null) {
				MethodsDeliverData.mDelCode = mHouseDetail.getDelCode();
				Intent it=new Intent(mContext,SeeFollowInDetailActivity.class);
				it.putExtra("delegationType",mHouseDetail.getDelegationType());
				startActivity(it);
			} else {
				MethodsExtra.toast(mContext, "mHouseDetail不能为空");
				Intent it=new Intent(mContext,SeeFollowInDetailActivity.class);
				it.putExtra("delegationType","");
				startActivityForResult(it,100);
			}
//			MethodsExtra.startActivity(mContext,SeeFollowInDetailActivity.class);
			break;
		case R.id.rlyt_passKey_houseDetailActivity:
			MethodsDeliverData.mKeyType = 3;
			showBorrowKey();
			break;
		case R.id.rlyt_borrowKey_houseDetailActivity:
			mBorrowKey.setEnabled(false);
			// 钥匙借用
			MethodsJni
					.callProxyFun(hif,
							CST_JS.JS_ProxyName_KeyProxy,
							CST_JS.JS_Function_KeyProxy_borrowKeyFromShop,
							CST_JS.getJsonStringForborrowKeyFromShop(MethodsDeliverData.mDelCode));
			break;
		case R.id.rlyt_returnKey_houseDetailActivity:
			// 归还钥匙
			MethodsJni
					.callProxyFun(hif,
							CST_JS.JS_ProxyName_KeyProxy,
							CST_JS.JS_Function_KeyProxy_returnKeyToShop,
							CST_JS.getJsonStringForborrowKeyFromShop(MethodsDeliverData.mDelCode));
			break;
		case R.id.rlyt_seize_houseDetailActivity:
			showCallCosturmerDialog();
			break;
		case R.id.rlyt_qiang_houseDetailActivity:
			showDialog();
			// 抢
			MethodsJni
					.callProxyFun(hif,
							CST_JS.JS_ProxyName_HouseResource,
							CST_JS.JS_Function_HouseResource_claimHouse,
							CST_JS.getJsonStringForClaimHouse(MethodsDeliverData.mDelCode));
			break;
		case R.id.rlimg_genjin_houseDetailActivity:
			if (mHouseDetail != null && mHouseDetail.getDelCode() != null) {
				MethodsDeliverData.mDelCode = mHouseDetail.getDelCode();
			} else {
				MethodsExtra.toast(mContext, "mHouseDetail不能为空");
			}
//			MethodsExtra.startActivity(mContext, AddFollowInHouseActivity.class);
			Intent l=new Intent(mContext, AddFollowInHouseActivity.class);
			startActivityForResult(l,10);
			break;
			//附近出售
			case R.id.rlyt_sell_house_main_page_slid_menus:
				startOrReturnIntent(0,false);
				break;
			//附近出租
			case R.id.rlyt_rent_house_main_page_slid_menus:
				startOrReturnIntent(1,false);
				break;
			//约看房源
			case R.id.rlyt_see_house_main_page_slid_menus:
				startOrReturnIntent(2,false);
				break;
			//我的出售
			case R.id.rlyt_my_house_main_page_slid_menus:
				startOrReturnIntent(3,false);
				break;
			//我的出租
			case R.id.rlyt_my_house_main_page_slid_menus2:
				startOrReturnIntent(4,false);
				break;
			//钥匙管理
			case R.id.rlyt_key_house_main_page_slid_menus:
				finish();
				MethodsExtra.startActivity(mContext, KeyManageActivity.class);
				break;
			//我的客源
			case R.id.rlyt_my_customer_main_page_slid_menus:
				finish();
				MethodsDeliverData.keYuanOrGongKe=1;
				MethodsDeliverData.isMyCustomer = true;
				MethodsExtra.startActivity(mContext,
						CustomerManageActivity.class);
				break;
			//抢公售
			case R.id.rlyt_grab_house_main_page_slid_menus:
				startOrReturnIntent(0, true);
				break;
			//抢公租
			case R.id.rlyt_grab_house_main_page_slid_menus2:
				startOrReturnIntent(1, true);
				break;
			//抢公客
			case R.id.rlyt_grab_customer_main_page_slid_menus:
				finish();
				MethodsDeliverData.keYuanOrGongKe=0;
				MethodsDeliverData.flag = 1;
				MethodsDeliverData.isMyCustomer = false;
				MethodsExtra.startActivity(mContext,
						CustomerManageActivity.class);
				break;
			//pin码
			case R.id.rlyt_password_main_page_slid_menus:
				finish();
				MethodsExtra.startActivity(mContext, KeyGetInActivity.class);
				break;
			//扫一扫
			case R.id.rlyt_sacn_customer_main_page_slid_menus:
				finish();
				MethodsExtra.startActivity(mContext, CaptureActivity.class);
				break;
			//我的提醒
			case R.id.rlyt_remind_customer_main_page_slid_menus:
				MethodsDeliverData.flag = -1;
				MethodsExtra.startActivity(mContext, MessageListActivity.class);
				break;
			//我的提醒
			case R.id.ry_exit:
				/*intent.setClass(this, UserLoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra(MyConstant.isExit, true);
				startActivity(intent);*/
				MyUtils.showDialog(intent,this, UserLoginActivity.class);
				break;
		default:
			break;
		}
	}
	private void startOrReturnIntent(int index,boolean flag){
		if(isIntoForList){
			houseDetailReturn(index,flag);
		}else{
			startIntentToHouseManager(index, flag);
		}
	}
	private void showMenuDialog() {
		mMenuDialog = new Dialog(mContext, R.style.Theme_dialog);
		mMenuDialog.setContentView(R.layout.dialog_menu_house_resouse_detail);
		Window win = mMenuDialog.getWindow();
		win.setGravity(Gravity.RIGHT | Gravity.TOP);
		mMenuDialog.setCanceledOnTouchOutside(true);
		mMenuDialog.show();
		win.setLayout(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

		LinearLayout mLLytBorrowKey = (LinearLayout) mMenuDialog
				.findViewById(R.id.llyt_borrowKey_HouseDetailActivity);
		LinearLayout mLLytConnectOwner = (LinearLayout) mMenuDialog
				.findViewById(R.id.llyt_connectOwner_HouseDetailActivity);
		LinearLayout mLLytShareHouse = (LinearLayout) mMenuDialog
				.findViewById(R.id.llyt_shareHouse_HouseDetailActivity);
		LinearLayout mLLytShareQuanHouse = (LinearLayout) mMenuDialog
				.findViewById(R.id.llyt_sharequan_HouseDetailActivity);
		LinearLayout mLLytAddPic = (LinearLayout) mMenuDialog
				.findViewById(R.id.llyt_addPic_HouseDetailActivity);
		LinearLayout mLLytSeeFollowIn = (LinearLayout) mMenuDialog
				.findViewById(R.id.llyt_seeFollowUp_HouseDetailActivity);
		mLLytBorrowKey.setOnClickListener(HouseDetailActivity.this);
		mLLytConnectOwner.setOnClickListener(HouseDetailActivity.this);
		mLLytShareHouse.setOnClickListener(HouseDetailActivity.this);
		mLLytShareQuanHouse.setOnClickListener(HouseDetailActivity.this);
		mLLytAddPic.setOnClickListener(HouseDetailActivity.this);
		mLLytSeeFollowIn.setOnClickListener(HouseDetailActivity.this);
	}

	private void showCallCosturmerDialog() {
		mCallCustormerDialog = new Dialog(mContext, R.style.Theme_dialog);
		mCallCustormerDialog
				.setContentView(R.layout.dialog_call_custormer_house_resouse_detial);
		Window win = mCallCustormerDialog.getWindow();
		win.setGravity(Gravity.BOTTOM);
		mCallCustormerDialog.setCanceledOnTouchOutside(true);

		win.setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

		MethodsJni
				.callProxyFun(hif,
						CST_JS.JS_ProxyName_HouseResource,
						CST_JS.JS_Function_HouseResource_getContactList,
						CST_JS.getJsonStringForGetContactList(MethodsDeliverData.mDelCode));
		mCallCustormerDialog.show();
	}

	// 展示钥匙相关按钮
	private void showBorrowKey() {
		mKeysNotHere.setVisibility(View.GONE);
		mKeysUsingFinish.setVisibility(View.GONE);
		mGetKeyAction.setVisibility(View.GONE);
		mBorrowKey.setVisibility(View.GONE);
		mGrabHouse.setVisibility(View.VISIBLE);
		if (MethodsDeliverData.mKeyType != -1) {
			mGrabHouse.setVisibility(View.GONE);
			if (MethodsDeliverData.mKeyType == 1) {
				mBorrowKey.setVisibility(View.VISIBLE);
			} else if (MethodsDeliverData.mKeyType == 2) {
				mKeysUsingFinish.setVisibility(View.VISIBLE);
			} else if (MethodsDeliverData.mKeyType == 3) {
				mGetKeyAction.setVisibility(View.VISIBLE);
			} else if (MethodsDeliverData.mKeyType == 4) {
				mKeysNotHere.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	protected void onDestroy() {
		if (myPagerAdapter != null) {
			myPagerAdapter.destory();
		}
		if (mCallCustormerDialog != null && mCallCustormerDialog.isShowing()) {
			mCallCustormerDialog.dismiss();
			mCallCustormerDialog = null;
		}
		baseView = null;
		System.gc();
		super.onDestroy();
		MethodsJni.removeNotificationObserver(
				CST_JS.NOTIFY_NATIVE_CONTACT_LIST_RESULT, TAG);
		MethodsJni.removeNotificationObserver(
				CST_JS.NOTIFY_NATIVE_HOU_DETAIL_RESULT, TAG);
		MethodsJni.removeNotificationObserver(
				CST_JS.NOTIFY_NATIVE_BORROW_KEY_FROM_SHOP_RESULT, TAG);
		MethodsJni.removeNotificationObserver(
				CST_JS.NOTIFY_NATIVE_CLAIM_HOUSE_RESULT, TAG);
		MethodsJni.removeAllNotifications(TAG);
	}



	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
			if(this.modelDialog.isShowing()){
				dismissDialog();
				fl_houes_detail.setVisibility(View.VISIBLE);
				tv_houes_detail.setText("点击重新加载");
				relt_house_detail_layout.setVisibility(View.GONE);
				return true;
			}else{
				finish();
			}

		}
		return super.onKeyDown(keyCode, event);
	}*/

	/**
	 * 微信分享 0：分享到微信好友 1：分享到微信朋友圈
	 **/
	private void wechatShare(Bitmap bitmap) {
		/*WXTextObject textObj = new WXTextObject();
		WXWebpageObject textObj=new WXWebpageObject();
//		textObj.text = "http://a.sh.centanet.com/sales-web/mobile/houShare/"
		if(shareImgUrl!=null&&shareImgUrl.length()>0){
			textObj.webpageUrl=shareImgUrl;
		}
		textObj.text = "http://a.sh.centanet.com/sales-web/mobile/houShare/"
				+ mHouseDetail.getDelCode() + "/"
				+ SharedPreferencesUtils.getUserId(mContext);
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = textObj;
		msg.title = "中原地产";
		msg.description = "中原地产房源分享";

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;
		req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession
				: SendMessageToWX.Req.WXSceneTimeline;
		*/
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = "http://a.sh.centanet.com/sales-web/mobile/houShare/"
				+ mHouseDetail.getDelCode() + "/"
				+ SharedPreferencesUtils.getUserId(mContext);
		WXMediaMessage msg = new WXMediaMessage(webpage);
		BigDecimal bPrice = new BigDecimal(mHouseDetail.getPrice());
		bPrice=bPrice.divide(new BigDecimal(10000), 0, BigDecimal.ROUND_HALF_UP);
		BigDecimal bSquare = new BigDecimal(mHouseDetail.getSquare());
		bSquare=bSquare.setScale(0, BigDecimal.ROUND_HALF_UP);
		msg.title = mHouseDetail.getArea()+" "+mHouseDetail.getAddr()+" "+mHouseDetail.getFrame()+" "+bSquare+"平 "+bPrice+"万";
		msg.description = "中原地产房源分享";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		bitmap.recycle();
		msg.thumbData =  baos.toByteArray();
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = "img"+String.valueOf(System.currentTimeMillis());
		req.message = msg;
		req.scene = shareTag == 0 ? SendMessageToWX.Req.WXSceneSession
				: SendMessageToWX.Req.WXSceneTimeline;
		AppInstance.mWXAPI.sendReq(req);
	}
	public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
								   double newHeight) {
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
				(int) height, matrix, true);
		return bitmap;
	}
	// 在需要分享的地方添加代码:
	// wechatShare(0);//分享到微信好友
	// wechatShare(1);//分享到微信朋友圈
	private void returnBitmap(final String urlpath) {
		new Thread(new Runnable() {
			public void run() {
				try {
					Bitmap btMap =null;
					URL url = new URL(urlpath);
					URLConnection conn = url.openConnection();
					InputStream in;
					conn.connect();
					in = conn.getInputStream();
					btMap = BitmapFactory.decodeStream(in);
					dismissDialog();
					Message msg = Message.obtain();
					msg.what =R.id.doGetImg;
					msg.obj=btMap;
					mHander.sendMessage(msg);
				} catch (IOException e) {
					e.printStackTrace();
					dismissDialog();
					mHander.sendEmptyMessage(R.id.doGetImgError);
//					MethodsExtra.toast(mContext,"图片获取失败");
				}
			}
		}).start();
	}

	@Override
	public void netWorkResult(String name, String className, Object data) {
		dismissDialog();
		if (name.equals(CST_JS.NOTIFY_NATIVE_DOROOMVIEW_RESULT)) {
			String strJson = (String) data;
			JSReturn jReturnHouseDetail = MethodsJson.jsonToJsReturn(strJson,
					HouseDetail.class);
			HouseDetail hDetail = (HouseDetail) jReturnHouseDetail.getObject();
			if(jReturnHouseDetail.isSuccess()){
				String roomNo= hDetail.getRoomNO();
				mTvName.setText(mHouseDetail.getAddr() + " "+hDetail.getBuiding()+" " + mHouseDetail.getDelCode());
				tv_lookshihao_houseDetailActivity.setVisibility(View.INVISIBLE);
				mTvLouceng.setText("楼层：" + hDetail.getFloor());
				LouCeng=hDetail.getFloor();
				tv_shihao_houseDetailActivity.setText("室号："+roomNo);
			}else{
				MethodsExtra.toast(mContext, jReturnHouseDetail.getMsg());
			}
		}else if (name.equals(CST_JS.NOTIFY_NATIVE_BORROW_KEY_FROM_SHOP_RESULT)) {
			mBorrowKey.setEnabled(true);
			// 借用钥匙返回
			String strJson = (String) data;
			JSReturn jReturn = MethodsJson.jsonToJsReturn(strJson,
					BorrowKey.class);

			BorrowKey borrowKey = (BorrowKey) jReturn.getObject();
			if (borrowKey != null) {
				if (borrowKey.isSuccess() == false) {
					// 钥匙不存在
					/*myDialog=new MyDialog.Builder(this);
					myDialog.setMessage(borrowKey.getMsg());
					myDialog.setTitle("提示");
					myDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					myDialog.create().show();*/
					MethodsExtra.toast(mContext,borrowKey.getMsg());
					MethodsDeliverData.mKeyType = 4;
				} else {
					/*// 借钥匙操作
					myDialog=new MyDialog.Builder(this);
					myDialog.setMessage(borrowKey.getMsg());
					myDialog.setTitle("提示");
					myDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							finish();
						}
					});
					myDialog.create().show();*/
					MethodsExtra.toast(mContext, borrowKey.getMsg());
				}
			} else {
				// 钥匙不存在
				MethodsDeliverData.mKeyType = 0;

				MethodsDeliverData.mKeyType = 4;
			}
//////////////			showBorrowKey();

		} else if (name.equals(CST_JS.NOTIFY_NATIVE_CONTACT_LIST_RESULT)) {
			String strJson = (String) data;
			JSReturn jReturn = MethodsJson.jsonToJsReturn(strJson,
					ContactDetail.class);

			ListView mListViewCustormer = (ListView) mCallCustormerDialog
					.findViewById(R.id.lv_custormerPhone_HouseDetailActivity);
			ContactDetail contactData = (ContactDetail) jReturn.getObject();
			List<ContactItem> testData = contactData.getContactList(); // new
			// ArrayList<ContactItem>();
			if (testData == null || testData.size() == 0) {
				MethodsExtra.toast(mContext, "此房源未录入电话");
			}
			CustormerPhoneAdapter phoneAdapter = new CustormerPhoneAdapter(
					mContext, testData);
			mListViewCustormer.setAdapter(phoneAdapter);
			mListViewCustormer
					.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
												int arg2, long arg3) {
							TextView tvTel = (TextView) arg1
									.findViewById(R.id.tv_custNothing_CustormerPhoneAdapter);

							Intent intent = new Intent(Intent.ACTION_CALL, Uri
									.parse("tel:" + tvTel.getText().toString().trim()));
							mContext.startActivity(intent);
							mCallCustormerDialog.dismiss();
						}
					});

		} else if (name.equals(CST_JS.NOTIFY_NATIVE_HOU_DETAIL_RESULT)) {

			// 这里再多次进入的时候会被多次调用，导致图片数据重叠，增加。暂时做了修复。
			if (isFirstDataCall == 0) {
				String strJson = (String) data;
				JSReturn jReturn = MethodsJson.jsonToJsReturn(strJson,
						HouseDetail.class);
				if (!jReturn.isSuccess()) {
					MethodsExtra.toast(mContext, jReturn.getMsg());
					myDialog = new MyDialog.Builder(this);
					myDialog.setMessage(jReturn.getMsg());
					myDialog.setTitle("提示");
					myDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					myDialog.setDismissListener(new DialogInterface.OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface dialog) {
							finish();
						}
					});
					myDialog.create().show();
				} else {
					ll_houes_detail.setVisibility(View.GONE);
					relt_house_detail_layout.setVisibility(View.VISIBLE);
					mHouseDetail = (HouseDetail) jReturn.getObject();
					final List<Image> images = mHouseDetail.getImg();
					boolean isNotTheSame = false;
					if (mListImages != null && mListImages.size() == images.size()) {
						for (int i = 0; i < mListImages.size(); i++) {
							Image image0 = mListImages.get(i);
							Image image1 = images.get(i);
							Log.d(TAG,
									"wanggsxHouseImage 数据回调 比较 " + image0.getUrl()
											+ ":" + image0.getType() + "<>"
											+ image1.getUrl() + ":"
											+ image1.getType());
							if (image0.getUrl() != image1.getUrl()
									|| image0.getType() != image1.getType()) {
								isNotTheSame = true;
							}
						}
					} else {
						isNotTheSame = true;
					}
					if (!isNotTheSame) {
						return;
					} else {
						mListImages = images;
						Log.d(TAG,
								"wanggsxHouseImage 数据回调 图片数量：" + mListImages.size());
					}
					if (images != null && images.size() >= 1) {
						Log.d(TAG, "wanggsxHouseImage 数据回调 创建图片路径列表");
						mArrayListViews.clear();
						for (int i = 0; i < images.size(); i++) {
							mArrayListViews
									.add(LayoutInflater
											.from(mContext)
											.inflate(
													R.layout.model_viewpager_adapter_item_view,
													null));
						}
						mArrayListBitmapPaths.clear();
						new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									for (int i = 0; i < mArrayListViews.size()
											&& i < images.size(); i++) {
										String url = images.get(i).getUrl();
										if(i==0){
											shareImgUrl=images.get(i).getUrl();
										}
										String filePath = MethodsFile
												.getAutoFileDirectory()
												+ MethodsFile
												.getFileNameFromPath(url);
										MethodsFile.downloadImgByUrl(url, filePath);
										mArrayListBitmapPaths.add(filePath);
										View view0 = mArrayListViews.get(i);
										ImageView mImageView = (ImageView) view0
												.findViewById(R.id.imgView_modelViewPagerAdapterItemView);
										mImageView.setTag(filePath);
									}

									mHander.sendEmptyMessage(R.id.doRequest);
								} catch (Exception e) {
								}
							}
						}).start();
					}
					BigDecimal b, bPrice;
					try {
						b = new BigDecimal(mHouseDetail.getUnitprice());// 保留两位小数
					} catch (Exception e) {
						b = new BigDecimal("0.00");
					}

//					if (mHouseDetail.getDelCode().charAt(4) == 'Z') {
					if (HouseItem.ZU.equals(mHouseDetail.getDelegationType())) {//Z
						mTvPriceUnit.setText("元");
						try {
							bPrice = new BigDecimal(mHouseDetail.getPrice());
						} catch (Exception e) {
							bPrice = new BigDecimal("0.00");
						}
						mTvPrice.setText(bPrice.setScale(0,
								BigDecimal.ROUND_HALF_UP) + "");
						mTvDetail.setText(mHouseDetail.getFrame() + "  "
								+ mHouseDetail.getSquare() + " " + "㎡  "
								+ mHouseDetail.getFloor() + "  "
								+ mHouseDetail.getOrient() + " "
								+ mHouseDetail.getActiveTime());
					} else {
						mTvPriceUnit.setText("万");
						try {
							bPrice = new BigDecimal(Double.parseDouble(mHouseDetail
									.getPrice()) / 10000);
						} catch (Exception e) {
							bPrice = new BigDecimal("0.00");
						}
						mTvPrice.setText(bPrice.setScale(0,
								BigDecimal.ROUND_HALF_UP) + "");

//						String delCode = mHouseDetail.getDelCode().substring(4, 5);
//						if ("Z".equalsIgnoreCase(delCode)) {
						if (HouseItem.ZU.equals(mHouseDetail.getDelegationType())) {
							mTvDetail.setText(mHouseDetail.getFrame()
									+ "  "
									+ mHouseDetail.getSquare()
									+ "㎡  "
									+ mHouseDetail.getFloor()
									+ "  "
									+ mHouseDetail.getOrient()
									+ "  "
									+ mHouseDetail.getActiveTime());
						} else {
							mTvDetail.setText(mHouseDetail.getFrame()
									+ "  "
									+ mHouseDetail.getSquare()
									+ "㎡  "
									+ mHouseDetail.getFloor()
									+ "  "
									+ mHouseDetail.getOrient()
									+ "  "
									+ b.setScale(2, BigDecimal.ROUND_HALF_UP)
									.doubleValue() + "万/㎡ "
									+ mHouseDetail.getActiveTime());
						}

					}
					mTvName.setText(mHouseDetail.getAddr() + " " + mHouseDetail.getDelCode());
					if(mHouseDetail.isShowroomBtn()){
						if(mHouseDetail.isShowroomInfo()){
							mTvLouceng.setText("楼层：" + mHouseDetail.getFloor());
							tv_shihao_houseDetailActivity.setText("室号："+mHouseDetail.roomNo);
							tv_lookshihao_houseDetailActivity.setVisibility(View.INVISIBLE);
						}else{
							tv_lookshihao_houseDetailActivity.setVisibility(View.VISIBLE);
							if(LouCeng!=null){
								mTvLouceng.setText("楼层："+LouCeng);
							}else{
								mTvLouceng.setText("楼层：");
							}
						}
					}else{
						if(mHouseDetail.isShowroomInfo()){
							mTvName.setText(mHouseDetail.getAddr() + " "+mHouseDetail.getBuildingname()+" " + mHouseDetail.getDelCode());
							mTvLouceng.setText("楼层：" + mHouseDetail.getFloor());
							tv_shihao_houseDetailActivity.setText("室号："+mHouseDetail.roomNo);
							tv_lookshihao_houseDetailActivity.setVisibility(View.INVISIBLE);
						}
					}
				/*	if(mHouseDetail.isShowroom()){
						mTvLouceng.setText("楼层：" + mHouseDetail.getFloor());
						tv_shihao_houseDetailActivity.setText("室号："+mHouseDetail.roomNo);
						tv_lookshihao_houseDetailActivity.setVisibility(View.INVISIBLE);
					}else{

					}*/
					//				mTvLouceng.setText("楼层：" + mHouseDetail.getFloor());
					mTvDistanst.setText("距离：" + mHouseDetail.getFloor());
					mTvYear.setText("年代：" + mHouseDetail.getYear());
					mTvBankuai.setText("板块：" + mHouseDetail.getArea());
					mTvSaleTime.setText("放盘时间：" + mHouseDetail.getDelDate());

					List<Exclude> excludes = mHouseDetail.getExclude();
					if (excludes != null && excludes.size() >= 1) {
						mEnsureUser.setVisibility(View.VISIBLE);
						HouseDetailSignAdapter adapter = new HouseDetailSignAdapter(
								mContext, excludes);
						mLvSigns.setAdapter(adapter);
						// MethodsExtra.resetListHeightWithFixItemHeight(mContext,
						// mLvSigns, 62, excludes.size());
						MethodsExtra.resetListHeightBasedOnChildren(mLvSigns);

					} else {
						mEnsureUser.setVisibility(View.GONE);
					}

					List<Track> tracks = mHouseDetail.getTrack();
					if (tracks != null && tracks.size() >= 1) {
						HouseDetailTrackAdapter houseDetailAdapter = new HouseDetailTrackAdapter(
								mContext, tracks);
						mLvTracks.setAdapter(houseDetailAdapter);
						// MethodsExtra.resetListHeightWithFixItemHeight(mContext,
						// mLvTracks, 70, tracks.size());
						MethodsExtra.resetListHeightBasedOnChildren(mLvTracks);
					}
				}
			}
			isFirstDataCall = isFirstDataCall + 1;
		} else if (name.equals(CST_JS.NOTIFY_NATIVE_CLAIM_HOUSE_RESULT)) {
			//抢公房
			JSReturn jsReturn = MethodsJson.jsonToJsReturn((String) data,
					Object.class);
			if (jsReturn.isSuccess()) {
				MethodsExtra.toast(mContext, jsReturn.getMsg());
				setResult(MyConstant.REFRESH);
				finish();
				/*myDialog=new MyDialog.Builder(this);
				myDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						setResult(ConstantResult.REFRESH);
						finish();
					}
				});
				myDialog.setTitle("提示");
				myDialog.setMessage(jsReturn.getMsg());
				myDialog.create().show();*/
			} else {
				MethodsExtra.toast(mContext, jsReturn.getMsg());
			}
		}
	}

	@Override
	public void onRefresh() {

	}

	@Override
	public void onLoadMore() {

	}
	public abstract class NoDoubleClickListener implements View.OnClickListener {

		public static final int MIN_CLICK_DELAY_TIME = 1000;
		private long lastClickTime = 0;

		@Override
		public void onClick(View v) {
			long currentTime = Calendar.getInstance().getTimeInMillis();
			if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
				lastClickTime = currentTime;
				onNoDoubleClick(v);
			}
		}

		protected abstract void onNoDoubleClick(View v);
	}
}
