package com.vocinno.centanet.apputils.selfdefineview.scrolltagviewradio;

import com.vocinno.centanet.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;

public class ScrollTagViewAdapter extends SuperTagAdapter {
	private Activity mActivity;

	public ScrollTagViewAdapter(Activity activity) {
		super();
		this.mActivity = activity;
	}

	@Override
	public View getView(int position) {
		LayoutInflater inflater = (LayoutInflater) mActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.scroll_tag_view_radio, null);
		RadioButton button = (RadioButton) v
				.findViewById(R.id.btn_tag_tagViewRadio);
		button.setText(mListTabs.get(position));
		return v;
	}

}
