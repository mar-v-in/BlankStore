package com.google.c2dm;

import com.google.android.AndroidContext;
import com.google.android.AndroidRequestKeys;
import com.google.tools.Client;
import com.google.tools.RequestContext;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class C2DMClient extends Client implements
        AndroidRequestKeys.DeviceIdentifiers, AndroidRequestKeys.OperatorMetrics,
        AndroidRequestKeys.UserIdentifiers, AndroidRequestKeys.BuildMetrics {
    private static final String REGISTER_URL = "https://android.clients.google.com/c2dm/register3";

    public static final String KEY_SMALEST_SCREEN_WIDTH_DP = "smalestScreenWidthDp";
    public static final String KEY_FILTER_LEVEL = "filterLevel";
    protected static final String REQUEST_CONTENT_TYPE_FORM = "application/x-www-form-urlencoded; charset=UTF-8";

    protected RequestContext context;

    public C2DMClient(RequestContext context) {
        this.context = context;
    }


    protected void prepareConnection(HttpURLConnection connection, String postType) {
        if (postType != null) {
            super.prepareConnection(connection, false);
            connection.setRequestProperty(REQUEST_CONTENT_TYPE_FIELD, postType);
        } else {
            connection.setUseCaches(false);
            connection.setDoInput(true);
        }
        connection.setRequestProperty("X-DFE-MCCMNC", context.getString(KEY_CELL_OPERATOR_NUMERIC));
        connection.setRequestProperty("Authorization", "GoogleLogin auth=" + context.getString(KEY_AUTHORIZATION_TOKEN));
        connection.setRequestProperty("X-DFE-Device-Id", context.getString(KEY_ANDROID_ID_HEX));
        connection.setRequestProperty("X-DFE-Client-Id", "am-android-google");
        connection.setRequestProperty("X-DFE-Logging-Id", context.getString(KEY_LOGGING_ID));
        connection.setRequestProperty("X-DFE-SmallestScreenWidthDp", context.getString(KEY_SMALEST_SCREEN_WIDTH_DP));
        connection.setRequestProperty("X-DFE-Filter-Level", context.getString(KEY_FILTER_LEVEL));
        connection.setRequestProperty("Accept-Language", context.get(AndroidContext.KEY_LOCALE, "en_US").replace("_", "-"));
        setUserAgent(connection, context);
    }

    public String registerC2DM(String app, String appCert, String sender, Map<String, String> extras) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(REGISTER_URL).openConnection();
            prepareConnection(connection, REQUEST_CONTENT_TYPE_FORM);
            //connection.setRequestProperty("app", app);
            if (extras != null) {
                for (String key : extras.keySet()) {
                    connection.setRequestProperty("X-" + key, extras.get(key));
                }
            }
            String send = "app=" + app + "&sender=" + URLEncoder.encode(sender) + "&device=" + context.getLong(KEY_ANDROID_ID_LONG) + "&device_user_id=0";
            if (DEBUG) {
                System.out.println("C2DMregister.out: " + send);
            }
            writeData(connection, send.getBytes(), false);
            byte[] bytes = readData(connection, false);
            String result = new String(bytes);
            if (DEBUG) {
                System.out.println("C2DMregister.in: " + result);
            }
            String token = null;
            for (String keyval : result.split("\n")) {
                if (keyval.startsWith("token=")) {
                    token = keyval.substring(6);
                }
            }
            return token;
        } catch (IOException e) {
            if (DEBUG_ERROR) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
