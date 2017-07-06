package com.android.vending;

public class Utils {
	public static String niceSize(int sizeInt) {
		float size = sizeInt;
		float multiply = 1;
		while (size > 750) {
			size /= 1000F;
			multiply *= 1000F;
		}
		size = Math.round(size * 100) / 100F;
		if (multiply == 1000) {
			return size + " KB";
		}
		if (multiply == 1000 * 1000) {
			return size + " MB";
		}
		if (multiply == 1000 * 1000 * 1000) {
			return size + " GB";
		}
		return size + " Bytes";
	}
}
