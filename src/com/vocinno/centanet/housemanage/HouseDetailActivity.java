package com.vocinno.centanet.housemanage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.AppInstance;
import com.vocinno.centanet.apputils.SharedPreferencesUtils;
import com.vocinno.centanet.apputils.SuperSlideMenuActivity;
import com.vocinno.centanet.apputils.adapter.MyPagerAdapter;
import com.vocinno.centanet.apputils.adapter.MyPagerAdapter.MType;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.apputils.selfdefineview.ListViewNeedResetHeight;
import com.vocinno.centanet.customermanage.adapter.CustormerPhoneAdapter;
import com.vocinno.centanet.model.BorrowKey;
import com.vocinno.centanet.model.ContactDetail;
import com.vocinno.centanet.model.ContactItem;
import com.vocinno.centanet.model.Exclude;
import com.vocinno.centanet.model.HouseDetail;
import com.vocinno.centanet.model.Image;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.model.Track;
import com.vocinno.utils.MethodsData;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsFile;
import com.vocinno.utils.MethodsJni;
import com.vocinno.utils.MethodsJson;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * 房源详情
 * 
 * @author Administrator
 * 
 */
public class HouseDetailActivity extends SuperSlideMenuActivity {

	private HouseDetail mHouseDetail = null;
	private ScrollView mScrollView = null;

	private View mBackView, mMoreView, mTitleView;
	private ListViewNeedResetHeight mLvTracks, mLvSigns;
	private TextView mTvName, mTvPrice, mTvDetail, mTvLouceng, mTvDistanst,
			mTvYear, mTvBankuai, mTvSaleTime, mTvPriceUnit;
	private Dialog mMenuDialog;
	private Dialog mCallCustormerDialog;
	private ViewPager mViewPager;
	private MyPagerAdapter myPagerAdapter;
	private List<Image> mListImages = null;
	private LinearLayout mllytDataContainer;
	private RelativeLayout mKeysNotHere; // 钥匙不在的提示栏
	private RelativeLayout mHouseUi;
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
	private ImageView mImageView;
	private Drawable drawable;

	private static final int Scroll_to_Top = 100001;

	@Override
	@SuppressLint("HandlerLeak")
	public Handler setHandler() {
		return new Handler() {
			@Override
			public void handleMessage(Message msg) {
				HouseDetailActivity.this.closeMenu(msg);
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
		MethodsExtra.findHeadTitle1(mContext, mRootView, R.string.housedecribe,
				null);
		mBackView = MethodsExtra.findHeadLeftView1(mContext, mRootView, 0, 0);
		mMoreView = MethodsExtra.findHeadRightView1(mContext, mRootView, 0, 0);
		mTitleView = MethodsExtra
				.findHeadTitle1(mContext, mRootView, 0, "房源详情");
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
		mImageView = (ImageView) findViewById(R.id.img_genjin_houseDetailActivity);
		mTvQiang = (TextView) findViewById(R.id.tv_seize_houseSoueceDetailActivity);
		mHouseUi = (RelativeLayout) findViewById(R.id.relt_house_detail_layout);
		mTvName = (TextView) findViewById(R.id.tv_name_houseDetailActivity);
		mTvPrice = (TextView) findViewById(R.id.tv_price_houseDetailActivity);
		mTvPriceUnit = (TextView) findViewById(R.id.tv_priceUnit_houseDetailActivity);
		mTvDetail = (TextView) findViewById(R.id.tv_details_houseDetailActivity);
		mTvLouceng = (TextView) findViewById(R.id.tv_louceng_houseDetailActivity);
		mTvDistanst = (TextView) findViewById(R.id.tv_distance_houseDetailActivity);
		mTvYear = (TextView) findViewById(R.id.tv_year_houseDetailActivity);
		mTvBankuai = (TextView) findViewById(R.id.tv_bankuai_houseDetailActivity);
		mTvSaleTime = (TextView) findViewById(R.id.tv_saleTime_houseDetailActivity);
		mLvSigns = (ListViewNeedResetHeight) findViewById(R.id.lv_sign_houseDetailActivity);
		mScrollView = (ScrollView) findViewById(R.id.scroller_houseDetailActivity);
		// 根据数据显示房源的具体详情
		mHouseUi.setVisibility(View.GONE);
	}

	@Override
	public void setListener() {
		mBackView.setOnClickListener(this);
		mMoreView.setOnClickListener(this);
		mBorrowKey.setOnClickListener(this);
		mReturnKey.setOnClickListener(this);
		mPassKey.setOnClickListener(this);
		mGrabHouse.setOnClickListener(this);
		mQiang.setOnClickListener(this);
		mImageView.setOnClickListener(this);
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
				CST_JS.NOTIFY_NATIVE_CONTACT_LIST_RESULT, TAG);
		MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_HOU_DETAIL_RESULT, TAG);
		MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_BORROW_KEY_FROM_SHOP_RESULT, TAG);
		MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_CLAIM_HOUSE_RESULT, TAG);
		// 调用初始化数据
		MethodsJni
				.callProxyFun(
						CST_JS.JS_ProxyName_HouseResource,
						CST_JS.JS_Function_HouseResource_getHouseDetail,
						CST_JS.getJsonStringForHouseListGetHouseDetail(MethodsDeliverData.mDelCode));
	}

	@Override
	protected void onResume() {
		super.onResume();
		isFirstDataCall = 0;
		MethodsJni
				.callProxyFun(
						CST_JS.JS_ProxyName_HouseResource,
						CST_JS.JS_Function_HouseResource_getHouseDetail,
						CST_JS.getJsonStringForHouseListGetHouseDetail(MethodsDeliverData.mDelCode));
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.img_left_mhead1:
			onBack();
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
			wechatShare(0);
			break;
		case R.id.llyt_sharequan_HouseDetailActivity:
			// 这里是友盟分享的dialog
			mMenuDialog.dismiss();
			wechatShare(1);
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
			MethodsExtra.startActivity(mContext,
					AddHousePictureActivity.class);
			break;
		case R.id.llyt_seeFollowUp_HouseDetailActivity:
			mMenuDialog.dismiss();
			if (mHouseDetail != null && mHouseDetail.getDelCode() != null) {
				MethodsDeliverData.mDelCode = mHouseDetail.getDelCode();
			} else {
				MethodsExtra.toast(mContext, "mHouseDetail不能为空");
			}
			MethodsExtra.startActivity(mContext,
					SeeFollowInDetailActivity.class);
			break;
		case R.id.rlyt_passKey_houseDetailActivity:
			MethodsDeliverData.mKeyType = 3;
			showBorrowKey();
			break;
		case R.id.rlyt_borrowKey_houseDetailActivity:
			// 钥匙借用
			MethodsJni
					.callProxyFun(
							CST_JS.JS_ProxyName_KeyProxy,
							CST_JS.JS_Function_KeyProxy_borrowKeyFromShop,
							CST_JS.getJsonStringForborrowKeyFromShop(MethodsDeliverData.mDelCode));
			break;
		case R.id.rlyt_returnKey_houseDetailActivity:
			// 归还钥匙
			MethodsJni
					.callProxyFun(
							CST_JS.JS_ProxyName_KeyProxy,
							CST_JS.JS_Function_KeyProxy_returnKeyToShop,
							CST_JS.getJsonStringForborrowKeyFromShop(MethodsDeliverData.mDelCode));
			break;
		case R.id.rlyt_seize_houseDetailActivity:
			showCallCosturmerDialog();
			break;
		case R.id.rlyt_qiang_houseDetailActivity:
			// 抢
			MethodsJni
					.callProxyFun(
							CST_JS.JS_ProxyName_HouseResource,
							CST_JS.JS_Function_HouseResource_claimHouse,
							CST_JS.getJsonStringForClaimHouse(MethodsDeliverData.mDelCode));
			break;
		case R.id.img_genjin_houseDetailActivity:
			if (mHouseDetail != null && mHouseDetail.getDelCode() != null) {
				MethodsDeliverData.mDelCode = mHouseDetail.getDelCode();
			} else {
				MethodsExtra.toast(mContext, "mHouseDetail不能为空");
			}
			MethodsExtra
					.startActivity(mContext, AddFollowInHouseActivity.class);
			break;
		default:
			break;
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
				.callProxyFun(
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
		mRootView = null;
		System.gc();
		super.onDestroy();
	}

	@Override
	public void onBack() {
		if (mCallCustormerDialog != null && mCallCustormerDialog.isShowing()) {
			mCallCustormerDialog.dismiss();
		} else {
			// 注销通知：
			MethodsJni.removeNotificationObserver(
					CST_JS.NOTIFY_NATIVE_CONTACT_LIST_RESULT, TAG);
			MethodsJni.removeNotificationObserver(
					CST_JS.NOTIFY_NATIVE_HOU_DETAIL_RESULT, TAG);
			MethodsJni.removeNotificationObserver(
					CST_JS.NOTIFY_NATIVE_BORROW_KEY_FROM_SHOP_RESULT, TAG);
			MethodsJni.removeNotificationObserver(
					CST_JS.NOTIFY_NATIVE_CLAIM_HOUSE_RESULT, TAG);
			MethodsJni.removeAllNotifications(TAG);

			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void notifCallBack(String name, String className, Object data) {
		if (name.equals(CST_JS.NOTIFY_NATIVE_BORROW_KEY_FROM_SHOP_RESULT)) {
			// 借用钥匙返回
			String strJson = (String) data;
			JSReturn jReturn = MethodsJson.jsonToJsReturn(strJson,
					BorrowKey.class);

			BorrowKey borrowKey = (BorrowKey) jReturn.getObject();
			if (borrowKey != null) {
				if (borrowKey.isSuccess() == false) {
					// 钥匙不存在
					MethodsDeliverData.mKeyType = 4;
				} else {
					// 借钥匙操作
					finish();
				}
			} else {
				// 钥匙不存在
				MethodsDeliverData.mKeyType = 0;

				MethodsDeliverData.mKeyType = 4;
			}
			showBorrowKey();

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
									.parse("tel:" + tvTel.getText().toString()));
							mContext.startActivity(intent);
							mCallCustormerDialog.dismiss();
						}
					});

		} else if (name.equals(CST_JS.NOTIFY_NATIVE_HOU_DETAIL_RESULT)) {
			// 这里再多次进入的时候会被多次调用，导致图片数据重叠，增加。暂时做了修复。
			if (isFirstDataCall == 0) {
				mHouseUi.setVisibility(View.VISIBLE);
				String strJson = (String) data;
				JSReturn jReturn = MethodsJson.jsonToJsReturn(strJson,
						HouseDetail.class);
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

				mTvName.setText(mHouseDetail.getAddr());
				if (mHouseDetail.getDelCode().charAt(4) == 'Z') {
					mTvPriceUnit.setText("元");
					try {
						bPrice = new BigDecimal(mHouseDetail.getPrice());
					} catch (Exception e) {
						bPrice = new BigDecimal("0.00");
					}
					mTvPrice.setText(bPrice.setScale(2,
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
					mTvPrice.setText(bPrice.setScale(2,
							BigDecimal.ROUND_HALF_UP) + "");
					mTvDetail.setText(mHouseDetail.getFrame()
							+ "  "
							+ mHouseDetail.getSquare()
							+ "㎡  "
							+ mHouseDetail.getFloor()
							+ "  "
							+ mHouseDetail.getOrient()
							+ "  "
							+ b.setScale(2, BigDecimal.ROUND_HALF_UP)
									.doubleValue() + "元/㎡ "
							+ mHouseDetail.getActiveTime());
				}
				mTvLouceng.setText("楼层：" + mHouseDetail.getFloor());
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
			isFirstDataCall = isFirstDataCall + 1;
		} else if (name.equals(CST_JS.NOTIFY_NATIVE_CLAIM_HOUSE_RESULT)) {
			JSReturn jsReturn = MethodsJson.jsonToJsReturn((String) data,
					Object.class);
			if (jsReturn.isSuccess()) {
				MethodsExtra.toast(mContext, "恭喜你抢到啦～～");
				onBack();
			} else {
				MethodsExtra.toast(mContext, "没抢到，不要哭哦～");
			}
		}

	}

	/**
	 * 微信分享 0：分享到微信好友 1：分享到微信朋友圈
	 **/
	private void wechatShare(int flag) {
		WXTextObject textObj = new WXTextObject();
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
		AppInstance.mWXAPI.sendReq(req);
	}
	// 在需要分享的地方添加代码:
	// wechatShare(0);//分享到微信好友
	// wechatShare(1);//分享到微信朋友圈
}
