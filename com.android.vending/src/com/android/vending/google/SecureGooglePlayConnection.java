package com.android.vending.google;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.util.Date;

import android.util.Log;

import com.android.vending.BlankConnection;
import com.android.vending.account.Account;
import com.gc.android.market.api.model.Market;
import com.google.android.AndroidAuth;
import com.google.android.AndroidContext;
import com.google.auth.AuthType;
import com.google.auth.DataField;
import com.google.play.DfeClient;
import com.google.play.DfeResponse;
import com.google.play.proto.*;
import com.google.tools.RequestContext;

public class SecureGooglePlayConnection extends BlankConnection {

    public File getCacheDir() {
        return cacheDir;
    }

    public interface Listener {

		boolean isCancelled();

		void onDownloadProgress(int progress, int max);

	}

	private final static String TAG = "SecureGooglePlay";
    private DfeClient client;
    private RequestContext dfeContext;
    private String password;
    private String email;
    private File cacheDir;

	public SecureGooglePlayConnection(Account account) {
		this(account.getLogin(), account.getPassword(), account.getAndroidId(),
				account.getOperatorAlpha(), account.getOperatorNumeric(), account.getDeviceName(), account.getSdkVersion());
	}

	public SecureGooglePlayConnection(String email, String password,
			String androidId, File cacheDir, String operatorAlpha,
			String operatorNumeric, String deviceName, int sdkVersion) {
        dfeContext = new RequestContext();
        dfeContext.set(DfeClient.KEY_ANDROID_ID_HEX, androidId);
        dfeContext.set(DfeClient.KEY_CELL_OPERATOR_NUMERIC, operatorNumeric);
        dfeContext.set(DfeClient.KEY_CLIENT_ID, "am-google");
        dfeContext.set(DfeClient.KEY_FILTER_LEVEL, 3);
        dfeContext.set(DfeClient.KEY_HTTP_USER_AGENT,
                "Android-Finsky/4.0.25 (api=3,versionCode=80200025,sdk=" + sdkVersion + ",device=" + deviceName + ",hardware=" + deviceName + ",product=occam)");
        dfeContext.set(DfeClient.KEY_LOGGING_ID, "");
        dfeContext.set(DfeClient.KEY_SMALEST_SCREEN_WIDTH_DP, 384);
        client = new DfeClient(dfeContext);
        this.cacheDir = cacheDir;
        this.email = email;
        this.password = password;
	}

    private synchronized DfeClient getClient() {
        if (dfeContext.getString(DfeClient.KEY_AUTHORIZATION_TOKEN) == null) {
            String authToken = new AndroidAuth()
                    .getAuthTokenResponse(AndroidContext.baseDevice().setEmail(email), password,
                            "androidmarket", "com.android.vending", "10321bd893f69af97f7573aafe9de1dc0901f3a1",
                            false, AuthType.Password).getData(DataField.AUTH_TOKEN);
            dfeContext.set(DfeClient.KEY_AUTHORIZATION_TOKEN, authToken);
        }
        return client;
    }

    public SecureGooglePlayConnection(String email, String password,
			String androidId, String operatorAlpha, String operatorNumeric, String deviceName, int sdkVersion) {
		this(email, password, androidId, GooglePlayConnection.DEFAULT_CACHE_DIR, operatorAlpha,
				operatorNumeric, deviceName, sdkVersion);
	}

	private File downloadPart(Market.GetAssetResponse.InstallAsset installAsset, File file,
			SecureGooglePlayConnection.Listener listener, int originalSize) {
		final File part = new File(file.getAbsolutePath() + ".part");
		if (originalSize == 0 && part.exists()) {
			part.delete();
		}
		int downloadedSize = (int) part.length();
		HttpURLConnection conn = setupConnectionForDownload(installAsset,
        			downloadedSize);

		InputStream inputstream = null;
		BufferedOutputStream stream = null;
		try {
                        final ConnResult connRes = openConnectionCheckRedirects(conn);
                        inputstream = connRes.getIs();
                        conn = connRes.getConn();
			stream = new BufferedOutputStream(new FileOutputStream(part, true));
			final int totalSize = downloadedSize
					+ (int) ((conn.getContentLength() != -1) ? conn
							.getContentLength() : installAsset.getAssetSize());
			final byte buf[] = new byte[1024];
			int bufferlength = 0;
			long lastPublish = 0;
			double lastPercent = 0;
			while ((bufferlength = inputstream.read(buf)) != -1) {
				stream.write(buf, 0, bufferlength);
				downloadedSize += bufferlength;
				final long t = new Date().getTime();
				final double percent = (double) downloadedSize
						/ (double) totalSize;
				if (percent < 1
						&& (lastPublish + 1000 < t || percent > lastPercent + 0.05)) {
					listener.onDownloadProgress(downloadedSize, totalSize);
					lastPublish = t;
					lastPercent = percent;
				}
				if (listener.isCancelled()) {
					stream.close();
					return null;
				}
			}
			inputstream.close();
			stream.close();
			part.renameTo(file);
			return file;
		} catch (final SocketException e) {
			Log.d(TAG, "appDownload", e);
			if (inputstream != null) {
				try {
					inputstream.close();
				} catch (final IOException ex) {
				}
			}
			if (stream != null) {
				try {
					stream.close();
				} catch (final IOException ex) {
				}
			}
			if (downloadedSize > originalSize) {
				Log.d(TAG, "appDownload: nevermind - tryin to continue");
				return downloadPart(installAsset, file, listener,
						downloadedSize);
			} else {
				Log.d(TAG,
						"appDownload: uhm - continue didn't help - than bail out!");
				return null;
			}
		} catch (final Exception e) {
			Log.d(TAG, "appDownload", e);
			return null;
		}
	}

	private Market.GetAssetResponse.InstallAsset getInstallAssetSynced(String packageName) {
        DfeClient client = getClient();
        DfeClient.DEBUG = true;
        DfeResponse<DetailsResponse> response = client.details(packageName);
        DfeResponse<DeliveryResponse> deliveryResponse =
                client.deliver(response.getResponse().docV2.docid,
                        response.getResponse().docV2.details.appDetails.versionCode);
        AndroidAppDeliveryData appDeliveryData;
        if (deliveryResponse.getResponse() == null || deliveryResponse.getResponse().status == 3) {
            DfeResponse<BuyResponse> buyResponse =
                    client.purchase(response.getResponse().docV2.docid,
                            response.getResponse().docV2.details.appDetails.versionCode);
            if (buyResponse.getResponse() != null && buyResponse.getResponse().purchaseStatusResponse != null &&
                    buyResponse.getResponse().purchaseStatusResponse.appDeliveryData != null) {
                appDeliveryData = buyResponse.getResponse().purchaseStatusResponse.appDeliveryData;
            } else {
                deliveryResponse = client.deliver(response.getResponse().docV2.docid,
                        response.getResponse().docV2.details.appDetails.versionCode);
                appDeliveryData = deliveryResponse.getResponse().appDeliveryData;
            }
        } else {
            appDeliveryData = deliveryResponse.getResponse().appDeliveryData;
        }
        if (appDeliveryData == null) {
            throw new RuntimeException("No app data!");
        }
        return Market.GetAssetResponse.InstallAsset.newBuilder()
                .setBlobUrl(appDeliveryData.downloadUrl)
                .setDownloadAuthCookieName(appDeliveryData.downloadAuthCookie.get(0).name)
                .setDownloadAuthCookieValue(appDeliveryData.downloadAuthCookie.get(0).value)
                .setVersionCode(response.getResponse().docV2.details.appDetails.versionCode)
                .setAssetSize(appDeliveryData.downloadSize).build();
	}

	@Override
	public boolean hasQueryAppDownload() {
		return true;
	}

    @Override
    public File queryAppDownload(Market.App app,
                                 SecureGooglePlayConnection.Listener listener) {
        Log.d(TAG, "appDownload: " + app.getPackageName());
        final File dir = new File(getCacheDir(), app.getPackageName());
        makeDirectoryReady(dir);
        File file = new File(dir, "APP-" + app.getVersionCode() + ".apk");
        if (file.exists()) {
            return file;
        }
        Market.GetAssetResponse.InstallAsset installAsset = getInstallAssetSynced(app.getPackageName());
        file = new File(dir, "APP-" + installAsset.getVersionCode() + ".apk");
        if (file.exists()) {
            return file;
        }
        return downloadPart(installAsset, file, listener, 0);
    }

    void makeDirectoryReady(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (!dir.exists()) {
            throw new RuntimeException("could not init " + dir + "!");
        }
        if (dir.isFile()) {
            throw new RuntimeException(dir + " must not be a file!");
        }
    }

    private HttpURLConnection setupConnectionForDownload(
			Market.GetAssetResponse.InstallAsset installAsset, int downloadedSize) {
		try {
			final HttpURLConnection conn = (HttpURLConnection) new URL(
					installAsset.getBlobUrl()).openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("User-Agent",
					"Android-Market/2 (sapphire PLAT-RC33); gzip");
			conn.setRequestProperty("Cookie",
					installAsset.getDownloadAuthCookieName() + "="
							+ installAsset.getDownloadAuthCookieValue()) ;
			if (downloadedSize > 0) {
				conn.setRequestProperty("Range", "bytes=" + downloadedSize
						+ "-");
			}
			return conn;
		} catch (final Exception e) {
			Log.d("SecureGooglePlay", "connectDownload", e);
			return null;
		}
	}

    private ConnResult openConnectionCheckRedirects(HttpURLConnection c) throws IOException {
        boolean redir;
        int redirects = 0;
        InputStream in = null;

        final String rMethod = c.getRequestMethod();
        final String rPropUa = c.getRequestProperty("User-Agent");
        final String rPropCo = c.getRequestProperty("Cookie");
        do {
            c.setInstanceFollowRedirects(false);
            // We want to open the input stream before getting headers
            // because getHeaderField() et al swallow IOExceptions.
            in = c.getInputStream();
            redir = false;
            HttpURLConnection http = c;
            int stat = http.getResponseCode();
            if (stat >= 300 && stat <= 307 && stat != 306
                    && stat != HttpURLConnection.HTTP_NOT_MODIFIED) {
                Log.d(TAG, "openConnection: Redirecting!");
                URL base = http.getURL();
                String loc = http.getHeaderField("Location");
                 Log.d(TAG, "openConnection: Loc: " + loc);
                URL target = null;
                if (loc != null) {
                    target = new URL(base, loc);
}
                http.disconnect();
                // Redirection should be allowed only for HTTP and HTTPS
                // and should be limited to 5 redirections at most.
                if (target == null || !(target.getProtocol().equals("http")
                        || target.getProtocol().equals("https"))
                        || redirects >= 5) {
                    throw new SecurityException("illegal URL redirect");
                }
                redir = true;
                c = (HttpURLConnection) target.openConnection();
                c.setRequestMethod(rMethod);
                c.setRequestProperty("User-Agent", rPropUa);
                c.setRequestProperty("Cookie", rPropCo);
                redirects++;
            }
        } while (redir);
        return new ConnResult(in,c);
    }

    final class ConnResult {

        private final InputStream is;
        private final HttpURLConnection conn;

        public ConnResult(InputStream is, HttpURLConnection conn) {
            this.is = is;
            this.conn = conn;
        }

        public InputStream getIs() {
            return is;
        }

        public HttpURLConnection getConn() {
            return conn;
        }

    }
}
