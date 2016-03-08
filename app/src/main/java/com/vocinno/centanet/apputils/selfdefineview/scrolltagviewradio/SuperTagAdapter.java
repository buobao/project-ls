package com.vocinno.centanet.apputils.selfdefineview.scrolltagviewradio;

import java.util.ArrayList;
import java.util.List;

import android.view.View;

public abstract class SuperTagAdapter {
	List<String> mListTabs = new ArrayList<String>();

	public abstract View getView(int position);

	public int getCount() {
		return mListTabs.size();
	}

	public void add(String name) {
		mListTabs.add(name);
	}

}
