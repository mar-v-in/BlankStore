package com.google.auth;

import com.google.tools.Base64;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;

public class AuthCrypto {
	private static final String CHARENC_UTF8 = "UTF-8";
	private static final String CRYPT_CIPHER = "RSA/ECB/OAEPWITHSHA1ANDMGF1PADDING";
	private static final String CRYPT_DIVIDER = "\uFFFD\uFFFD";
	private static final String CRYPT_HASH_TYPE = "SHA-1";
	private static final String CRYPT_PUBLIC_KEY =
			"AAAAgMom/1a/v0lblO2Ubrt60J2gcuXSljGFQXgcyZWveWLEwo6prwgi3iJIZdodyhKZQrNWp5nKJ3srRXcUW+F1BD3baEVGcmEgqaLZUNBjm057pKRI16kB0YppeGx5qIQ5QjKzsR8ETQbKLNWgRY0QRNVz34kMJR3P/LgHax/6rmf5AAAAAwEAAQ==";
	private static final String CRYPT_TYPE = "RSA";

	private static PublicKey createKey(final String keyStr, final byte ciphertextHeader[]) {
		try {
			final byte data[] = Base64.decode(keyStr, 0);

			final int modulusLength = readInt(data, 0);
			byte temp[] = new byte[modulusLength];
			System.arraycopy(data, 4, temp, 0, modulusLength);
			final BigInteger modulus = new BigInteger(1, temp);

			final int exponentLength = readInt(data, modulusLength + 4);
			temp = new byte[exponentLength];
			System.arraycopy(data, modulusLength + 8, temp, 0, exponentLength);
			final BigInteger exponent = new BigInteger(1, temp);

			final MessageDigest sha1 = MessageDigest.getInstance(CRYPT_HASH_TYPE);
			final byte hash[] = sha1.digest(data);
			ciphertextHeader[0] = 0;
			System.arraycopy(hash, 0, ciphertextHeader, 1, 4);

			return KeyFactory.getInstance(CRYPT_TYPE).generatePublic(new RSAPublicKeySpec(modulus, exponent));
		} catch (final Throwable t) {
			throw new RuntimeException(t);
		}

	}

	public static String encryptPassword(final String email, final String password) {
		return encryptPassword(email, password, CRYPT_PUBLIC_KEY);
	}

	private static String encryptPassword(final String email, final String password, final String publicKeyData) {
		final String combined = email + CRYPT_DIVIDER + password;
		return encryptString(combined, publicKeyData);
	}

	private static String encryptString(final String plain, final String publicKeyData) {
		// TODO Something seems to be non-working...
		if (publicKeyData == null || publicKeyData.isEmpty()) {
			return null;
		}
		final byte[] ciphertextHeader = new byte[5];
		final PublicKey publicKey = createKey(publicKeyData, ciphertextHeader);
		if (publicKey == null) {
			return null;
		}
		try {
			final Cipher cipher = Cipher.getInstance(CRYPT_CIPHER);
			final byte[] plainbytes = plain.getBytes(CHARENC_UTF8);
			final int chunks = 1 + (plainbytes.length - 1) / 86;
			final byte[] bytes = new byte[chunks * 133];
			for (int i = 0; i < chunks; i++) {
				cipher.init(Cipher.ENCRYPT_MODE, publicKey);
				final int start = i * 86;
				int length = 86;
				if (i == chunks - 1) {
					length = plainbytes.length - start;
				}
				final byte[] cipherbytes = cipher.doFinal(plainbytes, start, length);
				System.arraycopy(ciphertextHeader, 0, bytes, i * 133, ciphertextHeader.length);
				System.arraycopy(cipherbytes, 0, bytes, i * 133 + ciphertextHeader.length, cipherbytes.length);
			}
			return Base64.encodeToString(bytes, Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP);
		} catch (final Throwable t) {
		}
		return null;
	}

	private static int readInt(final byte src[], final int offset) {
		int i = 0 | (0xff & src[offset]) << 24;
		i |= (0xff & src[offset + 1]) << 16;
		i |= (0xff & src[offset + 2]) << 8;
		i |= 0xff & src[offset + 3];
		return i;
	}

}
