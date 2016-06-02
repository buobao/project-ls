package com.vocinno.centanet.apputils.selfdefineview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class FontLightTextView extends TextView {
	public FontLightTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initFont();
	}

	public FontLightTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initFont();
	}

	public FontLightTextView(Context context) {
		super(context);
		initFont();
	}

	private void initFont() {
	}
}
