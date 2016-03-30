package com.vocinno.centanet.customermanage;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.SuperSlideMenuActivity;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.customermanage.adapter.CustormerListAdapter;
import com.vocinno.centanet.model.CustomerItem;
import com.vocinno.centanet.model.CustomerList;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJni;
import com.vocinno.utils.MethodsJson;
import com.vocinno.utils.view.refreshablelistview.XListView;
import com.vocinno.utils.view.refreshablelistview.XListView.IXListViewListener;

/**
 * 客源管理
 * 
 * @author Administrator
 * 
 */
public class CustomerManageActivity extends SuperSlideMenuActivity implements
		IXListViewListener {

	private XListView mLvCustormers;
	private View mBack, mSubmit;
	private CustormerListAdapter mListAdapter;
	public boolean isMyCustomerType = true;// 是否是我的客源，如果不是就认为是公客
	private int mPageIndex = 1;
	private List<CustomerItem> mListCustomers = new ArrayList<CustomerItem>();
	private List<CustomerItem> mListCustomersLast = new ArrayList<CustomerItem>();
	private boolean isReFreshOrLoadMore=false;
	@Override
	public Handler setHandler() {
		return new Handler() {
			// 加载
			@Override
			public void handleMessage(Message msg) {
				CustomerManageActivity.this.closeMenu(msg);
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
			MethodsExtra.findHeadTitle1(mContext, mRootView,
					R.string.mycustomer, null);
		} else {
			MethodsExtra.findHeadTitle1(mContext, mRootView,
					R.string.customertitle, null);
		}

		mBack = MethodsExtra.findHeadLeftView1(mContext, mRootView, 0, 0);
		mSubmit = MethodsExtra.findHeadRightView1(mContext, mRootView, 0,
				R.drawable.universal_button_add);
		mLvCustormers = (XListView) findViewById(R.id.lv_custormerInfoList_CustomerManageActivity);
		mLvCustormers.setPullLoadEnable(false);
	}

	@Override
	public void setListener() {
		mBack.setOnClickListener(this);
		mSubmit.setOnClickListener(this);
		mLvCustormers.setXListViewListener(this);
	}

	@Override
	public void initData() {
		mListAdapter = new CustormerListAdapter(
				(CustomerManageActivity) mContext, null);
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
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.img_left_mhead1:
			onBack();
			break;
		case R.id.img_right_mhead1:
			MethodsExtra.startActivity(mContext, AddCustomerActivity.class);
			break;
		default:
			break;
		}
	}

	@Override
	public void onBack() {

		MethodsJni.removeNotificationObserver(
				CST_JS.NOTIFY_NATIVE_GET_CUSTOMER_LIST_RESULT, TAG);
		finish();
	}

	// 调用数据
	private void getDataFromNetwork(int page) {
		if(isReFreshOrLoadMore){
			isReFreshOrLoadMore=false;
		}else{
			showDialog();
		}
		String strReq = CST_JS.getJsonStringForCustomerList(
				(isMyCustomerType ? CST_JS.JS_CustomerList_Type_My
						: CST_JS.JS_CustomerList_Type_Public), page, 8);
		MethodsJni.callProxyFun(CST_JS.JS_ProxyName_CustomerList,
				CST_JS.JS_Function_CustomerList_getList, strReq);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void notifCallBack(String name, String className, Object data) {
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
		}
		isLoading = false;
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
			getDataFromNetwork(++mPageIndex);
			isReFreshOrLoadMore=true;
		}
	}

}
