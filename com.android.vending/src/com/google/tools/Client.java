package com.google.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public abstract class Client {

	protected static final String REQUEST_CONTENT_TYPE = "application/x-www-form-urlencoded";
	protected static final String REQUEST_CONTENT_TYPE_FIELD = "Content-Type";
	protected static final String REQUEST_USER_AGENT_FIELD = "User-Agent";
	protected static final String REQUEST_ACCEPT_FIELD = "Accept";
	protected static final String REQUEST_ACCEPT_ENCODING_FIELD = "Accept-Encoding";
	protected static final String REQUEST_METHOD = "POST";
	public static boolean DEBUG = false;
	public static boolean DEBUG_ERROR = true;
	public static boolean DEBUG_HEADER = false;

	protected boolean isError(final HttpURLConnection connection) {
		try {
			if (connection.getResponseCode() != 200) {
				if (DEBUG_ERROR) {
					System.out
						  .println("Error: " + connection.getResponseCode() + " " + connection.getResponseMessage());
				}
				return true;
			}
		} catch (final IOException e) {
			return true;
		}
		return false;
	}

	protected void prepareConnection(final HttpURLConnection connection, final boolean gzip) {
		try {
			connection.setRequestMethod(REQUEST_METHOD);
		} catch (final ProtocolException e) {
			if (DEBUG_ERROR) {
				System.out.println("Could not enable POST-Request");
			}
			throw new RuntimeException("Could not enable POST-Request", e);
		}
		connection.setRequestProperty(REQUEST_CONTENT_TYPE_FIELD, REQUEST_CONTENT_TYPE);
		connection.setRequestProperty(REQUEST_ACCEPT_ENCODING_FIELD, gzip ? "gzip" : "identity");
		connection.setUseCaches(false);
		connection.setDoInput(true);
		connection.setDoOutput(true);
	}

	protected byte[] readData(final HttpURLConnection connection, final boolean gunzip) {
		return readData(connection, isError(connection), gunzip);
	}

	protected byte[] readData(final HttpURLConnection connection, final boolean error, final boolean gunzip) {
		try {
			InputStream is = null;
			if (error) {
				is = connection.getErrorStream();
			} else {
				is = connection.getInputStream();
				if (gunzip) {
					is = new GZIPInputStream(is);
				}
			}
			if (is == null) {
				if (DEBUG) {
					System.out.println("InputStream is null ?!");
				}
			}
			return readStreamToEnd(is);
		} catch (final Exception e) {
			if (DEBUG_ERROR) {
				System.out.println("Could not read data!");
			}
			throw new RuntimeException("Could not read data", e);
		}
	}

	protected void setUserAgent(HttpURLConnection connection, RequestContext info) {
		connection.setRequestProperty(REQUEST_USER_AGENT_FIELD, info.get(RequestContext.KEY_HTTP_USER_AGENT));
	}

	protected void setUserAgent(HttpURLConnection connection, String userAgent) {
		connection.setRequestProperty(REQUEST_USER_AGENT_FIELD, userAgent);
	}

	protected byte[] readStreamToEnd(final InputStream is) throws IOException {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		if (is != null) {
			final byte[] buff = new byte[1024];
			while (true) {
				final int nb = is.read(buff);
				if (nb < 0) {
					break;
				}
				bos.write(buff, 0, nb);
			}
			is.close();
		}
		return bos.toByteArray();
	}

	protected void beforeRequest(final HttpURLConnection connection) {
		if (DEBUG_HEADER) {
			for (String key : connection.getRequestProperties().keySet()) {
				System.out.println("Header | " + key + ": " + connection.getRequestProperty(key));
			}
		}
	}

	protected void writeData(final HttpURLConnection connection, final byte[] bytes, final boolean gzip) {
		beforeRequest(connection);
		try {
			OutputStream os = connection.getOutputStream();
			if (gzip) {
				os = new GZIPOutputStream(os);
			}
			/*
			 * final DataOutputStream stream = new DataOutputStream(os);
			 * stream.writeBytes(string);
			 */
			os.write(bytes);
			os.flush();
			os.close();
		} catch (final IOException e) {
			if (DEBUG_ERROR) {
				System.out.println("Could not send data!");
				e.printStackTrace();
			}
			throw new RuntimeException("Could not send data", e);
		}
	}

	protected void writeData(final HttpURLConnection connection, final String string, final boolean gzip) {
		if (DEBUG) {
			System.out.println("Sending" + (gzip ? "(gzipped)" : "") + ": " + string);
		}
		writeData(connection, string.getBytes(), gzip);
	}
}
