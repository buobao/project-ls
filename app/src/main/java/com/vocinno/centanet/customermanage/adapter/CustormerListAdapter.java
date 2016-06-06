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
import com.vocinno.centanet.customermanage.CustomerManageActivity;
import com.vocinno.centanet.customermanage.GrabCustomerDetailActivity;
import com.vocinno.centanet.customermanage.MyCustomerDetailActivity;
import com.vocinno.centanet.model.CustomerItem;
import com.vocinno.centanet.tools.constant.MyConstant;
import com.vocinno.utils.MethodsDeliverData;

import java.util.List;

/**
 * 	我的客源  条目适配器
 */
public class CustormerListAdapter extends BaseAdapter {

	private CustomerManageActivity mContext;
	private LayoutInflater mInflater;
	private List<CustomerItem> mListCustomers;
	private boolean selectSOrZ=false;
	private boolean isGongKe=false;
	public void setListDatas(List<CustomerItem> listCustomers) {
		mListCustomers = listCustomers;
		notifyDataSetChanged();
	}
	public void addListDatas(List<CustomerItem> listCustomers) {
		if(this.mListCustomers==null||this.mListCustomers.size()<=0){
			this.mListCustomers = listCustomers;
		}else{
			this.mListCustomers.addAll(listCustomers);
		}
		notifyDataSetChanged();
	}
	public void setGongKe(boolean flag){
		isGongKe=flag;
	}
	public CustormerListAdapter(CustomerManageActivity mContext,
			List<CustomerItem> listCustomers) {
		this.mContext = mContext;
		this.mInflater = (LayoutInflater) this.mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mListCustomers = listCustomers;
	}
	public CustormerListAdapter(CustomerManageActivity mContext,
								List<CustomerItem> listCustomers,boolean selectSOrZ) {
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
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public CustomerItem getCustomerItem(int position) {
		return mListCustomers.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.item_custormer_manage_listview, null);
			holder.mTvCustormerId = (TextView) convertView
					.findViewById(R.id.tv_custormerId_customerManageListItem);
			holder.mTvHuXing = (TextView) convertView
					.findViewById(R.id.tv_houseDetail_huxing);
			holder.mTvCustormerName = (TextView) convertView
					.findViewById(R.id.tv_custormerName_customerManageListItem);
			holder.mTvDemandType = (TextView) convertView
					.findViewById(R.id.tv_demandType_customerManageListItem);
			holder.mTvDemandDetail = (TextView) convertView
					.findViewById(R.id.tv_houseDetail_customerManageListItem);
			holder.tv_quyu_customerManageListItem = (TextView) convertView
					.findViewById(R.id.tv_quyu_customerManageListItem);
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
		final CustomerItem item = mListCustomers.get(position);		//从集合取对应position的值
		// 客源编号
		holder.mTvCustormerId.setText(item.getCustCode());
		// 姓名
		holder.mTvCustormerName.setText(item.getName());
		// 需求类型
	/*	if(isGongKe){
			holder.mTvDemandType.setVisibility(View.GONE);
		}else{*/
			holder.mTvDemandType.setVisibility(View.VISIBLE);
			if(CustomerItem.ZU.equals(item.getReqType())){
				holder.mTvDemandType.setText("求租");
				holder.mTvDemandType.setBackground(mContext.getResources().getDrawable(R.drawable.shape_qiu_zu));
				holder.mTvDemandType.setVisibility(View.VISIBLE);
			} else if(CustomerItem.GOU.equals(item.getReqType())){
				holder.mTvDemandType.setText("求购");
				holder.mTvDemandType.setBackground(mContext.getResources().getDrawable(R.drawable.shape_qiu_gou));
				holder.mTvDemandType.setVisibility(View.VISIBLE);
			}else{
				holder.mTvDemandType.setBackground(mContext.getResources().getDrawable(android.R.color.transparent));
				holder.mTvDemandType.setVisibility(View.GONE);
			}
//		}

		// 区域
		if(item.getDistrictCode().equals("0")){
			holder.tv_quyu_customerManageListItem.setText("");
		}else{
			holder.tv_quyu_customerManageListItem.setText(item.getDistrictCode());
		}
		// 片区
		if("0".equals(item.getArea())){
			holder.mTvDemandDetail.setText("");
		}else{
			holder.mTvDemandDetail.setText(item.getArea());
		}
		// 户型+面积+价格
		String fangXing=item.getFromToRoom();
		String acreage=item.getAcreage();
		String price=item.getPrice();
		if("不限".equals(acreage)){
			acreage="";
		}
		if("不限".equals(price)){
			price="";
		}
		if("不限".equals(fangXing)){
			fangXing="";
		}
		holder.mTvHuXing.setText(fangXing);
		holder.mTvDemandPrice.setText( acreage+" "+price);
		// 说明
		holder.mTvDescription.setText(item.getOther());
		// 相对日期
		holder.mTvTime.setText(item.getRelativeDate());
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(selectSOrZ){
					Intent intent=new Intent();
					intent.putExtra(MyConstant.custCode,item.getCustCode());
					mContext.setResult(101,intent);
					mContext.finish();
				}else{
					MethodsDeliverData.string = item.getCustCode();
					/*MethodsExtra.startActivity(mContext,
							CustomerDetailActivity.class);*/
					Intent intent=null;
					if(MethodsDeliverData.keYuanOrGongKe==1){
						intent=new Intent(mContext,MyCustomerDetailActivity.class);
					}else{
						intent=new Intent(mContext, GrabCustomerDetailActivity.class);
					}
					intent.putExtra(MyConstant.custCode, item.getCustCode());
					intent.putExtra(MyConstant.isGongKe,isGongKe);
					mContext.startActivityForResult(intent,10);
				}
			}
		});
		return convertView;
	}

	public static class ViewHolder {
		TextView mTvCustormerId;
		TextView mTvHuXing;
		TextView mTvCustormerName;
		TextView mTvDemandType;
		TextView mTvDemandDetail,tv_quyu_customerManageListItem;
		TextView mTvDemandPrice;
		TextView mTvDescription;
		TextView mTvTime;
	}
}
