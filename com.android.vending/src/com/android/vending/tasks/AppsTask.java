package com.android.vending.tasks;

import java.util.List;

import com.android.vending.BlankAsyncTask;
import com.android.vending.BlankConnection;
import com.android.vending.BlankListener;
import com.gc.android.market.api.model.Market.App;

public class AppsTask extends
		BlankAsyncTask<AppsTask.DataSet, AppsTask.Result> {
	public class DataSet extends BlankAsyncTask<DataSet, Result>.DataSet {
		private final List<String> packageNames;
		private final boolean extendedInfo;

		public DataSet(List<BlankListener> listeners,
				BlankConnection connection, List<String> packageNames,
				boolean extendedInfo) {
			super(listeners, connection);
			this.packageNames = packageNames;
			this.extendedInfo = extendedInfo;
		}

		public List<String> getPackageNames() {
			return packageNames;
		}

		public boolean isExtendedInfoRequired() {
			return extendedInfo;
		}
	}

	public class Result extends BlankAsyncTask<DataSet, Result>.Result {
		private final List<App> apps;

		public Result(DataSet dataSet, List<App> apps) {
			super(dataSet);
			this.apps = apps;
		}

		public List<App> getApps() {
			return apps;
		}
	}

	@Override
	protected Result doInBackground(DataSet... params) {
		if (params.length >= 1) {
			final DataSet dataSet = params[0];
			final List<App> apps = dataSet.getConnection()
					.queryAppsByName(dataSet.getPackageNames(),
							dataSet.isExtendedInfoRequired());
			return new Result(dataSet, apps);
		}
		return new Result(null, null);
	}

	public DataSet getDataSet(List<BlankListener> listeners,
			BlankConnection connection, List<String> packageNames,
			boolean extendedInfo) {
		return new DataSet(listeners, connection, packageNames, extendedInfo);
	}

	@Override
	protected void onPostExecute(Result result) {
		for (final BlankListener listener : result.getDataSet().getListeners()) {
			listener.onQueryAppsResult(result.getApps());
		}
	}

}
