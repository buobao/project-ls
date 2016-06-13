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
import com.vocinno.centanet.customermanage.PotentialCustomerListActivity;
import com.vocinno.centanet.customermanage.PotentialCustomerDetailActivity;
import com.vocinno.centanet.model.CustomerItem;
import com.vocinno.centanet.tools.constant.MyConstant;

import java.util.List;

public class PotentialCustormerListAdapter extends BaseAdapter {

	private PotentialCustomerListActivity mContext;
	private LayoutInflater mInflater;
	private List<CustomerItem> mListCustomers;
	private boolean selectSOrZ=false;
	private boolean isGongKe=false;
	public void setListDatas(List<CustomerItem> listCustomers) {
		mListCustomers = listCustomers;
		notifyDataSetChanged();
	}
	public void addListDatas(List<CustomerItem> listCustomers) {
		mListCustomers.addAll(listCustomers);
		notifyDataSetChanged();
	}
	public PotentialCustormerListAdapter(PotentialCustomerListActivity mContext) {
		this.mContext = mContext;
		this.mInflater = (LayoutInflater) this.mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public PotentialCustormerListAdapter(PotentialCustomerListActivity mContext,
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
			holder.mTvDemandType = (TextView) convertView
					.findViewById(R.id.tv_demandType_customer);
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
		holder.mTvDemandType.setVisibility(View.VISIBLE);
		if(CustomerItem.ZU.equals(item.getReqType())){
			holder.mTvDemandType.setText("求租");
			holder.mTvDemandType.setBackground(mContext.getResources().getDrawable(R.drawable.shape_qiu_zu));
		}else if(CustomerItem.GOU.equals(item.getReqType())){
			holder.mTvDemandType.setText("求购");
			holder.mTvDemandType.setBackground(mContext.getResources().getDrawable(R.drawable.shape_qiu_gou));
		}else{
			holder.mTvDemandType.setBackground(mContext.getResources().getDrawable(android.R.color.transparent));
		}
		// 客源编号
		holder.mTvCustormerId.setText(item.getCustCode());
		// 姓名
		holder.mTvCustormerName.setText(item.getName());
		// 区域
		holder.mTvDemandDetail.setText(item.getArea() );
		holder.mTvHuXing.setText(item.getFrame());
		// 面积+价格
		String itemAcreage=item.getAcreage();
		String itemPrice=item.getPrice();
		if("不限".equals(itemAcreage)){
			itemAcreage="";
		}
		if("不限".equals(itemPrice)){
			itemPrice="";
		}
		holder.mTvDemandPrice.setText(itemAcreage+" "+itemPrice);
		// 说明
		holder.mTvDescription.setText(item.getOther());
		// 相对日期
		holder.mTvTime.setText(item.getRelativeDate());
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(mContext,PotentialCustomerDetailActivity.class);
				intent.putExtra(MyConstant.custCode,item.getCustCode());
				mContext.startActivityForResult(intent,10);
			}
		});
		return convertView;
	}

	public class ViewHolder {
		TextView mTvCustormerId;
		TextView mTvDemandType;
		TextView mTvHuXing;
		TextView mTvCustormerName;
		TextView mTvDemandDetail;
		TextView mTvDemandPrice;
		TextView mTvDescription;
		TextView mTvTime;
	}
}
