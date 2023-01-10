package com.wyze.sandglasslibrary.functionmoudle.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SLFFragmentAdapter extends FragmentPagerAdapter {
	private String[] mTitle;
	private Fragment[] fragments;

	public SLFFragmentAdapter(FragmentManager fm, Fragment[] fragments, String[] pageNames) {
		super(fm);
		this.fragments = fragments;
		this.mTitle = pageNames;
	}

	@Override
	public Fragment getItem(int i) {
		return fragments[i];
	}


	@Override
	public int getCount() {
		return fragments.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mTitle[position];
	}
}