package com.vocinno.centanet.housemanage.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.vocinno.centanet.R;

import java.util.List;


public class ImgAdapter extends BaseAdapter {

	private Activity mContext;
	private LayoutInflater mInflater;
	private List<String> imgList;
	public ImgAdapter(Activity mContext) {
		this.mContext = mContext;
		this.mInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setData(List<String> list) {
		this.imgList = list;
		notifyDataSetChanged();
	}
	public void addData(List<String> list) {
		if(this.imgList==null){
			this.imgList=list;
		}else{
			this.imgList.addAll(list);
		}
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return imgList==null?1:imgList.size();
	}
	@Override
	public Object getItem(int position) {
		return imgList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_first_pic, null);
			holder.iv_first_img = (ImageView) convertView.findViewById(R.id.iv_first_img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String imagePath = this.imgList.get(position);
		Glide.with(mContext).load(imagePath).centerCrop().crossFade().into(holder.iv_first_img);
		return convertView;
	}


	public class ViewHolder {
		ImageView iv_first_img;
	}



}
