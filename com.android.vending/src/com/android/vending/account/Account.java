package com.android.vending.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;

public class Account {
	public enum Type {
		GooglePlay;

		private static String GOOGLE_PLAY = "com.android.vending.google";

		public static Type get(String type) {
			if (type.equalsIgnoreCase(GOOGLE_PLAY)) {
				return GooglePlay;
			}
			return null;
		}

		public static Type[] getAllTypes() {
			return new Type[] { GooglePlay };
		}

		public String toTypeString() {
			switch (this) {
			case GooglePlay:
				return GOOGLE_PLAY;
			default:
				return null;
			}
		}
	}

	public static final String ACCOUNT_DATA_KEY_ANDROIDID = "androidId";
	public static final String ACCOUNT_DATA_KEY_OPERATORALPHA = "operatorAlpha";
	public static final String ACCOUNT_DATA_KEY_OPERATORNUMERIC = "operatorNumeric";
	public static final String ACCOUNT_DATA_KEY_DEVICENAME = "deviceName";
	public static final String ACCOUNT_DATA_KEY_SDKVERSION = "sdkVersion";
	public static final List<String> KNOWN_ACCOUNT_DATA_FIELDS = new ArrayList<String>();
	static {
		KNOWN_ACCOUNT_DATA_FIELDS.add(ACCOUNT_DATA_KEY_ANDROIDID);
		KNOWN_ACCOUNT_DATA_FIELDS.add(ACCOUNT_DATA_KEY_OPERATORALPHA);
		KNOWN_ACCOUNT_DATA_FIELDS.add(ACCOUNT_DATA_KEY_OPERATORNUMERIC);
		KNOWN_ACCOUNT_DATA_FIELDS.add(ACCOUNT_DATA_KEY_DEVICENAME);
		KNOWN_ACCOUNT_DATA_FIELDS.add(ACCOUNT_DATA_KEY_SDKVERSION);
	}

	private final String login;
	private final String password;
	private final Type type;
	private final Map<String, String> accountData;

	public Account(String login, String password, Type type) {
		this(login, password, type, new HashMap<String, String>());
	}

	public Account(String login, String password, Type type,
			Map<String, String> accountData) {
		this.login = login;
		this.password = password;
		this.type = type;
		this.accountData = accountData;
	}

	public String getAndroidId() {
		return accountData.get(ACCOUNT_DATA_KEY_ANDROIDID);
	}

	public String getAndroidIdAstrisks() {
		final StringBuilder sb = new StringBuilder();
		String id = getAndroidId();
		for (int i = 0; i < id.length(); i++) {
			if (i % 2 == 0) {
				sb.append("*");
			} else {
				sb.append(id.charAt(i));
			}
		}
		return sb.toString();
	}

	public Bundle getDataBundle() {
		return getDataBundle(null);
	}

	public Bundle getDataBundle(Bundle b) {
		if (b == null) {
			b = new Bundle();
		}
		for (final String key : accountData.keySet()) {
			b.putString(key, accountData.get(key));
		}
		return b;
	}

	public String getLogin() {
		return login;
	}

	public String getLoginAstrisks() {
		String[] split = login.split("@");
		if (split.length == 2) {
			final StringBuilder sb = new StringBuilder();
			for (int i = 0; i < split[0].length(); i++) {
				if (i % 2 == 0) {
					sb.append("*");
				} else {
					sb.append(split[0].charAt(i));
				}
			}
			sb.append("@").append(split[1]);
			return sb.toString();
		} else {
			return login;
		}
	}

	public String getOperatorAlpha() {
		return accountData.get(ACCOUNT_DATA_KEY_OPERATORALPHA);
	}

	public String getOperatorAlpha(Context context) {
		String account = getOperatorAlpha();
		if (account == null || account.isEmpty()) {
			final TelephonyManager telephonyManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			return telephonyManager.getSimOperatorName();
		}
		return account;
	}

	public String getOperatorNumeric() {
		return accountData.get(ACCOUNT_DATA_KEY_OPERATORNUMERIC);
	}

	public String getOperatorNumeric(Context context) {
		String account = getOperatorNumeric();
		if (account == null || account.isEmpty()) {
			final TelephonyManager telephonyManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			return telephonyManager.getSimOperator();
		}
		return account;
	}

	public String getPassword() {
		return password;
	}

	public String getPasswordAstrisks() {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < password.length(); i++) {
			sb.append("*");
		}
		return sb.toString();
	}

	public Type getType() {
		return type;
	}

	@Override
	public String toString() {
		return Account.class.getName() + "{type=\"" + getType()
				+ "\", login=\"" + getLoginAstrisks() + "\", password=\""
				+ getPasswordAstrisks() + "\", androidId=\""
				+ getAndroidIdAstrisks() + "\", deviceName=\""
				+ getDeviceName() + "\", sdkVersion=\"" + getSdkVersion()
				+ "\", operatorAlpha=\"" + getOperatorAlpha()
				+ "\", operatorNumeric=\"" + getOperatorNumeric() + "\"}";
	}

	public String getDeviceName() {
		String account = accountData.get(ACCOUNT_DATA_KEY_DEVICENAME);
		if (account == null || account.isEmpty()) {
			return Build.DEVICE;
		}
		return account;
	}

	public int getSdkVersion() {
		String account = accountData.get(ACCOUNT_DATA_KEY_SDKVERSION);
		if (account == null || account.isEmpty() || isParsableToInt(account)) {
			return Build.VERSION.SDK_INT;
		}
		return Integer.parseInt(account);
	}

	public boolean isParsableToInt(String i) {
		try {
			Integer.parseInt(i);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

}
