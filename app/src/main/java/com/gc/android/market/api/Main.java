package com.gc.android.market.api;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;

import com.gc.android.market.api.MarketSession.Callback;
import com.gc.android.market.api.model.Market.AppType;
import com.gc.android.market.api.model.Market.AppsRequest;
import com.gc.android.market.api.model.Market.AppsRequest.OrderType;
import com.gc.android.market.api.model.Market.AppsRequest.ViewType;
import com.gc.android.market.api.model.Market.AppsResponse;
import com.gc.android.market.api.model.Market.CommentsRequest;
import com.gc.android.market.api.model.Market.GetAssetResponse.InstallAsset;
import com.gc.android.market.api.model.Market.GetImageRequest;
import com.gc.android.market.api.model.Market.GetImageResponse;
import com.gc.android.market.api.model.Market.ResponseContext;
import com.gc.android.market.api.model.Market.GetImageRequest.AppImageUsage;

public class Main {

    private static void usage() {
        System.out.println("Usage :\n" +
                "market <email> <password> <androidId> <testId> <assetId> <query> \n" +
                "\temail, password : account linked to the specified android id\n" +
                "\tandroiId : a valid android id (retrieved typing *#*#8255#*#* " +
                "on device or emulator)\n" +
                "\ttestId : test you want to run (1 to test different asyncronous methods; " +
                "2 to test different syncronous call. Please note that if you run test 1 and 2 " +
                "consecutively, you have to change account to avoid Bad request response; " +
                " 3 to test download)\n\tassetId : application assetId to use for testing\n" +
                "\tquery to test setQuery in testId 1");
    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			if(args.length < 5) {
			    usage();
				return;
			}
			String login = args[0];
			String password = args[1];
			String androidId = args[2];
			Integer testId = new Integer(args[3]);
			String assetId = args[4];
			String query = args.length == 6 ? args[5] : "Test";
			switch (testId) {
			    case 1:
			        testAsyncronousCalls(login, password, androidId, assetId, query);
			        break;
			    case 2:
			        testSyncronousCalls(login, password, androidId, assetId);
			        break;
			    case 3:
			        testDownload(login, password, androidId, assetId);
			        break;
			    default :
			        usage();
			        return;
			}
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}
	
	private static void testAsyncronousCalls(String login, String password, 
	        String androidId, String assetId, String query)
	{
	    try {
        MarketSession session = new MarketSession(false);
        System.out.println("Login...");
        session.login(login,password, androidId);
        System.out.println("Login done");

        AppsRequest appsRequest = AppsRequest.newBuilder()
            .setQuery(query)
            .setStartIndex(2).setEntriesCount(10)
            .setWithExtendedInfo(true)
            .build();
        
        CommentsRequest commentsRequest = CommentsRequest.newBuilder()
            .setAppId(assetId)
            .setStartIndex(0)
            .setEntriesCount(10)
            .build();
        
        //

        GetImageRequest imgReq = GetImageRequest.newBuilder().setAppId(assetId)
            .setImageUsage(AppImageUsage.SCREENSHOT)
            .setImageId("1")
            .build();
        
        MarketSession.Callback callback = new MarketSession.Callback() {

            @Override
            public void onResult(ResponseContext context, Object response) {
                System.out.println("Response : " + response);
            }
            
        };
        session.append(appsRequest, callback);
        session.flush();
        session.append(imgReq, new Callback<GetImageResponse>() {
            
            @Override
            public void onResult(ResponseContext context, GetImageResponse response) {
                try {
                    FileOutputStream fos = new FileOutputStream("icon.png");
                    fos.write(response.getImageData().toByteArray());
                    fos.close();
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        session.flush();
        session.append(commentsRequest, callback);
        session.flush();
    } catch(Exception ex) {
        ex.printStackTrace();
    }	    
	}
    private static void testSyncronousCalls(String login, String password, 
            String androidId, String assetId)
    {
        try {
            MarketSession session = new MarketSession(false);
            List<Object> rg = new ArrayList<Object>();
            System.out.println("Login...");
            session.login(login,password, androidId);
            System.out.println("Login done");

            AppsRequest appsRequest = AppsRequest.newBuilder()
                                                .setAppType(AppType.GAME)
                                                .setStartIndex(1)
                                                .setEntriesCount(10)
                                                .setWithExtendedInfo(true)
                                                .setViewType(ViewType.FREE)
                                                .build();
            
            rg.addAll(session.queryApp(appsRequest));
            for(int j = 0; j < rg.size(); ++j) {
                AppsResponse apps = (AppsResponse)rg.get(j);
                System.out.println("#num apps: " + apps.getAppCount());
        }  
    } catch(Exception ex) {
        ex.printStackTrace();
    }       
    }
    private static void testDownload(String login, String password, 
            String androidId, String assetId)
    {
        try {
        MarketSession session = new MarketSession(true);

        System.out.println("Login...");
        session.login(login,password, androidId);
        System.out.println("Login done");

        InstallAsset ia = session.queryGetAssetRequest(assetId).getInstallAsset(0);
        String cookieName = ia.getDownloadAuthCookieName();
        String cookieValue = ia.getDownloadAuthCookieValue();
        URL url = new URL(ia.getBlobUrl());

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Android-Market/2 (sapphire PLAT-RC33); gzip");
        conn.setRequestProperty("Cookie", cookieName + "=" + cookieValue);

        InputStream inputstream =  (InputStream) conn.getInputStream();
        String fileToSave = assetId + ".apk";
        System.out.println("Downloading " + fileToSave);
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(fileToSave));
        byte buf[] = new byte[1024];
        int k = 0;
        for(long l = 0L; (k = inputstream.read(buf)) != -1; l += k )
            stream.write(buf, 0, k);
        inputstream.close();
        stream.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }       
    }
}
