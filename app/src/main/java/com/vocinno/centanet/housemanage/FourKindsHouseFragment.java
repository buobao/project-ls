package com.vocinno.centanet.housemanage;

import java.util.List;

import android.R.integer;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.SuperFragment;
import com.vocinno.centanet.housemanage.adapter.HouseListAdapter;
import com.vocinno.centanet.model.HouseItem;
import com.vocinno.utils.view.refreshablelistview.XListView;
import com.vocinno.utils.view.refreshablelistview.XListView.IXListViewListener;

/**
 * 房源管理
 * 
 * @author Administrator
 * @param <HouseItem>
 * 
 */
public class FourKindsHouseFragment<HouseItem> extends SuperFragment implements
		IXListViewListener {
	public int mType = HouseType.CHU_SHOU;
	public int mPageIndex = HouseManageActivity.getPageIndexFromType(mType);
	// 当前页面选中的tag的索引
	public int mCurTagIndex = 0;
	public XListView mLvHouseList;
	private HouseListAdapter mLvHouseListAdapter;
	public static boolean isRefreshOrLoadMore=false;
	public FourKindsHouseFragment() {
	}
	@SuppressLint("ValidFragment")
	public FourKindsHouseFragment(int pageIndex) {
		this.mPageIndex = pageIndex;
		this.mType = HouseManageActivity.getTypeFromPageIndex(mPageIndex);
	}

	public void notifyDatasetChanged() {
		Log.d(TAG, "wanggsx mType=" + mType);
		if (mContext == null) {
			mContext = getActivity();
		}
		List<HouseItem> listHouseItems = ((HouseManageActivity) mContext).mArrayHouseItemList[mPageIndex];
		if (listHouseItems == null || listHouseItems.size() == 0) {
			mLvHouseListAdapter.setDataList(null);
		} else {
			mLvHouseListAdapter
					.setDataList(((HouseManageActivity) mContext).mArrayHouseItemList[mPageIndex]);
		}
		if (((HouseManageActivity) mContext).mArrayHouseItemList[mPageIndex] == null
				|| ((HouseManageActivity) mContext).mArrayHouseItemList[mPageIndex]
						.size() == 0
				|| ((HouseManageActivity) mContext).mArrayHouseItemList[mPageIndex]
						.equals("")) {
			mLvHouseList.setPullLoadEnable(false);
		} else {
			mLvHouseList.setPullLoadEnable(true);
		}
	}

	@Override
	public Handler setHandler() {
		return new Handler() {
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case R.id.FINISH_LOAD_MORE:
					// 读取更多
					mLvHouseList.stopLoadMore();
					int datalistSize = ((HouseManageActivity) mContext).mArrayHouseItemList[mPageIndex]
							.size();
					if (datalistSize == 0 || datalistSize % 20 >= 1) {
						mLvHouseList.setPullLoadEnable(false);
					} else {
						mLvHouseList.setPullLoadEnable(true);
					}
					break;
				case R.id.FINISH_REFRESH:
					// 刷新页面
					mLvHouseList.stopRefresh();
					int datalistSize1 = ((HouseManageActivity) mContext).mArrayHouseItemList[mPageIndex]
							.size();
					if (datalistSize1 == 0 || datalistSize1 % 20 >= 1) {
						mLvHouseList.setPullLoadEnable(false);
					} else {
						mLvHouseList.setPullLoadEnable(true);
					}
					break;
				default:
					break;
				}
			}
		};
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d("fourkind", "fourkind  onCreate " + mType);
		super.onCreate(savedInstanceState);
	}

	@Override
	public int setContentLayoutId() {
		return R.layout.fragment_four_kinds_house;
	}

	@Override
	public void initView() {
		mLvHouseList = (XListView) mRootView
				.findViewById(R.id.xlistview_house_desc_houst_manage_activity);
		mLvHouseList.setPullLoadEnable(false);
	}

	@Override
	public void setListener() {
		mLvHouseList.setXListViewListener(this);
	}

	@Override
	public void initData() {
		// 这里getData
		mLvHouseListAdapter = new HouseListAdapter(mContext, mType);
		mLvHouseList.setAdapter(mLvHouseListAdapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
			break;
		}
	}

	public void sendmsgFinishRefreshOrLoadMore() {
		if (((HouseManageActivity) mContext).mPageIndexs[mPageIndex] == 1) {
			mHander.sendEmptyMessage(R.id.FINISH_REFRESH);
		} else {
			mHander.sendEmptyMessage(R.id.FINISH_LOAD_MORE);
		}
	}

	@Override
	public void onRefresh() {
		isRefreshOrLoadMore=true;
		((HouseManageActivity) getActivity()).mPageIndexs[mPageIndex] = 1;
		((HouseManageActivity) getActivity()).getDataFromNetwork(mType, 1);
	}

	@Override
	public void onLoadMore() {
		isRefreshOrLoadMore=true;
		((HouseManageActivity) getActivity()).mPageIndexs[mPageIndex]++;
		((HouseManageActivity) getActivity()).getDataFromNetwork(mType,
				((HouseManageActivity) getActivity()).mPageIndexs[mPageIndex]);

	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

}
