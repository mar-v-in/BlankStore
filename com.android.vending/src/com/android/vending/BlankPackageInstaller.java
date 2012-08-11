package com.android.vending;

import java.io.File;

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

	public void installPackage(App app, File file,
			InstallCallback installCallback) {
		int installFlags = 0;
		final PackageManager pm = context.getPackageManager();
		try {
			final PackageInfo pi = pm.getPackageInfo(app.getPackageName(),
					PackageManager.GET_UNINSTALLED_PACKAGES);
			if (pi != null) {
				installFlags |= PackageManager.INSTALL_REPLACE_EXISTING;
			}
		} catch (final NameNotFoundException e) {
		}
		if ((installFlags & PackageManager.INSTALL_REPLACE_EXISTING) != 0) {
			Log.d(TAG, "Replacing package: " + app.getPackageName());
		}
		installCallback.installStarted(app);
		pm.installPackage(Uri.fromFile(file), new InstallObserver(app,
				installCallback), installFlags, PACKAGE_NAME);
	}

	public void installPackage(App app, InstallCallback installCallback) {
		final File dir = new File(cacheDir, app.getPackageName());
		final File file = new File(dir, "APP-" + app.getVersionCode() + ".apk");
		installPackage(app, file, installCallback);
	}

	public void uninstallPackage(App app, UninstallCallback uninstallCallback) {
		final int uninstallFlags = 0;
		final PackageManager pm = context.getPackageManager();
		uninstallCallback.uninstallStarted(app);
		pm.deletePackage(app.getPackageName(), new UninstallObserver(app,
				uninstallCallback), uninstallFlags);
	}

}
