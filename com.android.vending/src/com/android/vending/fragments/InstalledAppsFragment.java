package com.android.vending.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
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

public class InstalledAppsFragment extends BlankFragment implements
		OnItemClickListener {
	private AppsAdapter adapter;
	private HashMap<String, App> appList;
	private ListView list;

	private final static String APP_SAVE = InstalledAppsFragment.class
			.getName() + ".apps";

	private ArrayList<App> getList() {
		final ArrayList<App> apps = new ArrayList<App>(appList.values());

		Collections.sort(apps, new Comparator<App>() {

			@Override
			public int compare(App lhs, App rhs) {
				if (rhs == null) {
					return -1;
				} else if (lhs == null) {
					return 1;
				}
				final PackageManager pm = getActivity().getPackageManager();
				int appVersionLhs = -1;
				int appVersionRhs = -1;
				try {
					PackageInfo info = pm.getPackageInfo(lhs.getPackageName(),
							0);
					appVersionLhs = info.versionCode;
					info = pm.getPackageInfo(rhs.getPackageName(), 0);
					appVersionRhs = info.versionCode;
				} catch (final NameNotFoundException e) {
				}
				if (appVersionLhs < lhs.getVersionCode()
						&& (!(appVersionRhs < rhs.getVersionCode()) && (rhs
								.getPrice() == null || rhs.getPrice().isEmpty()))
						&& (lhs.getPrice() == null || lhs.getPrice().isEmpty())) {
					return -1;
				} else if (appVersionRhs < rhs.getVersionCode()
						&& (!(appVersionLhs < lhs.getVersionCode()) && (lhs
								.getPrice() == null || lhs.getPrice().isEmpty()))
						&& (rhs.getPrice() == null || rhs.getPrice().isEmpty())) {
					return 1;
				} else {
					return lhs.getTitle().compareToIgnoreCase(rhs.getTitle());
				}
			}
		});

		return apps;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appList = new HashMap<String, App>();
		adapter = new AppsAdapter(getActivity(), R.layout.appentry);
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(APP_SAVE)) {
			try {
				@SuppressWarnings("unchecked")
				final ArrayList<App> apps = (ArrayList<App>) savedInstanceState
						.getSerializable(APP_SAVE);
				onQueryAppsResult(apps);
				return;
			} catch (final Exception ex) {
				Log.w("InstalledAppsFragment", "savedInstanceState invalid!");
			}
		}
		final PackageManager pm = getActivity().getPackageManager();
		final List<PackageInfo> packages = pm.getInstalledPackages(0);
		final List<String> packageNames = new ArrayList<String>();
		for (final PackageInfo packageInfo : packages) {
				packageNames.add(packageInfo.packageName);
		}
		((BlankActivity) getActivity()).getApps(packageNames, false);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View view = inflater.inflate(R.layout.applist, null);
		if (appList.size() > 0) {
			view.findViewById(R.id.prog_loading).setVisibility(View.GONE);
		}
		list = (ListView) view.findViewById(R.id.lst_apps);
		list.setOnItemClickListener(this);
		adapter.setAppList(getList());
		list.setAdapter(adapter);
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> listView, View selectedView,
			int position, long id) {
		((BlankActivity) getActivity()).goAppDetails(getList().get(position)
				.getPackageName());
	}

	@Override
	public void onQueryAppsResult(List<App> apps) {
		getView().findViewById(R.id.prog_loading).setVisibility(View.GONE);
		for (final App app : apps) {
			appList.put(app.getPackageName(), app);
		}
		adapter.setAppList(getList());
	}

	@Override
	public void onResume() {
		super.onResume();
		// appList.clear();

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (appList != null) {
			outState.putSerializable(APP_SAVE, getList());
		}
	}
}
