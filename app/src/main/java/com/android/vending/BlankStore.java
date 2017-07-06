package com.android.vending;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.net.Uri;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ImageView;

import com.android.vending.BlankPackageInstaller.InstallCallback;
import com.android.vending.BlankPackageInstaller.UninstallCallback;
import com.android.vending.tasks.AppDownloadTask;
import com.android.vending.tasks.AppListTask;
import com.android.vending.tasks.AppTask;
import com.android.vending.tasks.AppsTask;
import com.android.vending.tasks.ImageToViewTask;
import com.gc.android.market.api.LoginException;
import com.gc.android.market.api.model.Market.App;
import com.gc.android.market.api.model.Market.GetImageRequest.AppImageUsage;

public class BlankStore implements BlankListener, InstallCallback,
		UninstallCallback {

	private final List<BlankConnection> connections;
	private final List<BlankListener> listeners;
	private final BlankPackageInstaller installer;
	private final Activity activity;
	private NotificationManager notificationManager;
	private static final int APPLIST_QUERY_DEFAULT_SIZE = 10;

	private Date firstPublish;

	public BlankStore(Activity activity) {
		connections = new ArrayList<BlankConnection>();
		listeners = new ArrayList<BlankListener>();
		listeners.add(this);
		this.activity = activity;
		installer = new BlankPackageInstaller(activity);
	}

	public void addBlankConnection(BlankConnection blankConnection) {
		connections.add(blankConnection);
	}

	public void addListener(BlankListener listener) {
		listeners.add(listener);
	}

	private Notification buildDownloadNotification(App app, int progress,
			int max) {
		final NotificationCompat.Builder builder = new NotificationCompat.Builder(
				activity);

		if (firstPublish == null) {
			firstPublish = new Date();
		}
		builder.setOngoing(true);
		builder.setContentTitle(activity
				.getText(R.string.download_notify_title));
		builder.setSmallIcon(android.R.drawable.stat_sys_download);
		builder.setProgress(max, progress, false);
		builder.setContentInfo(Utils.niceSize(progress) + " / "
				+ Utils.niceSize(max));
		builder.setWhen(firstPublish.getTime());
		builder.setContentIntent(PendingIntent.getActivity(activity, 0,
				new Intent(), 0));

		builder.setContentText(app.getTitle() + " " + app.getVersion());
		return builder.build();
	}

	private Notification buildFailedNotification(App app) {
		final NotificationCompat.Builder builder = new NotificationCompat.Builder(
				activity);
		builder.setAutoCancel(true);
		builder.setContentTitle(activity
				.getText(R.string.download_notify_failed));
		builder.setSmallIcon(android.R.drawable.stat_notify_error);
		builder.setContentText(app.getTitle() + " " + app.getVersion());
		builder.setContentIntent(PendingIntent.getActivity(activity, 0,
				new Intent(), 0));

		return builder.build();
	}

	private Notification buildInstalledNotification(App app) {
		final NotificationCompat.Builder builder = new NotificationCompat.Builder(
				activity);
		builder.setAutoCancel(true);
		builder.setContentTitle(activity.getText(R.string.app_installed));
		builder.setSmallIcon(R.drawable.stat_sys_install_complete);
		builder.setContentText(app.getTitle() + " " + app.getVersion());
		builder.setContentIntent(PendingIntent.getActivity(activity, 0,
				new Intent(), 0));

		return builder.build();
	}

	private Notification buildInstallingNotification(App app) {
		final NotificationCompat.Builder builder = new NotificationCompat.Builder(
				activity);
		builder.setAutoCancel(true);
		builder.setContentTitle(activity.getText(R.string.app_installing));
		builder.setSmallIcon(android.R.drawable.stat_sys_download);
		builder.setContentText(app.getTitle() + " " + app.getVersion());
		builder.setContentIntent(PendingIntent.getActivity(activity, 0,
				new Intent(), 0));

		return builder.build();
	}

	public List<BlankListener> getListeners() {
		return Collections.unmodifiableList(listeners);
	}

	private NotificationManager getNotificationManager() {
		if (notificationManager == null) {
			notificationManager = (NotificationManager) activity
					.getSystemService(Context.NOTIFICATION_SERVICE);
		}
		return notificationManager;
	}

	@Override
	public void installDone(App app) {
		for (final BlankListener listener : getListeners()) {
			listener.onInstallAppDone(app);
		}
	}

	@Override
	public void installStarted(App app) {
		for (final BlankListener listener : getListeners()) {
			listener.onInstallAppStarted(app);
		}
	}

	public boolean isReady() {
		return !(connections.isEmpty());
	}

	@Override
	public void onDownloadAppDone(App app, File file) {
		if (isPrivileged()) {
			// direct install, if BlankStore is a system app
			getNotificationManager().notify(
					app.getPackageName().hashCode() + activity.hashCode(),
					buildInstallingNotification(app));
			startInstallApp(app, file);
		} else {
			// use the standard system dialog (manual confirmation)
			getNotificationManager().cancel(
					app.getPackageName().hashCode() + activity.hashCode());

			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(file),
					"application/vnd.android.package-archive");
			activity.startActivity(intent);
		}
	}

	@Override
	public void onDownloadAppFailed(App app) {
		getNotificationManager().notify(
				app.getPackageName().hashCode() + activity.hashCode(),
				buildFailedNotification(app));
	}

	@Override
	public void onDownloadAppProgess(App app, int progress, int max) {
		getNotificationManager().notify(
				app.getPackageName().hashCode() + activity.hashCode(),
				buildDownloadNotification(app, progress, max));
	}

	@Override
	public void onInstallAppDone(App app) {
		getNotificationManager().notify(
				app.getPackageName().hashCode() + activity.hashCode(),
				buildInstalledNotification(app));
	}

	@Override
	public void onInstallAppStarted(App app) {
		// NOTHING!
	}

	@Override
	public void onLoginException(LoginException ex) {
		for (final BlankListener listener : listeners) {
			listener.onLoginException(ex);
		}
	}

	@Override
	public void onQueryAppListResult(List<App> appList) {
		// NOTHING!
	}

	@Override
	public void onQueryAppResult(App app) {
		// NOTHING!
	}

	@Override
	public void onQueryAppsResult(List<App> apps) {
		// NOTHING!
	}

	@Override
	public void onUninstallAppDone(App app) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUninstallAppStarted(App app) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run(Runnable runnable) {
		activity.runOnUiThread(runnable);
	}

	public void startInstallApp(App app) {
		startInstallApp(app, this);
	}

	public void startInstallApp(App app, File file) {
		startInstallApp(app, file, this);
	}

	public boolean isPrivileged() {
		return installer.checkPackageManagerMethods();
	}

	public void startInstallApp(App app, File file, InstallCallback callback) {
		installer.installPackage(app, file, callback);
	}

	public void startInstallApp(App app, InstallCallback callback) {
		installer.installPackage(app, callback);
	}

	public void startOpenConnection() {
		for (final BlankConnection connection : connections) {
			final Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						connection.openConnection();
					} catch (final Exception ex) {
						Log.d("BlankStore", "openConnectionThread: Exception",
								ex);
						if (ex instanceof LoginException) {
							onLoginException((LoginException) ex);
						}
					}
				}
			});
			t.start();
		}
	}

	public void startQueryApp(String packageName, boolean extendedInfo) {
		for (final BlankConnection connection : connections) {
			if (connection.hasQueryApp()) {
				final AppTask task = new AppTask();
				task.execute(task.getDataSet(listeners, connection,
						packageName, extendedInfo));
				return;
			}
		}
	}

	public void startQueryAppDownload(App app, Context context) {
		for (final BlankConnection connection : connections) {
			if (connection.hasQueryAppDownload()) {
				final AppDownloadTask task = new AppDownloadTask();
				task.execute(task.getDataSet(listeners, connection, app));
				return;
			}
		}
	}

	public void startQueryAppList(String query) {
		startQueryAppList(query, true);
	}

	public void startQueryAppList(String query, boolean extendedInfo) {
		startQueryAppList(query, 0, extendedInfo);
	}

	public void startQueryAppList(String query, int start) {
		startQueryAppList(query, start, true);
	}

	public void startQueryAppList(String query, int start, boolean extendedInfo) {
		startQueryAppList(query, start, APPLIST_QUERY_DEFAULT_SIZE,
				extendedInfo);
	}

	public void startQueryAppList(String query, int start, int num) {
		startQueryAppList(query, start, num, true);
	}

	public void startQueryAppList(String query, int start, int num,
			boolean extendedInfo) {
		for (final BlankConnection connection : connections) {
			if (connection.hasQueryAppList()) {
				final AppListTask task = new AppListTask();
				task.execute(task.getDataSet(listeners, connection, query,
						start, num, extendedInfo));
				return;
			}
		}
	}

	public void startQueryApps(List<String> packageNames, boolean extendedInfo) {
		for (final BlankConnection connection : connections) {
			if (connection.hasQueryApp()) {
				final AppsTask task = new AppsTask();
				task.execute(task.getDataSet(listeners, connection,
						packageNames, extendedInfo));
				return;
			}
		}
	}

	public void startQueryImageToView(ImageView imageView, App app,
			String imageId, AppImageUsage appImageUsage) {
		for (final BlankConnection connection : connections) {
			if (connection.hasQueryImage()) {
				final ImageToViewTask task = new ImageToViewTask();
				task.execute(task.getDataSet(listeners, connection, app,
						imageId, appImageUsage, imageView));
				return;
			}
		}
	}

	public void startUninstallApp(App app) {
		startUninstallApp(app, this);
	}

	public void startUninstallApp(App app, UninstallCallback callback) {
		if (isPrivileged()) {
			installer.uninstallPackage(app, callback);
		} else {
			Intent intent = new Intent(Intent.ACTION_DELETE,
					Uri.fromParts("package", app.getPackageName(), null));
			activity.startActivity(intent);
		}
	}

	@Override
	public void uninstallDone(App app) {
		for (final BlankListener listener : getListeners()) {
			listener.onUninstallAppDone(app);
		}
	}

	@Override
	public void uninstallStarted(App app) {
		for (final BlankListener listener : getListeners()) {
			listener.onUninstallAppStarted(app);
		}
	}

}
