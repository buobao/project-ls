package com.vocinno.centanet.apputils.adapter;

import java.util.ArrayList;
import java.util.List;

import com.vocinno.centanet.R;
import com.vocinno.utils.MethodsData;
import com.vocinno.utils.MethodsFile;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

//引导页使用的pageview适配器
public class MyPagerAdapter extends PagerAdapter {
	public enum MType {
		Normal, HouseDetail
	}

	private int mCurrentUsedBitmapIndex = -1;
	private List<String> mListMustBeRecycled = new ArrayList<String>();
	private MType mType = MType.Normal;
	private Activity mContext;
	private List<View> mListviews = new ArrayList<View>();
	private int[] mIntScreenWithHeight;
	// ------------------MType.HouseDetail-----------------
	private String[] strImagePaths;
	private Bitmap[] bpList;
	// private static BitmapCache bpCache = null;
	// 用于高斯模糊处理
	private static int[] mIntWidthHeightLeftTopForViewBlur = { 0, 0, 0, 0 };
	private static int[] mIntWidthHeightLeftTopForViewBlurBorder = { 0, 0, 0, 0 };

	// ------------------MType.HouseDetail-----------------
	public MyPagerAdapter(Activity context, List<View> views, MType mType) {
		this.mListviews = views;
		this.mType = mType;
		this.mContext = context;
		mIntScreenWithHeight = MethodsData.getScreenWidthHeight(mContext);
		if (mType == MType.HouseDetail) {
			int length = views.size();
			strImagePaths = new String[length];
			bpList = new Bitmap[length];
			for (int i = 0; i < length; i++) {
				strImagePaths[i] = (String) views
						.get(i)
						.findViewById(
								R.id.imgView_modelViewPagerAdapterItemView)
						.getTag();
			}
		}
	}

	public void destory() {
		mListviews = null;
		this.notifyDataSetChanged();
		if (bpList != null) {
			for (int i = 0; i < bpList.length; i++) {
				if (bpList[i] != null) {
					bpList[i].recycle();
					Log.d("wanggsxHouseImage",
							"wanggsxHouseImage mypageradapter bitmap destory "
									+ i);
				}
			}
		}
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public int getCount() {
		return mListviews == null ? 0 : mListviews.size();
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		if (mListviews == null || position >= mListviews.size()) {
			return;
		}
		((ViewPager) container).removeView(mListviews.get(position));
		// if (mType == MType.HouseDetail) {
		// if (mCurrentUsedBitmapIndex != position) {
		// Log.d("wanggsxHouseImage",
		// "wanggsxHouseImage mypageradapter destroyItem "
		// + position);
		// if (bpList[position] != null) {
		// bpList[position].recycle();
		// bpList[position] = null;
		// Log.d("wanggsxHouseImage",
		// "wanggsxHouseImage mypageradapter destroyItem 销毁图片");
		// } else {
		// mListMustBeRecycled.add("" + position);
		// Log.d("wanggsxHouseImage",
		// "wanggsxHouseImage mypageradapter destroyItem 添加到图片销毁队列");
		// }
		// }
		// }
	}

	@Override
	public Object instantiateItem(View container, int position) {
		View view = mListviews.get(position);
		((ViewPager) container).addView(view);
		if (mType == MType.HouseDetail) {
			Message msg = new Message();
			msg.what = R.id.doRequest;
			msg.obj = view;
			msg.arg1 = position;
			mHandler.sendMessageDelayed(msg, 10);
		}
		return mListviews.get(position);
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case R.id.doRequest:
				mCurrentUsedBitmapIndex = msg.arg1;
				if (mListMustBeRecycled.contains("" + mCurrentUsedBitmapIndex)) {
					mListMustBeRecycled.remove("" + mCurrentUsedBitmapIndex);
				}
				onInstantiateItem((View) msg.obj, mCurrentUsedBitmapIndex);
				mCurrentUsedBitmapIndex = -1;
				break;
			}
		};
	};

	private void onInstantiateItem(View viewRoot, int position) {
		try {
			Log.d("wanggsxHouseImage",
					"wanggsxHouseImage mypageradapter onInstantiateItem "
							+ position);
			ImageView mImageView = (ImageView) viewRoot
					.findViewById(R.id.imgView_modelViewPagerAdapterItemView);
			View viewBlur = viewRoot
					.findViewById(R.id.tv_blur_modelLayerGradit);
			View viewBlurBorder = viewRoot
					.findViewById(R.id.tv_blurBorder_modelLayerGradit);
			Bitmap bpMirror = null;

			String strPath = strImagePaths[position];
			byte[] bt = MethodsFile.decodeBitmap(strPath);
			Bitmap bp0 = BitmapFactory.decodeByteArray(bt, 0, bt.length);
			Bitmap bp = MethodsFile
					.getScaledBitmap(bp0, mIntScreenWithHeight[0],
							(mIntScreenWithHeight[1] - MethodsData.dip2px(
									mContext, 25)) / 2, 0, 0);
			bt = null;
			bp0.recycle();
			bp0 = null;
			if (bp == null) {
				System.gc();
				return;
			}
			bpMirror = MethodsFile.getBitmapWithVerticalMirror(bp);
			// bpCache.addCacheBitmap(bpMirror, new Random().nextInt());
			bpList[position] = bpMirror;
			mImageView.setImageBitmap(bpMirror);
			bp.recycle();
			bp = null;
			if (position == 0) {
				MethodsFile.doBlurForViewBkgBitmap(mContext, bpMirror, 20,
						viewBlur, 1);
				MethodsFile.doBlurForViewBkgBitmap(mContext, bpMirror, 20,
						viewBlurBorder, 1);
				mIntWidthHeightLeftTopForViewBlur[0] = viewBlur
						.getMeasuredWidth();
				mIntWidthHeightLeftTopForViewBlur[1] = viewBlur
						.getMeasuredHeight();
				mIntWidthHeightLeftTopForViewBlur[2] = viewBlur.getLeft();
				mIntWidthHeightLeftTopForViewBlur[3] = viewBlur.getTop();
				mIntWidthHeightLeftTopForViewBlurBorder[0] = viewBlurBorder
						.getMeasuredWidth();
				mIntWidthHeightLeftTopForViewBlurBorder[1] = viewBlurBorder
						.getMeasuredHeight();
				mIntWidthHeightLeftTopForViewBlurBorder[2] = viewBlurBorder
						.getLeft();
				mIntWidthHeightLeftTopForViewBlurBorder[3] = viewBlurBorder
						.getTop();
			} else {
				MethodsFile.doBlurForViewBkgBitmap(mContext, bpMirror, 20,
						viewBlur, mIntWidthHeightLeftTopForViewBlur[0],
						mIntWidthHeightLeftTopForViewBlur[1],
						mIntWidthHeightLeftTopForViewBlur[2],
						mIntWidthHeightLeftTopForViewBlur[3], 1);
				MethodsFile.doBlurForViewBkgBitmap(mContext, bpMirror, 20,
						viewBlurBorder,
						mIntWidthHeightLeftTopForViewBlurBorder[0],
						mIntWidthHeightLeftTopForViewBlurBorder[1],
						mIntWidthHeightLeftTopForViewBlurBorder[2],
						mIntWidthHeightLeftTopForViewBlurBorder[3], 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
