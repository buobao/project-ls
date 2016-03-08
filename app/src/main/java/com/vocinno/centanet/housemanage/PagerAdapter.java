package com.vocinno.centanet.housemanage;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

	private final ArrayList<Fragment> mListFragments = new ArrayList<Fragment>();

	public PagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public void addFragment(Fragment fg) {
		mListFragments.add(fg);
	}

	@Override
	public Fragment getItem(int position) {
		return mListFragments.get(position);
	}

	@Override
	public int getCount() {
		return mListFragments.size();
	}

}
