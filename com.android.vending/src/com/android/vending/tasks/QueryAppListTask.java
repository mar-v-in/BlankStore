package com.android.vending.tasks;

import java.util.List;

import com.android.vending.BlankAsyncTask;
import com.android.vending.BlankConnection;
import com.android.vending.BlankListener;
import com.gc.android.market.api.model.Market.App;

public class QueryAppListTask extends
		BlankAsyncTask<QueryAppListTask.DataSet, QueryAppListTask.Result> {
	public class DataSet extends BlankAsyncTask<DataSet, Result>.DataSet {
		private final String query;
		private final int start;
		private final int num;
		private final boolean extendedInfo;

		public DataSet(List<BlankListener> listeners,
				BlankConnection connection, String query, int start, int num,
				boolean extendedInfo) {
			super(listeners, connection);
			this.query = query;
			this.start = start;
			this.num = num;
			this.extendedInfo = extendedInfo;
		}

		public int getNum() {
			return num;
		}

		public String getQuery() {
			return query;
		}

		public int getStart() {
			return start;
		}

		public boolean isExtendedInfoRequired() {
			return extendedInfo;
		}
	}

	public class Result extends BlankAsyncTask<DataSet, Result>.Result {
		private final List<App> appList;

		public Result(DataSet dataSet, List<App> appList) {
			super(dataSet);
			this.appList = appList;
		}

		public List<App> getAppList() {
			return appList;
		}
	}

	@Override
	protected Result doInBackground(DataSet... params) {
		if (params.length >= 1) {
			final DataSet dataSet = params[0];
			final List<App> appList = dataSet.getConnection().queryAppList(
					dataSet.getQuery(), dataSet.getStart(), dataSet.getNum(),
					dataSet.isExtendedInfoRequired());
			return new Result(dataSet, appList);
		}
		return new Result(null, null);
	}

	public DataSet getDataSet(List<BlankListener> listeners,
			BlankConnection connection, String query, int start, int num,
			boolean extendedInfo) {
		return new DataSet(listeners, connection, query, start, num,
				extendedInfo);
	}

	@Override
	protected void onPostExecute(Result result) {
		for (final BlankListener listener : result.getDataSet().getListeners()) {
			listener.onQueryAppListResult(result.getAppList());
		}
	}

}
