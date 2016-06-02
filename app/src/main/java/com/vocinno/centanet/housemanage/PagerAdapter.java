package com.vocinno.centanet.housemanage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class PagerAdapter extends FragmentStatePagerAdapter {

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
