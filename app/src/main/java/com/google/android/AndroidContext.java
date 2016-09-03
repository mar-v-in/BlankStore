package com.google.android;

import com.google.tools.RequestContext;

import java.util.*;

public class AndroidContext extends RequestContext implements
        AndroidRequestKeys.BuildMetrics, AndroidRequestKeys.DeviceMetrics,
        AndroidRequestKeys.OperatorMetrics, AndroidRequestKeys.DeviceIdentifiers,
        AndroidRequestKeys.UserIdentifiers, AndroidRequestKeys.UserMetrics {

    static Random rand = new Random();

    /**
     * The base device is a Galaxy Nexus with random data.
     * @return corresponding AndroidContext
     */
	public static AndroidContext baseDevice() {
		return new AndroidContext().<AndroidContext>set(KEY_DEVICE_NATIVE_PLATFORM, Arrays.asList("armeabi-v7a","armeabi"))
								   .setBuildBootloader("PRIMELA03")
								   .buildFingerprint("google/yakju/maguro:4.1.1/JRO03C/398337:user/release-keys")
								   .buildHardware("tuna").buildId("JRO03C").buildBrand("Google")
								   .setBuildRadio("I9250XXLA2").buildClientId("android-google")
								   .buildTime(new Date().getTime()).sdkVersion(16)
								   .buildDevice("maguro").buildModel("Galaxy Nexus")
								   .buildManufacturer("Samsung").buildProduct("yakju").otaInstalled(false)
								   .operator("310260", "T-Mobile USA").timeZone("America/New_York")
								   .roaming("mobile-notroaming").userNumber(0).loggingId(rand.nextLong())
								   .otaCert("71Q6Rn2DDZl1zPDVaaeEHItd").locale(Locale.US)
								   .serial(randomSerialNumber()).setAndroidId(0).setDeviceDensityDpi(320)
								   .setDigest("1-929a0dca0eee55513280171a8585da7dcd3700f8")
								   .macAddress(randomMacAddress()).meid(randomMeid()).deviceTouchScreen(3)
								   .deviceKeyboardType(1).deviceNavigation(1).deviceScreenLayout(2)
								   .deviceGlEsVersion(131072).deviceWidthPixels(720)
								   .deviceHeightPixels(1184).deviceSharedLibraries(
                        Arrays.asList("android.test.runner", "com.android.future.usb.accessory",
                                "com.android.location.provider", "com.android.nfc_extras",
                                "com.google.android.maps", "com.google.android.media.effects",
                                "com.google.widevine.software.drm", "javax.obex")).deviceFeatures(
                        Arrays.asList("android.hardware.bluetooth", "android.hardware.camera",
                                "android.hardware.camera.autofocus", "android.hardware.camera.flash",
                                "android.hardware.camera.front", "android.hardware.faketouch",
                                "android.hardware.location", "android.hardware.location.gps",
                                "android.hardware.location.network", "android.hardware.microphone",
                                "android.hardware.nfc", "android.hardware.screen.landscape",
                                "android.hardware.screen.portrait", "android.hardware.sensor.accelerometer",
                                "android.hardware.sensor.barometer", "android.hardware.sensor.compass",
                                "android.hardware.sensor.gyroscope", "android.hardware.sensor.light",
                                "android.hardware.sensor.proximity", "android.hardware.telephony",
                                "android.hardware.telephony.gsm", "android.hardware.touchscreen",
                                "android.hardware.touchscreen.multitouch",
                                "android.hardware.touchscreen.multitouch.distinct",
                                "android.hardware.touchscreen.multitouch.jazzhand",
                                "android.hardware.usb.accessory", "android.hardware.usb.host",
                                "android.hardware.wifi", "android.hardware.wifi.direct",
                                "android.software.live_wallpaper", "android.software.sip",
                                "android.software.sip.voip", "com.cyanogenmod.android",
                                "com.cyanogenmod.nfc.enhanced", "com.google.android.feature.GOOGLE_BUILD",
                                "com.nxp.mifare", "com.tmobile.software.themes")).deviceLocales(
                        Arrays.asList("af", "af_ZA", "am", "am_ET", "ar", "ar_EG", "bg", "bg_BG", "ca", "ca_ES", "cs",
                                "cs_CZ", "da", "da_DK", "de", "de_AT", "de_CH", "de_DE", "de_LI", "el", "el_GR",
                                "en", "en_AU", "en_CA", "en_GB", "en_NZ", "en_SG", "en_US", "es", "es_ES",
                                "es_US", "fa", "fa_IR", "fi", "fi_FI", "fr", "fr_BE", "fr_CA", "fr_CH", "fr_FR",
                                "hi", "hi_IN", "hr", "hr_HR", "hu", "hu_HU", "in", "in_ID", "it", "it_CH",
                                "it_IT", "iw", "iw_IL", "ja", "ja_JP", "ko", "ko_KR", "lt", "lt_LT", "lv",
                                "lv_LV", "ms", "ms_MY", "nb", "nb_NO", "nl", "nl_BE", "nl_NL", "pl", "pl_PL",
                                "pt", "pt_BR", "pt_PT", "rm", "rm_CH", "ro", "ro_RO", "ru", "ru_RU", "sk",
                                "sk_SK", "sl", "sl_SI", "sr", "sr_RS", "sv", "sv_SE", "sw", "sw_TZ", "th",
                                "th_TH", "tl", "tl_PH", "tr", "tr_TR", "ug", "ug_CN", "uk", "uk_UA", "vi",
                                "vi_VN", "zh_CN", "zh_TW", "zu", "zu_ZA")).glExtensions(
                        Arrays.asList("GL_EXT_debug_marker", "GL_EXT_discard_framebuffer", "GL_EXT_multi_draw_arrays",
                                "GL_EXT_shader_texture_lod", "GL_EXT_texture_format_BGRA8888",
                                "GL_IMG_multisampled_render_to_texture", "GL_IMG_program_binary",
                                "GL_IMG_read_format", "GL_IMG_shader_binary", "GL_IMG_texture_compression_pvrtc",
                                "GL_IMG_texture_format_BGRA8888", "GL_IMG_texture_npot",
                                "GL_IMG_vertex_array_object", "GL_OES_EGL_image", "GL_OES_EGL_image_external",
                                "GL_OES_blend_equation_separate", "GL_OES_blend_func_separate",
                                "GL_OES_blend_subtract", "GL_OES_byte_coordinates",
                                "GL_OES_compressed_ETC1_RGB8_texture", "GL_OES_compressed_paletted_texture",
                                "GL_OES_depth24", "GL_OES_depth_texture", "GL_OES_draw_texture",
                                "GL_OES_egl_sync", "GL_OES_element_index_uint", "GL_OES_extended_matrix_palette",
                                "GL_OES_fixed_point", "GL_OES_fragment_precision_high",
                                "GL_OES_framebuffer_object", "GL_OES_get_program_binary", "GL_OES_mapbuffer",
                                "GL_OES_matrix_get", "GL_OES_matrix_palette", "GL_OES_packed_depth_stencil",
                                "GL_OES_point_size_array", "GL_OES_point_sprite", "GL_OES_query_matrix",
                                "GL_OES_read_format", "GL_OES_required_internalformat", "GL_OES_rgb8_rgba8",
                                "GL_OES_single_precision", "GL_OES_standard_derivatives", "GL_OES_stencil8",
                                "GL_OES_stencil_wrap", "GL_OES_texture_cube_map", "GL_OES_texture_env_crossbar",
                                "GL_OES_texture_float", "GL_OES_texture_half_float",
                                "GL_OES_texture_mirrored_repeat", "GL_OES_vertex_array_object",
                                "GL_OES_vertex_half_float"));

	}

	public String getBuildId() {
		return getString(KEY_BUILD_ID);
	}

	public AndroidContext buildId(String id) {
		return set(KEY_BUILD_ID, id);
	}

	private static String randomImei() {
		final StringBuilder sb = new StringBuilder(15);
		for (int i = 0; i < 15; i++) {
			sb.append("0123456789".charAt(rand.nextInt(10)));
		}
		return sb.toString();
	}

	private static String randomMacAddress() {
		String mac = "b407f9";
		for (int i = 0; i < 6; i++) {
			mac += Integer.toString(rand.nextInt(16), 16);
		}
		return mac;
	}

	private static String randomMeid() {
		// http://en.wikipedia.org/wiki/International_Mobile_Equipment_Identity
		// We start with a known base, and generate random MEID
		String meid = "35503104";
		for (int i = 0; i < 6; i++) {
			meid += Integer.toString(rand.nextInt(10));
		}

		// Luhn algorithm (check digit)
		int sum = 0;
		for (int i = 0; i < meid.length(); i++) {
			int c = Integer.parseInt(String.valueOf(meid.charAt(i)));
			if ((meid.length() - i - 1) % 2 == 0) {
				c *= 2;
				c = c % 10 + c / 10;
			}

			sum += c;
		}
		final int check = (100 - sum) % 10;
		meid += Integer.toString(check);

		return meid;
	}

	private static String randomSerialNumber() {
		String serial = "3933E6";
		for (int i = 0; i < 10; i++) {
			serial += Integer.toString(rand.nextInt(16), 16);
		}
		serial = serial.toUpperCase();
		return serial;
	}

	public Long getAndroidId() {
		return getLong(KEY_ANDROID_ID_LONG);
	}

	public String getAndroidIdHex() {
		return getString(KEY_ANDROID_ID_HEX);
	}

	public String getBuildDevice() {
        return getString(KEY_BUILD_DEVICE);
    }

	public String getDigest() {
		return getString(KEY_CHECKIN_DIGEST);
	}

	public String getEmail() {
		return getString(KEY_EMAIL);
	}

	public String getMacAddress() {
		return getString(KEY_WIFI_MAC_ADDRESS);
	}

	public int getSdkVersion() {
		return getInt(KEY_SDK_VERSION);
	}

	public long getSecurityToken() {
		return getLong(KEY_CHECKIN_SECURITY_TOKEN);
	}

	public String getSimOperator() {
		return getString(KEY_SIM_OPERATOR_NUMERIC);
	}

	public AndroidContext userNumber(int userNumber) {
		set(KEY_USER_NUMBER, userNumber);
		return this;
	}

	public AndroidContext setAndroidId(final long androidId) {
		set(KEY_ANDROID_ID_HEX, Long.toHexString(androidId));
		set(KEY_ANDROID_ID_LONG, androidId);
		return this;
	}

	public AndroidContext setAndroidId(final String androidId) {
		set(KEY_ANDROID_ID_LONG, Long.parseLong(androidId, 16));
		set(KEY_ANDROID_ID_HEX, androidId);
		return this;
	}

	public AndroidContext setBuildBootloader(final String bootloader) {
		return set(KEY_BUILD_BOOTLOADER, bootloader);
	}

	public AndroidContext buildBrand(final String brand) {
		return set(KEY_BUILD_BRAND, brand);
	}

	public AndroidContext buildClientId(final String client) {
		set(KEY_CLIENT_ID, client);
		return this;
	}

	public AndroidContext buildDevice(final String device) {
		return set(KEY_BUILD_DEVICE, device);
	}

	public AndroidContext buildFingerprint(final String buildId) {
        return set(KEY_BUILD_FINGERPRINT, buildId);
	}

	public AndroidContext buildHardware(final String buildHardware) {
		return set(KEY_BUILD_HARDWARE, buildHardware);
	}

	public AndroidContext buildManufacturer(final String manufacturer) {
		return set(KEY_BUILD_MANUFACTURER, manufacturer);
	}

	public AndroidContext buildModel(final String model) {
		return set(KEY_BUILD_MODEL, model);
	}

	public AndroidContext otaInstalled(final boolean buildOtaInstalled) {
		return set(KEY_OTA_INSTALLED, buildOtaInstalled);
	}

	public AndroidContext buildProduct(final String product) {
		return set(KEY_BUILD_PRODUCT, product);
	}

	public AndroidContext setBuildRadio(final String radio) {
		return set(KEY_BUILD_RADIO, radio);
	}

	public AndroidContext sdkVersion(final int sdkVersion) {
		return set(KEY_SDK_VERSION, sdkVersion);
	}

	public AndroidContext setBuildSerial(final String buildSerial) {
		return set(KEY_BUILD_SERIAL, buildSerial);
	}

	public AndroidContext buildTime(final long buildTimestamp) {
		return set(KEY_BUILD_TIME, buildTimestamp);
	}

	public AndroidContext setCellOperator(final String numeric) {
		return set(KEY_CELL_OPERATOR_NUMERIC, numeric);
	}

	public AndroidContext setCellOperator(final String numeric, final String alpha) {
		setCellOperatorName(alpha);
		return setCellOperator(numeric);
	}

	public AndroidContext setCellOperatorName(final String alpha) {
		return set(KEY_CELL_OPERATOR_NAME, alpha);
	}

	public AndroidContext setDeviceDensityDpi(final int deviceDensityDpi) {
        return set(KEY_DEVICE_SCREEN_DPI, deviceDensityDpi);
	}

	public AndroidContext deviceFeatures(final List<String> systemFeatures) {
        return set(KEY_DEVICE_FEATURES, systemFeatures);
	}

	public AndroidContext deviceGlEsVersion(final int deviceGlEsVersion) {
        return set(KEY_DEVICE_GLES_VERSION, deviceGlEsVersion);
	}

	public AndroidContext deviceHeightPixels(final int deviceHeightPixels) {
		return set(KEY_DEVICE_SCREEN_HEIGHT_PX, deviceHeightPixels);
	}

	public AndroidContext deviceKeyboardType(final int deviceKeyboardType) {
        return set(KEY_DEVICE_KEYBOARD_TYPE, deviceKeyboardType);
	}

	public AndroidContext deviceLocales(final List<String> supportedLocales) {
        return set(KEY_DEVICE_LOCALES, supportedLocales);
	}

	public AndroidContext deviceNavigation(final int deviceNavigation) {
        return set(KEY_DEVICE_NAVIGATION, deviceNavigation);
	}

	public AndroidContext deviceScreenLayout(final int deviceScreenLayout) {
        return set(KEY_DEVICE_SCREEN_LAYOUT, deviceScreenLayout);
	}

	public AndroidContext deviceSharedLibraries(final List<String> sharedLibraries) {
        return set(KEY_DEVICE_SHARED_LIBRARY, sharedLibraries);
	}

	public AndroidContext deviceTouchScreen(final int deviceTouchScreen) {
        return set(KEY_DEVICE_TOUCH_SCREEN, deviceTouchScreen);
	}

	public AndroidContext deviceWidthPixels(final int deviceWidthPixels) {
		return set(KEY_DEVICE_SCREEN_WIDTH_PX, deviceWidthPixels);
	}

	public AndroidContext setDigest(final String digest) {
		return set(KEY_CHECKIN_DIGEST, digest);
	}

	public AndroidContext setEmail(final String email) {
		return set(KEY_EMAIL, email);
	}

	public AndroidContext setEsn(final String esn) {
        return set(KEY_ESN, esn);
	}

	public AndroidContext glExtensions(final List<String> glExtensions) {
		return set(KEY_DEVICE_GLES_EXTENSIONS, glExtensions);
	}

	public AndroidContext imei(final String imei) {
		return set(KEY_IMEI, imei);
	}

	public AndroidContext locale(final Locale locale) {
        return set(KEY_LOCALE, locale.getLanguage().toLowerCase()+"_"+locale.getCountry().toUpperCase());
	}

	public AndroidContext loggingId(final long loggingId) {
		set(KEY_LOGGING_ID, loggingId);
		return this;
	}

	public AndroidContext macAddress(final String mac) {
		return set(KEY_WIFI_MAC_ADDRESS, mac);
	}

	public AndroidContext meid(final String meid) {
		return set(KEY_MEID, meid);
	}

	public AndroidContext operator(final String numeric, final String alpha) {
		setCellOperator(numeric, alpha);
		simOperator(numeric, alpha);
		return this;
	}

	public AndroidContext otaCert(final String otaCert) {
		return set(KEY_OTA_CERT, otaCert);
	}

	public AndroidContext roaming(final String roaming) {
		return set(KEY_ROAMING, roaming);
	}

	public AndroidContext securityToken(final long securityToken) {
		return set(KEY_CHECKIN_SECURITY_TOKEN, securityToken);
	}

	public AndroidContext serial(final String serial) {
		return set(KEY_BUILD_SERIAL, serial);
	}

	public AndroidContext simOperator(final String operatorNumeric) {
		return set(KEY_SIM_OPERATOR_NUMERIC, operatorNumeric);
	}

	public AndroidContext simOperator(final String numeric, final String alpha) {
		simOperatorName(alpha);
		simOperator(numeric);
		return this;
	}

	public AndroidContext simOperatorName(final String operatorAlpha) {
		return set(KEY_SIM_OPERATOR_NAME, operatorAlpha);
	}

	public AndroidContext timeZone(final String timeZone) {
		return set(KEY_TIME_ZONE, timeZone);
	}
}
