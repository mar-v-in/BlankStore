package com.android.vending.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.vending.BlankActivity;
import com.android.vending.R;

public class StartFragment extends BlankFragment implements OnClickListener {
	private Button apps;
	private Button search;
	private TextView searchText;

	@Override
	public void onClick(View v) {
		if (v == search) {
			((BlankActivity) getActivity()).startSearch(searchText.getText().toString());
		} else {
			((BlankActivity) getActivity()).openInstalled();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.start, null);
		apps = (Button) view.findViewById(R.id.btn_apps);
		apps.setOnClickListener(this);
		if (Build.VERSION.SDK_INT <= 10) {
			search = (Button) view.findViewById(R.id.btn_search);
			search.setOnClickListener(this);
			searchText = (TextView) view.findViewById(R.id.compatSearchText);
			view.findViewById(R.id.compatSearchField).setVisibility(View.VISIBLE);
		}

		return view;
	}
}
