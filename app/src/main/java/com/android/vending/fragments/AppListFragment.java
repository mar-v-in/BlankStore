package com.android.vending.fragments;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.vending.BlankActivity;
import com.android.vending.R;
import com.android.vending.adapter.AppsAdapter;
import com.gc.android.market.api.model.Market.App;

public class AppListFragment extends BlankFragment implements
		OnItemClickListener {
	private AppsAdapter adapter;
	private List<App> appList;

	private void makeView(final View view) {
		view.findViewById(R.id.prog_loading).setVisibility(View.GONE);
		adapter.setAppList(appList);
		final ListView list = (ListView) view.findViewById(R.id.lst_apps);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.applist, null);
		if (adapter == null) {
			adapter = new AppsAdapter(getActivity(), R.layout.appentry);
		}
		if (appList != null) {
			makeView(view);
		}
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> listView, View selectedView,
			int position, long id) {
		((BlankActivity) getActivity()).goAppDetails(appList.get(position)
				.getPackageName());
	}

	@Override
	public void onQueryAppListResult(List<App> appList) {
		setAppList(appList);
	}

	public void setAppList(List<App> appList) {
		this.appList = appList;
		if (adapter != null) {
			adapter.setAppList(appList);
		} else {
			adapter = new AppsAdapter(getActivity(), R.layout.appentry);
			adapter.setAppList(appList);
		}
		if (getView() != null) {
			makeView(getView());
		}
	}
}
