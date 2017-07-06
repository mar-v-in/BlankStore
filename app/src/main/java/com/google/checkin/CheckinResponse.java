package com.google.checkin;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckinResponse {
	private final long androidId;
	private final long securityToken;
	private final Map<String, String> settings;
	private final boolean marketEnabled;
	private final String digest;

	public CheckinResponse(Response response) {
		this(response.androidId, response.securityToken, parseSettings(response.setting),
			 response.marketOk==null?false:response.marketOk, response.digest);
	}

	public CheckinResponse(final long androidId, final long securityToken, final Map<String, String> settings,
						   final boolean marketEnabled, final String digest) {
		this.androidId = androidId;
		this.securityToken = securityToken;
		this.settings = settings;
		this.marketEnabled = marketEnabled;
		this.digest = digest;
	}

	private static Map<String, String> parseSettings(List<Response.GservicesSetting> settings) {
		Map<String, String> map = new HashMap<String, String>();
		for (Response.GservicesSetting setting : settings) {
			String name = fromUtf8Bytes(setting.name.toByteArray());
			String value = fromUtf8Bytes(setting.value.toByteArray());
			map.put(name, value);
		}
		System.out.println(map);
		return Collections.unmodifiableMap(map);
	}

	private static String fromUtf8Bytes(byte[] bytes) {
		try {
			return new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	public String getDigest() {
		return digest;
	}

	public Map<String, String> getSettings() {
		return settings;
	}

	public boolean isMarketEnabled() {
		return marketEnabled;
	}

	public String getAndroidIdHex() {
		return Long.toHexString(androidId);
	}

	public long getAndroidId() {
		return androidId;
	}

	public long getSecurityToken() {
		return securityToken;
	}
}
