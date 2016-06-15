package com.vocinno.centanet.customermanage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.vocinno.centanet.R;
import com.vocinno.centanet.customermanage.ImportCustomerListActivity;
import com.vocinno.centanet.model.ImportCustomer;

import java.util.Date;
import java.util.List;

/**
 * 	我的导入客  条目适配器
 */
public class ImportCustormerAdapter extends BaseSwipeAdapter {

	private ImportCustomerListActivity context;
	private LayoutInflater mInflater;
	private List<ImportCustomer> customerList;
	private boolean isCanClick=true;
	public void setListData(List<ImportCustomer> listCustomers) {
		customerList = listCustomers;
		notifyDataSetChanged();
	}
	public void addListData(List<ImportCustomer> listCustomers) {
		if(this.customerList==null||this.customerList.size()<=0){
			this.customerList = listCustomers;
		}else{
			this.customerList.addAll(listCustomers);
		}
		notifyDataSetChanged();
	}
	public ImportCustormerAdapter(ImportCustomerListActivity mContext){
		this.context = mContext;
		this.mInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		return customerList== null?0:customerList.size();
	}

	@Override
	public ImportCustomer getItem(int position) {
		return customerList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getSwipeLayoutResourceId(int i) {
		return R.id.swipe;
	}

	@Override
	public View generateView(final int position, ViewGroup viewGroup) {
		ViewHolder holder;
		View convertView=null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.item_import_custormer, null);
			holder.ll_import_view = (LinearLayout) convertView
					.findViewById(R.id.ll_import_view);
			holder.iv_import_jiantou = (ImageView) convertView
					.findViewById(R.id.iv_import_jiantou);
			holder.tv_import_time = (TextView) convertView
					.findViewById(R.id.tv_import_time);
			holder.tv_import_tel = (TextView) convertView
					.findViewById(R.id.tv_import_tel);
			holder.tv_import_date = (TextView) convertView
					.findViewById(R.id.tv_import_date);
			holder.tv_import_source = (TextView) convertView
					.findViewById(R.id.tv_import_source);
			holder.iv_import_wixiao = (ImageView) convertView
					.findViewById(R.id.iv_import_wixiao);
			holder.iv_import_jieshou = (ImageView) convertView
					.findViewById(R.id.iv_import_jieshou);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final SwipeLayout swipeLayout = (SwipeLayout)convertView.findViewById(getSwipeLayoutResourceId(position));
		swipeLayout.addSwipeListener(swipListener());
		holder.iv_import_jiantou.setOnClickListener(closeSwipe(swipeLayout));
		holder.iv_import_wixiao.setOnClickListener(closeSwipe(swipeLayout));
		holder.iv_import_jieshou.setOnClickListener(closeSwipe(swipeLayout));
		if(position==0||position==2){
			holder.tv_import_time.setText("今天");
			holder.ll_import_view.setVisibility(View.VISIBLE);
		}else{
			holder.tv_import_time.setText("今天2");
			holder.ll_import_view.setVisibility(View.GONE);
		}
		ImportCustomer item = customerList.get(position);
		holder.tv_import_tel.setText(item.getPhone());
		holder.tv_import_source.setText(item.getImportSrc());
		holder.tv_import_time.setText(new Date(item.getImportTime())+"");
		holder.tv_import_date.setText(new Date(item.getImportTime())+"");
		
		return convertView;
	}

	@NonNull
	private SimpleSwipeListener swipListener() {
		return new SimpleSwipeListener(){
			@Override
			public void onOpen(SwipeLayout layout) {
				super.onOpen(layout);
				if(isCanClick){
					isCanClick=false;
				}
			}

			@Override
			public void onClose(SwipeLayout layout) {
				super.onClose(layout);
				if(!isCanClick){
					isCanClick=true;
				}
			}
		};
	}

	@NonNull
	private View.OnClickListener closeSwipe(final SwipeLayout swipeLayout) {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				swipeLayout.toggle();
			}
		};
	}

	@Override
	public void fillValues(int i, View view) {

	}

	public static class ViewHolder {
		TextView tv_import_tel,tv_import_date,tv_import_source,tv_import_time;
		LinearLayout ll_import_view,lllll;
		ImageView iv_import_wixiao,iv_import_jieshou,iv_import_jiantou;
	}
	/*holder.lllll.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isCanClick) {
					MyToast.showToast("=position=" + position);
				} else {
					swipeLayout.close();
				}
			}
		});*/
}
