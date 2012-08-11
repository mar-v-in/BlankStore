package com.android.vending;

import java.io.File;
import java.util.List;

import com.gc.android.market.api.LoginException;
import com.gc.android.market.api.model.Market.App;

public interface BlankListener {
	void onDownloadAppDone(App app, File file);

	void onDownloadAppProgess(App app, int progress, int max);

	void onInstallAppDone(App app);

	void onInstallAppStarted(App app);

	void onLoginException(LoginException ex);

	void onQueryAppListResult(List<App> appList);

	void onQueryAppResult(App app);

	void onQueryAppsResult(List<App> apps);

	void onUninstallAppDone(App app);

	void onUninstallAppStarted(App app);
}
