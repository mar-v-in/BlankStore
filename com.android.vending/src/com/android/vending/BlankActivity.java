package com.android.vending;

import java.io.File;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.android.vending.account.Account;
import com.android.vending.account.AccountManager;
import com.android.vending.fragments.AppFragment;
import com.android.vending.fragments.AppListFragment;
import com.android.vending.fragments.InstalledAppsFragment;
import com.android.vending.fragments.ScreenshotFragment;
import com.android.vending.fragments.StartFragment;
import com.android.vending.google.DefaultGooglePlayConnection;
import com.android.vending.google.SecureGooglePlayConnection;
import com.gc.android.market.api.LoginException;
import com.gc.android.market.api.model.Market.App;
import com.gc.android.market.api.model.Market.GetImageRequest.AppImageUsage;

public class BlankActivity extends FragmentActivity implements BlankListener {

	private boolean showingFragment = false;
	private FragmentManager fragmentManager;
	private Fragment currentFragment;
	private final BlankStore store;
	private Menu menu;
	private View searchView;

	public BlankActivity() {
		store = new BlankStore(this);
	}

	@TargetApi(14)
	public void collapseSearchView() {
		menu.findItem(R.id.menu_search).collapseActionView();
	}

	public void downloadApp(App app) {
		store.startQueryAppDownload(app, this);
	}

	@TargetApi(14)
	public void expandSearchView() {
		menu.findItem(R.id.menu_search).expandActionView();
	}

	public void getApp(String packageName, boolean extendedInfo) {
		store.startQueryApp(packageName, extendedInfo);
	}

	public void getApps(List<String> packageNames) {
		getApps(packageNames);
	}

	public void getApps(List<String> packageNames, boolean extendedInfo) {
		store.startQueryApps(packageNames, extendedInfo);
	}

	public void goAppDetails() {
		final AppFragment fragment = new AppFragment();
		goFragment(fragment);
	}

	public void goAppDetails(String packageName) {
		goAppDetails();
		getApp(packageName, false);
		getApp(packageName, true);
	}

	public void goAppList() {
		final AppListFragment fragment = new AppListFragment();
		goFragment(fragment);
	}

	private void goFragment(Fragment fragment) {
		if (fragment == null) {
			return;
		}
		final FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		if (showingFragment) {
			fragmentTransaction
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		}
		fragmentTransaction.replace(R.id.fragment_container, fragment, fragment
				.getClass().getName());
		fragmentTransaction.addToBackStack(fragment.getClass().getName());
		fragmentTransaction.commit();
		showingFragment = true;
		currentFragment = fragment;
	}

	public void goScreenshot(App app, int id) {
		final ScreenshotFragment fragment = new ScreenshotFragment();
		fragment.setScreenshot(app, id);
		goFragment(fragment);
	}

	public void hideSearch() {
		if (Build.VERSION.SDK_INT >= 14) {
			collapseSearchView();
		}
	}

	@TargetApi(11)
	public void initSearchView(Menu menu) {
		searchView = menu.findItem(R.id.menu_search).getActionView();
		((SearchView) searchView)
				.setQueryHint(getText(R.string.menu_search_hint));
		((SearchView) searchView)
				.setOnQueryTextListener(new OnQueryTextListener() {

					@Override
					public boolean onQueryTextChange(String newText) {
						return true;
					}

					@Override
					public boolean onQueryTextSubmit(String query) {
						startSearch(((SearchView) searchView).getQuery()
								.toString());
						return true;
					}
				});
	}

	public void installApp(App app) {
		store.startInstallApp(app);
	}

	@Override
	public void onBackPressed() {
		if (currentFragment instanceof StartFragment
				|| fragmentManager.getBackStackEntryCount() <= 1) {
			finish();
		} else {
			fragmentManager.popBackStackImmediate();
			currentFragment = fragmentManager
					.findFragmentById(R.id.fragment_container);
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		fragmentManager = getSupportFragmentManager();
		if (!showingFragment) {
			goFragment(new StartFragment());
		}
		setVisibleTitle(R.string.apps);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!store.isReady()) {
			final AccountManager accountManager = AccountManager
					.getInstance(this);
			final List<Account> accounts = accountManager.getAllAccounts();
			Log.d("BlankActivity", "Accounts: " + accounts.size());
			for (final Account account : accounts) {
				Log.d("BlankActivity", "Account: " + account.toString());
				switch (account.getType()) {
				case GooglePlay:
					store.addBlankConnection(new DefaultGooglePlayConnection(
							account));
					store.addBlankConnection(new SecureGooglePlayConnection(
							account));
					break;
				}
			}
			if (accounts.size() == 0) {
				Log.d("BlankActivity", "Need Account to work!");

				Intent intent = new Intent();
				intent.setClassName("com.android.vending",
						"com.android.vending.account.AccountActivity");
				intent.putExtra("StartOnFinish", "true");
				startActivity(intent);
				
				finish();
			}

			Log.d("BlankActivity", "Accounts initialized.");

			store.addListener(this);
			Log.d("BlankActivity", "BlankStore initialized.");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		final MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		this.menu = menu;
		if (Build.VERSION.SDK_INT >= 11) {
			initSearchView(menu);
		}

		return true;
	}

	@Override
	public void onDownloadAppDone(App app, File file) {
		if (currentFragment instanceof BlankListener) {
			((BlankListener) currentFragment).onDownloadAppDone(app, file);
		}
	}

	@Override
	public void onDownloadAppFailed(App app) {
		if (currentFragment instanceof BlankListener) {
			((BlankListener) currentFragment).onDownloadAppFailed(app);
		}
	}

	@Override
	public void onDownloadAppProgess(App app, int progress, int max) {
		if (currentFragment instanceof BlankListener) {
			((BlankListener) currentFragment).onDownloadAppProgess(app,
					progress, max);
		}
	}

	@Override
	public void onInstallAppDone(App app) {
		if (currentFragment instanceof BlankListener) {
			((BlankListener) currentFragment).onInstallAppDone(app);
		}
	}

	@Override
	public void onInstallAppStarted(App app) {
		if (currentFragment instanceof BlankListener) {
			((BlankListener) currentFragment).onInstallAppStarted(app);
		}
	}

	@Override
	public void onLoginException(LoginException ex) {
		Toast.makeText(this, R.string.login_exception, Toast.LENGTH_LONG)
				.show();
		Log.e("BlankStore", "onLoginException", ex);
	}

	@Override
	public void onQueryAppListResult(List<App> appList) {
		if (currentFragment instanceof BlankListener) {
			((BlankListener) currentFragment).onQueryAppListResult(appList);
		}
	}

	@Override
	public void onQueryAppResult(App app) {
		if (currentFragment instanceof BlankListener) {
			((BlankListener) currentFragment).onQueryAppResult(app);
		}
	}

	@Override
	public void onQueryAppsResult(List<App> apps) {
		if (currentFragment instanceof BlankListener) {
			((BlankListener) currentFragment).onQueryAppsResult(apps);
		}
	}

	@Override
	public void onUninstallAppDone(App app) {
		if (currentFragment instanceof BlankListener) {
			((BlankListener) currentFragment).onUninstallAppDone(app);
		}
	}

	@Override
	public void onUninstallAppStarted(App app) {
		if (currentFragment instanceof BlankListener) {
			((BlankListener) currentFragment).onUninstallAppStarted(app);
		}
	}

	public void openInstalled() {
		goFragment(new InstalledAppsFragment());
	}

	public void selectSearch() {
		if (Build.VERSION.SDK_INT >= 11) {
			selectSearchView();
		}
	}

	@TargetApi(11)
	public void selectSearchView() {
		if (Build.VERSION.SDK_INT >= 14) {
			expandSearchView();
		}
		((SearchView) searchView).requestFocus();
	}

	@TargetApi(11)
	public void setActionBarTitle(CharSequence title) {
		getActionBar().setTitle(title);
	}

	public void setVisibleTitle(CharSequence title) {
		if (Build.VERSION.SDK_INT >= 11) {
			setActionBarTitle(title);
		} else {
			setTitle(title);
		}
	}

	public void setVisibleTitle(int resId) {
		setVisibleTitle(getText(resId));
	}

	public void startApp(App app) {
		final Intent intent = getPackageManager().getLaunchIntentForPackage(
				app.getPackageName());
		startActivity(intent);
	}

	public void startSearch(String query) {
		goAppList();
		store.startQueryAppList(query, false);
		if (menu != null && searchView != null) {
			final InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
			if (Build.VERSION.SDK_INT >= 14) {
				collapseSearchView();
			}
		}
	}

	public void startSetImage(ImageView imageView, App app, String imageId,
			AppImageUsage appImageUsage) {
		store.startQueryImageToView(imageView, app, imageId, appImageUsage);
	}

	public void uninstallApp(App app) {
		store.startUninstallApp(app);
	}
}