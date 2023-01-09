package com.wyze.sandglasslibrary.functionmoudle.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wyze.sandglasslibrary.R;

@SuppressLint("ValidFragment")
public class SLFFeedbackAllHistoryFragment extends Fragment {
	private String textString;

	public SLFFeedbackAllHistoryFragment(String textString) {
		this.textString = textString;
	}

	public static SLFFeedbackAllHistoryFragment newInstance(String textString) {
		SLFFeedbackAllHistoryFragment mFragment = new SLFFeedbackAllHistoryFragment(textString);
		return mFragment;	
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.slf_list_history_feedback, container, false);
		return view;
	}
}
