package com.android.vending.tasks;

import java.util.List;

import android.graphics.Bitmap;

import com.android.vending.BlankAsyncTask;
import com.android.vending.BlankConnection;
import com.android.vending.BlankListener;
import com.gc.android.market.api.model.Market.App;
import com.gc.android.market.api.model.Market.GetImageRequest.AppImageUsage;

public abstract class ImageTask<Data, Res> extends BlankAsyncTask<Data, Res> {
	public class DataSet extends BlankAsyncTask<Data, Res>.DataSet {
		private final App app;
		private final String imageId;
		private final AppImageUsage appImageUsage;

		public DataSet(List<BlankListener> listeners,
				BlankConnection connection, App app, String imageId,
				AppImageUsage appImageUsage) {
			super(listeners, connection);
			this.app = app;
			this.imageId = imageId;
			this.appImageUsage = appImageUsage;
		}

		public App getApp() {
			return app;
		}

		public AppImageUsage getAppImageUsage() {
			return appImageUsage;
		}

		public String getImageId() {
			return imageId;
		}
	}

	public class Result extends BlankAsyncTask<Data, Res>.Result {
		private final Bitmap bitmap;

		public Result(Data dataSet, Bitmap bitmap) {
			super(dataSet);
			this.bitmap = bitmap;
		}

		public Bitmap getBitmap() {
			return bitmap;
		}
	}

}
