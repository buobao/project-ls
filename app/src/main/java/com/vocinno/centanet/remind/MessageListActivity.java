package com.vocinno.centanet.remind;

import java.util.ArrayList;
import java.util.List;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.SuperSlideMenuActivity;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.apputils.dialog.ModelDialog;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.model.MessageItem;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJni;
import com.vocinno.utils.MethodsJson;
import com.vocinno.utils.view.refreshablelistview.XListView;
import com.vocinno.utils.view.refreshablelistview.XListView.IXListViewListener;

import android.os.Handler;
import android.os.Message;
import android.view.View;

/**
 * 我的提醒
 * 
 * @author Administrator
 * 
 */
public class MessageListActivity extends SuperSlideMenuActivity implements
		IXListViewListener {

	private XListView mListView;
	private View mBackView;
	private MessageListAdapter mListAdapter;
	private int mPageIndex = 1;
	private List<MessageItem> mListMessages = new ArrayList<MessageItem>();

	@Override
	public void initView() {
		mListView = (XListView) findViewById(R.id.lv_remindList_MessageListActivity);
		MethodsExtra.findHeadTitle1(mContext, mRootView, R.string.remind_my,
				null);
		mBackView = MethodsExtra.findHeadLeftView1(mContext, mRootView, 0, 0);
	}

	@Override
	public Handler setHandler() {
		return new Handler() {
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				MessageListActivity.this.closeMenu(msg);
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
	public void setListener() {
		mBackView.setOnClickListener(this);
		mListView.setXListViewListener(this);
		mListView.setPullLoadEnable(false);
	}

	@Override
	public void initData() {
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
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.img_left_mhead1:
			onBack();
			break;
		case R.id.img_right_mhead1:
			break;

		default:
			break;
		}
	}

	@Override
	public void onBack() {
		finish();
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

	@SuppressWarnings("unchecked")
	@Override
	public void notifCallBack(String name, String className, Object data) {
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

	@Override
	public void onRefresh() {
		mPageIndex = 1;
		getDataFromNetwork(true, mPageIndex);
	}

	@Override
	public void onLoadMore() {
		getDataFromNetwork(false, ++mPageIndex);
	}

}
