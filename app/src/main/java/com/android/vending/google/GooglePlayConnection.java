package com.android.vending.google;

import java.io.File;

import android.os.Environment;

import com.android.vending.BlankConnection;
import com.gc.android.market.api.MarketSession;

public abstract class GooglePlayConnection extends BlankConnection {
	private MarketSession session;

	private final Integer sessionSync;
	private final String email;
	private final String password;
	private final String androidId;
	private final File cacheDir;
	private final String operatorAlpha;
	private final String operatorNumeric;
	private final String deviceName;
	private final int sdkVersion;

	public static File DEFAULT_CACHE_DIR = new File(
			Environment.getExternalStorageDirectory(), ".nogapps/blankstore");

	public GooglePlayConnection(String email, String password,
			String androidId, File cacheDir, String operatorAlpha,
			String operatorNumeric, String deviceName, int sdkVersion) {
		this.email = email;
		this.password = password;
		this.androidId = androidId;
		this.cacheDir = cacheDir;
		this.operatorAlpha = operatorAlpha;
		this.operatorNumeric = operatorNumeric;
		this.deviceName = deviceName;
		this.sdkVersion = sdkVersion;
		sessionSync = 1;
	}

	File getCacheDir() {
		return cacheDir;
	}

	/**
	 * Gets current MarketSession instance of the DefaultGooglePlayConnection
	 * 
	 * @return active MarketSession if available or null if not
	 */
	public MarketSession getSession() {
		synchronized (getSessionSync()) {
			return getSessionSynced();
		}
	}

	/**
	 * Gets current MarketSession instance of the DefaultGooglePlayConnection or
	 * creates one if needed
	 * 
	 * This shall not be called in ui thread, as it may be blocking and may
	 * requires network and filesystem access
	 * 
	 * @return active MarketSession
	 */
	public MarketSession getSessionBlocked() {
		synchronized (getSessionSync()) {
			if (!isReadySynced()) {
				openConnectionSynced();
			}
			return getSessionSynced();
		}
	}

	public Integer getSessionSync() {
		return sessionSync;
	}

	MarketSession getSessionSynced() {
		return session;
	}

	/**
	 * Check whether connected or not.
	 * 
	 * @return if connection is ready
	 */
	public boolean isReady() {
		synchronized (getSessionSync()) {
			return isReadySynced();
		}
	}

	boolean isReadySynced() {
		return (session != null);
	}

	abstract boolean isSecure();

	void makeDirectoryReady(File dir) {
		if (!dir.exists()) {
			dir.mkdirs();
		}
		if (!dir.exists()) {
			throw new RuntimeException("could not init " + dir + "!");
		}
		if (dir.isFile()) {
			throw new RuntimeException(dir + " must not be a file!");
		}
	}

	/**
	 * Open Connection to Google Play Store
	 * 
	 * This shall not be called in ui thread, as its blocking and requires
	 * network and filesystem access
	 */
	@Override
	public void openConnection() {
		synchronized (getSessionSync()) {
			if (session != null) {
				return;
			}
			openConnectionSynced();
		}
	}

	void openConnectionSynced() {
		makeDirectoryReady(getCacheDir());
		session = new MarketSession(isSecure());
		session.setOperator(operatorAlpha, operatorNumeric);
		session.setDeviceAndSdkVersion(deviceName, sdkVersion);
		session.login(email, password, androidId);
	}
}
