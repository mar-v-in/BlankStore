package com.android.vending.licensing;

import java.util.List;

import android.util.Log;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.android.vending.account.Account;
import com.android.vending.account.AccountManager;

import com.google.android.AndroidContext;

import com.gc.android.market.api.MarketSession;
import com.google.play.VendingClient;
import com.google.tools.RequestContext;

public class LicensingService extends Service {
    private static final String TAG = LicensingService.class.getName();
    private Account account;
    protected VendingClient client;
    protected String authToken;

    @Override
    public void onCreate()
    {
        final AccountManager accountManager = AccountManager.getInstance(this);
        final List<Account> accounts = accountManager.getAllAccounts();

        // TODO: this is stupid, but when there are several accounts, which one should be chosen?
        for (final Account account : accounts) {
            Log.d(TAG, "Account: " + account.toString());
            switch (account.getType()) {
                case GooglePlay:
                    this.account = account;
                    break;
            }
        }

        if (account != null)
        {
            RequestContext androidContext = AndroidContext.baseDevice()
                    .set(VendingClient.KEY_ANDROID_ID_HEX, account.getAndroidId())
                    .set(VendingClient.KEY_SDK_VERSION, account.getSdkVersion())
                    .set(VendingClient.KEY_BUILD_DEVICE, account.getDeviceName());

            this.client = new VendingClient(androidContext);
            this.authToken = null;
        }
    }

    @Override
    public IBinder onBind(final Intent intent) {
        if (intent.getAction().equals("com.android.vending.licensing.ILicensingService")) {
            return new LicensingServiceImpl(); //TODO
        }
        return null;
    }

    public class LicensingServiceImpl extends ILicensingService.Stub {
        @Override
        public void checkLicense(long nonce, String packageName, ILicenseResultListener listener)
            throws RemoteException
        {
            int versionCode = 0;
            final PackageManager pm = getPackageManager();
            PackageInfo info = null;

            Log.d(TAG, "checkLicense(" + nonce + ", \"" + packageName + "\", ...)");

            try {
                info = pm.getPackageInfo(packageName, 0);
                versionCode = info.versionCode;
                Log.d(TAG, "versionCode: " + versionCode);
            }
            catch (final NameNotFoundException e)
            {
                Log.e(TAG, "package not found (" + packageName + ")");
                listener.verifyLicense(0x102, null, null);
                return;
            }

            if (info.applicationInfo.uid != getCallingUid())
            {
                Log.e(TAG, "application trying to check a different application's license!");
                listener.verifyLicense(0x103, null, null);
            }

            if (authToken == null)
            {
                MarketSession session = new MarketSession(true); //TODO
                session.login(account.getLogin(), account.getPassword(), account.getAndroidId());
                authToken = session.getAuthSubToken();
            }
            Log.d(TAG, "authToken: " + authToken);

            client.DEBUG = true;
            client.checkLicenseRequest("androidsecure" /* TODO: really? */, authToken, packageName, versionCode, nonce);

            listener.verifyLicense(0x101, null, null); // TODO: for now, just pretend we didn't manage to connect
        }
    }
}
