package com.vocinno.centanet.housemanage;

import java.util.List;

import com.vocinno.centanet.R;
import com.vocinno.centanet.model.Exclude;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HouseDetailSignAdapter extends BaseAdapter {
	private List<Exclude> mListSigns;
	private Context mContext;

	public HouseDetailSignAdapter(Context context, List<Exclude> listSigns) {
		mContext = context;
		mListSigns = listSigns;
	}

	@Override
	public int getCount() {
		if (mListSigns == null) {
			return 0;
		}
		return mListSigns.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mListSigns.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		SignView signView = null;
		if (arg1 == null) {
			arg1 = LayoutInflater.from(mContext).inflate(
					R.layout.item_sign_house_detail_listview, null);
			signView = new SignView();
			signView.mTvSignStart = (TextView) arg1
					.findViewById(R.id.tv_signBeginTime_itemSignHouseDetailListView);
			signView.mTvSignEnd = (TextView) arg1
					.findViewById(R.id.tv_signEndTime_itemSignHouseDetailListView);
			signView.mTvSignState = (TextView) arg1
					.findViewById(R.id.tv_signState_itemSignHouseDetailListView);
			signView.mTvSigner = (TextView) arg1
					.findViewById(R.id.tv_signer_itemSignHouseDetailListView);
			arg1.setTag(signView);
		} else {
			signView = (SignView) arg1.getTag();
		}
		signView.mTvSignStart.setText("开始："
				+ mListSigns.get(arg0).getStartTime());
		signView.mTvSignEnd.setText("结束：" + mListSigns.get(arg0).getEndTime());
		signView.mTvSignState.setText("状态：" + mListSigns.get(arg0).getStatus());
		signView.mTvSigner.setText("签赔人：" + mListSigns.get(arg0).getUser());
		return arg1;
	}

	class SignView {
		TextView mTvSignStart, mTvSignEnd, mTvSignState, mTvSigner;
	}

}
