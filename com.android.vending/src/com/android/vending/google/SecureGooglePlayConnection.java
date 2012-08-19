package com.android.vending.google;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.util.Date;

import android.util.Log;

import com.android.vending.account.Account;
import com.gc.android.market.api.model.Market.App;
import com.gc.android.market.api.model.Market.GetAssetResponse.InstallAsset;

public class SecureGooglePlayConnection extends GooglePlayConnection {

	public interface Listener {

		boolean isCancelled();

		void onDownloadProgress(int progress, int max);

	}

	private final static String TAG = "SecureGooglePlay";

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

	private File downloadPart(InstallAsset installAsset, File file,
			SecureGooglePlayConnection.Listener listener, int originalSize) {
		final File part = new File(file.getAbsolutePath() + ".part");
		if (originalSize == 0 && part.exists()) {
			part.delete();
		}
		int downloadedSize = (int) part.length();
		final HttpURLConnection conn = setupConnectionForDownload(installAsset,
				downloadedSize);
		InputStream inputstream = null;
		BufferedOutputStream stream = null;
		try {
			inputstream = conn.getInputStream();
			stream = new BufferedOutputStream(new FileOutputStream(part, true));
			final int totalSize = downloadedSize
					+ (int) ((conn.getContentLength() != -1) ? conn
							.getContentLength() : installAsset.getAssetSize());
			final byte buf[] = new byte[1024];
			int bufferlength = 0;
			long lastPublish = 0;
			double lastPercent = 0;
			while ((bufferlength = inputstream.read(buf)) != -1) {
				stream.write(buf, 0, bufferlength);
				downloadedSize += bufferlength;
				final long t = new Date().getTime();
				final double percent = (double) downloadedSize
						/ (double) totalSize;
				if (percent < 1
						&& (lastPublish + 1000 < t || percent > lastPercent + 0.05)) {
					listener.onDownloadProgress(downloadedSize, totalSize);
					lastPublish = t;
					lastPercent = percent;
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
		} catch (final SocketException e) {
			Log.d(TAG, "appDownload", e);
			if (inputstream != null) {
				try {
					inputstream.close();
				} catch (final IOException ex) {
				}
			}
			if (stream != null) {
				try {
					stream.close();
				} catch (final IOException ex) {
				}
			}
			if (downloadedSize > originalSize) {
				Log.d(TAG, "appDownload: nevermind - tryin to continue");
				return downloadPart(installAsset, file, listener,
						downloadedSize);
			} else {
				Log.d(TAG,
						"appDownload: uhm - continue didn't help - than bail out!");
				return null;
			}
		} catch (final Exception e) {
			Log.d(TAG, "appDownload", e);
			return null;
		}
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
		return downloadPart(installAsset, file, listener, 0);
	}

	private HttpURLConnection setupConnectionForDownload(
			InstallAsset installAsset, int downloadedSize) {
		try {
			final HttpURLConnection conn = (HttpURLConnection) new URL(
					installAsset.getBlobUrl()).openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("User-Agent",
					"Android-Market/2 (sapphire PLAT-RC33); gzip");
			conn.setRequestProperty("Cookie",
					installAsset.getDownloadAuthCookieName() + "="
							+ installAsset.getDownloadAuthCookieValue());
			if (downloadedSize > 0) {
				conn.setRequestProperty("Range", "bytes=" + downloadedSize
						+ "-");
			}
			return conn;
		} catch (final Exception e) {
			Log.d("SecureGooglePlay", "connectDownload", e);
			return null;
		}
	}

}
