package com.vocinno.centanet.remind;

import android.os.Handler;
import android.view.View;

import com.squareup.okhttp.Request;
import com.vocinno.centanet.R;
import com.vocinno.centanet.baseactivity.OtherBaseActivity;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.model.MessageItem;
import com.vocinno.centanet.tools.Loading;
import com.vocinno.centanet.tools.MyToast;
import com.vocinno.centanet.tools.OkHttpClientManager;
import com.vocinno.centanet.tools.constant.MyConstant;
import com.vocinno.centanet.tools.constant.NetWorkConstant;
import com.vocinno.centanet.tools.constant.NetWorkMethod;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJson;
import com.vocinno.utils.view.refreshablelistview.XListView;
import com.vocinno.utils.view.refreshablelistview.XListView.IXListViewListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的提醒
 * 
 * @author Administrator
 * 
 */
public class MessageListActivity extends OtherBaseActivity implements
		IXListViewListener {

	private MessageListAdapter mListAdapter;
	private int mPageIndex = 1;
	private List<MessageItem> mListMessages = new ArrayList<MessageItem>();

	@Override
	public void initView() {
		mListView = (XListView) findViewById(R.id.lv_remindList_MessageListActivity);
		MethodsExtra.findHeadTitle1(mContext, baseView, R.string.remind_my,null);
		mBackView = MethodsExtra.findHeadLeftView1(mContext, baseView, 0, 0);


		mBackView.setOnClickListener(this);
		mListView.setXListViewListener(this);
		mListView.setPullLoadEnable(false);
	}

	@Override
	public Handler setHandler() {
		return null;
	}

	@Override
	public int setContentLayoutId() {
		return R.layout.activity_my_remind;
	}


	@Override
	public void initData() {
		mListAdapter = new MessageListAdapter(mContext);
		mListView.setAdapter(mListAdapter);
		getMsgData();
		// 添加通知
		/*MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_MESSAGE_LIST_RESULT, TAG);
		getDataFromNetwork(true, mPageIndex);*/
	}

	private void getMsgData() {
		getMsgData("new", true, true);
	}
	private void getLoadMsgData() {
		getMsgData("old", false, false);
	}
	private void getMsgData(String type,boolean isFirstViewLoad,final boolean isRefresh) {
		if(isFirstViewLoad){
			Loading.showForExit(this, true);
		}
		URL= NetWorkConstant.PORT_URL+ NetWorkMethod.msgList;
		Map<String,String>map=new HashMap<String,String>();
		map.put(NetWorkMethod.type,type);
		map.put(NetWorkMethod.page,page+"");
		map.put(NetWorkMethod.pageSize, MyConstant.pageSize + "");
		OkHttpClientManager.getAsyn(URL, map, new OkHttpClientManager.ResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {
				stopRefreshOrLoadMore();
			}
			@Override
			public void onResponse(String response) {
				stopRefreshOrLoadMore();
				JSReturn jsReturn = MethodsJson.jsonToJsReturn(response, MessageItem.class);
				if (jsReturn.isSuccess()) {
					if (jsReturn.getListDatas().size() < MyConstant.pageSize) {
						mListView.setPullLoadEnable(false);
					} else {
						mListView.setPullLoadEnable(true);
					}
					if (isRefresh) {
						mListAdapter.setListDatas(jsReturn.getListDatas());
					} else {
						page++;
						mListAdapter.addListDatas(jsReturn.getListDatas());
					}
				} else {
					MyToast.showToast(jsReturn.getMsg());
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.img_left_mhead1:
			finish();
			break;
		case R.id.img_right_mhead1:
			break;
			/*//钥匙管理
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
				startIntentToGongFangManager(0);
				break;
			//抢公租
			case R.id.rlyt_grab_house_main_page_slid_menus2:
				finish();
				MethodsDeliverData.flag = 1;
				MethodsDeliverData.mIntHouseType = HouseType.GONG_FANGZU;
				startIntentToGongFangManager(1);
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
				break;*/
		}
	}
	public void notifCallBack(String name, String className, Object data) {
	}

	@Override
	public void onRefresh() {
		page = 2;
		getMsgData("new", false, true);
	}

	@Override
	public void onLoadMore() {
		getLoadMsgData();
	}

	@Override
	public void netWorkResult(String name, String className, Object data) {

	}
}
