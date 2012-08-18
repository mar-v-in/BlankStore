package com.android.vending.google;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import android.util.Log;

import com.android.vending.account.Account;
import com.gc.android.market.api.model.Market.App;
import com.gc.android.market.api.model.Market.GetAssetResponse.InstallAsset;

public class SecureGooglePlayConnection extends GooglePlayConnection {

	private final static String TAG = "SecureGooglePlay";

	public interface Listener {

		boolean isCancelled();

		void onDownloadProgress(int progress, int max);

	}

	public SecureGooglePlayConnection(Account account) {
		this(account.getLogin(), account.getPassword(), account.getAndroidId(),
				account.getOperatorAlpha(), account.getOperatorNumeric());
	}

	public SecureGooglePlayConnection(String email, String password,
			String androidId, File cacheDir, String operatorAlpha,
			String operatorNumeric) {
		super(email, password, androidId, cacheDir, operatorAlpha,
				operatorNumeric);
	}

	public SecureGooglePlayConnection(String email, String password,
			String androidId, String operatorAlpha, String operatorNumeric) {
		this(email, password, androidId, DEFAULT_CACHE_DIR, operatorAlpha,
				operatorNumeric);
	}

	private InstallAsset getInstallAssetSynced(String packageName) {
		if (!isReadySynced()) {
			openConnectionSynced();
		}
		return getSessionSynced().queryGetAssetRequest(packageName)
				.getInstallAsset(0);
	}

	@Override
	public boolean hasQueryAppDownload() {
		return true;
	}

	@Override
	boolean isSecure() {
		return true;
	}

	@Override
	public File queryAppDownload(App app,
			SecureGooglePlayConnection.Listener listener) {
		Log.d(TAG, "appDownload: " + app.getPackageName());
		final File dir = new File(getCacheDir(), app.getPackageName());
		makeDirectoryReady(dir);
		File file = new File(dir, "APP-" + app.getVersionCode() + ".apk");
		if (file.exists()) {
			return file;
		}
		InstallAsset installAsset;
		synchronized (getSessionSync()) {
			installAsset = getInstallAssetSynced(app.getPackageName());
		}
		file = new File(dir, "APP-" + installAsset.getVersionCode() + ".apk");
		if (file.exists()) {
			return file;
		}
		File part = new File(file.getAbsolutePath()+".part");
		if (part.exists()) {
			part.delete();
		}
		final HttpURLConnection conn = setupConnectionForDownload(installAsset);
		try {
			final InputStream inputstream = conn.getInputStream();
			final BufferedOutputStream stream = new BufferedOutputStream(
					new FileOutputStream(part));
			final int totalSize = conn.getContentLength();
			int downloadedSize = 0;
			final byte buf[] = new byte[1024];
			int bufferlength = 0;
			long lastPublish = 0;
			while ((bufferlength = inputstream.read(buf)) != -1) {
				stream.write(buf, 0, bufferlength);
				downloadedSize += bufferlength;
				final long t = new Date().getTime();
				if (lastPublish + 2000 < t) {
					listener.onDownloadProgress(downloadedSize, totalSize);
					lastPublish = t;
				}
				if (listener.isCancelled()) {
					stream.close();
					return null;
				}

			}
			inputstream.close();
			stream.close();
			part.renameTo(file);
			return file;
		} catch (final Exception e) {
			Log.d(TAG, "appDownload", e);
			return null;
		}

	}

	private HttpURLConnection setupConnectionForDownload(
			InstallAsset installAsset) {
		try {
			final HttpURLConnection conn = (HttpURLConnection) new URL(
					installAsset.getBlobUrl()).openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("User-Agent",
					"Android-Market/2 (sapphire PLAT-RC33); gzip");
			conn.setRequestProperty("Cookie",
					installAsset.getDownloadAuthCookieName() + "="
							+ installAsset.getDownloadAuthCookieValue());
			return conn;
		} catch (final Exception e) {
			Log.d("SecureGooglePlay", "connectDownload", e);
			return null;
		}
	}

}
