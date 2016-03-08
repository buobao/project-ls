package com.vocinno.centanet.apputils.selfdefineview;

import com.vocinno.centanet.R;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * 仅仅是为了扩展屏幕外的空间
 * 
 * @author wanggsx
 * 
 */
public class MyHorizontalScrollView extends HorizontalScrollView {
	private static final int SCROLL_TO_POSITION_WITH_INIT = 1001;

	public MyHorizontalScrollView(Context context) {
		super(context);
	}

	public MyHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mHandler.sendEmptyMessageDelayed(SCROLL_TO_POSITION_WITH_INIT, 1);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// 不能滚动
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return false;
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SCROLL_TO_POSITION_WITH_INIT:
				View leftView = MyHorizontalScrollView.this
						.findViewById(R.id.llyt_leftContainer_itemKeyManageListView);
				MyHorizontalScrollView.this.smoothScrollTo(
						leftView.getMeasuredWidth(), 0);
				break;
			default:
				break;
			}
		};
	};

}
