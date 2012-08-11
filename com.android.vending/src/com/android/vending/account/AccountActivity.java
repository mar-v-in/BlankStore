package com.android.vending.account;

import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.android.vending.R;

public class AccountActivity extends Activity implements OnClickListener {
	@Override
	public void onClick(View v) {
		final AccountManager accountManager = AccountManager.getInstance(this);
		HashMap<String, String> accountData = new HashMap<String, String>();
		accountData.put(Account.ACCOUNT_DATA_KEY_ANDROIDID,
				((EditText) findViewById(R.id.txt_androidId)).getText()
						.toString());
		accountData.put(Account.ACCOUNT_DATA_KEY_OPERATORALPHA,
				((EditText) findViewById(R.id.txt_opAlpha)).getText()
						.toString());
		accountData.put(Account.ACCOUNT_DATA_KEY_OPERATORNUMERIC,
				((EditText) findViewById(R.id.txt_opNum)).getText().toString());
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
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		((EditText) findViewById(R.id.txt_opAlpha)).setText(telephonyManager
				.getSimOperatorName());
		((EditText) findViewById(R.id.txt_opNum)).setText(telephonyManager
				.getSimOperator());
		findViewById(R.id.btn_create_account).setOnClickListener(this);
	}
}
