package com.vocinno.centanet.apputils.selfdefineview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * 仅仅是为了扩展屏幕外的空间
 * 
 * @author wanggsx
 * 
 */
public class MyGridView extends GridView {

	public MyGridView(Context context) {
		super(context);
	}

	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// 不能滚动
		getParent().requestDisallowInterceptTouchEvent(true);
		return super.onInterceptTouchEvent(ev);
	}



}
