/*
 * Copyright (c) 2014 μg Project Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.play;

import com.google.play.proto.*;
import com.google.tools.Client;
import com.google.tools.RequestContext;
import com.squareup.wire.Wire;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DfeClient extends Client {

	protected static final String REQUEST_CONTENT_TYPE_PROTOBUF = "application/x-protobuf";
	protected static final String REQUEST_CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
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
	private DfeContext context;

	public DfeClient(DfeContext context) {
		this.context = context;
	}

	protected void prepareConnection(HttpURLConnection connection, String postType) {
		if (postType != null) {
			super.prepareConnection(connection, false);
			connection.setRequestProperty(REQUEST_CONTENT_TYPE_FIELD, postType);
			/*
			byte[] nonce = new byte[100];
			new Random().nextBytes(nonce);
			try {
				connection.setRequestProperty("X-DFE-Signature-Request", "nonce="+Base64.encodeBytes(nonce, Base64.URL_SAFE));
			} catch (IOException e) {
				e.printStackTrace();
			}*/
		} else {
			connection.setUseCaches(false);
			connection.setDoInput(true);
		}
		connection.setRequestProperty("X-DFE-MCCMNC", context.get(RequestContext.KEY_CELL_OPERATOR_NUMERIC));
		connection.setRequestProperty("Authorization",
									  "GoogleLogin auth=" + context.get(RequestContext.KEY_AUTHORIZATION_TOKEN));
		connection.setRequestProperty("X-DFE-Device-Id", context.get(RequestContext.KEY_ANDROID_ID_HEX));
		connection.setRequestProperty("X-DFE-Client-Id", context.get(RequestContext.KEY_CLIENT_ID));
		connection.setRequestProperty("X-DFE-Logging-Id", context.get(RequestContext.KEY_LOGGING_ID));
		connection.setRequestProperty("X-DFE-SmallestScreenWidthDp",
									  context.get(RequestContext.KEY_SMALEST_SCREEN_WIDTH_DP));
		connection.setRequestProperty("X-DFE-Filter-Level", context.get(RequestContext.KEY_FILTER_LEVEL));
		connection.setRequestProperty("Accept-Language", "en-GB");
		setUserAgent(connection, context);
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

	public DfeResponse<BrowseResponse> requestBrowse(String url) {
		DfeResponse<BrowseResponse> response = simpleGetRequest(prepareUrl(url, BROWSE_URL));
		if (response.hasWrapperPayload())
			response.setResponse(response.getWrapper().payload.browseResponse);
		return response;
	}

	public DfeResponse<BulkDetailsResponse> requestBulkDetails(BulkDetailsRequest request) {
		DfeResponse<BulkDetailsResponse> response =
				simplePostRequest(BULK_DETAILS_URL, REQUEST_CONTENT_TYPE_PROTOBUF, request.toByteArray());
		if (response.hasWrapperPayload())
			response.setResponse(response.getWrapper().payload.bulkDetailsResponse);
		return response;
	}

	public DfeResponse<DeliveryResponse> requestDeliver(String docId, int versionCode) {
		return requestDeliver(docId, 1, versionCode);
	}

	public DfeResponse<DeliveryResponse> requestDeliver(String docId, int ot, int versionCode) {
		DfeResponse<DeliveryResponse> response =
				simpleGetRequest(DELIVERY_URL + "?doc=" + docId + "&ot=" + ot + "&vc=" + versionCode);
		if (response.hasWrapperPayload())
			response.setResponse(response.getWrapper().payload.deliveryResponse);
		return response;
	}

	public DfeResponse<DetailsResponse> requestDetails(String url) {
		DfeResponse<DetailsResponse> response =
				simpleGetRequest(prepareUrl(url.contains("=") ? url : ("doc=" + url), DETAILS_URL));
		if (response.hasWrapperPayload())
			response.setResponse(response.getWrapper().payload.detailsResponse);
		return response;
	}

	public DfeResponse<LibraryReplicationResponse> requestLibraryReplication(
			LibraryReplicationRequest request) {
		DfeResponse<LibraryReplicationResponse> response =
				simplePostRequest(REPLICATE_LIBRARY_URL, REQUEST_CONTENT_TYPE_PROTOBUF, request.toByteArray());
		if (response.hasWrapperPayload())
			response.setResponse(response.getWrapper().payload.libraryReplicationResponse);
		return response;
	}

	public DfeResponse<ListResponse> requestList(String url) {
		DfeResponse<ListResponse> response = simpleGetRequest(prepareUrl(url, LIST_URL));
		if (response.hasWrapperPayload())
			response.setResponse(response.getWrapper().payload.listResponse);
		return response;
	}

	// untested
	public DfeResponse<BuyResponse> requestPurchase(String docId, int versionCode) {
		return requestPurchase(docId, 1, versionCode);
	}

	// untested
	public DfeResponse<BuyResponse> requestPurchase(String docId, int ot, int versionCode) {
		DfeResponse<BuyResponse> response =
				simplePostRequest(PURCHASE_URL, REQUEST_CONTENT_TYPE_FORM, ("ot=" + ot + "&doc=" + docId +
																			"&vc=" + versionCode + "&").getBytes());
		if (response.hasWrapperPayload())
			response.setResponse(response.getWrapper().payload.buyResponse);
		return response;
	}

	public DfeResponse<ListResponse> requestSuggest(String url) {
		DfeResponse<ListResponse> response = simpleGetRequest(prepareUrl(url, SUGGEST_URL));
		if (response.hasWrapperPayload())
			response.setResponse(response.getWrapper().payload.listResponse);
		return response;
	}

	public DfeResponse<TocResponse> requestToc(String shh, String deviceConfigToken) {
		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) new URL(TOC_URL + "?shh=" + shh).openConnection();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		prepareConnection(connection, false);
		if (deviceConfigToken != null) {
			connection.setRequestProperty("X-DFE-Device-Config-Token", deviceConfigToken);
			try {
				Date date = new Date(Long.parseLong(deviceConfigToken));
				connection.setRequestProperty("If-Modified-Since", PATTERN_RFC1123_FORMAT.format(date));
				connection.setRequestProperty("If-None-Match", "3690"); // TODO: What's this?!
			} catch (Exception e) {
				// Ignore
			}
		}
		beforeRequest(connection);
		byte[] bytes = readData(connection, false);
        ResponseWrapper wrapper = null;
        try {
            wrapper = new Wire().parseFrom(bytes, ResponseWrapper.class);
            return new DfeResponse<TocResponse>(wrapper, wrapper.payload.tocResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}

	private <T> DfeResponse<T> simpleGetRequest(String url) {
		if (DEBUG)
			System.out.println("GET " + url);
		HttpURLConnection connection = null;
        byte[] bytes = null;
		try {
			connection = (HttpURLConnection) new URL(url).openConnection();
			prepareConnection(connection, null);
			bytes = readData(connection, false);
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

	private <T> DfeResponse<T> simplePostRequest(String url, String postType, byte[] data) {
		if (DEBUG)
			System.out.println("POST " + url);
        HttpURLConnection connection = null;
        byte[] bytes = null;
		try {
			connection = (HttpURLConnection) new URL(url).openConnection();
			prepareConnection(connection, postType);
			writeData(connection, data, false);
			bytes = readData(connection, false);
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
