package com.vocinno.centanet.apputils.selfdefineview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class FontBoldTextView extends TextView {

	public FontBoldTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initFont();
	}

	public FontBoldTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initFont();
	}

	public FontBoldTextView(Context context) {
		super(context);
		initFont();
	}

	private void initFont() {
	}

}
