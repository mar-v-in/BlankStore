package com.gc.android.market.api;

public class LoginException extends RuntimeException {

	public static final String ERROR_BAD_AUTHENTICATION = "BadAuthentication";
	public static final String ERROR_NOT_VERIFIED = "NotVerified";
	public static final String ERROR_TERMS_NOT_AGREED = "TermsNotAgreed";
	public static final String ERROR_CAPTCHA_REQUIRED = "CaptchaRequired";
	public static final String ERROR_UNKNOWN = "Unknown";
	public static final String ERROR_ACCOUNT_DELETED = "AccountDeleted";
	public static final String ERROR_ACCOUNT_DISABLED = "AccountDisabled";
	public static final String ERROR_SERVICE_DISABLED = "ServiceDisabled";
	public static final String ERROR_SERVICE_UNAVAILABLE = "ServiceUnavailable";
	
	private String googleErrorCode;
	
	public LoginException(String googleErrorCode) {
		super(getErrorMessage(googleErrorCode).isEmpty() ? googleErrorCode : getErrorMessage(googleErrorCode));
		this.googleErrorCode = googleErrorCode;
	}
	
	public String getErrorCode() {
		return googleErrorCode;
	}
	
	private static String getErrorMessage(String errorCode) {
		// from http://code.google.com/intl/fr-FR/apis/accounts/docs/AuthForInstalledApps.html
		if(errorCode.equals(ERROR_BAD_AUTHENTICATION))
			return "The login request used a username or password that is not recognized.";
		if(errorCode.equals(ERROR_NOT_VERIFIED))
			return "The account email address has not been verified. The user will need to access their Google account directly to resolve the issue before logging in using a non-Google application.";
		if(errorCode.equals(ERROR_TERMS_NOT_AGREED))
			return "The user has not agreed to terms. The user will need to access their Google account directly to resolve the issue before logging in using a non-Google application.";
		if(errorCode.equals(ERROR_CAPTCHA_REQUIRED))
			return "A CAPTCHA is required. (A response with this error code will also contain an image URL and a CAPTCHA token.)";
		if(errorCode.equals(ERROR_UNKNOWN))
			return "The error is unknown or unspecified; the request contained invalid input or was malformed.";
		if(errorCode.equals(ERROR_ACCOUNT_DELETED))
			return "The user account has been deleted.";
		if(errorCode.equals(ERROR_ACCOUNT_DISABLED))
			return "The user account has been disabled.";
		if(errorCode.equals(ERROR_SERVICE_DISABLED))
			return "The user's access to the specified service has been disabled. (The user account may still be valid.)";
		if(errorCode.equals(ERROR_SERVICE_UNAVAILABLE))
			return "The service is not available; try again later.";
		return "";
	}
}
