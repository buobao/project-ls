package com.vocinno.centanet.customermanage.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vocinno.centanet.R;
import com.vocinno.centanet.customermanage.CustomerManageActivity;
import com.vocinno.centanet.customermanage.GrabCustomerDetailActivity;
import com.vocinno.centanet.customermanage.MyCustomerDetailActivity;
import com.vocinno.centanet.model.CustomerItem;
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
					R.layout.item_custormer_manage, null);
			holder.tv_name_code = (TextView) convertView
					.findViewById(R.id.tv_name_code);
			holder.iv_zu_gou = (ImageView) convertView
					.findViewById(R.id.iv_zu_gou);
			holder.tv_area_price = (TextView) convertView
					.findViewById(R.id.tv_area_price);
			holder.tv_custom_quyu = (TextView) convertView
					.findViewById(R.id.tv_custom_quyu);
			holder.tv_custom_pianqu = (TextView) convertView
					.findViewById(R.id.tv_custom_pianqu);
			holder.tv_custom_huxing = (TextView) convertView
					.findViewById(R.id.tv_custom_huxing);
			holder.tv_custom_time = (TextView) convertView
					.findViewById(R.id.tv_custom_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final CustomerItem item = mListCustomers.get(position);		//从集合取对应position的值
		// 姓名/客源编号
		holder.tv_name_code.setText(item.getName() + "/" + item.getCustCode());
		if(CustomerItem.ZU.equals(item.getReqType())){
			holder.iv_zu_gou.setVisibility(View.VISIBLE);
			holder.iv_zu_gou.setImageResource(R.drawable.qiuzu);
		} else if(CustomerItem.GOU.equals(item.getReqType())){
			holder.iv_zu_gou.setVisibility(View.VISIBLE);
			holder.iv_zu_gou.setImageResource(R.drawable.qiugou);
		}else{
			holder.iv_zu_gou.setVisibility(View.GONE);
		}
		// 区域
		if(item.getDistrictCode().equals("0")){
			holder.tv_custom_quyu.setText("");
		}else{
			holder.tv_custom_quyu.setText(item.getDistrictCode());
		}
		// 片区
		if("0".equals(item.getArea())){
			holder.tv_custom_pianqu.setText("");
		}else{
			holder.tv_custom_pianqu.setText(item.getArea());
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
		holder.tv_custom_huxing.setText(fangXing);
		holder.tv_area_price.setText( acreage+" "+price);
		// 说明
		// 相对日期
		holder.tv_custom_time.setText(item.getRelativeDate());
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(selectSOrZ){
					Intent intent=new Intent();
					intent.putExtra("custCode",item.getCustCode());
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
					intent.putExtra("custCode",item.getCustCode());
					mContext.startActivityForResult(intent,10);
					if (mContext.isMyCustomerType) {
						MethodsDeliverData.flag1 = -1;
					} else {
						MethodsDeliverData.flag1 = 1;
					}
				}
			}
		});
		return convertView;
	}

	public static class ViewHolder {
		TextView tv_name_code;
		ImageView iv_zu_gou;
		TextView tv_area_price;
		TextView tv_custom_quyu;
		TextView tv_custom_pianqu;
		TextView tv_custom_huxing,tv_custom_time;
	}
}
