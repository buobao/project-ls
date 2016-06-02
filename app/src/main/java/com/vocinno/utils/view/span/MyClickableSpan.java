package com.vocinno.utils.view.span;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;

public class MyClickableSpan extends ClickableSpan implements OnClickListener {
	private final OnClickListener mListener;

	public MyClickableSpan(OnClickListener l) {
		super();
		mListener = l;
	}

	@Override
	public void onClick(View v) {
		mListener.onClick(v);
	}

	@Override
	public void updateDrawState(TextPaint ds) {
		ds.setARGB(255, 255, 0, 0);
		ds.setUnderlineText(false);
	}
}