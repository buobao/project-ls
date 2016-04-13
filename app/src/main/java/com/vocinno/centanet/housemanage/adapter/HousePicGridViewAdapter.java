package com.vocinno.centanet.housemanage.adapter;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.vocinno.centanet.R;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsFile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
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
//	private int type;//0,1,2,3,4,5 房型，室，厅，厨，卫，其他
	private MyInterface myInterface;
	public HousePicGridViewAdapter() {
	}

	public HousePicGridViewAdapter(Activity mContext, String type) {
		this.mContext = mContext;
		this.mInflater = (LayoutInflater) this.mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mType = type;
		this.myInterface=(MyInterface)mContext;
	}

	public void setData(List<String> imageList, List<String> imageDescription) {
		this.mImageList = imageList;
		this.mImageDescription = imageDescription;
	}
	public void addData(List<String> imageList, List<String> imageDescription) {
		if(this.mImageList==null){
			this.mImageList=new ArrayList<String>();
		}
		if(this.mImageDescription==null){
			this.mImageDescription=new ArrayList<String>();
		}
		this.mImageList.addAll(imageList);
		this.mImageDescription.addAll(imageDescription);
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
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.item_house_pic_add_house_pic, null);
			holder.mImgHousePic = (ImageView) convertView
					.findViewById(R.id.img_house_pic_add_house_pic_activity);
			holder.tv_img_path = (TextView) convertView.findViewById(R.id.tv_img_path);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final int index = position;
		if (index == mCellNumber - 1) {
			holder.mImgHousePic.setImageResource(R.drawable.work_icon_add);
			holder.mImgHousePic.setOnClickListener(getListSize());
		} else {
			/*holder.mImgHousePic.setImageBitmap(MethodsFile.decodeFile(
					this.mImageList.get(index), false, true));*/

			String imagePath = this.mImageList.get(index);
			String imageUrl = ImageDownloader.Scheme.FILE.wrap(imagePath);
			MethodsFile.downloadImgByUrl(imageUrl, holder.mImgHousePic);


			holder.tv_img_path.setText(this.mImageList.get(index));
			// 点击进行图片描述编辑
			holder.mImgHousePic.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (index != mCellNumber - 1) {
//						myInterface.editPhoto(mType,holder.tv_img_path.getText().toString(), mImageDescription.get(index));
						myInterface.editPhoto(mType,mImageList.get(index),"");
						/*// 记录类型，方便再resume中进行操作
						MethodsDeliverData.mHouseType = mType;
						MethodsDeliverData.mEditorImage = mImageList.get(index);
						MethodsDeliverData.mEditorImageDescriptionString = mImageDescription.get(index);
						MethodsExtra.startActivity(mContext,EditPicDetailActivity.class);*/
					} else {
						//choosePicOrCamera();
					}
				}
			});
		}

		return convertView;
	}


	public class ViewHolder {
		ImageView mImgHousePic;
		TextView tv_img_path;
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
				myInterface.takePhoto(mType);
				break;
			case R.id.tv_choosePic_HousePicGridViewAdapter:
				// 跳转到指定的activity
				mChooseDialog.dismiss();
				MethodsDeliverData.mHouseType = mType;
				myInterface.selectPhoto(mType);
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
				if (mImageList != null && mImageList.size() >=9) {
					MethodsExtra.toast(mContext, "图片不能超过9张！");
					return;
				}else{
					choosePicOrCamera();
				}
			}
		};
	}

}
