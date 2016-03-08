package com.vocinno.centanet.customermanage;

import java.util.List;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.SuperSlideMenuActivity;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.apputils.selfdefineview.ListViewNeedResetHeight;
import com.vocinno.centanet.customermanage.adapter.CustomerDetailAdapter;
import com.vocinno.centanet.model.CustomerDetail;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.model.Requets;
import com.vocinno.centanet.model.Track;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJni;
import com.vocinno.utils.MethodsJson;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomerDetailActivity extends SuperSlideMenuActivity {
	private String mCusterCode = null;
	private View mBackView, mImgViewAddTrack;
	private RelativeLayout mGrabCustomer;
	private TextView mTvCustomerCode, mTvCustomerName, mTvType, mTvAcreage,
			mTvPrice, mTvTenancyTime, mTvMoney, mTvPaymenttype;
	private ListViewNeedResetHeight mLvTracks;
	private ImageView mImgViewPhone, mImgViewQQ, mImgWeixin;
	private CustomerDetail mDetail = null;
	private Drawable drawable;

	private static final int RESET_LISTVIEW_TRACK = 1001;

	public CustomerDetailActivity() {

	}

	@Override
	public Handler setHandler() {
		return new Handler() {
			@Override
			public void handleMessage(Message msg) {
				CustomerDetailActivity.this.closeMenu(msg);
				switch (msg.what) {
				case RESET_LISTVIEW_TRACK:
					MethodsExtra.resetListHeightBasedOnChildren(mLvTracks);
					break;
				default:
					break;
				}
			}
		};
	}

	@Override
	public int setContentLayoutId() {
		return R.layout.activity_customer_detail;
	}

	@SuppressLint("NewApi")
	@Override
	public void initView() {
		MethodsExtra.findHeadTitle1(mContext, mRootView, R.string.customernews,
				null);
		mBackView = MethodsExtra.findHeadLeftView1(mContext, mRootView, 0, 0);
		mGrabCustomer = (RelativeLayout) findViewById(R.id.rlyt_seize_customerDetailActivity);
		mTvCustomerCode = (TextView) findViewById(R.id.tv_customercode_customerDetailActivity);
		mTvCustomerName = (TextView) findViewById(R.id.tv_customername_customerDetailActivity);
		mTvType = (TextView) findViewById(R.id.tv_type_customerDetailActivity);
		mTvAcreage = (TextView) findViewById(R.id.tv_acreage_customerDetailActivity);
		mTvPrice = (TextView) findViewById(R.id.tv_price_customerDetailActivity);
		mTvTenancyTime = (TextView) findViewById(R.id.tv_tenancytime_customerDetailActivity);
		mTvMoney = (TextView) findViewById(R.id.tv_money_customerDetailActivity);
		mTvPaymenttype = (TextView) findViewById(R.id.tv_paymenttype_customerDetailActivity);
		mLvTracks = (ListViewNeedResetHeight) findViewById(R.id.lv_track_customerDetailActivity);
		mImgViewAddTrack = findViewById(R.id.imgView_addTrack_customerDetailActivity);
		mImgViewPhone = (ImageView) findViewById(R.id.imgView_phone_customerDetailActivity);
		mImgViewQQ = (ImageView) findViewById(R.id.imgView_qq_customerDetailActivity);
		mImgWeixin = (ImageView) findViewById(R.id.imgView_wx_customerDetailActivity);

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		// adapter.notifyDataSetChanged();
	}

	@Override
	public void setListener() {
		mBackView.setOnClickListener(this);
		mGrabCustomer.setOnClickListener(this);
		mImgViewAddTrack.setOnClickListener(this);
		mImgViewQQ.setOnClickListener(this);
		mImgViewPhone.setOnClickListener(this);
		mImgWeixin.setOnClickListener(this);
	}

	@Override
	public void initData() {
		// 添加通知
		MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_GET_CUSTOMER_DETAIL_RESULT, TAG);
		MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_CLAIM_CUSTOMER_RESULT, TAG);
		mCusterCode = MethodsDeliverData.string;
		MethodsDeliverData.string = null;
		// 调用数据
		MethodsJni.callProxyFun(CST_JS.JS_ProxyName_CustomerList,
				CST_JS.JS_Function_CustomerList_getCustomerInfo,
				CST_JS.getJsonStringForGetCustomerInfo(mCusterCode));
		if (MethodsDeliverData.flag1 == 1) {
			MethodsDeliverData.flag1 = -1;
			mGrabCustomer.setVisibility(View.VISIBLE);
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.img_left_mhead1:
			onBack();
			break;
		case R.id.imgView_addTrack_customerDetailActivity:
			MethodsDeliverData.string = mCusterCode;

			// listTracks
			Intent intent = new Intent(mContext,
					AddFollowInCustomerActivity.class);
			MethodsExtra.startActivityForResult(mContext, 10, intent);
			// MethodsExtra.startActivity(mContext,
			// AddFollowInCustomerActivity.class);
			break;
		case R.id.imgView_phone_customerDetailActivity:
			if (mDetail != null && mDetail.getPhone() != null) {
				MethodsExtra.tel(mContext, mDetail.getPhone());
			} else {
				MethodsExtra.toast(mContext, "该房源没有维护电话号码");
			}
			break;
		case R.id.imgView_qq_customerDetailActivity:
			// MethodsExtra.openQQChat(mContext, "504964825");
			if (mDetail != null && !TextUtils.isEmpty(mDetail.getQq())
					&& !mDetail.getQq().equals("null")) {
				MethodsExtra.openQQChat(mContext, mDetail.getQq());
			} else {
				MethodsExtra.toast(mContext, "该房源没有维护QQ号码");
			}
			break;
		case R.id.imgView_wx_customerDetailActivity:
			if (mDetail != null && !TextUtils.isEmpty(mDetail.getWechat())
					&& !mDetail.getWechat().equals("null")) {
				try {
					// 登录微信
					Intent intent1 = new Intent();
					ComponentName cmp = new ComponentName("com.tencent.mm",
							"com.tencent.mm.ui.LauncherUI");
					intent1.setAction(Intent.ACTION_MAIN);
					intent1.addCategory(Intent.CATEGORY_LAUNCHER);
					intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent1.setComponent(cmp);
					startActivity(intent1);
				} catch (Exception e) {
					MethodsExtra.toast(mContext, "没有安装微信");
				}
			} else {
				MethodsExtra.toast(mContext, "该房源没有维护微信号码");
			}
			break;
		case R.id.rlyt_seize_customerDetailActivity:
			// 抢
			MethodsJni.callProxyFun(CST_JS.JS_ProxyName_CustomerList,
					CST_JS.JS_Function_CustomerList_claimCustomer,
					CST_JS.getJsonStringForGetCustomerInfo(mCusterCode));
		default:
			break;
		}
	}

	@Override
	public void onBack() {
		MethodsJni.removeNotificationObserver(
				CST_JS.NOTIFY_NATIVE_GET_CUSTOMER_DETAIL_RESULT, TAG);
		MethodsJni.removeNotificationObserver(
				CST_JS.NOTIFY_NATIVE_CLAIM_CUSTOMER_RESULT, TAG);
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {
			return;
		}
		if (requestCode == 10) {
			Track track = new Track();
			track.setTracktime(data.getStringExtra("time"));
			track.setContent(data.getStringExtra("content"));
			listTracks.add(listTracks.size(), track);
			adapter = new CustomerDetailAdapter(mContext, listTracks);
			mLvTracks.setAdapter(adapter);
			mHander.sendEmptyMessageDelayed(RESET_LISTVIEW_TRACK, 50);
			adapter.notifyDataSetChanged();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void notifCallBack(String name, String className, Object data) {
		String strJson = (String) data;
		JSReturn jsReturn = MethodsJson.jsonToJsReturn(strJson,
				CustomerDetail.class);
		if (!jsReturn.isSuccess() || jsReturn.getObject() == null) {
			return;
		}
		mDetail = (CustomerDetail) jsReturn.getObject();
		mTvCustomerCode.setText("编号：" + mDetail.getCustCode());
		mTvCustomerName.setText("姓名：" + mDetail.getName());
		mTvPaymenttype.setText("付款方式：" + mDetail.getPaymentType());
		if (mDetail.isPay() == false) {
			mTvMoney.setText(R.string.money_false);
		} else {
			mTvMoney.setText(R.string.money_true);
		}
		// 填充跟踪信息列表
		listTracks = mDetail.getTracks();
		if (listTracks != null && listTracks.size() >= 1) {
			adapter = new CustomerDetailAdapter(mContext, listTracks);
			mLvTracks.setAdapter(adapter);
			mHander.sendEmptyMessageDelayed(RESET_LISTVIEW_TRACK, 50);
		}
		// 填充需求信息
		List<Requets> listReqs = mDetail.getRequets();
		if (listReqs != null && listReqs.size() >= 1) {
			Requets req = listReqs.get(0);
			mTvType.setText("类型：" + req.getReqType());// 类型
			mTvAcreage.setText("区域：" + req.getAcreage());
			mTvPrice.setText("租价：" + req.getPrice());// 价格
			mTvTenancyTime.setText("租期：" + req.getTenancyTime());
		}
		// 联系方式
		if (mDetail == null || TextUtils.isEmpty(mDetail.getPhone())||mDetail.getPhone().equals("null")) {
			mImgViewPhone.setImageResource(R.drawable.c_manage_icon_contact01);
		}
		if (mDetail == null || TextUtils.isEmpty(mDetail.getQq())||mDetail.getQq().equals("null")) {
			mImgViewQQ.setImageResource(R.drawable.c_manage_icon_qq01);
		}
		if (mDetail == null || TextUtils.isEmpty(mDetail.getWechat())||mDetail.getWechat().equals("null")) {
			mImgWeixin.setImageResource(R.drawable.c_manage_icon_wechat01);
		}
		if (name.equals(CST_JS.NOTIFY_NATIVE_CLAIM_CUSTOMER_RESULT)) {
			JSReturn jsReturn2 = MethodsJson.jsonToJsReturn(strJson,
					Object.class);
			if (jsReturn.isSuccess()) {
				MethodsExtra.toast(mContext, "恭喜你抢到啦～～");
				finish();
			} else {
				MethodsExtra.toast(mContext, "没抢到，不要哭哦～");
			}
		}
	}

	private CustomerDetailAdapter adapter;
	private List<Track> listTracks;

	public TextView getmTvCustomerCode() {
		return mTvCustomerCode;
	}
}
