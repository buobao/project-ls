package com.vocinno.centanet.apputils.selfdefineview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vocinno.centanet.R;

/**
 * Created by fanxl2 on 2016/6/17.
 */
public class RightInputText extends LinearLayout {

	private String leftText;
	private int inputMarginLeft;
	private String inputTips;
	private int leftTextSize;
	private int inputTextSize;
	private int leftTextColor;
	private int inputTextColor;
	private final int DEFAULT_TEXT_COLOR = Color.parseColor("#606060");

	public RightInputText(Context context) {
		this(context, null);
	}

	public RightInputText(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RightInputText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setOrientation(LinearLayout.HORIZONTAL);

		inputTextColor = leftTextColor = DEFAULT_TEXT_COLOR;

		TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RightInputText, defStyleAttr, 0);
		int count = ta.getIndexCount();
		for (int i=0; i<count; i++){
			int attr = ta.getIndex(i);
			switch (attr){
				case R.styleable.RightInputText_leftText:
					leftText = ta.getString(attr);
					break;
				case R.styleable.RightInputText_inputMarginLeft:
					inputMarginLeft = ta.getDimensionPixelOffset(attr, 0);
					break;
				case R.styleable.RightInputText_inputTips:
					inputTips = ta.getString(attr);
					break;
				case R.styleable.RightInputText_leftTextSize:
					// 默认设置为12sp，TypeValue也可以把sp转化为px
					leftTextSize = ta.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
					break;
				case R.styleable.RightInputText_inputTextSize:
					// 默认设置为12sp，TypeValue也可以把sp转化为px
					inputTextSize = ta.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
					break;
				case R.styleable.RightInputText_leftTextColor:
					leftTextColor = ta.getColor(attr, DEFAULT_TEXT_COLOR);
					break;
				case R.styleable.RightInputText_inputTextColor:
					inputTextColor = ta.getColor(attr, DEFAULT_TEXT_COLOR);
					break;
			}
		}
		ta.recycle();

		TextView leftTv = new TextView(context);
		leftTv.setText(leftText);
		leftTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTextSize);
		leftTv.setTextColor(leftTextColor);
		LayoutParams leftParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		leftParams.gravity = Gravity.CENTER_VERTICAL;
		addView(leftTv, leftParams);

		EditText input = new EditText(context);
		input.setBackground(null);
		if (!TextUtils.isEmpty(inputTips)){
			input.setHint(inputTips);
		}
		input.setTextColor(inputTextColor);
		input.setGravity(Gravity.LEFT| Gravity.CENTER_VERTICAL);
		input.setTextSize(TypedValue.COMPLEX_UNIT_PX, inputTextSize);
		input.setHintTextColor(Color.parseColor("#c5c5c7"));
		LayoutParams inputParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
		inputParams.weight = 1;
		inputParams.gravity = Gravity.CENTER_VERTICAL;
		inputParams.leftMargin = inputMarginLeft;
		addView(input, inputParams);

	}
}
