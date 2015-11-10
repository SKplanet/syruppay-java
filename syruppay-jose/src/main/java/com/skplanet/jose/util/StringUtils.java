package com.skplanet.jose.util;

import java.io.UnsupportedEncodingException;

/**
 * Created by 박병찬 on 2015-08-13.
 */
public class StringUtils {
	public final static String DEFAULT_CHARSET = "UTF-8";

	public static byte[] stringtoBytes(String src) {
		if (src == null)
			return null;

		byte[] b = new byte[0];
		try {
			b = src.getBytes(DEFAULT_CHARSET);
		} catch (UnsupportedEncodingException e) {
			b = src.getBytes();
		}

		return b;
	}

	public static String byteToString(byte[] src) {
		if (src == null)
			return null;

		String str = null;
		try {
			str = new String(src, DEFAULT_CHARSET);
		} catch (UnsupportedEncodingException e) {
			str = new String(src);
		}

		return str;
	}

	public static boolean isEmpty(String src) {
		return src == null || src.length() == 0?true:false;
	}

	public static boolean isNotEmpty(String src) {
		return !isEmpty(src);
	}

	public static String isDefaultEmpty(String src, String defaultStr) {
		return isEmpty(src)?defaultStr:src;
	}
}
