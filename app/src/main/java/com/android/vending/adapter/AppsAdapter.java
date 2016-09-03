package com.android.vending.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.vending.BlankActivity;
import com.android.vending.Cache;
import com.android.vending.R;
import com.gc.android.market.api.model.Market.App;
import com.gc.android.market.api.model.Market.GetImageRequest.AppImageUsage;

public class AppsAdapter extends BaseAdapter {

	// private static final int VIEW_CACHE_SIZE = 50;

	private List<App> apps;
	private final Context context;
	private final int layout;

	private final Cache<String, View> viewCache;

	public AppsAdapter(Context context, int layout) {
		apps = new ArrayList<App>();
		this.context = context;
		this.layout = layout;
		viewCache = new Cache<String, View>(0);
	}

	@Override
	public int getCount() {
		return apps.size();
	}

	@Override
	public Object getItem(int position) {
		return apps.get(position);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).hashCode();
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final App app = apps.get(position);
		boolean fromCache = false;
		if (app != null) {
			view = viewCache.get(app.getPackageName());
		} else {
			view = null;
		}

		if (view == null) {
			final LayoutInflater layoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(layout, parent, false);
		} else {
			fromCache = true;
		}
		if (app == null) {
			return view;
		}
		final PackageManager pm = context.getPackageManager();
		int appVersion = -1;
		PackageInfo info = null;
		try {
			info = pm.getPackageInfo(app.getPackageName(), 0);
			appVersion = info.versionCode;
		} catch (final NameNotFoundException e) {
		}
		final TextView priceinfo = (TextView) view
				.findViewById(R.id.txt_priceinfo);
		if (appVersion == app.getVersionCode()) {
			priceinfo.setText(context.getString(R.string.app_installed));
		} else if (appVersion != -1) {
			if (app.getPrice() == null || app.getPrice().isEmpty()) {
				if (app.getPrice() == null || app.getPrice().isEmpty()) {
					priceinfo.setText(context.getString(R.string.app_update));
				} else {
					priceinfo.setText(app.getPrice() + " "
							+ context.getString(R.string.app_update));
				}
			}
		} else {
			if (app.getPrice() == null || app.getPrice().isEmpty()) {
				priceinfo.setText(context.getString(R.string.app_price_free));
			} else {
				priceinfo.setText(app.getPrice());
			}
		}

		final TextView title = (TextView) view.findViewById(R.id.txt_title);
		title.setText(app.getTitle());

		final TextView subtitle = (TextView) view
				.findViewById(R.id.txt_subtitle);
		subtitle.setText(app.getCreator());

		if (!fromCache) {
			final ImageView icon = (ImageView) view.findViewById(R.id.app_icon);
			icon.setId(R.id.app_icon + app.getPackageName().hashCode());
			if (!app.getCreator().isEmpty()) {
				((BlankActivity) context).startSetImage(icon, app, "1",
						AppImageUsage.ICON);
			}
			if (info != null) {
				icon.setImageDrawable(pm
						.getApplicationIcon(info.applicationInfo));
			}
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

		viewCache.put(app.getPackageName(), view);
		return view;
	}

	@Override
	public void notifyDataSetChanged() {
		viewCache.setSize(apps.size());
		super.notifyDataSetChanged();
	}

	public void setAppList(List<App> appList) {
		apps = appList;
		viewCache.setSize(apps.size());
		notifyDataSetChanged();
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
}
