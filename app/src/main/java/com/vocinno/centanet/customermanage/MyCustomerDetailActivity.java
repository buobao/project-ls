package com.vocinno.centanet.customermanage;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.selfdefineview.ListViewNeedResetHeight;
import com.vocinno.centanet.baseactivity.OtherBaseActivity;
import com.vocinno.centanet.customermanage.adapter.ContentAdapter;
import com.vocinno.centanet.customermanage.adapter.CustomerDetailAdapter;
import com.vocinno.centanet.model.CustomerDetail;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.model.Requets;
import com.vocinno.centanet.model.Track;
import com.vocinno.centanet.tools.Loading;
import com.vocinno.centanet.tools.OkHttpClientManager;
import com.vocinno.centanet.tools.constant.MyConstant;
import com.vocinno.centanet.tools.constant.NetWorkConstant;
import com.vocinno.centanet.tools.constant.NetWorkMethod;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyCustomerDetailActivity extends OtherBaseActivity {
	private String mCusterCode = null;
	private View mBackView, mImgViewAddTrack,mSubmit;
	private TextView mTvCustomerCode, mTvCustomerName, mTvType, mTvAcreage,tv_fangxing_cust,tv_quyu_customerDetailActivity,
			mTvPrice,tv_area_custdetail/*, mTvTenancyTimemTvMoney, *//*mTvPaymenttype*/,tv_money_customerDetailActivity;
	private ListViewNeedResetHeight mLvTracks;
	//	private ImageView  mImgViewQQ, mImgWeixin;
	private CustomerDetail mDetail = null;
	private RelativeLayout mImgViewPhone;
	private Drawable drawable;
	private static final int RESET_LISTVIEW_TRACK = 1001;
	private boolean firstRefresh=true,robRefresh=true,returnRefresh=true,firstGetContent=true;//防止重复加载数据
	private ImageView iv_add_demand_detail;
	public MyCustomerDetailActivity() {
	}

	@Override
	public Handler setHandler() {
		return null;
	}

	@Override
	public int setContentLayoutId() {
		return R.layout.activity_my_customer_detail;
	}

	@SuppressLint("NewApi")
	@Override
	public void initView() {
		drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		MethodsExtra.findHeadTitle1(mContext, baseView, R.string.customernews,
				null);
		mBackView = MethodsExtra.findHeadLeftView1(mContext, baseView, 0, 0);
		mTvCustomerCode = (TextView) findViewById(R.id.tv_customercode_customerDetailActivity);
		mTvCustomerName = (TextView) findViewById(R.id.tv_customername_customerDetailActivity);
		mTvType = (TextView) findViewById(R.id.tv_type_customerDetailActivity);
		mTvAcreage = (TextView) findViewById(R.id.tv_acreage_customerDetailActivity);
		tv_quyu_customerDetailActivity = (TextView) findViewById(R.id.tv_quyu_customerDetailActivity);
		tv_fangxing_cust = (TextView) findViewById(R.id.tv_fangxing_cust);
		mTvPrice = (TextView) findViewById(R.id.tv_price_customerDetailActivity);
		tv_area_custdetail = (TextView) findViewById(R.id.tv_area_custdetail);
		tv_money_customerDetailActivity = (TextView) findViewById(R.id.tv_money_customerDetailActivity);
		mLvTracks = (ListViewNeedResetHeight) findViewById(R.id.lv_track_customerDetailActivity);
		mImgViewAddTrack = findViewById(R.id.imgView_addTrack_customerDetailActivity);
		mImgViewPhone = (RelativeLayout) findViewById(R.id.imgView_phone_customerDetailActivity);

		mBackView.setOnClickListener(this);
		mImgViewAddTrack.setOnClickListener(this);
		mImgViewPhone.setOnClickListener(this);

		iv_add_demand_detail= (ImageView) findViewById(R.id.iv_add_demand_detail);
		iv_add_demand_detail.setOnClickListener(this);
	}

	@Override
	public void initData() {
		mCusterCode=getIntent().getStringExtra(MyConstant.custCode);
		getData();
		mImgViewPhone.setVisibility(View.VISIBLE);
	}
	private void getData() {
		Loading.showForExit(this);
		URL = NetWorkConstant.PORT_URL + NetWorkMethod.custInfo;
		Map<String, String> map = new HashMap<String, String>();
		map.put(NetWorkMethod.custCode, mCusterCode);
		OkHttpClientManager.getAsyn(URL, map, new OkHttpClientManager.ResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {
				Loading.dismissLoading();
			}
			@Override
			public void onResponse(String response) {
				Loading.dismissLoading();
				JSReturn jsReturn = MethodsJson.jsonToJsReturn(response, CustomerDetail.class);
				if (jsReturn.isSuccess()) {
					getCustInfo(jsReturn);
				} else {
					MethodsExtra.toast(mContext, jsReturn.getMsg());
				}
			}
		});
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.iv_add_demand_detail:
				intent=new Intent(this, AddDemandActivity.class);
				intent.putExtra(MyConstant.custCode, mCusterCode);
				startActivityForResult(intent,101);
				break;
			case R.id.img_right_mhead1:
				if(mDetail.getPhone()==null||mDetail.getPhone().length()<=0){
					MethodsExtra.toast(mContext,"暂无联系人号码");
				}else{
					List<CustomerDetail.Content>list=new ArrayList<CustomerDetail.Content>();
					CustomerDetail.Content content=new CustomerDetail().new Content();
					content.setName(mDetail.getName());
					content.setPhone(mDetail.getPhone());
					list.add(content);
					showCallCosturmerDialog(list);
				}
				break;
			case R.id.img_left_mhead1:
				finish();
				break;
			case R.id.imgView_addTrack_customerDetailActivity:
				MethodsDeliverData.string = mCusterCode;
				// listTracks
				intent=new Intent();
				intent.setClass(mContext, AddFollowInCustomerActivity.class);
				intent.putExtra(MyConstant.custCode, mCusterCode);
				startActivityForResult(intent, 101);
				break;
			case R.id.imgView_phone_customerDetailActivity:
				firstGetContent=true;
				getCustContactList();
				break;
			/*//钥匙管理
			case R.id.rlyt_key_house_main_page_slid_menus:
				MyUtils.removeActivityFromList();
				MethodsExtra.startActivity(mContext, KeyManageActivity.class);
				break;
			//我的客源
			case R.id.rlyt_my_customer_main_page_slid_menus:
				MyUtils.removeActivityFromList();
				MethodsDeliverData.keYuanOrGongKe=1;
				MethodsDeliverData.isMyCustomer = true;
				MethodsExtra.startActivity(mContext,
						CustomerManageActivity.class);
				break;
			//抢公售
			case R.id.rlyt_grab_house_main_page_slid_menus:
				MyUtils.removeActivityFromList();
				MethodsDeliverData.flag = 1;
				MethodsDeliverData.mIntHouseType = HouseType.GONG_FANG;
				startIntentToGongFangManager(0);
				break;
			//抢公租
			case R.id.rlyt_grab_house_main_page_slid_menus2:
				MyUtils.removeActivityFromList();
				MethodsDeliverData.flag = 1;
				MethodsDeliverData.mIntHouseType = HouseType.GONG_FANGZU;
				startIntentToGongFangManager(1);
				break;
			//抢公客
			case R.id.rlyt_grab_customer_main_page_slid_menus:
				MyUtils.removeActivityFromList();
				MethodsDeliverData.keYuanOrGongKe=0;
				MethodsDeliverData.flag = 1;
				MethodsDeliverData.isMyCustomer = false;
				MethodsExtra.startActivity(mContext,
						CustomerManageActivity.class);
				break;
			//pin码
			case R.id.rlyt_password_main_page_slid_menus:
				MyUtils.removeActivityFromList();
				MethodsExtra.startActivity(mContext, KeyGetInActivity.class);
				break;
			//扫一扫
			case R.id.rlyt_sacn_customer_main_page_slid_menus:
				MyUtils.removeActivityFromList();
				MethodsExtra.startActivity(mContext, CaptureActivity.class);
				break;
			//我的提醒
			case R.id.rlyt_remind_customer_main_page_slid_menus:
				MyUtils.removeActivityFromList();
				MethodsExtra.startActivity(mContext, MessageListActivity.class);
				break;*/

			default:
				break;
		}
	}
	private void getCustContactList() {
		Loading.showForExit(this);
		URL = NetWorkConstant.PORT_URL + NetWorkMethod.custContactList;
		Map<String, String> map = new HashMap<String, String>();
		map.put(NetWorkMethod.custCode, mCusterCode);
		OkHttpClientManager.getAsyn(URL, map, new OkHttpClientManager.ResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {
				Loading.dismissLoading();
			}

			@Override
			public void onResponse(String response) {
				Loading.dismissLoading();
				JSReturn jsReturn = MethodsJson.jsonToJsReturn(response, CustomerDetail.class);
				if (jsReturn.isSuccess()) {
					CustomerDetail contentList = getContent(response);
					if (contentList != null) {
						List<CustomerDetail.Content> list = contentList.getContent();
						showCallCosturmerDialog(list);
					} else {
						MethodsExtra.toast(mContext, jsReturn.getMsg());
					}
				} else {
					MethodsExtra.toast(mContext, jsReturn.getMsg());
				}
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == MyConstant.REFRESH) {
			URL= NetWorkConstant.PORT_URL+ NetWorkMethod.custInfo;
			Map<String,String>map=new HashMap<String,String>();
			map.put(NetWorkMethod.custCode,mCusterCode);
			Loading.show(this);
			OkHttpClientManager.postAsyn(URL, map, new OkHttpClientManager.ResultCallback<String>() {
				@Override
				public void onError(Request request, Exception e) {
					Loading.dismissLoading();
				}
				@Override
				public void onResponse(String response) {
					Loading.dismissLoading();
					JSReturn jsReturn = MethodsJson.jsonToJsReturn(response,
							CustomerDetail.class);
					if(jsReturn.isSuccess()){
						getCustInfo(jsReturn);
					}
				}
			});
		}
	}
	private Dialog mCallCustormerDialog;
	private void showCallCosturmerDialog(List<CustomerDetail.Content> list) {
		mCallCustormerDialog = new Dialog(mContext, R.style.Theme_dialog);
		mCallCustormerDialog
				.setContentView(R.layout.dialog_call_custormer_house_resouse_detial);
		Window win = mCallCustormerDialog.getWindow();
		win.setGravity(Gravity.BOTTOM);
		mCallCustormerDialog.setCanceledOnTouchOutside(true);

		win.setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

		ListView mListViewCustormer = (ListView) mCallCustormerDialog
				.findViewById(R.id.lv_custormerPhone_HouseDetailActivity);

		ContentAdapter phoneAdapter = new ContentAdapter(
				mContext, list);
		mListViewCustormer.setAdapter(phoneAdapter);
		mListViewCustormer
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
		mCallCustormerDialog.show();
	}
	public void notifCallBack(String name, String className, Object data) {

	}

	private CustomerDetail getContent(String strJson) {
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(strJson);
			CustomerDetail customerDetail=(CustomerDetail)new Gson().fromJson(jsonObject.toString(),CustomerDetail.class);
//			customerDetail.getContent();
			return customerDetail;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	private CustomerDetailAdapter adapter;
	private List<Track> listTracks;

	public TextView getmTvCustomerCode() {
		return mTvCustomerCode;
	}

	@Override
	public void netWorkResult(String name, String className, Object data) {

	}

	private void getCustInfo(JSReturn jsReturn) {
		mDetail = (CustomerDetail) jsReturn.getObject();
		mTvCustomerCode.setText("编号：" + mDetail.getCustCode());
		mTvCustomerName.setText("姓名：" + mDetail.getName());
		// 填充跟踪信息列表
		listTracks = mDetail.getTracks();
		if (listTracks != null && listTracks.size() >= 1) {
            adapter = new CustomerDetailAdapter(mContext, listTracks);
            mLvTracks.setAdapter(adapter);
			MethodsExtra.resetListHeightBasedOnChildren(mLvTracks);
        }
		// 填充需求信息
		List<Requets> listReqs = mDetail.getRequets();
		if (listReqs != null && listReqs.size() >= 1) {
            Requets req = listReqs.get(0);
            mTvType.setText("类型：" + req.getReqType());// 类型
            tv_fangxing_cust.setText("房型：" + req.getFromToRoom());// 类型
			tv_quyu_customerDetailActivity.setText("城区：" + req.getDistrictCode());//区域

			String customArea=req.getArea();
			if("0".equals(customArea)){
				customArea="";
			}
            mTvAcreage.setText("片区：" +customArea);//片区
            mTvPrice.setText("价格：" + req.getPrice());// 价格
			tv_area_custdetail.setText("面积：" + req.getAcreage());// 面积
            tv_money_customerDetailActivity.setText(req.getSelfDescription());
        }
	}

	@Override
	public void onRefresh() {

	}

	@Override
	public void onLoadMore() {

	}
}
