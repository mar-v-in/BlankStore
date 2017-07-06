package com.google.checkin;

import com.google.android.AndroidRequestKeys;
import com.google.tools.Client;
import com.google.tools.RequestContext;
import com.squareup.wire.Wire;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CheckinClient extends Client implements
        AndroidRequestKeys.BuildMetrics, AndroidRequestKeys.DeviceMetrics,
        AndroidRequestKeys.OperatorMetrics, AndroidRequestKeys.DeviceIdentifiers,
        AndroidRequestKeys.UserIdentifiers, AndroidRequestKeys.UserMetrics {
    private static final String REQUEST_CONTENT_TYPE = "application/x-protobuffer";
    private static final String CHECKIN_URL = "https://android.clients.google.com/checkin";
    private static final int VERSION = 3;
    private final Wire wire = new Wire();

    public CheckinClient() {
        requestContentType = REQUEST_CONTENT_TYPE;
    }

    public Response sendRequest(final Request request, final String url) {

		byte[] bytes = null;
		try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            prepareConnection(connection, false);
            setUserAgent(connection, "Android-Checkin/2.0");
            writeData(connection, request.toByteArray(), false);
            bytes = readData(connection, false);
			return wire.parseFrom(bytes, Response.class);
		} catch (final Exception e) {
			if (DEBUG_ERROR) {
				e.printStackTrace(System.err);
				if (bytes != null) {
					System.err.println(new String(bytes));
				}
			}
			throw new RuntimeException(e);
		}

	}

    public CheckinResponse checkin(RequestContext context) {
        return checkin(context, context.getString(KEY_AUTHORIZATION_TOKEN));
    }

    public CheckinResponse checkin(RequestContext context, String authToken) {
        Object TODO = null;
        long now = System.currentTimeMillis();
        Request request = new Request.Builder()
                .accountCookie(buildAccountCookie(context.getString(KEY_EMAIL), authToken))
                .androidId(context.getLong(KEY_ANDROID_ID_LONG))
                .checkin(new Request.Checkin.Builder()
                        .build(new Request.Checkin.Build.Builder()
                                .bootloader(context.getString(KEY_BUILD_BOOTLOADER))
                                .brand(context.getString(KEY_BUILD_BRAND))
                                .clientId(context.getString(KEY_CLIENT_ID))
                                .device(context.getString(KEY_BUILD_DEVICE))
                                .fingerprint(context.getString(KEY_BUILD_FINGERPRINT))
                                .hardware(context.getString(KEY_BUILD_HARDWARE))
                                .manufacturer(context.getString(KEY_BUILD_MANUFACTURER))
                                .model(context.getString(KEY_BUILD_MODEL))
                                .otaInstalled(context.getBoolean(KEY_OTA_INSTALLED, false))
                                .packageVersionCode(context.getInt(KEY_SDK_VERSION))
                                .product(context.getString(KEY_BUILD_PRODUCT))
                                .radio(context.getString(KEY_BUILD_RADIO))
                                .sdkVersion(context.getInt(KEY_SDK_VERSION))
                                .time(context.getLong(KEY_BUILD_TIME, now) / 1000L)
                                .build())
                        .cellOperator(context.getString(KEY_CELL_OPERATOR_NUMERIC))
                        .event(Arrays.asList(new Request.Checkin.Event("event_log_start", null, now)))
                        .lastCheckinMs(0L)
                                //.requestedGroup(TODO)
                        .roaming(context.getString(KEY_ROAMING))
                        .simOperator(context.getString(KEY_SIM_OPERATOR_NUMERIC))
                                //.stat(TODO)
                        .userNumber(0)
                        .build())
                        //.desiredBuild(TODO)
                .deviceConfiguration(new Request.DeviceConfig.Builder()
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
                        .widthPixels(context.getInt(KEY_DEVICE_SCREEN_WIDTH_PX))
                        .build())
                .digest(context.getString(KEY_CHECKIN_DIGEST))
                .esn(context.getString(KEY_ESN))
                .fragment(0)
                        //.imei(context.getString(KEY_IMEI)) // Note: this is never set, use meid instead
                .locale(context.getString(KEY_LOCALE))
                .loggingId(context.getLong(KEY_LOGGING_ID))
                .macAddress(Arrays.asList(context.getString(KEY_WIFI_MAC_ADDRESS)))
                .macAddressType(Arrays.asList("wifi"))
                        //.marketCheckin(TODO)
                .meid(context.get(KEY_IMEI, context.getString(KEY_MEID)))
                .otaCert(Arrays.asList(context.getString(KEY_OTA_CERT)))
                .securityToken(context.getLong(KEY_CHECKIN_SECURITY_TOKEN))
                .serial(context.getString(KEY_BUILD_SERIAL))
                .timeZone(context.getString(KEY_TIME_ZONE))
                        //.userName(TODO)
                        //.userSerialNumber(TODO)
                .version(VERSION)
                .build();

        if (Client.DEBUG) {
            System.out.println(request);
        }

        final Response response = sendRequest(request, CHECKIN_URL);
        if (Client.DEBUG) {
            System.out.println(response);
        }

        if (response == null) {
            return null;
        } else {
            return new CheckinResponse(response);
        }
    }

    private List<String> buildAccountCookie(String email, String authToken) {
        if (email != null && !email.isEmpty()) {
            if (authToken != null && !authToken.isEmpty()) {
                return Arrays.asList("[" + email + "]", authToken);
            } else {
                return Arrays.asList("[" + email + "]");
            }
        }
        return Arrays.asList("");
    }
}
