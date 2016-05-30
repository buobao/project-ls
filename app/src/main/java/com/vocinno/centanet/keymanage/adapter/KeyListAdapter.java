package com.vocinno.centanet.keymanage.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.selfdefineview.TouchableImageView;
import com.vocinno.centanet.keymanage.KeyManageActivity;
import com.vocinno.centanet.keymanage.adapter.ViewHolderGiveKey.KeyItemState;
import com.vocinno.centanet.model.KeyItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class KeyListAdapter extends BaseAdapter {

	private KeyManageActivity mContext;
	private LayoutInflater mInflater;
	private int mScreenWidth = 0;
	private List<KeyItem> mListKeys;

	public void setListDatas(List<KeyItem> listDatas) {
		mListKeys = listDatas;
		notifyDataSetChanged();
	}

	public KeyListAdapter(KeyManageActivity mContext) {
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
	public Object getItem(int position) {
		if (mListKeys == null) {
			return null;
		}
		return mListKeys.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	boolean isIn(View moveView, float left, float right, float top,
			float bottom, float width) {
		// 确定被移动view的中心点
		float cx0 = (moveView.getLeft() + moveView.getRight()) / 2;
		float cy0 = (moveView.getTop() + moveView.getBottom()) / 2;

		float cx1 = (left + right) / 2;
		float cy1 = (top + bottom) / 2;

		float hd = cx1 - cx0;
		float yd = cy1 - cy0;
		if ((hd * hd + yd * yd) < (moveView.getWidth() + width)
				* (moveView.getWidth() + width) / 4) {
			moveView.layout((int) left, (int) top, (int) right, (int) bottom);
			return true;
		}
		return false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolderGiveKey holder;
		if (convertView == null) {
			holder = new ViewHolderGiveKey();
			convertView = mInflater.inflate(R.layout.item_key_manage_listview,
					null);
			holder.mRlytContent = (RelativeLayout) convertView
					.findViewById(R.id.rlyt_content_itemKeyManage);
			holder.iv_house_img = (ImageView) convertView
					.findViewById(R.id.iv_house_img);
			holder.mImageViewGiveKey1 = (ImageView) convertView
					.findViewById(R.id.imgView_giveKey1_itemKeyManageListView);
			holder.mTouchableImgViewGiveKey0 = (TouchableImageView) convertView
					.findViewById(R.id.imgView_giveKey_itemKeyManageListView);
			holder.mTouchableImgViewGiveKey0.setIsCanTouch(false);
			holder.mImgViewGiveKey0Bg = (ImageView) convertView
					.findViewById(R.id.imgView_giveKeyBg_itemKeyManageListView);

			holder.mImgViewRtnKey = (ImageView) convertView
					.findViewById(R.id.imgView_returnKey_itemKeyManageListView);
			holder.mHouseAddress = (TextView) convertView
					.findViewById(R.id.tv_address_itemKeyManageListView);
			holder.mKeyNumb = (TextView) convertView
					.findViewById(R.id.tv_detail_itemKeyManageListView);
			holder.tv_fenhang = (TextView) convertView
					.findViewById(R.id.tv_fenhang_itemKeyManageListView);
			holder.tv_time = (TextView) convertView
					.findViewById(R.id.tv_time_itemKeyManageListView);
			holder.mBorrowState01 = (TextView) convertView
					.findViewById(R.id.tv_price01_itemKeyManageListView);
			holder.mBorrowState02 = (TextView) convertView
					.findViewById(R.id.tv_price02_itemKeyManageListView);
			holder.mTvTime = (TextView) convertView
					.findViewById(R.id.tv_time_itemKeyManageListView);
			holder.mTvTishi = (TextView) convertView
					.findViewById(R.id.tv_tishi_itemKeyManageListView);
			holder.mTvGiveKeyPassWord = (TextView) convertView
					.findViewById(R.id.tv_giveKeyPassword_itemKeyManageListView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolderGiveKey) convertView.getTag();
			holder.keyItemState = KeyItemState.NORMAL;
			convertView.invalidate();
		}
		LinearLayout.LayoutParams params = (LayoutParams) holder.mRlytContent
				.getLayoutParams();
		if (params == null) {
			params = new LinearLayout.LayoutParams(mScreenWidth, 80);
		} else {
			params.width = mScreenWidth;
		}
		holder.mRlytContent.setLayoutParams(params);
		holder.mTouchableImgViewGiveKey0
				.setOnTouchListener(new OnTouchListener() {
					float startRawX, startRawY, distant;
					boolean isFinish = false;
					float left1, right1, top1, bottom1, width1;

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if (!holder.mTouchableImgViewGiveKey0.getIsCanTouch()) {
							return false;
						}
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							Log.d("wanggsx", "touchable down ");
							if (v.getLeft() == holder.mImageViewGiveKey1
									.getLeft()) {
								// 钥匙拖动开关
								holder.mTouchableImgViewGiveKey0
										.setIsCanTouch(false);
								return false;
							}
							startRawX = event.getRawX();
							startRawY = event.getRawY();
							View v0 = (View) v.getParent();
							View v1 = (View) v0.getParent();
							ImageView imgView = (ImageView) v1
									.findViewById(R.id.imgView_giveKey1_itemKeyManageListView);
							left1 = imgView.getLeft();
							right1 = imgView.getRight();
							top1 = imgView.getTop();
							bottom1 = imgView.getBottom();
							width1 = imgView.getWidth();
							break;
						case MotionEvent.ACTION_MOVE:
							Log.d("wanggsx", "touchable move");
							v.layout(
									(int) (event.getRawX() - startRawX + v
											.getMeasuredWidth() / 2),
									v.getTop(),
									(int) (event.getRawX() - startRawX
											+ v.getMeasuredWidth() + v
											.getMeasuredWidth() / 2),
									v.getTop() + v.getMeasuredHeight());

							isFinish = isIn(v, left1, right1, top1, bottom1,
									width1);
							break;
						case MotionEvent.ACTION_UP:
							Log.d("wanggsx", "touchable up");
							if (isFinish
									|| isIn(v, left1, right1, top1, bottom1,
											width1)) {
								mContext.setGiveKeyFinish(holder.mStrKeyNum);
							} else {
								v.layout(holder.mImgViewGiveKey0Bg.getLeft(),
										holder.mImgViewGiveKey0Bg.getTop(),
										holder.mImgViewGiveKey0Bg.getRight(),
										holder.mImgViewGiveKey0Bg.getBottom());
							}
							break;
						default:
							break;
						}
						return true;
					}

				});
		KeyItem keyItem = mListKeys.get(position);

		if (keyItem.getImg()!=null&&keyItem.getImg().length()>0) {
			Glide.with(mContext).load(keyItem.getImg()).centerCrop()
					.crossFade()
					.error(R.drawable.default_img)
					.into(holder.iv_house_img);
		}
		holder.mStrKeyNum = keyItem.getKeyNum();
		// 地址
		holder.mHouseAddress.setText(keyItem.getAddr());
		// 钥匙编号
		holder.mKeyNumb.setText(keyItem.getKeyNum());
		holder.tv_fenhang.setText(keyItem.getStore());
		// 借用时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd=HH:mm:ss");
		Date date= new Date(keyItem.getBorrowTime());
		String[] dateString=sdf.format(date).split("=");
		holder.mBorrowState01.setText(dateString[0]);
		holder.tv_time.setText(dateString[1]+"借用");

		if (keyItem.getIsWaitingConfirm().equals("true")) {
			holder.mBorrowState01.setVisibility(View.GONE);
			holder.mBorrowState02.setVisibility(View.VISIBLE);
			mContext.mStrtrKeyNo = keyItem.getKeyNum();
		} else {
			holder.mBorrowState01.setVisibility(View.VISIBLE);
			holder.mBorrowState02.setVisibility(View.GONE);
		}
		return convertView;
	}

}
