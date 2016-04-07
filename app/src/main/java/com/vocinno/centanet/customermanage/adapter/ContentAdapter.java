package com.vocinno.centanet.customermanage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vocinno.centanet.R;
import com.vocinno.centanet.model.ContactItem;
import com.vocinno.centanet.model.CustomerDetail;
import com.vocinno.centanet.model.Image;

import java.util.List;

public class ContentAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<CustomerDetail.Content> mContactLists;

	public ContentAdapter() {

	}

	public ContentAdapter(Context mContext, List<CustomerDetail.Content> list) {
		this.mContext = mContext;
		this.mInflater = (LayoutInflater) this.mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mContactLists = list;
	}
	
	
	
	@Override
	public int getCount() {

		return this.mContactLists.size();
	}

	@Override
	public Object getItem(int position) {

		return null;
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.item_call_custormer_dialog_house_resouse_detail,
					null);
			holder.mLlytCallCustormerAllItem = (LinearLayout) convertView
					.findViewById(R.id.llyt_call_custormer_item_all);
			holder.mTvUserNameTextView = (TextView) convertView.findViewById(R.id.tv_custName_CustormerPhoneAdapter);
			holder.mTvUserTel = (TextView) convertView.findViewById(R.id.tv_custNothing_CustormerPhoneAdapter);
			holder.img_custPic_CustormerPhoneAdapter = (ImageView) convertView.findViewById(R.id.img_custPic_CustormerPhoneAdapter);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mTvUserNameTextView.setText(this.mContactLists.get(position).getName());
		holder.mTvUserTel.setText(this.mContactLists.get(position).getPhone());
		holder.img_custPic_CustormerPhoneAdapter.setBackgroundResource(R.drawable.default_head);
		
		
		return convertView;
	}

	public class ViewHolder {
		LinearLayout mLlytCallCustormerAllItem;
		TextView mTvUserNameTextView;
		TextView mTvUserTel;
		ImageView img_custPic_CustormerPhoneAdapter;
	}

}
