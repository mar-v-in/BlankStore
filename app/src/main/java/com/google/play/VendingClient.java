package com.google.play;

import com.google.android.AndroidRequestKeys;
import com.google.play.proto.*;
import com.google.tools.Base64;
import com.google.tools.Client;
import com.google.tools.RequestContext;
import com.squareup.wire.Wire;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class VendingClient extends Client implements AndroidRequestKeys.BuildMetrics, AndroidRequestKeys.DeviceIdentifiers, AndroidRequestKeys.UserMetrics {

    public static final int SOFTWARE_VERSION = 8015017;
    private final static int PROTOCOL_VERSION = 2;
    private final static String REQUEST_COOKIE_FIELD = "Cookie";
	private final static String REQUEST_USER_AGENT_FIELD = "User-Agent";
	private final static String REQUEST_ACCEPT_CHARSET_FIELD = "Accept-Charset";
	private final static String REQUEST_ACCEPT_CHARSET = "utf-8;q=0.7,*;q=0.7";
	private final static String REQUEST_X_PUBLIC_ANDROID_ID_FIELD = "X-Public-Android-Id";
    protected RequestContext context;

    public VendingClient(RequestContext context) {
        this.context = context;
    }

    protected void prepareConnection(final HttpURLConnection connection, final RequestInfo info) {
        prepareConnection(connection, false);
        connection.setRequestProperty(REQUEST_COOKIE_FIELD, info.getCookie());
        connection.setRequestProperty(REQUEST_X_PUBLIC_ANDROID_ID_FIELD, info.getAndroidId());
        connection.setRequestProperty(REQUEST_USER_AGENT_FIELD, "Android-Market/2");
        connection.setRequestProperty(REQUEST_ACCEPT_CHARSET_FIELD, REQUEST_ACCEPT_CHARSET);
    }

    public RequestProto createRequestProto(RequestPropertiesProto props, List<InnerRequestProto> inner) {
        return new RequestProto(props, inner);
    }

    public RequestProto createRequestProto(RequestPropertiesProto props,
                                           InnerRequestProto request) {
        return createRequestProto(props, Arrays.asList(request));
    }

    public InnerRequestProto createRequest(CheckLicenseRequestProto request) {
        return new InnerRequestProto.Builder().checkLicenseRequest(request).build();
        }

    public CheckLicenseResponseProto sendRequest(RequestPropertiesProto props,
                                                 CheckLicenseRequestProto request,
                                                 RequestInfo info) {
        InnerResponseProto r = sendRequest(props, createRequest(request), info);
        if (r != null && r.checkLicenseResponse != null) {
            return r.checkLicenseResponse;
        } else {
            return null;
        }
    }

    public List<InnerResponseProto> sendRequests(RequestPropertiesProto props,
                                                 final List<InnerRequestProto> requests,
                                                 final RequestInfo info) {
        return sendRequest(createRequestProto(props, requests), info).Response;
    }

    public InnerResponseProto sendRequest(RequestPropertiesProto props,
                                          final InnerRequestProto request,
                                          final RequestInfo info) {
        ResponseProto r = sendRequest(createRequestProto(props, request), info);
        if (r != null && !r.Response.isEmpty()) {
            return r.Response.get(0);
        } else {
            return null;
        }
    }

    public ResponseProto sendRequest(final RequestProto request, final RequestInfo info) {
        byte[] bytes = null;
        try {
            if (DEBUG) {
                System.out.println(request);
            }
            bytes = sendString("version=" + PROTOCOL_VERSION + "&request=" +
                    Base64.encodeToString(request.toByteArray(), Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP), info);
            return new Wire().parseFrom(bytes, ResponseProto.class);
        } catch (final Exception e) {
            if (DEBUG) {
                e.printStackTrace();
                if (bytes != null) {
					try {
						FileOutputStream stream = new FileOutputStream("/data/local/tmp/vending.debug");
						stream.write(bytes);
						stream.flush();
						stream.close();
					} catch (Exception ex) {
					}
					System.out.println(new String(bytes));
				}
			}
			return null;
		}
	}

	private byte[] sendString(final HttpURLConnection connection, final String string, final RequestInfo info) {
		prepareConnection(connection, info);
        writeData(connection, string, false);
        return readData(connection, isError(connection), true);
	}

	private byte[] sendString(final String string, final RequestInfo info) {
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) info.getRequestUrl().openConnection();
			return sendString(connection, string, info);
		} catch (final IOException e) {
			if (DEBUG) {
				System.err.println("Could not open Connection!");
			}
			throw new RuntimeException("Could not open Connection!", e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

    public RequestInfo getRequestInfo(String service, String authToken) {
        return new RequestInfo(authToken, service, context.getString(KEY_ANDROID_ID_HEX),
                context.getString(KEY_BUILD_DEVICE) + " " + context.getString(KEY_BUILD_ID));
    }

    public RequestPropertiesProto getRequestProperties(String authToken) {
        RequestPropertiesProto.Builder props = new RequestPropertiesProto.Builder();
        props.aid(context.getString(KEY_ANDROID_ID_HEX));
        props.userCountry(context.getString(KEY_LOCALE).split("_")[1]);
        props.userLanguage(context.getString(KEY_LOCALE).split("_")[0]);
        props.softwareVersion(SOFTWARE_VERSION);
        props.productNameAndVersion(context.getString(KEY_BUILD_DEVICE) + ":" + context.getInt(KEY_SDK_VERSION, 16));
        props.clientId(context.getString(KEY_CLIENT_ID));
        props.loggingId(context.getString(KEY_LOGGING_ID));
        props.userAuthToken(authToken);
        props.userAuthTokenSecure(true);
        return props.build();
    }

    public CheckLicenseResponseProto checkLicenseRequest(String service, String authToken, String packageName, int versionCode, long nonce) {
        CheckLicenseRequestProto request = new CheckLicenseRequestProto(packageName, versionCode, nonce);
        return sendRequest(getRequestProperties(authToken), request, getRequestInfo(service, authToken));
    }

    public static class RequestInfo {

		private static final String DEFAULT_URL = "https://android.clients.google.com/vending/api/ApiRequest";
		private final String authToken;
		private URL requestUrl;
		private String deviceIdent; // like "maguro JRO03L"
		private String service; // android, androidmarket or androidsecure
		private String androidId;

		public RequestInfo(final String authToken, final String service, final String androidId,
						   final String deviceIdent) {
			this.authToken = authToken;
			this.service = service;
			this.androidId = androidId;
			this.deviceIdent = deviceIdent;
			setRequestUrl(DEFAULT_URL);
		}

		public RequestInfo(final String authToken, final String requestUrl, final String service,
						   final String androidId, final String deviceIdent) {
			this.authToken = authToken;
			this.service = service;
			this.androidId = androidId;
			this.deviceIdent = deviceIdent;
			setRequestUrl(requestUrl);
		}

		public String getDeviceIdent() {
			return deviceIdent;
		}

		public String getAndroidId() {
			return androidId;
		}

		public String getCookie() {
			return service.toUpperCase() + "=" + authToken;
		}

		public URL getRequestUrl() {
			return requestUrl;
		}

		public void setRequestUrl(final URL requestUrl) {
			if (requestUrl == null) {
				throw new RuntimeException("RequestUrl should not be null!");
			}
			this.requestUrl = requestUrl;
		}

		public void setRequestUrl(final String requestUrl) {
			if (requestUrl == null || requestUrl.isEmpty()) {
				throw new RuntimeException("RequestUrl should not be empty!");
			}
			try {
				setRequestUrl(new URL(requestUrl));
			} catch (final Exception e) {
				throw new RuntimeException("RequestUrl should be a valid URL!", e);
			}
		}

		public String getService() {
			return service;
		}

	}
}
