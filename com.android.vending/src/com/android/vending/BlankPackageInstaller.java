package com.android.vending;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.SecurityException;
import java.lang.reflect.InvocationTargetException;

import android.content.Context;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.RemoteException;
import android.util.Log;

import com.gc.android.market.api.model.Market.App;

public class BlankPackageInstaller {

	// internal flags from PackageManager.java
	public static final int INSTALL_FORWARD_LOCK = 0x00000001;
	public static final int INSTALL_REPLACE_EXISTING = 0x00000002;
	public static final int INSTALL_ALLOW_TEST = 0x00000004;
	public static final int INSTALL_EXTERNAL = 0x00000008;
	public static final int INSTALL_INTERNAL = 0x00000010;
	public static final int INSTALL_FROM_ADB = 0x00000020;
	public static final int INSTALL_ALL_USERS = 0x00000040;
	public static final int INSTALL_ALLOW_DOWNGRADE = 0x00000080;

	private Method mInstallPackageMethod;
	private Method mDeletePackageMethod;
	private boolean mReflectionSuccessful;
	private boolean mReflectionDone = false;

	public static final int RPC_SUCCESS = 0;
	public static final int RPC_NO_PERMISSION = 1;
	public static final int RPC_EXCEPTION = 2;

	public interface InstallCallback {
		void installDone(App app);

		void installStarted(App app);

		void run(Runnable runnable);
	}

	private class InstallObserver extends IPackageInstallObserver.Stub {
		private final InstallCallback installCallback;
		private final App app;

		public InstallObserver(App app, InstallCallback installCallback) {
			this.installCallback = installCallback;
			this.app = app;
		}

		@Override
		public void packageInstalled(String arg0, int arg1)
				throws RemoteException {
			Log.d(TAG, "Finished installing " + app.getPackageName() + "("
					+ arg0 + ", " + arg1 + ")");
			installCallback.run(new Runnable() {

				@Override
				public void run() {
					installCallback.installDone(app);
				}
			});
		}

	}

	public interface UninstallCallback {
		void run(Runnable runnable);

		void uninstallDone(App app);

		void uninstallStarted(App app);
	}

	private class UninstallObserver extends IPackageDeleteObserver.Stub {
		private final UninstallCallback uninstallCallback;
		private final App app;

		public UninstallObserver(App app, UninstallCallback uninstallCallback) {
			this.uninstallCallback = uninstallCallback;
			this.app = app;
		}

		@Override
		public void packageDeleted(String arg0, int arg1)
				throws RemoteException {
			Log.d(TAG, "Finished uninstalling " + app.getPackageName() + "("
					+ arg0 + ", " + arg1 + ")");
			uninstallCallback.run(new Runnable() {

				@Override
				public void run() {
					uninstallCallback.uninstallDone(app);
				}
			});
		}

	}

	private static final String TAG = "BlankPackageInstaller";

	private final File cacheDir;

	private final Context context;

	private static final String PACKAGE_NAME = "com.android.vending";

	public BlankPackageInstaller(Context context) {
		this(context, new File(Environment.getExternalStorageDirectory(),
				".nogapps/blankstore"));
	}

	public BlankPackageInstaller(Context context, File cacheDir) {
		this.cacheDir = cacheDir;
		this.context = context;
	}

	public boolean checkPackageManagerMethods() {
		if (mReflectionDone) {
			return mReflectionSuccessful;
		}

		PackageManager pm = context.getPackageManager();
		Method[] list = pm.getClass().getDeclaredMethods();
		for (Method m: list) {
			if (m.getName().equals("installPackage")) {
				mInstallPackageMethod = m;
			} else if (m.getName().equals("deletePackage")) {
				mDeletePackageMethod = m;
			}
		}

		if (mInstallPackageMethod != null && mDeletePackageMethod != null) {
			mReflectionSuccessful = true;
		} else {
			mReflectionSuccessful = false;
			Log.w(TAG, "Could not look up internal PackageManager APIs");
		}

		mReflectionDone = true;
		return mReflectionSuccessful;
	}

	private int handleReflectException(String op, Throwable e) {
		if (e instanceof InvocationTargetException) {
			Throwable cause = e.getCause();
			if (cause != null) {
				e = cause;
			}
		}
		if (e instanceof SecurityException) {
			// this just means we're not a system app; non fatal.
			// Normally InvocationTargetException is the first
			// link of the exception chain.
			Log.i(TAG, op + ": permission denied");
			return RPC_NO_PERMISSION;
		}
		Log.e(TAG, op + ": caught exception", e);
		return RPC_EXCEPTION;
	}

	private int callInstallPackage(final PackageManager pm, final Uri packageURI,
			final IPackageInstallObserver observer, final int flags,
			final String installerPackageName) {
		try {
			mInstallPackageMethod.invoke(pm, packageURI, observer, flags, installerPackageName);
			return RPC_SUCCESS;
		} catch (Exception e) {
			return handleReflectException("installPackage", e);
		}
	}

	private int callDeletePackage(final PackageManager pm, final String packageName,
			final IPackageDeleteObserver observer, final int flags) {
		try {
			mDeletePackageMethod.invoke(pm, packageName, observer, flags);
			return RPC_SUCCESS;
		} catch (Exception e) {
			return handleReflectException("deletePackage", e);
		}
	}

	public void installPackage(App app, File file,
			InstallCallback installCallback) {
		if (checkPackageManagerMethods() == false) {
			return;
		}
		if (!file.exists()) {
			Log.d(TAG, "Could not install " + app.getPackageName()
					+ "! File does not exist: " + file.getAbsolutePath());
			installCallback.installDone(app);
			return;
		}
		int installFlags = 0;
		final PackageManager pm = context.getPackageManager();
		try {
			final PackageInfo pi = pm.getPackageInfo(app.getPackageName(),
					PackageManager.GET_UNINSTALLED_PACKAGES);
			if (pi != null) {
				installFlags |= INSTALL_REPLACE_EXISTING;
			}
		} catch (final NameNotFoundException e) {
		}
		if ((installFlags & INSTALL_REPLACE_EXISTING) != 0) {
			Log.d(TAG, "Replacing package: " + app.getPackageName());
		}
		installCallback.installStarted(app);
		callInstallPackage(pm, Uri.fromFile(file), new InstallObserver(app, installCallback),
				installFlags, PACKAGE_NAME);
	}

	public void installPackage(App app, InstallCallback installCallback) {
		final File dir = new File(cacheDir, app.getPackageName());
		final File file = new File(dir, "APP-" + app.getVersionCode() + ".apk");
		installPackage(app, file, installCallback);
	}

	public void uninstallPackage(App app, UninstallCallback uninstallCallback) {
		if (checkPackageManagerMethods() == false) {
			return;
		}
		final int uninstallFlags = 0;
		final PackageManager pm = context.getPackageManager();
		uninstallCallback.uninstallStarted(app);
		callDeletePackage(pm, app.getPackageName(),
				new UninstallObserver(app, uninstallCallback), uninstallFlags);
	}

}
