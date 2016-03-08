package com.vocinno.centanet.housemanage.adapter;

import java.util.List;

import com.vocinno.centanet.R;
import com.vocinno.centanet.housemanage.EditPicDetailActivity;
import com.vocinno.utils.MethodsData;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsFile;
import com.vocinno.utils.imageutils.selector.SelectorImageActivity;
import com.vocinno.utils.media.camera.CameraActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HousePicGridViewAdapter extends BaseAdapter {

	private Activity mContext;
	private LayoutInflater mInflater;
	private Dialog mChooseDialog;
	private int mCellNumber = 5;
	private List<String> mImageList;
	private List<String> mImageDescription;
	private String mType;

	public HousePicGridViewAdapter() {
	}

	public HousePicGridViewAdapter(Activity mContext, String type) {
		this.mContext = mContext;
		this.mInflater = (LayoutInflater) this.mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mType = type;
	}

	public void setData(List<String> imageList, List<String> imageDescription) {
		this.mImageList = imageList;
		this.mImageDescription = imageDescription;
	}

	public void setCount(int number) {
		mCellNumber = number;
	}

	@Override
	public int getCount() {
		if (this.mImageList == null) {
			return mCellNumber;
		} else {
			mCellNumber = this.mImageList.size() + 1;
			return mCellNumber;
		}
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.item_house_pic_add_house_pic, null);
			holder.mImgHousePic = (ImageView) convertView
					.findViewById(R.id.img_house_pic_add_house_pic_activity);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final int index = position;
		if (index == mCellNumber - 1) {
			holder.mImgHousePic.setImageResource(R.drawable.work_icon_add);
		} else {
			holder.mImgHousePic.setImageBitmap(MethodsFile.decodeFile(
					this.mImageList.get(index), false, true));
		}
		// 点击进行图片描述编辑
		holder.mImgHousePic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (index != mCellNumber - 1) {
					// 记录类型，方便再resume中进行操作
					MethodsDeliverData.mHouseType = mType;
					MethodsDeliverData.mEditorImage = mImageList.get(index);
					MethodsDeliverData.mEditorImageDescriptionString = mImageDescription
							.get(index);
					MethodsExtra.startActivity(mContext,
							EditPicDetailActivity.class);
				} else {
					choosePicOrCamera();
				}
			}
		});
		return convertView;
	}

	public class ViewHolder {
		ImageView mImgHousePic;
	}

	private void choosePicOrCamera() {
		mChooseDialog = new Dialog(mContext, R.style.Theme_dialog);
		mChooseDialog
				.setContentView(R.layout.dialog_choose_pic_edit_pic_detail);
		Window win = mChooseDialog.getWindow();
		win.setGravity(Gravity.BOTTOM);
		mChooseDialog.setCanceledOnTouchOutside(true);
		mChooseDialog.show();
		win.setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
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
			if (mImageList != null && mImageList.size() >= 9) {
				MethodsExtra.toast(mContext, "图片不能超过9张！");
				return;
			}
			if (mImageList != null) {
				MethodsDeliverData.hasImageNum = mImageList.size();
			} else {
				MethodsDeliverData.hasImageNum = 0;
			}
			switch (v.getId()) {
			case R.id.tv_cameraAdd_HousePicGridViewAdapter:
				// 启动相机.
				mChooseDialog.dismiss();
				MethodsDeliverData.mHouseType = mType;
				MethodsExtra.startActivity(mContext, CameraActivity.class);
				break;
			case R.id.tv_choosePic_HousePicGridViewAdapter:
				// 跳转到指定的activity
				mChooseDialog.dismiss();
				MethodsDeliverData.mHouseType = mType;
				MethodsExtra.startActivity(mContext,
						SelectorImageActivity.class);
				break;
			case R.id.tv_cancel_HousePicGridViewAdapter:
				mChooseDialog.dismiss();
				break;
			default:
				break;
			}
		}
	};
}
