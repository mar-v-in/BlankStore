package com.android.vending.tasks;

import java.io.File;
import java.util.List;

import android.os.AsyncTask;

import com.android.vending.BlankConnection;
import com.android.vending.BlankListener;
import com.android.vending.google.SecureGooglePlayConnection;
import com.gc.android.market.api.model.Market.App;

public class AppDownloadTask
		extends
		AsyncTask<AppDownloadTask.DataSet, AppDownloadTask.Progress, AppDownloadTask.Result>
		implements SecureGooglePlayConnection.Listener {

	public class DataSet {
		private final List<BlankListener> listeners;
		private final BlankConnection connection;
		private final App app;

		public DataSet(List<BlankListener> listeners,
				BlankConnection connection, App app) {
			this.listeners = listeners;
			this.connection = connection;
			this.app = app;
		}

		public App getApp() {
			return app;
		}

		public BlankConnection getConnection() {
			return connection;
		}

		public List<BlankListener> getListeners() {
			return listeners;
		}
	}

	public class Progress {
		private final DataSet dataSet;
		private final int progress;
		private final int max;

		public Progress(DataSet dataSet, int progress, int max) {
			this.dataSet = dataSet;
			this.progress = progress;
			this.max = max;
		}

		public DataSet getDataSet() {
			return dataSet;
		}

		public int getMax() {
			return max;
		}

		public int getProgress() {
			return progress;
		}
	}

	public class Result extends Progress {

		private final File file;

		public Result(DataSet dataSet, File file) {
			super(dataSet, dataSet.getApp().getExtendedInfo().getInstallSize(),
					dataSet.getApp().getExtendedInfo().getInstallSize());
			this.file = file;
		}

		public File getFile() {
			return file;
		}
	}

	private DataSet dataSet;

	@Override
	protected Result doInBackground(DataSet... params) {
		if (params.length >= 1) {
			dataSet = params[0];
			final File file = dataSet.getConnection().queryAppDownload(
					dataSet.getApp(), this);
			return new Result(dataSet, file);
		}
		return new Result(null, null);
	}

	public DataSet getDataSet(List<BlankListener> listeners,
			BlankConnection connection, App app) {
		return new DataSet(listeners, connection, app);
	}

	@Override
	public void onDownloadProgress(int progress, int max) {
		publishProgress(new Progress(dataSet, progress, max));
	}

	@Override
	protected void onPostExecute(Result result) {
		super.onPostExecute(result);
		for (final BlankListener listener : dataSet.getListeners()) {
			listener.onDownloadAppDone(dataSet.getApp(), result.getFile());
		}
	}

	@Override
	protected void onProgressUpdate(Progress... values) {
		for (final BlankListener listener : dataSet.getListeners()) {
			listener.onDownloadAppProgess(dataSet.getApp(),
					values[0].getProgress(), values[0].getMax());
		}
	}
}
