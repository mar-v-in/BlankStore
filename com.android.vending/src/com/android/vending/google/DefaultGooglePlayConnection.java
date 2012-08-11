package com.android.vending.google;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.android.vending.Cache;
import com.android.vending.Reference;
import com.android.vending.account.Account;
import com.gc.android.market.api.MarketSession.Callback;
import com.gc.android.market.api.model.Market.App;
import com.gc.android.market.api.model.Market.AppsRequest;
import com.gc.android.market.api.model.Market.AppsResponse;
import com.gc.android.market.api.model.Market.GetImageRequest;
import com.gc.android.market.api.model.Market.GetImageRequest.AppImageUsage;
import com.gc.android.market.api.model.Market.GetImageResponse;
import com.gc.android.market.api.model.Market.ResponseContext;

public class DefaultGooglePlayConnection extends GooglePlayConnection {

	private static final String TAG = "DefaultGooglePlayConnection";

	private static final int APP_CACHE_SIZE = 500;
	private static final int IMAGE_CACHE_SIZE = 500;

	private final Cache<String, App> appList;
	private final Cache<String, Bitmap> imageCache;

	private static final int CACHE_DAYS = 14;
	private static final long MILLISECONDS_A_DAY = 1000 * 60 * 60 * 24;

	public DefaultGooglePlayConnection(Account account) {
		this(account.getLogin(), account.getPassword(), account.getAndroidId(),
				account.getOperatorAlpha(), account.getOperatorNumeric());
	}

	public DefaultGooglePlayConnection(String email, String password,
			String androidId, File cacheDir, String operatorAlpha,
			String operatorNumeric) {
		super(email, password, androidId, cacheDir, operatorAlpha,
				operatorNumeric);
		appList = new Cache<String, App>(APP_CACHE_SIZE);
		imageCache = new Cache<String, Bitmap>(IMAGE_CACHE_SIZE);
	}

	public DefaultGooglePlayConnection(String email, String password,
			String androidId, String operatorAlpha, String operatorNumeric) {
		this(email, password, androidId, DEFAULT_CACHE_DIR, operatorAlpha,
				operatorNumeric);
	}

	private void addAppToCache(App app) {
		if (app == null) {
			return;
		}
		synchronized (appList) {
			appList.put(app.getPackageName(), fillAppWithExtendedInfo(app));
		}
	}

	private void addAppToCache(List<App> result) {
		for (final App app : result) {
			addAppToCache(app);
		}
	}

	private void addImageToCache(App app, String identifier, Bitmap bitmap) {
		addImageToCache(app.getPackageName(), identifier, bitmap);
	}

	private void addImageToCache(String packageName, String identifier,
			Bitmap bitmap) {
		imageCache.put(packageName + ":" + identifier, bitmap);
	}

	private GetImageRequest createMarketImageRequest(String appId,
			String imageId, AppImageUsage appImageUsage) {
		final GetImageRequest.Builder requestBuilder = GetImageRequest
				.newBuilder();
		requestBuilder.setAppId(appId);
		requestBuilder.setImageId(imageId);
		requestBuilder.setImageUsage(appImageUsage);
		final GetImageRequest request = requestBuilder.build();
		return request;
	}

	private AppsRequest createQueryAppsRequest(String query, int start,
			int num, boolean extendedInfo) {
		final AppsRequest.Builder requestBuilder = AppsRequest.newBuilder();
		requestBuilder.setQuery(query);
		requestBuilder.setStartIndex(start);
		requestBuilder.setEntriesCount(num);
		requestBuilder.setWithExtendedInfo(extendedInfo);
		final AppsRequest request = requestBuilder.build();
		return request;
	}

	private List<App> extractAppListFromAppsResponse(List<Object> responseList) {
		if (responseList.size() >= 1
				&& responseList.get(0) instanceof AppsResponse) {
			final AppsResponse response = (AppsResponse) responseList.get(0);
			final List<App> result = response.getAppList();
			if (result != null) {
				addAppToCache(result);
			}
			return result;
		}
		return null;
	}

	private App fillAppWithExtendedInfo(App app) {
		if (app == null) {
			return null;
		}
		if (app.hasExtendedInfo() || getExtendedInfo(app) == null) {
			return app;
		}
		return app.toBuilder().setExtendedInfo(getExtendedInfo(app)).build();
	}

	private App getAppFromCache(String packageName) {
		synchronized (appList) {
			return appList.get(packageName);
		}
	}

	private App.ExtendedInfo getExtendedInfo(App app) {
		if (app == null) {
			return null;
		}
		if (app.hasExtendedInfo()) {
			return app.getExtendedInfo();
		}
		return getExtendedInfoFromCache(app.getPackageName());
	}

	private App.ExtendedInfo getExtendedInfoFromCache(String packageName) {
		final App app = getAppFromCache(packageName);
		if (app != null && app.hasExtendedInfo()) {
			return app.getExtendedInfo();
		}
		return null;
	}

	private Bitmap getImageFromCache(App app, String identifier) {
		return getImageFromCache(app.getPackageName(), identifier);
	}

	private Bitmap getImageFromCache(String packageName, String identifier) {
		return imageCache.get(packageName + ":" + identifier);
	}

	private Bitmap getMarketImageFromFile(File file) {
		if (file.exists()) {
			final long lastModified = file.lastModified();
			final long now = new Date().getTime();
			if (now - lastModified < CACHE_DAYS * MILLISECONDS_A_DAY) {
				return BitmapFactory.decodeFile(file.getAbsolutePath());
			}
		}
		return null;
	}

	@Override
	public boolean hasQueryApp() {
		return true;
	}

	@Override
	public boolean hasQueryAppList() {
		return true;
	}

	@Override
	public boolean hasQueryImage() {
		return true;
	}

	@Override
	boolean isSecure() {
		return false;
	}

	@Override
	public List<App> queryAppList(String query, int start, int num,
			boolean extendedInfo) {
		final AppsRequest request = createQueryAppsRequest(query, start, num,
				extendedInfo);
		List<Object> responseList;
		Log.d(TAG, "queryeAppList: " + query + "[START]");
		responseList = requestApps(request);
		Log.d(TAG, "queryeAppList: " + query + "[DONE]");
		return extractAppListFromAppsResponse(responseList);
	}

	@Override
	public List<App> queryAppsByName(List<String> packageNames,
			boolean extendedInfo) {
		final List<App> results = new ArrayList<App>();
		final List<AppsRequest> toRetrieve = new ArrayList<AppsRequest>();
		final Callback<AppsResponse> callback = new Callback<AppsResponse>() {
			@Override
			public void onResult(ResponseContext context, AppsResponse response) {
				if (response.getAppCount() > 0) {
					results.add(response.getApp(0));
					Log.d("DefaultGooglePlayConnection",
							"queryAppsByName: pname:"
									+ response.getApp(0).getPackageName()
									+ " [DONE]");
				}
			}
		};
		for (final String packageName : packageNames) {
			final App app = getAppFromCache(packageName);
			if (app != null && (!extendedInfo || app.hasExtendedInfo())) {
				results.add(app);
			} else {
				final AppsRequest request = createQueryAppsRequest("pname:"
						+ packageName, 0, 1, extendedInfo);
				toRetrieve.add(request);
			}
		}
		for (final AppsRequest request : toRetrieve) {
			Log.d("DefaultGooglePlayConnection",
					"queryAppsByName: " + request.getQuery() + " [START]");
			synchronized (getSessionSync()) {
				startAppsRequestSynced(request, callback);
				getSession().flush();
			}
		}
		addAppToCache(results);
		return results;
	}

	/**
	 * Retrieves a specific image from Google Play Store
	 * 
	 * @param appId
	 *            App to retrieve image for
	 * @param imageId
	 *            Id of the image to retrieve
	 * @param appImageUsage
	 *            Type of the image to retrieve
	 */
	@Override
	public Bitmap queryImage(App app, String imageId,
			AppImageUsage appImageUsage) {
		final File dir = new File(getCacheDir(), app.getPackageName());
		makeDirectoryReady(dir);
		final String identifier = appImageUsage.toString() + "-" + imageId;
		final File file = new File(dir, identifier + ".png");
		final Reference<Bitmap> bitmap = new Reference<Bitmap>(
				getImageFromCache(app, identifier));
		if (!bitmap.isNull()) {
			return bitmap.get();
		}
		bitmap.set(getMarketImageFromFile(file));
		if (!bitmap.isNull()) {
			addImageToCache(app, identifier, bitmap.get());
			return bitmap.get();
		}
		final GetImageRequest request = createMarketImageRequest(app.getId(),
				imageId, appImageUsage);
		final Callback<GetImageResponse> callback = new Callback<GetImageResponse>() {

			@Override
			public void onResult(ResponseContext context,
					GetImageResponse response) {
				writeImageResponseToCache(file, response);
				bitmap.set(getMarketImageFromFile(file));
				synchronized (bitmap) {
					bitmap.notify();
				}
			}
		};
		startGetImageRequest(request, callback);
		if (!bitmap.isNull()) {
			if (appImageUsage == AppImageUsage.ICON
					|| appImageUsage == AppImageUsage.SCREENSHOT_THUMBNAIL) {
				addImageToCache(app, identifier, bitmap.get());
			}
		}
		return bitmap.get();
	}

	private List<Object> requestApps(final AppsRequest request) {
		List<Object> responseList;
		synchronized (getSessionSync()) {
			responseList = requestAppsSynced(request);
		}
		return responseList;
	}

	private List<Object> requestAppsSynced(AppsRequest request) {
		if (!isReadySynced()) {
			openConnectionSynced();
		}
		List<Object> responseList;
		responseList = getSessionSynced().queryApp(request);
		return responseList;
	}

	private void startAppsRequestSynced(AppsRequest request,
			Callback<AppsResponse> callback) {
		if (!isReadySynced()) {
			openConnectionSynced();
		}
		getSessionSynced().append(request, callback);
		getSessionSynced().flush();
	}

	private void startGetImageRequest(final GetImageRequest request,
			final Callback<GetImageResponse> callback) {
		synchronized (getSessionSync()) {
			startGetImageRequestSynced(request, callback);
		}
	}

	private void startGetImageRequestSynced(GetImageRequest request,
			Callback<GetImageResponse> callback) {
		if (!isReadySynced()) {
			openConnectionSynced();
		}
		getSessionSynced().append(request, callback);
		getSessionSynced().flush();
	}

	private void writeImageResponseToCache(File file, GetImageResponse response) {
		try {
			final FileOutputStream fos = new FileOutputStream(file);
			fos.write(response.getImageData().toByteArray());
			fos.close();
		} catch (final Exception ex) {
		}
	}

}
