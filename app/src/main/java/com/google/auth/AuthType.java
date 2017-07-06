package com.google.auth;

public enum AuthType {
	AuthToken, EncryptedPassword, MasterToken, Password;

	public static AuthType fromInt(final int i) {
		switch (i) {
			case 1:
				return AuthToken;
			case 2:
				return EncryptedPassword;
			case 4:
				return Password;
			case 3:
			default:
				return MasterToken;
		}
	}

	public int toInt() {
		switch (this) {
			case AuthToken:
				return 1;
			case EncryptedPassword:
				return 2;
			case MasterToken:
				return 3;
			case Password:
				return 4;
			default:
				return 0;
		}
	}
}
