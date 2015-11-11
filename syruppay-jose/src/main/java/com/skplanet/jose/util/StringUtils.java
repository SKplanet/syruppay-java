/*
 * Copyright (c) 2015 SK PLANET. ALL Rights Reserved.
 *
 * Syrup Pay Jose Library
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
