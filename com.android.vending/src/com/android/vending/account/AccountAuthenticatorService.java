package com.android.vending.account;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

public class AccountAuthenticatorService extends Service {

	public class AccountAuthenticator extends AbstractAccountAuthenticator {

		private final Context context;

		public AccountAuthenticator(Context context) {
			super(context);
			this.context = context;
		}

		@Override
		public Bundle addAccount(AccountAuthenticatorResponse response,
				String accountType, String authTokenType,
				String[] requiredFeatures, Bundle options)
				throws NetworkErrorException {
			final Bundle reply = new Bundle();
			final Intent i = new Intent(context, AccountActivity.class);
			i.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE,
					response);
			reply.putParcelable(AccountManager.KEY_INTENT, i);
			return reply;
		}

		@Override
		public Bundle confirmCredentials(AccountAuthenticatorResponse response,
				Account account, Bundle options) throws NetworkErrorException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Bundle editProperties(AccountAuthenticatorResponse response,
				String accountType) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Bundle getAuthToken(AccountAuthenticatorResponse response,
				Account account, String authTokenType, Bundle options)
				throws NetworkErrorException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getAuthTokenLabel(String authTokenType) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Bundle hasFeatures(AccountAuthenticatorResponse response,
				Account account, String[] features)
				throws NetworkErrorException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Bundle updateCredentials(AccountAuthenticatorResponse response,
				Account account, String authTokenType, Bundle options)
				throws NetworkErrorException {
			// TODO Auto-generated method stub
			return null;
		}

	}

	private AccountAuthenticator accountAuthenticator;

	private AccountAuthenticator getAuthenticator() {
		if (accountAuthenticator == null) {
			accountAuthenticator = new AccountAuthenticator(this);
		}
		return accountAuthenticator;
	}

	@Override
	public IBinder onBind(Intent intent) {
		IBinder ret = null;
		if (intent.getAction().equals(
				android.accounts.AccountManager.ACTION_AUTHENTICATOR_INTENT)) {
			ret = getAuthenticator().getIBinder();
		}
		return ret;
	}

}
