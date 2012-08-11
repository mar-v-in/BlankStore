package com.android.vending.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.vending.BlankActivity;
import com.android.vending.R;

public class StartFragment extends BlankFragment implements OnClickListener {
	private Button apps;
	private Button search;

	@Override
	public void onClick(View v) {
		if (v == search) {
			((BlankActivity) getActivity()).selectSearch();
		} else {
			((BlankActivity) getActivity()).openInsalled();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.start, null);
		apps = (Button) view.findViewById(R.id.btn_apps);
		apps.setOnClickListener(this);
		search = (Button) view.findViewById(R.id.btn_search);
		search.setOnClickListener(this);

		return view;
	}
}
