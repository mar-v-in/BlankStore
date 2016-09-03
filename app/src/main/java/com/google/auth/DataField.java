package com.google.auth;

import java.util.HashMap;

public enum DataField {
	ACCOUNT_TYPE("accountType"), ADDED_ACCOUNT("add_account"), ANDROID_ID("androidId"), AUTH_TOKEN("Auth"),
	CAN_UPGRADE_PLUS("GooglePlusUpgrade"), CAPTCHA_ANSWER("logincaptcha"), CAPTCHA_BITMAP("captchaBitmap"),
	CAPTCHA_DATA("CaptchaData"), CAPTCHA_TOKEN("logintoken"), CAPTCHA_TOKEN_RES("CaptchaToken"),
	CAPTCHA_URL("CaptchaUrl"), CLIENT_ID("client_id"), CREATED("created"), DETAIL("ErrorDetail"),
	DEVICE_COUNTRY("device_country"), EMAIL("Email"), ENCRYPTED_PASSWORD("EncryptedPasswd"), EXPIRY("Expiry"),
	FIRST_NAME("firstName"), GENDER("gender"), GPLUS_CHECK("gplus_check"), ID("id"), INFO("Info"),
	JSON_STATUS("status"), LANGUAGE("lang"), LAST_NAME("lastName"), LSID("LSID"), MASTER_TOKEN("Token"),
	NEEDS_CREDIT_CARD("CC"), OAUTH2_EXTRA_PREFIX("oauth2_"), OPERATOR_COUNTRY("operatorCountry"), PACKAGE_NAME("app"),
	PACKAGE_SIGNATURE("client_sig"), PASSWORD("Passwd"), PERMISSION("Permission"), PERMISSION_ADVICE("issueAdvice"),
	PHOTO("photo"), PICASA_USER("PicasaUser"), SDK_VERSION("sdk_version"), SECURITY_TOKEN("secTok"), SERVICE("service"),
	SERVICE_GPLUS("googleme"), SERVICE_HOSTED("HOSTED"), SERVICES("services"), SID("SID"), SOURCE("source"),
	STATUS("Error"), STORED_PERMISSION("has_permission"), TIME_STAMP("timeStmp"), URL("Url"),
	YOUTUBE_USER("YouTubeUser");

	private static HashMap<String, DataField> internalMap;

	public static DataField fromInternalName(final String internalName) {
		return internalMap.get(internalName);
	}

	private final String internalName;

	private DataField(final String internalName) {
		this.internalName = internalName;
		saveToMap();
	}

	private void saveToMap() {
		if (internalMap == null) {
			internalMap = new HashMap<String, DataField>();
		}
		internalMap.put(internalName, this);
	}

	public String toEnumName() {
		return super.toString();
	}

	public String toInternalName() {
		return internalName;
	}

	@Override
	public String toString() {
		return internalName;
	}
}