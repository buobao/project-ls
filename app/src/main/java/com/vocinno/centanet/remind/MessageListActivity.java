package com.vocinno.centanet.remind;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.apputils.dialog.ModelDialog;
import com.vocinno.centanet.baseactivity.OtherHomeMenuBaseActivity;
import com.vocinno.centanet.customermanage.CustomerManageActivity;
import com.vocinno.centanet.housemanage.HouseManageActivity;
import com.vocinno.centanet.housemanage.HouseManageActivity2;
import com.vocinno.centanet.housemanage.HouseType;
import com.vocinno.centanet.keymanage.KeyGetInActivity;
import com.vocinno.centanet.keymanage.KeyManageActivity;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.model.MessageItem;
import com.vocinno.centanet.myinterface.HttpInterface;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJni;
import com.vocinno.utils.MethodsJson;
import com.vocinno.utils.view.refreshablelistview.XListView;
import com.vocinno.utils.view.refreshablelistview.XListView.IXListViewListener;
import com.zbar.lib.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的提醒
 * 
 * @author Administrator
 * 
 */
public class MessageListActivity extends OtherHomeMenuBaseActivity implements
		IXListViewListener {

	private MessageListAdapter mListAdapter;
	private int mPageIndex = 1;
	private List<MessageItem> mListMessages = new ArrayList<MessageItem>();

	@Override
	public void initView() {
		mListView = (XListView) findViewById(R.id.lv_remindList_MessageListActivity);
		MethodsExtra.findHeadTitle1(mContext, baseView, R.string.remind_my,
				null);
		mBackView = MethodsExtra.findHeadLeftView1(mContext, baseView, 0, 0);


		mBackView.setOnClickListener(this);
		mListView.setXListViewListener(this);
		mListView.setPullLoadEnable(false);
	}

	@Override
	public Handler setHandler() {
		return new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case R.id.FINISH_LOAD_ALL_DATA:
					mListAdapter.setListDatas(mListMessages);
					if (mListMessages == null || mListMessages.size() == 0) {
						mListView.setPullLoadEnable(false);
					} else {
						mListView.setPullLoadEnable(true);
					}
					break;
				case R.id.FINISH_LOAD_MORE:
					mListView.stopLoadMore();
					break;
				case R.id.FINISH_REFRESH:
					mListView.stopRefresh();
					break;
				default:
					break;
				}
			}
		};
	}

	@Override
	public int setContentLayoutId() {
		return R.layout.activity_my_remind;
	}


	@Override
	public void initData() {
		intent=new Intent();
		if(methodsJni==null){
			methodsJni=new MethodsJni();
			methodsJni.setMethodsJni((HttpInterface)this);
		}
		if(modelDialog==null){
			modelDialog= ModelDialog.getModelDialog(this);
		}
		modelDialog.show();
		mListAdapter = new MessageListAdapter(mContext);
		mListView.setAdapter(mListAdapter);
		// 添加通知
		MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_MESSAGE_LIST_RESULT, TAG);
		getDataFromNetwork(true, mPageIndex);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MethodsJni.removeNotificationObserver(CST_JS.NOTIFY_NATIVE_MESSAGE_LIST_RESULT, TAG);
	}

	public void startIntent(int index){
		finish();
		intent.setClass(this, HouseManageActivity2.class);
		intent.putExtra("viewPageIndex",index);
		startActivity(intent);
	};
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_left_mhead1:
			finish();
			break;
		case R.id.img_right_mhead1:
			break;
			//附近出售
			case R.id.rlyt_sell_house_main_page_slid_menus:
				startIntent(0);
				break;
			//附近出租
			case R.id.rlyt_rent_house_main_page_slid_menus:
				startIntent(1);
				break;
			//约看房源
			case R.id.rlyt_see_house_main_page_slid_menus:
				startIntent(2);
				break;
			//我的出售
			case R.id.rlyt_my_house_main_page_slid_menus:
				startIntent(3);
				break;
			//我的出租
			case R.id.rlyt_my_house_main_page_slid_menus2:
				startIntent(4);
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
				finish();
				MethodsDeliverData.flag = 1;
				MethodsDeliverData.mIntHouseType = HouseType.GONG_FANG;
				MethodsExtra.startActivity(mContext, HouseManageActivity.class);
				break;
			//抢公租
			case R.id.rlyt_grab_house_main_page_slid_menus2:
				finish();
				MethodsDeliverData.flag = 1;
				MethodsDeliverData.mIntHouseType = HouseType.GONG_FANGZU;
				MethodsExtra.startActivity(mContext, HouseManageActivity.class);
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
				drawer_layout.closeDrawer(leftMenuView);
				break;
	}
}

	// 调用数据
	private void getDataFromNetwork(boolean isRefresh, int page) {
		MethodsJni
				.callProxyFun(CST_JS.JS_ProxyName_Message,
						CST_JS.JS_Function_MessageProxy_getMessageList, CST_JS
								.getJsonStringForGetMessageList(
										(isRefresh ? CST_JS.JS_Message_Type_new
												: CST_JS.JS_Message_Type_old),
										page, 0));
	}

	public void notifCallBack(String name, String className, Object data) {
		/*if(modelDialog!=null&&modelDialog.isShowing()){
			modelDialog.dismiss();
		}
		String strJson = (String) data;
		JSReturn jsReturn = MethodsJson.jsonToJsReturn(strJson,
				MessageItem.class);
		if (jsReturn.isSuccess()) {
			if (jsReturn.getParams().getIsAppend()) {
				mListMessages.addAll(jsReturn.getListDatas());
			} else {
				mListMessages = jsReturn.getListDatas();
			}
			mHander.sendEmptyMessage(R.id.FINISH_LOAD_ALL_DATA);
			if (mPageIndex == 1) {
				// 刷新
				mHander.sendEmptyMessageDelayed(R.id.FINISH_REFRESH, 500);
			} else {
				// 加载更多
				mHander.sendEmptyMessageDelayed(R.id.FINISH_LOAD_MORE, 500);
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
		}*/
	}

	@Override
	public void onRefresh() {
		mPageIndex = 1;
		getDataFromNetwork(true, mPageIndex);
	}

	@Override
	public void onLoadMore() {
		getDataFromNetwork(false, ++mPageIndex);
	}

	@Override
	public void netWorkResult(String name, String className, Object data) {
		if(modelDialog!=null&&modelDialog.isShowing()){
			modelDialog.dismiss();
		}
		String strJson = (String) data;
		JSReturn jsReturn = MethodsJson.jsonToJsReturn(strJson,
				MessageItem.class);
		if (jsReturn.isSuccess()) {
			if (jsReturn.getParams().getIsAppend()) {
				mListMessages.addAll(jsReturn.getListDatas());
			} else {
				mListMessages = jsReturn.getListDatas();
			}
			mHander.sendEmptyMessage(R.id.FINISH_LOAD_ALL_DATA);
			if (mPageIndex == 1) {
				// 刷新
				mHander.sendEmptyMessageDelayed(R.id.FINISH_REFRESH, 500);
			} else {
				// 加载更多
				mHander.sendEmptyMessageDelayed(R.id.FINISH_LOAD_MORE, 500);
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
	}
}
