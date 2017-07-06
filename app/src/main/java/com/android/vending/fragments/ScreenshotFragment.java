package com.android.vending.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.android.vending.BlankActivity;
import com.android.vending.R;
import com.gc.android.market.api.model.Market.App;
import com.gc.android.market.api.model.Market.GetImageRequest.AppImageUsage;

public class ScreenshotFragment extends BlankFragment {
	private App app;
	private int id;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final ImageView view = new ImageView(getActivity());
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		view.setId(hashCode());
		view.setBackgroundColor(Color.parseColor(getString(R.color.header_bg)));

		retrieveScreenshot(view);

		return view;
	}

	private void retrieveScreenshot(ImageView view) {
		if (app != null && view != null) {
			((BlankActivity) getActivity()).startSetImage(view, app, id + "",
					AppImageUsage.SCREENSHOT);
		}
	}

	public void setScreenshot(App app, int id) {
		this.app = app;
		this.id = id;
		retrieveScreenshot((ImageView) getView());
	}
}