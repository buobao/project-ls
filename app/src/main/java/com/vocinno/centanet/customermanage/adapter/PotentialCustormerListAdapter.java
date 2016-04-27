package com.vocinno.centanet.customermanage.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vocinno.centanet.R;
import com.vocinno.centanet.customermanage.MyCustomerDetailActivity;
import com.vocinno.centanet.customermanage.PotentialCustomerActivity;
import com.vocinno.centanet.model.CustomerItem;

import java.util.List;

public class PotentialCustormerListAdapter extends BaseAdapter {

	private PotentialCustomerActivity mContext;
	private LayoutInflater mInflater;
	private List<CustomerItem> mListCustomers;
	private boolean selectSOrZ=false;
	private boolean isGongKe=false;
	public void setListDatas(List<CustomerItem> listCustomers) {
		mListCustomers = listCustomers;
	}
	public void addListDatas(List<CustomerItem> listCustomers) {
		mListCustomers.addAll(listCustomers);
	}
	public PotentialCustormerListAdapter(PotentialCustomerActivity mContext) {
		this.mContext = mContext;
		this.mInflater = (LayoutInflater) this.mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public PotentialCustormerListAdapter(PotentialCustomerActivity mContext,
										 List<CustomerItem> listCustomers, boolean selectSOrZ) {
		this.mContext = mContext;
		this.mInflater = (LayoutInflater) this.mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mListCustomers = listCustomers;
		this.selectSOrZ=selectSOrZ;
	}
	@Override
	public int getCount() {
		if (mListCustomers == null) {
			return 0;
		}
		return mListCustomers.size();
	}

	@Override
	public Object getItem(int position) {
		return mListCustomers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.item_potential_custormer_listview, null);
			holder.mTvCustormerId = (TextView) convertView
					.findViewById(R.id.tv_custormerId_customerManageListItem);
			holder.mTvHuXing = (TextView) convertView
					.findViewById(R.id.tv_houseDetail_huxing);
			holder.mTvCustormerName = (TextView) convertView
					.findViewById(R.id.tv_custormerName_customerManageListItem);
			holder.mTvDemandDetail = (TextView) convertView
					.findViewById(R.id.tv_houseDetail_customerManageListItem);
			holder.mTvDemandPrice = (TextView) convertView
					.findViewById(R.id.tv_demandPrice_customerManageListItem);
			holder.mTvDescription = (TextView) convertView
					.findViewById(R.id.tv_description_customerManageListItem);
			holder.mTvTime = (TextView) convertView
					.findViewById(R.id.tv_time_customerManageListItem);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final CustomerItem item = mListCustomers.get(position);
		// 客源编号
		holder.mTvCustormerId.setText(item.getCustCode());
		// 姓名
		holder.mTvCustormerName.setText(item.getName());
		// 区域
		holder.mTvDemandDetail.setText(item.getArea() );
		holder.mTvHuXing.setText(item.getFrame());
		// 户型+面积+价格
		holder.mTvDemandPrice.setText( item.getAcreage()+" "+item.getPrice());
		// 说明
		holder.mTvDescription.setText(item.getOther());
		// 相对日期
		holder.mTvTime.setText(item.getRelativeDate());
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(mContext,MyCustomerDetailActivity.class);
				intent.putExtra("custCode",item.getCustCode());
				mContext.startActivityForResult(intent,10);
			}
		});
		return convertView;
	}

	public class ViewHolder {
		TextView mTvCustormerId;
		TextView mTvHuXing;
		TextView mTvCustormerName;
		TextView mTvDemandDetail;
		TextView mTvDemandPrice;
		TextView mTvDescription;
		TextView mTvTime;
	}
}
