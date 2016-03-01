package com.vocinno.centanet.apputils.selfdefineview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class TouchableImageView extends ImageView {
	// 是否可以监听触摸事件
	private boolean mIsCanTouch = true;

	public boolean getIsCanTouch() {
		return mIsCanTouch;
	}

	public void setIsCanTouch(boolean isCanTouch) {
		mIsCanTouch = isCanTouch;
	}

	public TouchableImageView(Context context) {
		super(context);
	}

	public TouchableImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (mIsCanTouch) {
			getParent().requestDisallowInterceptTouchEvent(true);
		} else {
			getParent().requestDisallowInterceptTouchEvent(false);
		}
		return super.dispatchTouchEvent(ev);
	}

}
