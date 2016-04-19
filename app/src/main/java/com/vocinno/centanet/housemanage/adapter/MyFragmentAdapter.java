package com.vocinno.centanet.housemanage.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class MyFragmentAdapter extends FragmentStatePagerAdapter {
	private List<Fragment>list;
	public MyFragmentAdapter(FragmentManager fm) {
		super(fm);
	}
	public void setFragmentList(List<Fragment>list){
		this.list=list;
	}
	@Override
	public Fragment getItem(int position) {
		return list.get(position);
	}

	@Override
	public int getCount() {
		return this.list==null?0:this.list.size();
	}
}
