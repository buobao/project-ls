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
import com.vocinno.centanet.myinterface.ImportCustInterface;

import java.util.List;

/**
 * 	我的导入客  条目适配器
 */
public class ImportCustormerAdapter extends BaseSwipeAdapter {

	private ImportCustomerListActivity context;
	private LayoutInflater mInflater;
	private List<ImportCustomer> customerList;
	private boolean isCanClick=true;
	private ImportCustInterface custInterface;
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
	public ImportCustormerAdapter(ImportCustomerListActivity mContext,ImportCustInterface importCustInterface){
		this.context = mContext;
		this.mInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		custInterface=importCustInterface;
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
		View convertView= mInflater.inflate(R.layout.item_import_custormer, null);
		return convertView;
	}

	@NonNull
	private View.OnClickListener importCustInvalid(final int type,final int position,final ImportCustomer item, final SwipeLayout swipeLayout) {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(type==0){
					custInterface.importCustInvalid(position,item.getPkid(), swipeLayout);
				}else{
					swipeLayout.close();
					custInterface.importCustAccept(position,item.getPkid(), swipeLayout);
				}
			}
		};
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

	private ViewHolder holder;
	@Override
	public void fillValues(int position, View convertView) {
		final ImportCustomer item = customerList.get(position);
		 ll_import_title = (LinearLayout) convertView
					.findViewById(R.id.ll_import_title);
			 ll_import_view = (LinearLayout) convertView
					.findViewById(R.id.ll_import_view);
			iv_import_jiantou = (ImageView) convertView
					.findViewById(R.id.iv_import_jiantou);
			tv_import_time = (TextView) convertView
					.findViewById(R.id.tv_import_time);
			tv_import_tel = (TextView) convertView
					.findViewById(R.id.tv_import_tel);
			tv_import_date = (TextView) convertView
					.findViewById(R.id.tv_import_date);
			tv_import_source = (TextView) convertView
					.findViewById(R.id.tv_import_source);
			iv_import_wixiao = (ImageView) convertView
					.findViewById(R.id.iv_import_wixiao);
			iv_import_jieshou = (ImageView) convertView
					.findViewById(R.id.iv_import_jieshou);
		final SwipeLayout swipeLayout = (SwipeLayout)convertView.findViewById(getSwipeLayoutResourceId(position));
		swipeLayout.addSwipeListener(swipListener());
		iv_import_jiantou.setOnClickListener(closeSwipe(swipeLayout));
		iv_import_wixiao.setOnClickListener(importCustInvalid(0,position, item, swipeLayout));
		iv_import_jieshou.setOnClickListener(importCustInvalid(1,position, item, swipeLayout));
		ll_import_view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isCanClick) {
				} else {
					swipeLayout.close();
				}
			}
		});
		/*if(position==0||position==2){
			holder.tv_import_time.setText("今天");
			holder.ll_import_title.setVisibility(View.VISIBLE);
		}else{
			holder.tv_import_time.setText("今天2");
		}*/
		if(item.getTitle()!=null&&item.getTitle().toString().trim().length()>0){
			ll_import_title.setVisibility(View.VISIBLE);
			tv_import_time.setText(item.getTitle());
		}else{
			ll_import_title.setVisibility(View.GONE);
		}
		tv_import_tel.setText(item.getPhone());
		tv_import_date.setText(item.getFormatDate());
		tv_import_source.setText(item.getImportSrc());//+"==="+position+"==="+item.getPkid()

	}

	private TextView tv_import_tel,tv_import_date,tv_import_source,tv_import_time;
	private LinearLayout ll_import_view,ll_import_title;
	private ImageView iv_import_wixiao,iv_import_jieshou,iv_import_jiantou;
	private static class ViewHolder {
		TextView tv_import_tel,tv_import_date,tv_import_source,tv_import_time;
		LinearLayout ll_import_view,ll_import_title;
		ImageView iv_import_wixiao,iv_import_jieshou,iv_import_jiantou;
	}
}
