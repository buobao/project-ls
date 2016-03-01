package com.vocinno.centanet.apputils.selfdefineview.scrolltagviewline;

import java.util.ArrayList;

import com.vocinno.centanet.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class ScrollTagView extends HorizontalScrollView {

	private SuperTagAdapter mTagAdapter;

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

	public void setAdapter(SuperTagAdapter tabAdapter) {
		this.mTagAdapter = tabAdapter;
		initTabs();
	}

	private void initTabs() {
		for (int i = 0; i < mTagAdapter.getCount(); i++) {
			final int position = i;
			View tab = mTagAdapter.getView(i);
			if (i == mTagAdapter.getCount() - 1) {
				tab.setPadding(10, 0, 18, 0);
			}
			mContainer.addView(tab);
			tab.findViewById(R.id.btn_tag_tagViewLine).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							selectedTab(position, true);
						}
					});
		}
		selectedTab(0, true);
	}

	public void selectedTab(int position, boolean isRefreshData) {
		int st = 0;
		boolean tf = false;
		if (position >= 1 && mListlens.size() == 0) {
			tf = true;
		}
		for (int i = 0; i < mContainer.getChildCount(); i++) {
			mContainer.getChildAt(i).setSelected(position == i);
			if (tf) {
				st += mContainer.getChildAt(i).getWidth();
				mListlens.add(st);
			}
		}
		if (position <= 1) {
			smoothScrollTo(0, 0);
		} else {
			int halfCur = mListlens.get(position) - mListlens.get(position - 1);
			smoothScrollTo(mListlens.get(position - 1) - mScreenW / 2 + halfCur / 2
					- 18, 0);
		}
	}

}
