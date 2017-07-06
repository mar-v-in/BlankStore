package com.android.vending.fragments;

import java.io.File;
import java.util.List;

import android.support.v4.app.Fragment;

import com.android.vending.BlankListener;
import com.gc.android.market.api.LoginException;
import com.gc.android.market.api.model.Market.App;

public class BlankFragment extends Fragment implements BlankListener {

	@Override
	public void onDownloadAppDone(App app, File file) {

	}

	@Override
	public void onDownloadAppFailed(App app) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDownloadAppProgess(App app, int progress, int max) {

	}

	@Override
	public void onInstallAppDone(App app) {

	}

	@Override
	public void onInstallAppStarted(App app) {

	}

	@Override
	public void onLoginException(LoginException ex) {

	}

	@Override
	public void onQueryAppListResult(List<App> appList) {

	}

	@Override
	public void onQueryAppResult(App app) {

	}

	@Override
	public void onQueryAppsResult(List<App> apps) {

	}

	@Override
	public void onUninstallAppDone(App app) {

	}

	@Override
	public void onUninstallAppStarted(App app) {

	}

}
