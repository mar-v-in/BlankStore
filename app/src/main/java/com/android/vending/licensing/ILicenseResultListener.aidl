package com.android.vending.licensing;

/* According to https://code.google.com/p/marketlicensing/source/browse/library/src/com/android/vending/licensing/LicenseValidator.java
  responseCodes are:

   private static final int LICENSED = 0x0;
   private static final int NOT_LICENSED = 0x1;
   private static final int LICENSED_OLD_KEY = 0x2;
   private static final int ERROR_NOT_MARKET_MANAGED = 0x3;
   private static final int ERROR_SERVER_FAILURE = 0x4;
   private static final int ERROR_OVER_QUOTA = 0x5;

   private static final int ERROR_CONTACTING_SERVER = 0x101;
   private static final int ERROR_INVALID_PACKAGE_NAME = 0x102;
   private static final int ERROR_NON_MATCHING_UID = 0x103;
*/

interface ILicenseResultListener {
    void verifyLicense(int responseCode, String signedData, String signature);
}

