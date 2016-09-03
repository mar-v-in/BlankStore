package com.google.android;

import com.google.auth.*;
import com.google.tools.Client;

public class AndroidAuth extends AuthClient implements AndroidRequestKeys.UserMetrics {

	private final static String ACCOUNT_TYPE_HOSTED_OR_GOOGLE = "HOSTED_OR_GOOGLE";
	private static final String REQUEST_URL_ANDROID_CLIENT_AUTH = "https://android.clients.google.com/auth";
	private final static String SERVICE_DEFAULT_AC2DM = "ac2dm";
	private final static String SOURCE_ANDROID = "android";

	private AuthRequest createBaseRequest(final AndroidContext info) {
		final AuthRequest request = new AuthRequest();
		request.setRequestUrl(REQUEST_URL_ANDROID_CLIENT_AUTH);
		request.putData(DataField.ACCOUNT_TYPE, ACCOUNT_TYPE_HOSTED_OR_GOOGLE);
		request.putData(DataField.SOURCE, SOURCE_ANDROID);
		request.putData(DataField.SERVICE, SERVICE_DEFAULT_AC2DM);
		request.putData(DataField.EMAIL, info.getEmail());
        if (info.getAndroidId() != null && info.getAndroidId() != Long.MIN_VALUE) {
            request.putData(DataField.ANDROID_ID, info.getAndroidIdHex());
		}
        String[] locale = info.get(KEY_LOCALE, "en_US").split("_");
        if (locale.length != 2) {
            locale = new String[]{"en", "US"};
        }
        request.putData(DataField.DEVICE_COUNTRY, locale[1]);
        request.putData(DataField.OPERATOR_COUNTRY, locale[1]);
        request.putData(DataField.LANGUAGE, locale[0]);
        request.putData(DataField.SDK_VERSION, Integer.toString(info.getSdkVersion()));
        request.setUserAgent("GoogleLoginService/1.3 (" + info.getBuildDevice() + " " + info.getBuildId() + ")");
		return request;
	}

	public String getAuthToken(final AndroidContext info, final String masterToken, final String service) {
		return getAuthToken(info, masterToken, service, AuthType.MasterToken);
	}

	public String getAuthToken(final AndroidContext info, final String auth, final String service,
							   final AuthType authType) {
		final AuthRequest request = createBaseRequest(info);
		saveAuthInRequest(request, authType, auth);
		request.putData(DataField.SERVICE, service);
		final AuthResponse response = sendRequest(request);
		if (DEBUG) {
			System.out.println(response.toString());
		}
		return response.getData(DataField.AUTH_TOKEN);
	}

	public String getAuthToken(final AndroidContext info, final String masterToken, final String service,
							   final String packageName, final String packageSignature,
							   final boolean storedPermission) {
		final DataMapReader response =
				getAuthTokenResponse(info, masterToken, service, packageName, packageSignature, storedPermission);
		return response.getData(DataField.AUTH_TOKEN);
	}

	public DataMapReader getAuthTokenResponse(final AndroidContext info, final String auth, final String service,
											  final String packageName, final String packageSignature,
											  final boolean storedPermission) {
		return getAuthTokenResponse(info, auth, service, packageName, packageSignature, storedPermission,
									AuthType.MasterToken);
	}

	public DataMapReader getAuthTokenResponse(final AndroidContext info, final String auth, final String service,
											  final String packageName, final String packageSignature,
											  final boolean storedPermission, final AuthType authType) {
		if (DEBUG) {
			System.out.println(
					"getAuthToken: " + service + " | " + hide(auth) + " | " + packageName + " | " + packageSignature +
					" | " + authType);
		}
		final AuthRequest request = createBaseRequest(info);
		saveAuthInRequest(request, authType, auth);
		request.putData(DataField.SERVICE, service);
		request.putData(DataField.PACKAGE_NAME, packageName);
		request.putData(DataField.PACKAGE_SIGNATURE, packageSignature);
		request.putData(DataField.STORED_PERMISSION, !storedPermission ? "1" : "0");
		final AuthResponse response = sendRequest(request);
		if (DEBUG) {
			System.out.println(response.toString());
		}
		return response;
	}

	public String getMasterToken(final AndroidContext info, final String auth, final AuthType authType) {
		final DataMapReader response = getMasterTokenResponse(info, auth, authType);
		return response.getData(DataField.MASTER_TOKEN);
	}

	public String getMasterToken(final AndroidContext info, final String password, final AuthType authType,
								 final String service) {
		final DataMapReader response = getMasterTokenResponse(info, password, authType, service);
		return response.getData(DataField.MASTER_TOKEN);
	}

	public DataMapReader getMasterTokenResponse(final AndroidContext info, final String auth, final AuthType authType) {
		return getMasterTokenResponse(info, auth, authType, null);
	}

	public DataMapReader getMasterTokenResponse(final AndroidContext info, final String auth, final AuthType authType,
												final String service) {
		final AuthRequest request = createBaseRequest(info);
		saveAuthInRequest(request, authType, auth);
		if (service != null) {
			request.putData(DataField.SERVICE, service);
		}
		final AuthResponse response = sendRequest(request);
		if (Client.DEBUG) {
			System.out.println(response.toString());
		}
		return response;
	}

	private static String hide(final String s) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			if (i % 2 == 1) {
				sb.append("*");
			} else {
				sb.append(s.charAt(i));
			}
		}
		return sb.toString();
	}

	private static void saveAuthInRequest(final AuthRequest request, final AuthType type, final String auth) {
		switch (type) {
			case MasterToken:
				request.putData(DataField.MASTER_TOKEN, auth);
				break;
			case Password:
				request.putData(DataField.PASSWORD, auth);
				break;
			case EncryptedPassword:
				request.putData(DataField.ENCRYPTED_PASSWORD, auth);
				break;
			case AuthToken:
				request.putData(DataField.AUTH_TOKEN, auth);
				break;
		}
	}
}
