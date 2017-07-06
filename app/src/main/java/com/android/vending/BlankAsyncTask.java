package com.android.vending;

import java.util.List;

import android.os.AsyncTask;

public abstract class BlankAsyncTask<Data, Res> extends
		AsyncTask<Data, Void, Res> {
	public class DataSet {
		private final List<BlankListener> listeners;
		private final BlankConnection connection;

		public DataSet(List<BlankListener> listeners, BlankConnection connection) {
			this.listeners = listeners;
			this.connection = connection;
		}

		public BlankConnection getConnection() {
			return connection;
		}

		public List<BlankListener> getListeners() {
			return listeners;
		}
	}

	public class Result {
		private final Data dataSet;

		public Result(Data dataSet) {
			this.dataSet = dataSet;
		}

		public Data getDataSet() {
			return dataSet;
		}
	}
}
