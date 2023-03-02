package com.sandglass.sandglasslibrary.functionmoudle.adapter;

import android.text.SpannableStringBuilder;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sandglass.sandglasslibrary.commonapi.SLFApi;
import com.sandglass.sandglasslibrary.theme.SLFFontSet;

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
		return SLFFontSet.setStringFont(SLFApi.getSLFContext(),mTitle[position]);
	}

	@Override
	public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
		//super.destroyItem(container, position, object);
	}
}