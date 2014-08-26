package com.google.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignatureTools {

	public static String secureSHA1hash(byte[] signatureBytes) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (final NoSuchAlgorithmException e) {
			return null;
		}
		if (md != null) {
			signatureBytes = md.digest(signatureBytes);
			if (signatureBytes != null) {
				try {
					return noPadding(Base64.encodeBytes(signatureBytes, Base64.URL_SAFE));
				} catch (Exception e) {
				}
			}
		}
		return null;
	}

	private static String noPadding(String base64) {
		if (base64 == null)
			return null;
		if (base64.endsWith("=")) {
			base64 = base64.substring(0, base64.length() - 1);
			if (base64.endsWith("=")) {
				base64 = base64.substring(0, base64.length() - 1);
			}
		}
		return base64;
	}

	public static String secureMD5hash(byte[] signatureBytes) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (final NoSuchAlgorithmException e) {
			return null;
		}
		if (md != null) {
			signatureBytes = md.digest(signatureBytes);
			if (signatureBytes != null) {
				try {
					return noPadding(Base64.encodeBytes(signatureBytes, Base64.URL_SAFE));
				} catch (Exception e) {
				}
			}
		}
		return null;
	}

	public static String signatureDigest(byte[] signatureBytes) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA1");
		} catch (final NoSuchAlgorithmException e) {
			return null;
		}
		if (md != null) {
			signatureBytes = md.digest(signatureBytes);
			if (signatureBytes != null) {
				return toHex(signatureBytes);
			}
		}
		return null;
	}

	public static String toHex(final byte buffer[]) {
		final StringBuffer stringbuffer = new StringBuffer(2 * buffer.length);
		for (final byte b : buffer) {
			final Object aobj[] = new Object[1];
			aobj[0] = Byte.valueOf(b);
			stringbuffer.append(String.format("%02x", aobj));
		}
		return stringbuffer.toString();
	}
}
