package com.dentist.util;


public class UrlSafeEncryption {

	public static String encrypt(String string) {
		return string.replaceAll("/", "_").replaceAll("\\+", "-");
	}

	public static String decrypt(String string) {
		return string.replaceAll("_", "/").replaceAll("-", "\\+");
	}
}
