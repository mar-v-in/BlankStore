package com.android.vending.account;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

public class AccountManager {
	public static AccountManager instance;

	public static AccountManager getInstance(Context context) {
		if (instance == null) {
			if (context == null) {
				throw new RuntimeException("");
			}
			instance = new AccountManager(context);
		}
		return instance;
	}

	private final android.accounts.AccountManager accountManager;

	private AccountManager(Context context) {
		accountManager = android.accounts.AccountManager.get(context);
	}

	public void addAccount(Account account) {
		final android.accounts.Account a = new android.accounts.Account(
				account.getLogin(), account.getType().toTypeString());
		accountManager.addAccountExplicitly(a, account.getPassword(),
				account.getDataBundle());
	}

	private Account getAccountByInternal(android.accounts.Account account) {
		final String login = account.name;
		final Map<String, String> accountData = new HashMap<String, String>();
		for (final String key : Account.KNOWN_ACCOUNT_DATA_FIELDS) {
			final String value = accountManager.getUserData(account, key);
			if (value != null) {
				accountData.put(key, value);
			}
		}
		final String password = accountManager.getPassword(account);
		final String type = account.type;

		return new Account(login, password, Account.Type.get(type), accountData);
	}

	public Account getAccountByName(String name) {
		final List<android.accounts.Account> accounts = getAllInternalAccounts();
		android.accounts.Account account = null;
		for (final android.accounts.Account account2 : accounts) {
			if (account2.name.equalsIgnoreCase(name)) {
				account = account2;
				break;
			}
		}
		if (account == null) {
			return null;
		}
		return getAccountByInternal(account);
	}

	public List<Account> getAllAccounts() {
		final ArrayList<Account> allAccounts = new ArrayList<Account>();
		final List<android.accounts.Account> accounts = getAllInternalAccounts();
		for (final android.accounts.Account account : accounts) {
			allAccounts.add(getAccountByInternal(account));
		}
		return allAccounts;
	}

	private List<android.accounts.Account> getAllInternalAccounts() {
		return getInternalAccounts(Account.Type.getAllTypes());
	}

	private List<android.accounts.Account> getInternalAccounts(
			Account.Type... types) {
		final ArrayList<android.accounts.Account> result = new ArrayList<android.accounts.Account>();
		for (final Account.Type accountType : types) {
			result.addAll(getInternalAccounts(accountType));
		}
		return result;
	}

	private List<android.accounts.Account> getInternalAccounts(Account.Type type) {
		return Arrays.asList(accountManager.getAccountsByType(type
				.toTypeString()));
	}
}
