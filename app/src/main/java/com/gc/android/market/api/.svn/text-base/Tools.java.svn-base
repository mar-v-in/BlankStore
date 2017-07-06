package com.gc.android.market.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class Tools {
	
	public static class HttpException extends RuntimeException {
		
		private int errorCode;
		private String errorData;
		
		public HttpException(int errorCode, String errorData) {
			super("HTTP Code "+errorCode+" : "+errorData);
			this.errorCode = errorCode;
			this.errorData = errorData;
		}

		public int getErrorCode() {
			return errorCode;
		}

		public String getErrorData() {
			return errorData;
		}
		
		
	}

	public static String postUrl(String url, Map<String, String> params) throws IOException {
		String data = "";
		for (String key : params.keySet()) {
			data += "&"+URLEncoder.encode(key, "UTF-8") + "="
					+ URLEncoder.encode(params.get(key), "UTF-8");
		}
		data = data.substring(1);
		//System.out.println(data);
		// Make the connection to Authoize.net
		URL aURL = new java.net.URL(url);
		HttpURLConnection aConnection = (java.net.HttpURLConnection) aURL
				.openConnection();
		try {
			aConnection.setDoOutput(true);
			aConnection.setDoInput(true);
			aConnection.setRequestMethod("POST");
			//aConnection.setAllowUserInteraction(false);
			// POST the data
			OutputStreamWriter streamToAuthorize = new java.io.OutputStreamWriter(
					aConnection.getOutputStream());
			streamToAuthorize.write(data);
			streamToAuthorize.flush();
			streamToAuthorize.close();
			
			// check error
			int errorCode = aConnection.getResponseCode();
			if(errorCode >= 400) {
				InputStream errorStream = aConnection.getErrorStream();
				try {
					String errorData = streamToString(errorStream);
					throw new HttpException(errorCode,errorData);
				} finally {
					errorStream.close();
				}
			}
			
			// Get the Response
			InputStream resultStream = aConnection.getInputStream();
			try {
				String responseData = streamToString(resultStream);
				return responseData;
			} finally {
				resultStream.close();
			}
		} finally {
			aConnection.disconnect();
		}
	}
	
	private static String streamToString(InputStream resultStream) throws IOException {
		BufferedReader aReader = new java.io.BufferedReader(
				new java.io.InputStreamReader(resultStream));
		StringBuffer aResponse = new StringBuffer();
		String aLine = aReader.readLine();
		while (aLine != null) {
			aResponse.append(aLine+"\n");
			aLine = aReader.readLine();
		}
		return aResponse.toString();
		
	}

	
	
}
