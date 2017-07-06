/*
 * Copyright (c) 2014 μg Project Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android;

public class AndroidRequestKeys {
    public static interface BuildMetrics {
        public static final String KEY_BUILD_BOOTLOADER = "buildBootloader";
        public static final String KEY_BUILD_BRAND = "buildBrand";
        public static final String KEY_BUILD_DEVICE = "buildDevice";
        public static final String KEY_BUILD_FINGERPRINT = "buildFingerprint";
        public static final String KEY_BUILD_HARDWARE = "buildHardware";
        public static final String KEY_BUILD_MANUFACTURER = "buildManufacturer";
        public static final String KEY_BUILD_MODEL = "buildModel";
        public static final String KEY_BUILD_PRODUCT = "buildProduct";
        public static final String KEY_BUILD_RADIO = "buildRadio";
        public static final String KEY_BUILD_SERIAL = "buildSerial";
        public static final String KEY_BUILD_ID = "buildId";
        public static final String KEY_BUILD_TIME = "buildTime";

        public static final String KEY_CLIENT_ID = "clientId";
        public static final String KEY_OTA_INSTALLED = "otaInstalled";
        public static final String KEY_OTA_CERT = "otaCert";
        public static final String KEY_SDK_VERSION = "sdkVersion";
    }

    public static interface DeviceMetrics {
        public static final String KEY_DEVICE_FEATURES = "deviceFeatures";
        public static final String KEY_DEVICE_SCREEN_DPI = "deviceScreenDpi";
        public static final String KEY_DEVICE_CLASS = "deviceClass";
        public static final String KEY_DEVICE_GLES_VERSION = "deviceGlEsVersion";
        public static final String KEY_DEVICE_GLES_EXTENSIONS = "deviceGlEsExtensions";
        public static final String KEY_DEVICE_FIVE_WAY_NAVIGATION = "deviceFiveWayNavigation";
        public static final String KEY_DEVICE_HARD_KEYBOARD = "deviceHardKeyboard";
        public static final String KEY_DEVICE_SCREEN_HEIGHT_PX = "deviceScreenHeightPx";
        public static final String KEY_DEVICE_KEYBOARD_TYPE = "deviceKeyboardType";
        public static final String KEY_DEVICE_LOCALES = "deviceLocales";
        public static final String KEY_DEVICE_NATIVE_PLATFORM = "deviceNativePlatform";
        public static final String KEY_DEVICE_NAVIGATION = "deviceNavigation";
        public static final String KEY_DEVICE_SCREEN_LAYOUT = "deviceScreenLayout";
        public static final String KEY_DEVICE_SHARED_LIBRARY = "deviceSharedLibrary";
        public static final String KEY_DEVICE_TOUCH_SCREEN = "deviceTouchscreen";
        public static final String KEY_DEVICE_SCREEN_WIDTH_PX = "deviceScreenWidthPx";
    }

    public static interface OperatorMetrics {
        public static final String KEY_CELL_OPERATOR_NAME = "cellOperatorName";
        public static final String KEY_CELL_OPERATOR_NUMERIC = "cellOperatorNumeric";
        public static final String KEY_SIM_OPERATOR_NAME = "simOperatorName";
        public static final String KEY_SIM_OPERATOR_NUMERIC = "simOperatorNumeric";
        public static final String KEY_ROAMING = "roaming";
    }

    public static interface UserMetrics {
        public static final String KEY_LOCALE = "locale";
        public static final String KEY_TIME_ZONE = "timeZone";
        public static final String KEY_CHECKIN_DIGEST = "checkinDigest";
    }

    public static interface DeviceIdentifiers {
        public static final String KEY_ANDROID_ID_LONG = "androidIdLong";
        public static final String KEY_ANDROID_ID_HEX = "androidIdHex";
        public static final String KEY_LOGGING_ID = "loggingId";
        public static final String KEY_WIFI_MAC_ADDRESS = "wifiMacAddress";
        public static final String KEY_MEID = "meid";
        public static final String KEY_ESN = "esn";
        public static final String KEY_IMEI = "imei";
        public static final String KEY_CHECKIN_SECURITY_TOKEN = "checkinSecurityToken";
    }

    public static interface UserIdentifiers {
        public static final String KEY_AUTHORIZATION_TOKEN = "authToken";
        public static final String KEY_EMAIL = "email";
        public static final String KEY_USER_NUMBER = "userNumber";
    }
}
