package com.android.vending.account;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.android.vending.R;

public class AccountActivity extends Activity implements OnClickListener {
	@Override
	public void onClick(View v) {
		final AccountManager accountManager = AccountManager.getInstance(this);
		final String androidId = ((TextView) findViewById(R.id.txt_androidId))
				.getText().toString();
		final String login = ((TextView) findViewById(R.id.txt_login))
				.getText().toString();
		final String password = ((TextView) findViewById(R.id.txt_password))
				.getText().toString();
		final Account account = new Account(login, password,
				Account.Type.GooglePlay, androidId);
		accountManager.addAccount(account);
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account);
		findViewById(R.id.btn_create_account).setOnClickListener(this);
	}
}
