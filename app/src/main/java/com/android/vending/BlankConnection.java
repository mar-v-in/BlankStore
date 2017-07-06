package com.android.vending;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

import com.android.vending.google.SecureGooglePlayConnection;
import com.gc.android.market.api.model.Market.App;
import com.gc.android.market.api.model.Market.GetImageRequest.AppImageUsage;

public abstract class BlankConnection {
	public boolean hasQueryApp() {
		return false;
	}

	public boolean hasQueryAppDownload() {
		return false;
	}

	public boolean hasQueryAppList() {
		return false;
	}

	public boolean hasQueryImage() {
		return false;
	}

	public void openConnection() {
	};

	public App queryAppByName(String packageName, boolean extendedInfo) {
		final List<String> packageNames = new ArrayList<String>();
		packageNames.add(packageName);
		final List<App> apps = queryAppsByName(packageNames, extendedInfo);
		if (apps == null || apps.size() == 0) {
			return null;
		}
		return apps.get(0);
	}

	public File queryAppDownload(App app,
			SecureGooglePlayConnection.Listener listener) {
		return null;
	}

	public List<App> queryAppList(String query, int start, int num,
			boolean extendedInfo) {
		return null;
	}

	public List<App> queryAppsByName(List<String> packageNames,
			boolean extendedInfo) {
		return null;
	}

	public Bitmap queryImage(App app, String imageId,
			AppImageUsage appImageUsage) {
		return null;
	}
}
