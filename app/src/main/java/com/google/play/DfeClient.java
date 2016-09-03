package com.google.play;

import com.google.android.AndroidRequestKeys;
import com.google.c2dm.C2DMClient;
import com.google.play.proto.*;
import com.google.tools.RequestContext;
import com.squareup.wire.Wire;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DfeClient extends C2DMClient implements AndroidRequestKeys.DeviceMetrics, AndroidRequestKeys.BuildMetrics {
    protected static final String REQUEST_CONTENT_TYPE_PROTOBUF = "application/x-protobuf";
	private static final String BASE_URL = "https://android.clients.google.com/fdfe/";
	private static final String PURCHASE_URL = BASE_URL + "purchase";
	private static final String BROWSE_URL = BASE_URL + "browse";
	private static final String DELIVERY_URL = BASE_URL + "delivery";
	private static final String DETAILS_URL = BASE_URL + "details";
	private static final String SUGGEST_URL = BASE_URL + "suggest";
	private static final String LIST_URL = BASE_URL + "list";
	private static final String BULK_DETAILS_URL = BASE_URL + "bulkDetails";
	private static final String REPLICATE_LIBRARY_URL = BASE_URL + "replicateLibrary";
	private static final String UPLOAD_DEVICE_CONFIG_URL = BASE_URL + "uploadDeviceConfig";
	private static final String TOC_URL = BASE_URL + "toc";
	private static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";
	private static final SimpleDateFormat PATTERN_RFC1123_FORMAT = new SimpleDateFormat(PATTERN_RFC1123);

    public DfeClient(RequestContext context) {
        super(context);
	}

	private String prepareUrl(String url, String requestBase) {
		if (url.startsWith(requestBase))
			return url;
		if (url.startsWith(requestBase.substring(BASE_URL.length())))
			return BASE_URL + (url.startsWith("/") ? url.substring(1) : url);
		if (url.startsWith("?"))
			return requestBase + url;
		return requestBase + "?" + url;
	}

    public DfeResponse<BrowseResponse> browse(String url) {
        DfeResponse<BrowseResponse> response = simpleGetRequest(prepareUrl(url, BROWSE_URL));
        if (response.hasWrapperPayload())
            response.setResponse(response.getWrapper().payload.browseResponse);
        return response;
	}

    public DfeResponse<BulkDetailsResponse> bulkDetails(List<String> docIds, boolean includeChildDocs) {
        return bulkDetails(new BulkDetailsRequest(docIds, includeChildDocs));
    }

    public DfeResponse<BulkDetailsResponse> bulkDetails(BulkDetailsRequest request) {
        DfeResponse<BulkDetailsResponse> response =
                simplePostRequest(BULK_DETAILS_URL, REQUEST_CONTENT_TYPE_PROTOBUF, request.toByteArray());
		if (response.hasWrapperPayload())
            response.setResponse(response.getWrapper().payload.bulkDetailsResponse);
        return response;
	}

    public DfeResponse<DeliveryResponse> deliver(String docId, int versionCode) {
        return deliver(docId, 1, versionCode);
	}

    public DfeResponse<DeliveryResponse> deliver(String docId, int ot, int versionCode) {
        DfeResponse<DeliveryResponse> response =
                simpleGetRequest(DELIVERY_URL + "?doc=" + docId + "&ot=" + ot + "&vc=" + versionCode);
		if (response.hasWrapperPayload())
            response.setResponse(response.getWrapper().payload.deliveryResponse);
        return response;
	}

    public DfeResponse<DetailsResponse> details(String url) {
        DfeResponse<DetailsResponse> response =
                simpleGetRequest(prepareUrl(url.contains("=") ? url : ("doc=" + url), DETAILS_URL));
		if (response.hasWrapperPayload())
            response.setResponse(response.getWrapper().payload.detailsResponse);
        return response;
	}

    public DfeResponse<LibraryReplicationResponse> libraryReplication(
            LibraryReplicationRequest request) {
        DfeResponse<LibraryReplicationResponse> response =
                simplePostRequest(REPLICATE_LIBRARY_URL, REQUEST_CONTENT_TYPE_PROTOBUF, request.toByteArray());
		if (response.hasWrapperPayload())
            response.setResponse(response.getWrapper().payload.libraryReplicationResponse);
        return response;
	}

    public DfeResponse<ListResponse> list(String url) {
        DfeResponse<ListResponse> response = simpleGetRequest(prepareUrl(url, LIST_URL));
        if (response.hasWrapperPayload())
            response.setResponse(response.getWrapper().payload.listResponse);
        return response;
	}

    public DfeResponse<BuyResponse> purchase(String docId, int versionCode) {
        return purchase(docId, 1, versionCode);
	}

    public DfeResponse<BuyResponse> purchase(String docId, int ot, int versionCode) {
        DfeResponse<BuyResponse> response =
                simplePostRequest(PURCHASE_URL, REQUEST_CONTENT_TYPE_FORM, ("ot=" + ot + "&doc=" + docId +
																			"&vc=" + versionCode + "&").getBytes());
		if (response.hasWrapperPayload())
            response.setResponse(response.getWrapper().payload.buyResponse);
        return response;
	}

    public DfeResponse<ListResponse> suggest(String url) {
        DfeResponse<ListResponse> response = simpleGetRequest(prepareUrl(url, SUGGEST_URL));
        if (response.hasWrapperPayload())
            response.setResponse(response.getWrapper().payload.listResponse);
        return response;
	}

    public DfeResponse<TocResponse> toc(String shh, String deviceConfigToken) {
        HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) new URL(TOC_URL).openConnection();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		prepareConnection(connection, null);
		if (deviceConfigToken != null) {
			connection.setRequestProperty("X-DFE-Device-Config-Token", deviceConfigToken);
			/*try {
				Date date = new Date(Long.parseLong(deviceConfigToken));
				connection.setRequestProperty("If-Modified-Since", PATTERN_RFC1123_FORMAT.format(date));
				connection.setRequestProperty("If-None-Match", "3690"); // TODO: What's this?!
			} catch (Exception e) {
				// Ignore
			}*/
		}
		beforeRequest(connection);
        DfeResponse<TocResponse> response = parseResponseWrapper(connection);
        if (response.hasWrapperPayload())
            response.setResponse(response.getWrapper().payload.tocResponse);
        return response;
    }

    public DfeResponse<UploadDeviceConfigResponse> uploadDeviceConfig() {
        return uploadDeviceConfig(null);
    }

    public DfeResponse<UploadDeviceConfigResponse> uploadDeviceConfig(String deviceConfigToken) {
        return uploadDeviceConfig((String)null, deviceConfigToken);
    }

    public DfeResponse<UploadDeviceConfigResponse> uploadDeviceConfig(String gcmRegistrationId, String deviceConfigToken) {
        return uploadDeviceConfig(new DeviceConfig.Builder()
                .availableFeature(context.get(KEY_DEVICE_FEATURES, Collections.<String>emptyList()))
                .densityDpi(context.getInt(KEY_DEVICE_SCREEN_DPI))
                        //.deviceClass(context.getInt(KEY_DEVICE_CLASS))
                .glEsVersion(context.getInt(KEY_DEVICE_GLES_VERSION))
                .glExtension(context.get(KEY_DEVICE_GLES_EXTENSIONS, Collections.<String>emptyList()))
                .hasFiveWayNavigation(context.getBoolean(KEY_DEVICE_FIVE_WAY_NAVIGATION, false))
                .hasHardKeyboard(context.getBoolean(KEY_DEVICE_HARD_KEYBOARD, false))
                .heightPixels(context.getInt(KEY_DEVICE_SCREEN_HEIGHT_PX))
                .keyboardType(context.getInt(KEY_DEVICE_KEYBOARD_TYPE, 0))
                .locale(context.get(KEY_DEVICE_LOCALES, Collections.<String>emptyList()))
                        //.maxApkDownloadSizeMb(TODO)
                .nativePlatform(context.get(KEY_DEVICE_NATIVE_PLATFORM, Collections.<String>emptyList()))
                .navigation(context.getInt(KEY_DEVICE_NAVIGATION, 0))
                .screenLayout(context.getInt(KEY_DEVICE_SCREEN_LAYOUT, 0))
                .sharedLibrary(context.get(KEY_DEVICE_SHARED_LIBRARY, Collections.<String>emptyList()))
                .touchScreen(context.getInt(KEY_DEVICE_TOUCH_SCREEN, 0))
                .widthPixels(context.getInt(KEY_DEVICE_SCREEN_WIDTH_PX)).build(),
                context.getString(KEY_BUILD_MANUFACTURER), gcmRegistrationId, deviceConfigToken);
    }

    public DfeResponse<UploadDeviceConfigResponse> uploadDeviceConfig(DeviceConfig deviceConfig, String manufacturer,
                                                                      String gcmRegistrationId, String deviceConfigToken) {
        return uploadDeviceConfig(new UploadDeviceConfigRequest(deviceConfig, manufacturer, gcmRegistrationId), deviceConfigToken);
    }

    public DfeResponse<UploadDeviceConfigResponse> uploadDeviceConfig(UploadDeviceConfigRequest request, String deviceConfigToken) {
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) new URL(UPLOAD_DEVICE_CONFIG_URL).openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        prepareConnection(connection, REQUEST_CONTENT_TYPE_PROTOBUF);
        if (deviceConfigToken != null) {
            connection.setRequestProperty("X-DFE-Device-Config-Token", deviceConfigToken);
        }
        writeData(connection, request.toByteArray(), false);
        DfeResponse<UploadDeviceConfigResponse> response = parseResponseWrapper(connection);
        if (response.hasWrapperPayload())
            response.setResponse(response.getWrapper().payload.uploadDeviceConfigResponse);
        return response;
    }

	private <T> DfeResponse<T> simpleGetRequest(String url) {
		if (DEBUG)
			System.out.println("GET " + url);
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) new URL(url).openConnection();
			prepareConnection(connection, null);
            return parseResponseWrapper(connection);
        } catch (Throwable e) {
			if (connection != null) {
				try {
					return new DfeResponse<T>(connection.getResponseCode(), connection.getResponseMessage(), e);
				} catch (IOException ignored) {
				}
			}
			return new DfeResponse<T>(e);
		}
	}

	private <T> DfeResponse<T> simplePostRequest(String url, String postType, byte[] data) {
		if (DEBUG)
			System.out.println("POST " + url);
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			prepareConnection(connection, postType);
			writeData(connection, data, false);
            return parseResponseWrapper(connection);
        } catch (Throwable e) {
			return new DfeResponse<T>(e);
		}
	}

    private <T> DfeResponse<T> parseResponseWrapper(HttpURLConnection connection) {
        try {
            byte[] bytes = readData(connection, false);
            ResponseWrapper wrapper = new Wire().parseFrom(bytes, ResponseWrapper.class);
            return new DfeResponse<T>(wrapper, connection.getResponseCode(), connection.getResponseMessage());
        } catch (Throwable e) {
            if (connection != null) {
                try {
                    return new DfeResponse<T>(connection.getResponseCode(), connection.getResponseMessage(), e);
                } catch (IOException ignored) {
                }
            }
            return new DfeResponse<T>(e);
        }
    }

}
