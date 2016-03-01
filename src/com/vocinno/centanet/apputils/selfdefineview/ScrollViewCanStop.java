package com.vocinno.centanet.apputils.selfdefineview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class ScrollViewCanStop extends ScrollView {

	public ScrollViewCanStop(Context context) {
		super(context);
	}

	public ScrollViewCanStop(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScrollViewCanStop(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	private boolean isStoped = false;

	public void setStoped(boolean isStop) {
		this.isStoped = isStop;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (isStoped) {
			return true;
		} else {
			return super.onTouchEvent(ev);
		}
	}
}
