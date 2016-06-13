package com.vocinno.centanet.keymanage.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vocinno.centanet.R;
import com.vocinno.centanet.keymanage.KeyManageActivity2;
import com.vocinno.centanet.model.KeyItem;

import java.util.List;

public class KeyListAdapter2 extends BaseAdapter {

	private KeyManageActivity2 mContext;
	private LayoutInflater mInflater;
	private int mScreenWidth = 0;
	private List<KeyItem> mListKeys;

	public void setListDatas(List<KeyItem> listDatas) {
		mListKeys = listDatas;
		notifyDataSetChanged();
	}
	public void addListDatas(List<KeyItem> listDatas) {
		if(mListKeys!=null){
			mListKeys.addAll(listDatas);
		}else{
			mListKeys = listDatas;
		}
		notifyDataSetChanged();
	}

	public KeyListAdapter2(KeyManageActivity2 mContext) {
		this.mContext = mContext;
		this.mInflater = (LayoutInflater) this.mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mScreenWidth = ((Activity) mContext).getWindowManager()
				.getDefaultDisplay().getWidth();
	}

	@Override
	public int getCount() {
		if (mListKeys == null) {
			return 0;
		}
		return mListKeys.size();
	}

	@Override
	public KeyItem getItem(int position) {
		if (mListKeys == null) {
			return null;
		}
		return mListKeys.get(position);
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
			convertView = mInflater.inflate(R.layout.item_key_listview,null);
			holder.tv_key_housename= (TextView) convertView.findViewById(R.id.tv_key_housename);
			holder.tv_key_id= (TextView) convertView.findViewById(R.id.tv_key_id);
			holder.tv_key_status= (TextView) convertView.findViewById(R.id.tv_key_status);
			holder.tv_key_borrowto= (TextView) convertView.findViewById(R.id.tv_key_borrowto);
			holder.tv_key_backtime= (TextView) convertView.findViewById(R.id.tv_key_backtime);
			holder.tv_key_fenhang= (TextView) convertView.findViewById(R.id.tv_key_fenhang);

			holder.iv_key_img= (ImageView) convertView.findViewById(R.id.iv_key_img);
			holder.iv_key_gaoceng= (ImageView) convertView.findViewById(R.id.iv_key_gaoceng);
			holder.iv_key_isborrow= (ImageView) convertView.findViewById(R.id.iv_key_isborrow);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		KeyItem keyItem = mListKeys.get(position);
		if (keyItem.getImg()!=null&&keyItem.getImg().length()>0) {
			Glide.with(mContext).load(keyItem.getImg()).centerCrop()
					.crossFade()
					.error(R.drawable.default_img)
					.into(holder.iv_key_img);
		}

		holder.tv_key_housename.setText(keyItem.getAddr());
		holder.tv_key_id.setText(keyItem.getKeyNum());
		holder.tv_key_fenhang.setText(keyItem.getStore());
		holder.tv_key_backtime.setText(keyItem.getCreateTime());
		String borrowString;
		if(keyItem.getBorrowTime()!=null&&keyItem.getBorrowTime().trim().length()>0){
			borrowString=keyItem.getHolder()+"/"+keyItem.getBorrowTime();
		}else{
			borrowString=keyItem.getHolder();
		}
		holder.tv_key_status.setText(keyItem.getReturnComfirm());
		if (keyItem.isKeyStatus()){
			holder.iv_key_isborrow.setImageResource(R.drawable.key_zaidian);
			holder.tv_key_borrowto.setText("收:" + keyItem.getHolder());
		}else{
			holder.iv_key_isborrow.setImageResource(R.drawable.key_jieyong);
			holder.tv_key_borrowto.setText("借:" + keyItem.getBorrowBy()+"/"+keyItem.getBorrowTime());
		}
		return convertView;
	}
	@Override
	public int getViewTypeCount() {
		// menu type count
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		KeyItem keyItem = mListKeys.get(position);
		if(keyItem!=null&&keyItem.getReturnComfirm()!=null&keyItem.getReturnComfirm().trim().length()>0){
			return -1;
		}else{
			return mListKeys.get(position).isKeyStatus()==true?1:0;
		}
	}

	public class ViewHolder{
		TextView tv_key_housename,tv_key_id,tv_key_status,tv_key_borrowto,tv_key_backtime,tv_key_fenhang;
		ImageView iv_key_img,iv_key_gaoceng,iv_key_isborrow;
	}
}
