package com.vocinno.centanet.customermanage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vocinno.centanet.R;
import com.vocinno.centanet.customermanage.AddAccompanyActivity;
import com.vocinno.centanet.housemanage.adapter.CustomGridView;
import com.vocinno.centanet.housemanage.adapter.ImgAdapter;
import com.vocinno.centanet.model.FirstHouse;

import java.util.List;

/**
 * 	我的客源  条目适配器
 */
public class FirstHouseAdapter extends BaseAdapter {

	private AddAccompanyActivity mContext;
	private LayoutInflater mInflater;
	private List<FirstHouse> houseList;
	private ImgAdapter imgAdapter;
	public void setList(List<FirstHouse> listCustomers) {
		houseList = listCustomers;
		notifyDataSetChanged();
	}
	public void addList(List<FirstHouse> listCustomers) {
		if(this.houseList ==null||this.houseList.size()<=0){
			this.houseList = listCustomers;
		}else{
			this.houseList.addAll(listCustomers);
		}
		notifyDataSetChanged();
	}
	public FirstHouseAdapter(AddAccompanyActivity mContext) {
		this.mContext = mContext;
		this.mInflater = (LayoutInflater) this.mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		if (houseList == null) {
			return 0;
		}
		return houseList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public FirstHouse getCustomerItem(int position) {
		return houseList.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.item_firsthand_house, null);
			holder.tv_accompany_people = (TextView) convertView.findViewById(R.id.tv_accompany_people);
			holder.tv_accompany_promise = (TextView) convertView.findViewById(R.id.tv_accompany_promise);
			holder.tv_accompany_address = (TextView) convertView.findViewById(R.id.tv_accompany_address);
			holder.gv_first_house_item = (CustomGridView) convertView.findViewById(R.id.gv_first_house_item);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final FirstHouse item = houseList.get(position);		//从集合取对应position的值
		holder.tv_accompany_people.setText(item.getPeiKan());
		String commitment="否";
		if("1".equals(item.getCommitment())){
			commitment="是";
		}
		holder.tv_accompany_promise.setText(commitment);
		holder.tv_accompany_address.setText(item.getAddress());

			imgAdapter=new ImgAdapter(mContext);
		imgAdapter.setData(item.getImgPath());
		holder.gv_first_house_item.setAdapter(imgAdapter);
		return convertView;
	}

	public static class ViewHolder {
		TextView tv_accompany_people,tv_accompany_promise,tv_accompany_address;
		CustomGridView gv_first_house_item;
	}
}
