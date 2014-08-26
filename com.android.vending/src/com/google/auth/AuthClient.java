package com.google.auth;

import com.google.tools.Client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class AuthClient extends Client {

	private DataMap sendData(final URL url, final DataMapReader data, final String userAgent) {
		return DataMap.fromUrlDataString(sendString(url, data.toString(), userAgent));

	}

	public AuthResponse sendRequest(final AuthRequest request) {
		return new AuthResponse(sendData(request.getRequestUrl(), request, request.getUserAgent()));
	}

	private String sendString(final HttpURLConnection connection, final String dataString) {
		prepareConnection(connection, false);
		writeData(connection, dataString, false);
		return new String(readData(connection, isError(connection), false));
	}

	private String sendString(final URL url, final String dataString, final String userAgent) {
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) url.openConnection();
			setUserAgent(connection, userAgent);
			return sendString(connection, dataString);
		} catch (final IOException e) {
			if (DEBUG) {
				System.err.println("Could not open Connection!");
			}
			throw new RuntimeException("Could not open Connection!", e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}
