package com.google.tools;

import java.util.HashMap;
import java.util.Map;

public class RequestContext extends HashMap<String, String> {
	public static final String KEY_AUTHORIZATION_TOKEN = "authToken";
	public static final String KEY_ANDROID_ID_HEX = "androidIdHex";
	public static final String KEY_ANDROID_ID_LONG = "androidIdLong";
	public static final String KEY_CELL_OPERATOR_NUMERIC = "cellOperatorNumeric";
	public static final String KEY_CELL_OPERATOR_NAME = "cellOperatorName";
	public static final String KEY_CLIENT_ID = "clientId";
	public static final String KEY_LOGGING_ID = "loggingId";
	public static final String KEY_SMALEST_SCREEN_WIDTH_DP = "smalestScreenWidthDp";
	public static final String KEY_FILTER_LEVEL = "filterLevel";
	public static final String KEY_HTTP_USER_AGENT = "userAgent";

	public RequestContext() {
	}

	public RequestContext(Map<? extends String, ? extends String> m) {
		super(m);
	}

	public long getLong(String key) {
		try {
			return Long.parseLong(get(key));
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public int getInt(String key) {
		return Integer.parseInt(get(key));
	}

	public String put(String key, int value) {
		return put(key, Integer.toString(value));
	}

	public String put(String key, long value) {
		return put(key, Long.toString(value));
	}
}
