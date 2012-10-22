package com.android.vending.account;

import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.android.vending.R;

public class AccountActivity extends Activity implements OnClickListener {
	@Override
	public void onClick(View v) {
		final AccountManager accountManager = AccountManager.getInstance(this);
		final HashMap<String, String> accountData = new HashMap<String, String>();
		accountData.put(Account.ACCOUNT_DATA_KEY_ANDROIDID,
				((EditText) findViewById(R.id.txt_androidId)).getText()
						.toString());
		accountData.put(Account.ACCOUNT_DATA_KEY_OPERATORALPHA,
				((EditText) findViewById(R.id.txt_opAlpha)).getText()
						.toString());
		accountData.put(Account.ACCOUNT_DATA_KEY_OPERATORNUMERIC,
				((EditText) findViewById(R.id.txt_opNum)).getText().toString());
		accountData.put(Account.ACCOUNT_DATA_KEY_SDKVERSION,
				((EditText) findViewById(R.id.txt_sdkV)).getText().toString());
		accountData.put(Account.ACCOUNT_DATA_KEY_DEVICENAME,
				((EditText) findViewById(R.id.txt_devName)).getText().toString());
		final String login = ((EditText) findViewById(R.id.txt_login))
				.getText().toString();
		final String password = ((EditText) findViewById(R.id.txt_password))
				.getText().toString();
		final Account account = new Account(login, password,
				Account.Type.GooglePlay, accountData);
		accountManager.addAccount(account);
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account);
		Account a = new Account(null, null, null);
		((EditText) findViewById(R.id.txt_opAlpha)).setText(a
				.getOperatorAlpha(this));
		((EditText) findViewById(R.id.txt_opNum)).setText(a
				.getOperatorNumeric(this));
		((EditText) findViewById(R.id.txt_devName)).setText(a.getDeviceName());
		((EditText) findViewById(R.id.txt_sdkV))
				.setText(a.getSdkVersion() + "");
		findViewById(R.id.btn_create_account).setOnClickListener(this);
	}
}
