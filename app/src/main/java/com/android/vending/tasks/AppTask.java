package com.android.vending.tasks;

import java.util.List;

import com.android.vending.BlankAsyncTask;
import com.android.vending.BlankConnection;
import com.android.vending.BlankListener;
import com.gc.android.market.api.model.Market.App;

public class AppTask extends BlankAsyncTask<AppTask.DataSet, AppTask.Result> {
	public class DataSet extends BlankAsyncTask<DataSet, Result>.DataSet {
		private final String packageName;
		private final boolean extendedInfo;

		public DataSet(List<BlankListener> listeners,
				BlankConnection connection, String packageName,
				boolean extendedInfo) {
			super(listeners, connection);
			this.packageName = packageName;
			this.extendedInfo = extendedInfo;
		}

		public String getPackageName() {
			return packageName;
		}

		public boolean isExtendedInfoRequired() {
			return extendedInfo;
		}
	}

	public class Result extends BlankAsyncTask<DataSet, Result>.Result {
		private final App app;

		public Result(DataSet dataSet, App app) {
			super(dataSet);
			this.app = app;
		}

		public App getApp() {
			return app;
		}
	}

	@Override
	protected Result doInBackground(DataSet... params) {
		if (params.length >= 1) {
			final DataSet dataSet = params[0];
			final App app = dataSet.getConnection().queryAppByName(
					dataSet.getPackageName(), dataSet.isExtendedInfoRequired());
			return new Result(dataSet, app);
		}
		return new Result(null, null);
	}

	public DataSet getDataSet(List<BlankListener> listeners,
			BlankConnection connection, String packageName, boolean extendedInfo) {
		return new DataSet(listeners, connection, packageName, extendedInfo);
	}

	@Override
	protected void onPostExecute(Result result) {
		for (final BlankListener listener : result.getDataSet().getListeners()) {
			listener.onQueryAppResult(result.getApp());
		}
	}

}
