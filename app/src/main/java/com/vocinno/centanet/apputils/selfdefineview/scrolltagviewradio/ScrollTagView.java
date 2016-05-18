package com.vocinno.centanet.apputils.selfdefineview.scrolltagviewradio;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.vocinno.centanet.R;

import java.util.ArrayList;

public class ScrollTagView extends HorizontalScrollView {

	private SuperTagAdapter mTagAdapter;

	private onScrollTagViewChangeListener mCallBack;

	private Context mContext;

	private int mScreenW = 0;

	private int mSelectedItemIndex = 0;

	private LinearLayout mContainer;
	private ArrayList<Integer> mListlens = new ArrayList<Integer>();

	public ScrollTagView(Context context) {
		this(context, null);
	}

	public ScrollTagView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ScrollTagView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		mScreenW = ((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getWidth();
		mContainer = new LinearLayout(mContext);
		mContainer.setOrientation(LinearLayout.HORIZONTAL);
		addView(mContainer);
	}

	public SuperTagAdapter getAdapter() {
		return mTagAdapter;
	}

	public void setAdapter(SuperTagAdapter tabAdapter,
			onScrollTagViewChangeListener callBack) {
		this.mTagAdapter = tabAdapter;
		this.mCallBack = callBack;
		initTabs();
	}

	private void initTabs() {
		for (int i = 0; i < mTagAdapter.getCount(); i++) {
			final int position = i;
			View view = mTagAdapter.getView(i);
			mContainer.addView(view);
			RadioButton btn = (RadioButton) view
					.findViewById(R.id.btn_tag_tagViewRadio);
			RelativeLayout containerChild = (RelativeLayout) view
					.findViewById(R.id.rlyt_tag_tagView);
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) containerChild
					.getLayoutParams();
			if (params == null) {
				params = new LinearLayout.LayoutParams(mScreenW / 3,
						android.view.ViewGroup.LayoutParams.MATCH_PARENT);
			} else {
				params.width = mScreenW / 3;
			}
			containerChild.setLayoutParams(params);

			btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					selectedTab1(position, true, true);
				}
			});
		}
	}

	public void selectedTab1(int position, boolean isClicked,
			boolean isRefreshData) {
		if (isClicked) {
			mCallBack.onChanged(position);
		}
		int st = 0;
		boolean tf = false;
		if (position >= 1 && mListlens.size() == 0) {
			tf = true;
		}
		for (int i = 0; i < mContainer.getChildCount(); i++) {
			View view = mContainer.getChildAt(i);
			RadioButton rbtn = (RadioButton) view
					.findViewById(R.id.btn_tag_tagViewRadio);
			if (position == i) {
				// 选中
				rbtn.setChecked(true);
				rbtn.setTextColor(getResources().getColor(R.color.red));
			} else {
				// 不选中
				rbtn.setChecked(false);
				rbtn.setTextColor(getResources().getColor(R.color.black));
			}
			if (tf) {
				st += mContainer.getChildAt(i).getWidth();
				mListlens.add(st);
			}
		}
		if (position <= 1) {
//			smoothScrollTo(0, 0);
		} else {
			/*int halfCur = mListlens.get(position) - mListlens.get(position - 1);
			smoothScrollTo(mListlens.get(position - 1) - mScreenW / 2 + halfCur / 2
					- 18, 0);*/
		}
	}
	int mPosX,mPosY,mCurrentPosX,mCurrentPosY;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(MotionEvent.ACTION_DOWN==event.getAction()){
			mPosX = (int)event.getX();
			mPosY = (int)event.getY();
		}
		/*if(MotionEvent.ACTION_UP == event.getAction()){
			mPosX = (int)event.getX();
			mPosY = (int)event.getY();
			return true;
		}*/
		if (MotionEvent.ACTION_MOVE == event.getAction()) {
			mCurrentPosX = mPosX-(int)event.getX();
			mCurrentPosY = mPosY-(int)event.getY();
			if(mCurrentPosX!=0){
				smoothScrollTo(mCurrentPosX,mCurrentPosY);
			}
			/*mPosX = (int)event.getX();
			mPosY = (int)event.getY();*/
			/*if(mCurrentPosX<0){
				smoothScrollTo(0,0);
				return  true;
			}else if(mCurrentPosX>0){
				smoothScrollTo(9000,0);
				return  true;
			}*/
			return true;
		}

		/*if (mCurrentPosX  > 0 && Math.abs(mCurrentPosY - mPosY) < 1000) {

			Log.i("==", "向右的按下位置" + mPosX + "移动位置"+mCurrentPosX);

		}
		else if (mCurrentPosX  < 0 && Math.abs(mCurrentPosY - mPosY) < 1000
				) {

			Log.i("==", "向左的按下位置" + mPosX + "移动位置"+mCurrentPosX);

		}
		else if (mCurrentPosY> 0 && Math.abs(mCurrentPosX - mPosX) < 1000)

		{
			Log.i("==", "向下的按下位置" + mPosX + "移动位置"+mCurrentPosX);

		}
		else if (mCurrentPosY  < 0 && Math.abs(mCurrentPosX - mPosX) < 1000)

		{
			Log.i("==", "向上的按下位置" + mPosX + "移动位置"+mCurrentPosX);

		}*/
		return super.onTouchEvent(event);
	}
}
