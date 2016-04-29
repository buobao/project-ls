package com.vocinno.centanet.customermanage;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.baseactivity.OtherBaseActivity;
import com.vocinno.centanet.customermanage.adapter.CustormerListAdapter;
import com.vocinno.centanet.housemanage.adapter.SearchAdapter;
import com.vocinno.centanet.model.CustomerItem;
import com.vocinno.centanet.model.CustomerList;
import com.vocinno.centanet.model.EstateSearchItem;
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
	private Dialog mMenuDialog,mSearchDialog;
	private XListView mLvCustormers;
	private View mBack, mSubmit;
	private CustormerListAdapter mListAdapter;
	private int mPageIndex = 1;
	private List<CustomerItem> mListCustomers = new ArrayList<CustomerItem>();
	private List<CustomerItem> mListCustomersLast = new ArrayList<CustomerItem>();
	private boolean isReFreshOrLoadMore=false;
//	private String sOrZ;//用来判断客源跟进跳转查询出售还是出租的客源列表
	private String delegationType;//用来判断客源跟进跳转查询出售还是出租的客源列表
	private Intent intent;
	private List<EstateSearchItem> mSearchListData;
	private String type;
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
			// 添加通知
			MethodsJni.addNotificationObserver(
					CST_JS.NOTIFY_NATIVE_GET_CUSTOMER_LIST_RESULT, TAG);
			MethodsJni.addNotificationObserver(
					CST_JS.NOTIFY_NATIVE_SEARCH_ITEM_CUSTOMER_RESULT, TAG);
		} else {
			MethodsExtra.findHeadTitle1(mContext, baseView,
					R.string.customertitle, null);
			// 添加通知
			MethodsJni.addNotificationObserver(
					CST_JS.NOTIFY_NATIVE_GET_CUSTOMER_LIST_RESULT, TAG+"gk");
			MethodsJni.addNotificationObserver(
					CST_JS.NOTIFY_NATIVE_SEARCH_ITEM_CUSTOMER_RESULT, TAG+"gk");
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
		mSubmit.setVisibility(View.VISIBLE);
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
			showSearchDialog();
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
		case R.id.btn_close_dialogSearchHouseManage:
			mEtSearch.setText("");
			mLvHostory.setVisibility(View.INVISIBLE);
			break;
		default:
			break;
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(isMyCustomerType){
			MethodsJni.removeNotificationObserver(
					CST_JS.NOTIFY_NATIVE_GET_CUSTOMER_LIST_RESULT, TAG);
			MethodsJni.removeNotificationObserver(
					CST_JS.NOTIFY_NATIVE_SEARCH_ITEM_CUSTOMER_RESULT, TAG);
		}else{
			MethodsJni.removeNotificationObserver(
					CST_JS.NOTIFY_NATIVE_GET_CUSTOMER_LIST_RESULT, TAG+"gk");
			MethodsJni.removeNotificationObserver(
					CST_JS.NOTIFY_NATIVE_SEARCH_ITEM_CUSTOMER_RESULT, TAG+"gk");
		}
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
		MethodsJni.callProxyFun(hif,CST_JS.JS_ProxyName_CustomerList,
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
		if (name.equals(CST_JS.NOTIFY_NATIVE_GET_CUSTOMER_LIST_RESULT)) {
			if(jsReturn.isSuccess()){
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
		}else if (name.equals(CST_JS.NOTIFY_NATIVE_SEARCH_ITEM_CUSTOMER_RESULT)) {
			JSReturn jReturn = MethodsJson.jsonToJsReturn((String) data,
					EstateSearchItem.class);
			if(jReturn.isSuccess()){
				if(jReturn.getListDatas().size()>0){
					mSearchListData = jReturn.getListDatas();
					SearchAdapter mSearch = new SearchAdapter(mContext, mSearchListData);
					mListView.setAdapter(mSearch);
				}else{
//					MethodsExtra.toast(mContext,"抱歉没有搜索到房源");
					//抱歉没有搜索到该房源
				}
			}else{
				MethodsExtra.toast(mContext, jsReturn.getMsg());
			}

		}
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
		ll_search_customer.setOnClickListener(this);
		LinearLayout ll_add_customer = (LinearLayout) mMenuDialog
				.findViewById(R.id.ll_add_customer);
		ll_add_customer.setOnClickListener(this);
		if(isMyCustomerType){
			ll_add_customer.setVisibility(View.VISIBLE);
		}else{
			ll_add_customer.setVisibility(View.GONE);
		}
	}

	private ListView mListView;
	private ListView mLvHostory;
	public static EditText mEtSearch;
	private List<String> mHistorySearch;
	private void showSearchDialog() {
		mSearchDialog = new Dialog(mContext, R.style.Theme_dialog);
		mSearchDialog.setContentView(R.layout.dialog_search_house_manage);
		Window win = mSearchDialog.getWindow();
		win.setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		win.setGravity(Gravity.TOP);
		mSearchDialog.setCanceledOnTouchOutside(true);
		mListView = (ListView) mSearchDialog.findViewById(R.id.lv_historySearch_dialogSearchHouseManage);
		SearchAdapter mSearch = new SearchAdapter(mContext,
				new ArrayList<EstateSearchItem>());
		mListView.setAdapter(mSearch);

		mEtSearch = (EditText) mSearchDialog
				.findViewById(R.id.et_search_dialogSearchHouseManage);
		Button mBtnSearch = (Button) mSearchDialog
				.findViewById(R.id.btn_search_dialogSearchHouseManage);
		TextView mTvAround = (TextView) mSearchDialog
				.findViewById(R.id.tv_around_dialogSearchHouseManage);
		mLvHostory = (ListView) mSearchDialog
				.findViewById(R.id.lv_historySearch_dialogSearchHouseManage);
		Button mBtnClean = (Button) mSearchDialog
				.findViewById(R.id.btn_close_dialogSearchHouseManage);
		mBtnSearch.setOnClickListener(this);
		mTvAround.setOnClickListener(this);
		mBtnClean.setOnClickListener(this);
		// 根据mEtSearch得到的字符串去请求

		mEtSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
				Log.d("on text changed", "true");
			}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,int arg2, int arg3) {
				Log.d("before text changed", "true");
			}
			@Override
			public void afterTextChanged(Editable arg0) {
				searchHouse(arg0.toString().trim());
			}
		});

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				String custCode = mSearchListData.get(arg2).getCustCode();
				String reqparm = CST_JS.getJsonStringForCustomerList((isMyCustomerType ? CST_JS.JS_CustomerList_Type_My
						: CST_JS.JS_CustomerList_Type_Public),custCode, 1, 20);
				MethodsJni.callProxyFun(CST_JS.JS_ProxyName_CustomerList,
                        CST_JS.JS_Function_CustomerList_getList, reqparm);
				mSearchDialog.dismiss();
				showDialog();
			}

		});
		// 然后填充入listView
		if (mHistorySearch != null) {
			mLvHostory.setVisibility(View.VISIBLE);
		}
		mSearchDialog.show();
	}
	private void searchHouse(String editString) {
		mLvHostory.setVisibility(View.INVISIBLE);
		if(editString==null||editString.length()<=0){
		}else{
			// 在打字期间添加搜索栏数据
			String reqparm = CST_JS
					.getJsonStringForKeYuanGuanJianZi((isMyCustomerType ? CST_JS.JS_CustomerList_Type_My
							: CST_JS.JS_CustomerList_Type_Public),editString, 1, 20);
			MethodsJni.callProxyFun(hif,CST_JS.JS_ProxyName_CustomerList,
					CST_JS.JS_Function_CustListMobile_Serarch, reqparm);
			mLvHostory.setVisibility(View.VISIBLE);
		}
	}
}
