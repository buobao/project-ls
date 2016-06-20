package com.vocinno.centanet.housemanage.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vocinno.centanet.R;
import com.vocinno.centanet.tools.MyToast;
import com.vocinno.utils.MethodsDeliverData;

import java.util.List;


public class FirstHandPicAdapter extends BaseAdapter {

	private Activity mContext;
	private LayoutInflater mInflater;
	private Dialog mChooseDialog;
	private List<String> imgList;
	private MyInterface myInterface;
	public FirstHandPicAdapter(Activity mContext,MyInterface iface) {
		this.mContext = mContext;
		this.mInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		myInterface=iface;
	}

	public void setData(List<String> list) {
		this.imgList = list;
		notifyDataSetChanged();
	}
	public void addData(List<String> list) {
		if(this.imgList==null){
			this.imgList=list;
		}
		this.imgList.addAll(list);
		notifyDataSetChanged();
	}


	@Override
	public int getCount() {
		return imgList==null?1:imgList.size()+1;
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
		final int index = position;
		if (imgList==null||index == imgList.size() - 1 ){
			holder.iv_first_img.setImageResource(R.drawable.adpic);
			holder.iv_first_img.setOnClickListener(getListSize());
		} else {

			String imagePath = this.imgList.get(index);
			Glide.with(mContext).load(imagePath).centerCrop().crossFade().into(holder.iv_first_img);
			// 点击进行图片描述编辑
			holder.iv_first_img.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (index != imgList.size() - 1) {
						myInterface.editPhoto(null,imgList.get(index),null);
					}
				}
			});
		}

		return convertView;
	}


	public class ViewHolder {
		ImageView iv_first_img;
	}

	private void choosePicOrCamera() {
		mChooseDialog = new Dialog(mContext, R.style.Theme_dialog);
		mChooseDialog
				.setContentView(R.layout.dialog_choose_pic_edit_pic_detail);
		Window win = mChooseDialog.getWindow();
		win.setGravity(Gravity.BOTTOM);
		mChooseDialog.setCanceledOnTouchOutside(true);
		mChooseDialog.show();
		win.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		TextView mTvCamera = (TextView) mChooseDialog
				.findViewById(R.id.tv_cameraAdd_HousePicGridViewAdapter);
		TextView mTvPic = (TextView) mChooseDialog
				.findViewById(R.id.tv_choosePic_HousePicGridViewAdapter);
		TextView mTvCancel = (TextView) mChooseDialog
				.findViewById(R.id.tv_cancel_HousePicGridViewAdapter);
		mTvCamera.setOnClickListener(itemClick);
		mTvPic.setOnClickListener(itemClick);
		mTvCancel.setOnClickListener(itemClick);
	}
	public OnClickListener itemClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (imgList != null) {
				MethodsDeliverData.hasImageNum = imgList.size();
			} else {
				MethodsDeliverData.hasImageNum = 0;
			}
			switch (v.getId()) {
			case R.id.tv_cameraAdd_HousePicGridViewAdapter:
				// 启动相机.
				mChooseDialog.dismiss();
				myInterface.takePhoto(null);
				break;
			case R.id.tv_choosePic_HousePicGridViewAdapter:
				// 跳转到指定的activity
				mChooseDialog.dismiss();
				myInterface.selectPhoto(null);
				break;
			case R.id.tv_cancel_HousePicGridViewAdapter:
				mChooseDialog.dismiss();
				break;
			default:
				break;
			}
		}
	};

	@NonNull
	private OnClickListener getListSize() {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (imgList != null && imgList.size() >=9) {
					MyToast.showToast("图片不能超过9张!");
					return;
				}else{
					choosePicOrCamera();
				}
			}
		};
	}

}
