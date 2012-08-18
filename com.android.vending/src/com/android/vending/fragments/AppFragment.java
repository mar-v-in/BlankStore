package com.android.vending.fragments;

import java.io.File;

import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.vending.BlankActivity;
import com.android.vending.R;
import com.android.vending.Utils;
import com.gc.android.market.api.model.Market.App;
import com.gc.android.market.api.model.Market.GetImageRequest.AppImageUsage;

public class AppFragment extends BlankFragment implements OnClickListener {
	private App app;
	private final static String APP_SAVE = AppFragment.class.getName() + ".app";

	private final OnClickListener screenshotOnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (app != null && v.getTag() instanceof Integer) {
				((BlankActivity) getActivity()).goScreenshot(app,
						(Integer) v.getTag());
			}
		}
	};

	private void initDownload() {
		final ProgressBar download = (ProgressBar) getView().findViewById(
				R.id.prg_download);
		download.setIndeterminate(true);
		final TextView bytes = (TextView) getView().findViewById(
				R.id.txt_download_bytes);
		bytes.setText("0 Bytes / "
				+ Utils.niceSize(app.getExtendedInfo().getInstallSize()));
		final TextView percent = (TextView) getView().findViewById(
				R.id.txt_download_percent);
		percent.setText("0%");
	}

	private void makeView(final View view) {
		if (app == null || getActivity() == null || view == null) {
			return;
		}
		view.findViewById(R.id.view_description).setVisibility(View.VISIBLE);
		view.findViewById(R.id.view_details).setVisibility(View.VISIBLE);
		view.findViewById(R.id.view_header).setVisibility(View.VISIBLE);
		view.findViewById(R.id.prog_loading).setVisibility(View.GONE);
		view.findViewById(R.id.btn_install).setVisibility(View.INVISIBLE);
		view.findViewById(R.id.installed_view).setVisibility(View.GONE);
		view.findViewById(R.id.installing_view).setVisibility(View.GONE);

		final TextView title = (TextView) view.findViewById(R.id.txt_title);
		title.setText(app.getTitle());

		final TextView subtitle = (TextView) view
				.findViewById(R.id.txt_subtitle);
		subtitle.setText(app.getCreator());

		ImageView icon = (ImageView) view.findViewById(R.id.app_icon);
		if (icon == null) {
			icon = (ImageView) view.findViewById(R.id.app_icon
					+ app.getPackageName().hashCode());
		}
		if (icon != null) {
			icon.setId(R.id.app_icon + app.getPackageName().hashCode());
			((BlankActivity) getActivity()).startSetImage(icon, app, "1",
					AppImageUsage.ICON);
		}

		final double rating = Double.parseDouble(app.getRating());
		final ImageView star1 = (ImageView) view.findViewById(R.id.img_rate1);
		setStarRating(star1, 1, rating);
		final ImageView star2 = (ImageView) view.findViewById(R.id.img_rate2);
		setStarRating(star2, 2, rating);
		final ImageView star3 = (ImageView) view.findViewById(R.id.img_rate3);
		setStarRating(star3, 3, rating);
		final ImageView star4 = (ImageView) view.findViewById(R.id.img_rate4);
		setStarRating(star4, 4, rating);
		final ImageView star5 = (ImageView) view.findViewById(R.id.img_rate5);
		setStarRating(star5, 5, rating);

		final TextView ratecount = (TextView) view
				.findViewById(R.id.txt_ratecount);
		ratecount.setText(app.getRatingsCount() + "");

		final TextView lastUpdate = (TextView) view
				.findViewById(R.id.txt_lastupdate);
		lastUpdate.setText("Version " + app.getVersion());

		final TextView downloads = (TextView) view
				.findViewById(R.id.txt_downloads);
		downloads.setText(app.getExtendedInfo().getDownloadsCountText() + " "
				+ getText(R.string.details_downloads));

		final TextView size = (TextView) view.findViewById(R.id.txt_size);
		size.setText(Utils.niceSize(app.getExtendedInfo().getInstallSize()));

		final TextView description = (TextView) view
				.findViewById(R.id.txt_description);
		description.setText(Html.fromHtml(app.getExtendedInfo()
				.getDescription().replace("\n", "<br />")));

		final Button install = (Button) view.findViewById(R.id.btn_install);
		install.setOnClickListener(this);

		final Button start = (Button) view.findViewById(R.id.btn_start);
		start.setOnClickListener(this);

		final Button uninstall = (Button) view.findViewById(R.id.btn_uninstall);
		uninstall.setOnClickListener(this);

		final PackageManager pm = getActivity().getPackageManager();
		int appVersion = -1;
		try {
			final PackageInfo info = pm.getPackageInfo(app.getPackageName(), 0);
			appVersion = info.versionCode;
		} catch (final NameNotFoundException e) {
		}
		install.setText(R.string.app_install);

		if (appVersion == -1) {
			install.setVisibility(View.VISIBLE);
		} else {
			install.setVisibility(View.INVISIBLE);
			view.findViewById(R.id.installed_view).setVisibility(View.VISIBLE);
			start.setText(R.string.app_start);
			start.setTag("start");
			if (appVersion != app.getVersionCode()) {
				start.setText(R.string.app_update);
				start.setTag("update");
			}
		}
		if (app.hasExtendedInfo()
				&& app.getExtendedInfo().getScreenshotsCount() > 0) {
			final LinearLayout screenshotContainer = (LinearLayout) getView()
					.findViewById(R.id.screenshots);
			for (int i = 1; i <= app.getExtendedInfo().getScreenshotsCount(); i++) {
				final ImageView screenshot = new ImageView(getActivity());
				screenshot.setTag(i);
				screenshot.setId(R.id.screenshots + app.hashCode() + i);
				screenshot.setOnClickListener(screenshotOnClick);
				if (i != app.getExtendedInfo().getScreenshotsCount()) {
					screenshot.setPadding(0, 0, 10, 0);
				}
				screenshotContainer.addView(screenshot);
				((BlankActivity) getActivity()).startSetImage(screenshot, app,
						i + "", AppImageUsage.SCREENSHOT_THUMBNAIL);
			}
		}
	}

	@Override
	public void onClick(View clickedView) {
		if (clickedView.getId() == R.id.btn_install) {
			startDownload();
		} else if (clickedView.getId() == R.id.btn_start) {
			if (clickedView.getTag() != null
					&& clickedView.getTag().toString().equals("update")) {
				startDownload();
			} else {
				((BlankActivity) getActivity()).startApp(app);
			}
		} else if (clickedView.getId() == R.id.btn_uninstall) {
			startUninstall();
		} else {
			Log.d("AppFragment", "Unknown clicked");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.appdetail, null);

		/*
		 * if (app != null) { makeView(view); }
		 */

		return view;
	}

	@Override
	public void onDownloadAppDone(App app, File file) {
		if (app != null
				&& this.app != null
				&& app.getPackageName().equalsIgnoreCase(
						this.app.getPackageName())) {
			makeView(getView());
		}
	}

	@Override
	public void onDownloadAppFailed(App app) {
		if (this.app != null
				&& app.getPackageName().equalsIgnoreCase(
						this.app.getPackageName())) {
			viewDownload();
			final ProgressBar download = (ProgressBar) getView().findViewById(
					R.id.prg_download);
			download.setIndeterminate(true);
			final TextView bytes = (TextView) getView().findViewById(
					R.id.txt_download_bytes);
			bytes.setText(getString(R.string.app_installing));
			final TextView percent = (TextView) getView().findViewById(
					R.id.txt_download_percent);
			percent.setText("");
		}
	}

	@Override
	public void onDownloadAppProgess(App app, int progress, int max) {
		if (this.app != null
				&& app.getPackageName().equalsIgnoreCase(
						this.app.getPackageName())) {
			viewDownload();
			final ProgressBar download = (ProgressBar) getView().findViewById(
					R.id.prg_download);
			download.setIndeterminate(false);
			download.setMax(max);
			download.setProgress(progress);
			final TextView bytes = (TextView) getView().findViewById(
					R.id.txt_download_bytes);
			bytes.setText(Utils.niceSize(progress) + " / "
					+ Utils.niceSize(max));
			final TextView percent = (TextView) getView().findViewById(
					R.id.txt_download_percent);
			percent.setText(Math.round(progress * 10000F / max) / 100F + "%");
		}
	}

	@Override
	public void onInstallAppDone(App app) {
		if (app == null || this.app == null) {
			return;
		}
		if (app.getPackageName().equalsIgnoreCase(this.app.getPackageName())) {
			makeView(getView());
			final NotificationManager notificationManager = (NotificationManager) getActivity()
					.getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.cancel(app.hashCode()
					+ getActivity().hashCode());
		}
	}

	@Override
	public void onQueryAppResult(App app) {
		if (app == null) {
			return;
		}
		if (this.app == null) {
			setApp(app);
		} else if (this.app.getPackageName().equalsIgnoreCase(
				app.getPackageName())) {
			setApp(app);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		makeView(getView());
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (app != null) {
			outState.putSerializable(APP_SAVE, app);
		}
	}

	@Override
	public void onUninstallAppDone(App app) {
		if (app.getPackageName().equalsIgnoreCase(this.app.getPackageName())) {
			makeView(getView());
			final NotificationManager notificationManager = (NotificationManager) getActivity()
					.getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.cancel(app.hashCode()
					+ getActivity().hashCode());
		}
	}

	public void setApp(App a) {
		app = a;
		if (getView() != null) {
			makeView(getView());
		}
	}

	private void setStarRating(ImageView view, int star, double rating) {
		if (rating > star - 0.25) {
			view.setImageResource(R.drawable.ic_rate_star_on);
		} else if (rating < star - 0.75) {
			view.setImageResource(R.drawable.ic_rate_star_off);
		} else {
			view.setImageResource(R.drawable.ic_rate_star_half);
		}
	}

	private void startDownload() {
		((BlankActivity) getActivity()).downloadApp(app);
		viewDownload();
		initDownload();
	}

	private void startUninstall() {
		((BlankActivity) getActivity()).uninstallApp(app);
		viewDownload();
		final ProgressBar download = (ProgressBar) getView().findViewById(
				R.id.prg_download);
		download.setIndeterminate(true);
		final TextView bytes = (TextView) getView().findViewById(
				R.id.txt_download_bytes);
		bytes.setText(getString(R.string.app_uninstalling));
		final TextView percent = (TextView) getView().findViewById(
				R.id.txt_download_percent);
		percent.setText("");
	}

	private void viewDownload() {
		getView().findViewById(R.id.btn_install).setVisibility(View.INVISIBLE);
		getView().findViewById(R.id.installed_view).setVisibility(View.GONE);
		getView().findViewById(R.id.installing_view)
				.setVisibility(View.VISIBLE);
	}
}
