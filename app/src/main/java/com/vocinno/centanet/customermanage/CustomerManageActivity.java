package com.vocinno.centanet.customermanage;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.baseactivity.OtherBaseActivity;
import com.vocinno.centanet.customermanage.adapter.CustormerListAdapter;
import com.vocinno.centanet.model.CustomerItem;
import com.vocinno.centanet.model.CustomerList;
import com.vocinno.centanet.model.HouseItem;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.myinterface.HttpInterface;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJni;
import com.vocinno.utils.MethodsJson;
import com.vocinno.utils.view.refreshablelistview.XListView;
import com.vocinno.utils.view.refreshablelistview.XListView.IXListViewListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 客源管理
 * 
 * @author Administrator
 * 
 */
public class CustomerManageActivity extends OtherBaseActivity implements
		IXListViewListener {
	private Dialog mMenuDialog;
	private XListView mLvCustormers;
	private View mBack, mSubmit;
	private CustormerListAdapter mListAdapter;
	public boolean isMyCustomerType = true;// 是否是我的客源，如果不是就认为是公客
	private int mPageIndex = 1;
	private List<CustomerItem> mListCustomers = new ArrayList<CustomerItem>();
	private List<CustomerItem> mListCustomersLast = new ArrayList<CustomerItem>();
	private boolean isReFreshOrLoadMore=false;
//	private String sOrZ;//用来判断客源跟进跳转查询出售还是出租的客源列表
	private String delegationType;//用来判断客源跟进跳转查询出售还是出租的客源列表
	private Intent intent;
	@Override
	public Handler setHandler() {
		return new Handler() {
			// 加载
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case R.id.FINISH_LOAD_ALL_DATA:
					mListAdapter.setListDatas(mListCustomers);
					if (mListCustomers == null || mListCustomers.size() == 0) {
						mLvCustormers.setPullLoadEnable(false);
					} else {
						mLvCustormers.setPullLoadEnable(true);
					}
					break;
				case R.id.FINISH_LOAD_MORE:
					mLvCustormers.stopLoadMore();
					if (msg.arg1 == 0) {
						mLvCustormers.setPullLoadEnable(false);
					}
					break;
				case R.id.FINISH_REFRESH:
					mLvCustormers.stopRefresh();
					break;
				default:
					break;
				}
			}
		};
	}

	@Override
	public int setContentLayoutId() {
		return R.layout.activity_custormer_manage;
	}

	@Override
	public void initView() {
		isMyCustomerType = MethodsDeliverData.isMyCustomer;
		if (isMyCustomerType) {
			MethodsExtra.findHeadTitle1(mContext, baseView,
					R.string.mycustomer, null);
		} else {
			MethodsExtra.findHeadTitle1(mContext, baseView,
					R.string.customertitle, null);
		}

		mBack = MethodsExtra.findHeadLeftView1(mContext, baseView, 0, 0);
		/*mSubmit = MethodsExtra.findHeadRightView1(mContext, baseView, 0,
				R.drawable.universal_button_add);*/
		mSubmit = MethodsExtra.findHeadRightView1(mContext, baseView, 0, 0);
		mLvCustormers = (XListView) findViewById(R.id.lv_custormerInfoList_CustomerManageActivity);
		mLvCustormers.setPullLoadEnable(false);
		mLvCustormers.setPullRefreshEnable(true);
		mBack.setOnClickListener(this);
		mSubmit.setOnClickListener(this);
		mLvCustormers.setXListViewListener(this);
	}

	@Override
	public void initData() {
		methodsJni=new MethodsJni();
		methodsJni.setMethodsJni((HttpInterface)this);
		intent=getIntent();
//		sOrZ=intent.getStringExtra("sOrZ");
		delegationType=intent.getStringExtra("delegationType");
		boolean isGongKe = intent.getBooleanExtra("isGongKe", false);
		if(HouseItem.SHOU.equals(delegationType)||HouseItem.ZU.equals(delegationType)){
			mListAdapter = new CustormerListAdapter(
					(CustomerManageActivity) mContext, null,true);
		}else{
			mListAdapter = new CustormerListAdapter(
					(CustomerManageActivity) mContext, null);
		}
		mListAdapter.setGongKe(isGongKe);
		mLvCustormers.setAdapter(mListAdapter);
		if (MethodsDeliverData.flag == 1) {
			MethodsDeliverData.flag1 = 1;
			MethodsDeliverData.flag = -1;
			mSubmit.setVisibility(View.GONE);
		} else {
			mSubmit.setVisibility(View.VISIBLE);
		}
		// 添加通知
		MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_GET_CUSTOMER_LIST_RESULT, TAG);
		// 调用数据
		getDataFromNetwork(mPageIndex);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode){
			case ConstantResult.REFRESH:
				getDataFromNetwork(mPageIndex);
			break;
		}
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_search_customer:
//			MethodsExtra.startActivity(mContext, AddCustomerActivity.class);
			mMenuDialog.dismiss();
			break;
		case R.id.ll_add_customer:
			MethodsExtra.startActivity(mContext, AddCustomerActivity.class);
			mMenuDialog.dismiss();
			break;
		case R.id.img_left_mhead1:
			finish();
			break;
		case R.id.img_right_mhead1:
			showMenuDialog();
			break;
			/*//钥匙管理
			case R.id.rlyt_key_house_main_page_slid_menus:
				MyUtils.removeActivityFromList();
				MethodsExtra.startActivity(mContext, KeyManageActivity.class);
				break;
			//我的客源
			case R.id.rlyt_my_customer_main_page_slid_menus:
				if(MethodsDeliverData.isMyCustomer){
					drawer_layout.closeDrawer(leftMenuView);
				}else{
					MyUtils.removeActivityFromList();
					MethodsDeliverData.keYuanOrGongKe=1;
					MethodsDeliverData.isMyCustomer = true;
					MethodsExtra.startActivity(mContext,
							CustomerManageActivity.class);
				}
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
				if(MethodsDeliverData.isMyCustomer){
					MyUtils.removeActivityFromList();
					MethodsDeliverData.keYuanOrGongKe=1;
					MethodsDeliverData.isMyCustomer = true;
					MethodsExtra.startActivity(mContext,
							CustomerManageActivity.class);
				}else{
					drawer_layout.closeDrawer(leftMenuView);
				}
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
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MethodsJni.removeNotificationObserver(
				CST_JS.NOTIFY_NATIVE_GET_CUSTOMER_LIST_RESULT, TAG);
	}
	// 调用数据
	private void getDataFromNetwork(int page) {
		if(isReFreshOrLoadMore){
			isReFreshOrLoadMore=false;
		}else{
			showDialog();
		}
		String strReq;
//		if("S".equalsIgnoreCase(sOrZ)){
		if(HouseItem.SHOU.equals(delegationType)){
			  strReq = CST_JS.getJsonStringForCustomerList(
					(isMyCustomerType ? CST_JS.JS_CustomerList_Type_My
							: CST_JS.JS_CustomerList_Type_Public), page, 8,"S");
//		}else if("Z".equalsIgnoreCase(sOrZ)){
		}else if(HouseItem.ZU.equals(delegationType)){
			  strReq = CST_JS.getJsonStringForCustomerList(
					(isMyCustomerType ? CST_JS.JS_CustomerList_Type_My
							: CST_JS.JS_CustomerList_Type_Public), page, 8,"Z");
		}else{
			  strReq = CST_JS.getJsonStringForCustomerList(
					(isMyCustomerType ? CST_JS.JS_CustomerList_Type_My
							: CST_JS.JS_CustomerList_Type_Public), page, 8);
		}
		MethodsJni.callProxyFun(CST_JS.JS_ProxyName_CustomerList,
				CST_JS.JS_Function_CustomerList_getList, strReq);
	}

	@SuppressWarnings("unchecked")
	public void notifCallBack(String name, String className, Object data) {

	}

	@Override
	public void onRefresh() {
		mPageIndex = 1;
		isReFreshOrLoadMore=true;
		getDataFromNetwork(mPageIndex);
	}

	private boolean isLoading = false;

	@Override
	public void onLoadMore() {
		if (!isLoading) {
			isLoading = true;
			isReFreshOrLoadMore=true;
			getDataFromNetwork(++mPageIndex);
		}
	}

	@Override
	public void netWorkResult(String name, String className, Object data) {
		dismissDialog();
		String strJson = (String) data;
		JSReturn jsReturn = MethodsJson.jsonToJsReturn(strJson,
				CustomerList.class);
		if (jsReturn.isSuccess()) {
			// 检测数据是否重复回调过来
			boolean isTheSame = false;
			if (mListCustomersLast != null
					&& jsReturn.getListDatas() != null
					&& mListCustomersLast.size() >= 1
					&& jsReturn.getListDatas().size() == mListCustomersLast
					.size()) {
				List<CustomerItem> thisCustomerItems = jsReturn.getListDatas();
				for (int i = 0; i < thisCustomerItems.size(); i++) {
					CustomerItem lastCustomerItem = mListCustomersLast.get(i);
					CustomerItem thisCustomerItem = thisCustomerItems.get(i);
					if (!lastCustomerItem.getName().equals(
							thisCustomerItem.getName())) {
						isTheSame = false;
						break;
					}
				}
			}
			if (isTheSame) {
				if (mPageIndex == 1) {
					// 刷新失败
					mHander.sendEmptyMessage(R.id.FINISH_REFRESH);
				} else {
					// 加载更多失败
					mHander.sendEmptyMessage(R.id.FINISH_LOAD_MORE);
				}
			} else {

				if (jsReturn.getParams().getIsAppend()) {
					mListCustomers.addAll(jsReturn.getListDatas());
				} else {
					mListCustomers = jsReturn.getListDatas();
				}
				mHander.sendEmptyMessage(R.id.FINISH_LOAD_ALL_DATA);
				if (mPageIndex == 1) {
					// 刷新
					mHander.sendEmptyMessageDelayed(R.id.FINISH_REFRESH, 500);
				} else {
					// 加载更多
					Message msg = mHander.obtainMessage();
					msg.arg1 = -1;
					msg.what = R.id.FINISH_LOAD_MORE;
					if (jsReturn.getListDatas() == null
							|| jsReturn.getListDatas().size() == 0) {
						msg.arg1 = 0;
					} else {
						msg.arg1 = jsReturn.getListDatas().size();
					}
					mHander.sendMessageDelayed(msg, 500);

				}
			}
		} else {
			if (mPageIndex == 1) {
				// 刷新失败
				mHander.sendEmptyMessage(R.id.FINISH_REFRESH);
			} else {
				// 加载更多失败
				mHander.sendEmptyMessage(R.id.FINISH_LOAD_MORE);
				mPageIndex--;
			}
			MethodsExtra.toast(mContext, jsReturn.getMsg());
		}
		isLoading = false;
	}

	private void showMenuDialog() {
		mMenuDialog = new Dialog(mContext, R.style.Theme_dialog);
		mMenuDialog.setContentView(R.layout.dialog_menu_customer_manage);
		Window win = mMenuDialog.getWindow();
		win.setLayout(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		win.setGravity(Gravity.RIGHT | Gravity.TOP);
		mMenuDialog.setCanceledOnTouchOutside(true);
		mMenuDialog.show();
		LinearLayout ll_search_customer = (LinearLayout) mMenuDialog
				.findViewById(R.id.ll_search_customer);
		LinearLayout ll_add_customer = (LinearLayout) mMenuDialog
				.findViewById(R.id.ll_add_customer);
		ll_search_customer.setOnClickListener(this);
		ll_add_customer.setOnClickListener(this);
	}
}
