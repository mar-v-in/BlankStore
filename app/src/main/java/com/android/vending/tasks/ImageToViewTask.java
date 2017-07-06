package com.android.vending.tasks;

import java.util.List;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.vending.BlankConnection;
import com.android.vending.BlankListener;
import com.gc.android.market.api.model.Market.App;
import com.gc.android.market.api.model.Market.GetImageRequest.AppImageUsage;

public class ImageToViewTask
		extends
		ImageTask<ImageToViewTask.DataSet, ImageTask<ImageToViewTask.DataSet, ?>.Result> {
	public class DataSet
			extends
			ImageTask<DataSet, ImageTask<ImageToViewTask.DataSet, ?>.Result>.DataSet {
		private final ImageView imageView;

		public DataSet(List<BlankListener> listeners,
				BlankConnection connection, App app, String imageId,
				AppImageUsage appImageUsage, ImageView imageView) {
			super(listeners, connection, app, imageId, appImageUsage);
			this.imageView = imageView;
		}

		public ImageView getImageView() {
			return imageView;
		}
	}

	Bitmap resultBitmap = null;

	Integer resultBitmapSync = 1;

	@Override
	protected ImageTask<ImageToViewTask.DataSet, ?>.Result doInBackground(
			DataSet... params) {
		if (params.length >= 1) {
			final DataSet dataSet = params[0];
			final Bitmap bitmap = dataSet.getConnection().queryImage(
					dataSet.getApp(), dataSet.getImageId(),
					dataSet.getAppImageUsage());
			return new Result(dataSet, bitmap);
		}
		return new Result(null, null);
	}

	public DataSet getDataSet(List<BlankListener> listeners,
			BlankConnection connection, App app, String imageId,
			AppImageUsage appImageUsage, ImageView imageView) {
		return new DataSet(listeners, connection, app, imageId, appImageUsage,
				imageView);
	}

	@Override
	protected void onPostExecute(
			ImageTask<ImageToViewTask.DataSet, ?>.Result result) {
		resultBitmap = result.getBitmap();
		if (resultBitmap != null) {
			result.getDataSet().getImageView().setImageBitmap(resultBitmap);
		}
	}

}
