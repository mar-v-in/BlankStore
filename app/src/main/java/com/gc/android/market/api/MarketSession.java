package com.gc.android.market.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.gc.android.market.api.model.Market.AppsRequest;
import com.gc.android.market.api.model.Market.AppsResponse;
import com.gc.android.market.api.model.Market.CategoriesRequest;
import com.gc.android.market.api.model.Market.CategoriesResponse;
import com.gc.android.market.api.model.Market.CommentsRequest;
import com.gc.android.market.api.model.Market.CommentsResponse;
import com.gc.android.market.api.model.Market.GetImageRequest;
import com.gc.android.market.api.model.Market.GetImageResponse;
import com.gc.android.market.api.model.Market.Request;
import com.gc.android.market.api.model.Market.Request.RequestGroup;
import com.gc.android.market.api.model.Market.RequestContext;
import com.gc.android.market.api.model.Market.Response;
import com.gc.android.market.api.model.Market.Response.ResponseGroup;
import com.gc.android.market.api.model.Market.ResponseContext;
import com.gc.android.market.api.model.Market.GetAssetRequest;
import com.gc.android.market.api.model.Market.GetAssetResponse;;

/**
 * MarketSession session = new MarketSession();
 * session.login(login,password, androidId);
 * For asyncronous calls use append, callback and flush
 * session.append(xxx,yyy);
 * session.append(xxx,yyy);
 * ...
 * session.flush();
 *  For syncronous call, use the specific method
 */
public class MarketSession {
	
	public static interface Callback<T> {
		
		public void onResult(ResponseContext context, T response);
	}
	/*
	 * SERVICE : Service required to the market. 
	 * Default value: android. This service must be used to query info to the Market
	 * androidsecure: This service must be used to download apps
	 * sierra (checkout): This service must be used for checkout (at moment unused)
	 */

	public String SERVICE = "android";

	private static final String URL_LOGIN = "https://www.google.com/accounts/ClientLogin";
	public static final String ACCOUNT_TYPE_GOOGLE = "GOOGLE";
	public static final String ACCOUNT_TYPE_HOSTED = "HOSTED";
	public static final String ACCOUNT_TYPE_HOSTED_OR_GOOGLE = "HOSTED_OR_GOOGLE";
	public static final int PROTOCOL_VERSION = 2;
	Request.Builder request = Request.newBuilder();
	RequestContext.Builder context = RequestContext.newBuilder();
	public RequestContext.Builder getContext() {
		return context;
	}
	
	List<Callback<?>> callbacks = new Vector<Callback<?>>(); 
	String authSubToken = null;
	
	public String getAuthSubToken() {
		return authSubToken;
	}

	/*
	 * Login must set isSecure to false for list and download
	 */
	public MarketSession(Boolean isSecure) {
        if (isSecure)
            SERVICE = "androidsecure";
        else 
            SERVICE = "android";
        context.setIsSecure(false);
        context.setVersion(2009011);
        setLocale(Locale.getDefault());
        context.setDeviceAndSdkVersion("passion:9");
        setOperatorTMobile();
	}
	
	public void setLocale(Locale locale) {
		context.setUserLanguage(locale.getLanguage().toLowerCase());
		context.setUserCountry(locale.getCountry().toLowerCase());
	}
	
	public void setOperator(String alpha, String numeric) {
		setOperator(alpha, alpha, numeric, numeric);
	}
	
	public void setOperatorTMobile() {
		setOperator("T-Mobile", "310260");
	}
	
	public void setOperatorSFR() {
		setOperator("F SFR", "20810");
	}
	
	public void setOperatorO2() {
		setOperator("o2 - de", "26207");
	}
	
	public void setOperatorSimyo() {
		setOperator("E-Plus", "simyo", "26203", "26203");
	}
	
	public void setOperatorSunrise() {
		setOperator("sunrise", "22802");
	}
	
	/**
	 * http://www.2030.tk/wiki/Android_market_switch
	 */
	public void setOperator(String alpha, String simAlpha, String numeric, String simNumeric) {
		context.setOperatorAlpha(alpha);
		context.setSimOperatorAlpha(simAlpha);
		context.setOperatorNumeric(numeric);
		context.setSimOperatorNumeric(simNumeric);
	}
	
	public void setAuthSubToken(String authSubToken) {
		context.setAuthSubToken(authSubToken);
		this.authSubToken = authSubToken; 
	}
	
	public void setIsSecure(Boolean isSecure) {
		context.setIsSecure(isSecure);
	}
	
	public void setAndroidId(String androidId) {
		context.setAndroidId(androidId);
	}
	
	
	public void login(String email, String password, String androidId) {
		this.login(email, password, androidId, ACCOUNT_TYPE_HOSTED_OR_GOOGLE);
	}
	
	public void login(String email, String password, String androidId,
			String accountType) {
		//Android ID must an unique identifier associated to the account 
		//used in in the login
		setAndroidId(androidId);
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("Email", email);
		params.put("Passwd", password);
		
		params.put("service", SERVICE);
	//	params.put("source", source);
		params.put("accountType", accountType);

		// Login at Google.com
		try {
			String data = Tools.postUrl(URL_LOGIN, params);
			StringTokenizer st = new StringTokenizer(data, "\n\r=");
			String authKey = null;
			while (st.hasMoreTokens()) {
				if (st.nextToken().equalsIgnoreCase("Auth")) {
					authKey = st.nextToken();
					break;
				}
			}
			if(authKey == null)
				throw new RuntimeException("authKey not found in "+ data);

			setAuthSubToken(authKey);
		} catch(Tools.HttpException httpEx) {
			if(httpEx.getErrorCode() != 403)
				throw httpEx;
			
			String data = httpEx.getErrorData();
			StringTokenizer st = new StringTokenizer(data, "\n\r=");
			String googleErrorCode = null;
			while (st.hasMoreTokens()) {
				if (st.nextToken().equalsIgnoreCase("Error")) {
					googleErrorCode = st.nextToken();
					break;
				}
			}
			if(googleErrorCode == null)
				throw httpEx;
			
			throw new LoginException(googleErrorCode);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public List<Object> queryApp(AppsRequest requestGroup)
	{
		List<Object> retList = new ArrayList<Object>();
		
		request.addRequestGroup(RequestGroup.newBuilder().setAppsRequest(requestGroup));
		
		RequestContext ctxt = context.build();
		context = RequestContext.newBuilder(ctxt);
		request.setContext(ctxt);
		try {
			Response resp = executeProtobuf(request.build());
			for(ResponseGroup grp : resp.getResponseGroupList()) {
				if(grp.hasAppsResponse())
					retList.add(grp.getAppsResponse());
			}
		} finally {
			request = Request.newBuilder();
		}
		return retList;
	}
	
	public CategoriesResponse queryCategories() {
		RequestContext ctxt = context.build();
		context = RequestContext.newBuilder(ctxt);
		request.setContext(ctxt);
		CategoriesResponse categoriesResponse = null;
		try {
			Response response = executeProtobuf(request.addRequestGroup(
					RequestGroup.newBuilder().setCategoriesRequest(
							CategoriesRequest.newBuilder().build())).setContext(ctxt).build());
			categoriesResponse = response.getResponseGroup(0).getCategoriesResponse();
		} finally {
			request = Request.newBuilder();;
		}
		return categoriesResponse;
	}	

	public GetAssetResponse queryGetAssetRequest(String assetId){
	    setIsSecure(true);
	    RequestContext ctxt = context.build();
	    context = RequestContext.newBuilder(ctxt);
	    request.setContext(ctxt);
	    GetAssetResponse assetResponse = null;
	    try {
	        Response response = executeProtobuf(request.addRequestGroup(
	                    RequestGroup.newBuilder().setGetAssetRequest(
	                    GetAssetRequest.newBuilder().setAssetId(
	                    assetId).build())).setContext(ctxt).build());
	        assetResponse = response.getResponseGroup(0).getGetAssetResponse();
	    } finally {
	        setIsSecure(false);
	        request = Request.newBuilder(); 
	    }
	    return assetResponse; 
	}
	
	
	public void append(AppsRequest requestGroup, Callback<AppsResponse> responseCallback) {
		request.addRequestGroup(RequestGroup.newBuilder().setAppsRequest(requestGroup));
		callbacks.add(responseCallback);
	}
	
	public void append(GetImageRequest requestGroup, Callback<GetImageResponse> responseCallback) {
		request.addRequestGroup(RequestGroup.newBuilder().setImageRequest(requestGroup));
		callbacks.add(responseCallback);
	}
	
	public void append(CommentsRequest requestGroup, Callback<CommentsResponse> responseCallback) {
		request.addRequestGroup(RequestGroup.newBuilder().setCommentsRequest(requestGroup));
		callbacks.add(responseCallback);
	}
	
	public void append(CategoriesRequest requestGroup, Callback<CategoriesResponse> responseCallback) {
		request.addRequestGroup(RequestGroup.newBuilder().setCategoriesRequest(requestGroup));
		callbacks.add(responseCallback);
	}
	
	@SuppressWarnings("unchecked")
	public void flush() {
		RequestContext ctxt = context.build();
		context = RequestContext.newBuilder(ctxt);
		request.setContext(ctxt);
		try {
			Response resp = executeProtobuf(request.build());
			int i = 0;
			for(ResponseGroup grp : resp.getResponseGroupList()) {
				Object val = null;
				if(grp.hasAppsResponse())
					val = grp.getAppsResponse();
				if(grp.hasCategoriesResponse())
					val = grp.getCategoriesResponse();
				if(grp.hasCommentsResponse())
					val = grp.getCommentsResponse();
				if(grp.hasImageResponse())
					val = grp.getImageResponse();
			((Callback)callbacks.get(i)).onResult(grp.getContext(), val);
				i++;
			}
		} finally {
			request = Request.newBuilder();
			callbacks.clear();
		}
	}
	
	public ResponseGroup execute(RequestGroup requestGroup) {
		RequestContext ctxt = context.build();
		context = RequestContext.newBuilder(ctxt);
		request.setContext(ctxt);
		Response resp = executeProtobuf(request.addRequestGroup(requestGroup).setContext(ctxt).build());
		return resp.getResponseGroup(0);
	}
	
	private Response executeProtobuf(Request request) {
		byte[] requestBytes = request.toByteArray();
		byte[] responseBytes = null;		
		try {
		    if (!context.getIsSecure())
		        responseBytes = executeRawHttpQuery(requestBytes);
		    else 
		        responseBytes = executeRawHttpsQuery(requestBytes);
			Response r = Response.parseFrom(responseBytes);
			return r;
		} catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private byte[] executeRawHttpQuery(byte[] request) {
		try {
			
			URL url = new URL("http://android.clients.google.com/market/api/ApiRequest");
			HttpURLConnection cnx = (HttpURLConnection)url.openConnection();
			cnx.setDoOutput(true);
			cnx.setRequestMethod("POST");
			cnx.setRequestProperty("Cookie","ANDROID="+authSubToken);
			cnx.setRequestProperty("User-Agent", "Android-Market/2 (sapphire PLAT-RC33); gzip");
			cnx.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			cnx.setRequestProperty("Accept-Charset","ISO-8859-1,utf-8;q=0.7,*;q=0.7");
			
			String request64 = Base64.encodeBytes(request,Base64.URL_SAFE);
			
			String requestData = "version="+PROTOCOL_VERSION+"&request="+request64;
			
			
			cnx.setFixedLengthStreamingMode(requestData.getBytes("UTF-8").length);
			OutputStream os = cnx.getOutputStream();
			os.write(requestData.getBytes());
			os.close();
			
			if(cnx.getResponseCode() >= 400) {
				throw new RuntimeException("Response code = " + cnx.getResponseCode() + 
						", msg = " + cnx.getResponseMessage());
			}
			
			InputStream is = cnx.getInputStream();
			GZIPInputStream gzIs = new GZIPInputStream(is);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buff = new byte[1024];
			while(true) {
				int nb = gzIs.read(buff);
				if(nb < 0)
					break;
				bos.write(buff,0,nb);
			}
			is.close();
			cnx.disconnect();

			return bos.toByteArray();
		} catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
    private Boolean trustAll() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
            };
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier( new HostnameVerifier()
            {
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            }
            ); 
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    private byte[] executeRawHttpsQuery(byte[] request){
        if (request == null)
            return null;        
        if (!trustAll())
            return null;
        try {
            URL url = new URL("https://android.clients.google.com/market/api/ApiRequest");
            HttpsURLConnection cnx = (HttpsURLConnection)url.openConnection();
            cnx.setDoOutput(true);
            cnx.setRequestMethod("POST");
            cnx.setRequestProperty("Cookie","ANDROIDSECURE=" + this.getAuthSubToken());
            cnx.setRequestProperty("User-Agent", "Android-Market/2 (sapphire PLAT-RC33); gzip");
            cnx.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            cnx.setRequestProperty("Accept-Charset","ISO-8859-1,utf-8;q=0.7,*;q=0.7");
            String request64 = Base64.encodeBytes(request,Base64.URL_SAFE);
            String requestData = "version="+PROTOCOL_VERSION+"&request="+request64;
            cnx.setFixedLengthStreamingMode(requestData.getBytes("UTF-8").length);
            OutputStream os = cnx.getOutputStream();
            os.write(requestData.getBytes());
            os.close();
            if(cnx.getResponseCode() >= 400) {
                cnx.disconnect();
                throw new IOException("Response code = " + cnx.getResponseCode() + 
                        ", msg = " + cnx.getResponseMessage());
            }
            InputStream is = cnx.getInputStream();
            GZIPInputStream gzIs = new GZIPInputStream(is);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            while(true) {
                int nb = gzIs.read(buff);
                if(nb < 0)
                    break;
                bos.write(buff,0,nb);
            }
            is.close();
            cnx.disconnect();
            return bos.toByteArray();
      } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }       	
}
