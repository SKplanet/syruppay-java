package com.skplanet.jose.util;

/**
 * Created by 박병찬 on 2015-08-13.
 */
public class ByteUtils {
	public static boolean equals(byte[] b1, byte[] b2) {
		if (b1.length != b2.length)
			return false;

		for (int i = 0; i < b1.length; i++) {
			if (b1[i] != b2[i])
				return false;
		}

		return true;
	}

	public static int getBitLength(byte[] b) {
		return b.length * 8;
	}

	public static byte[] firstHalf(byte[] b) {
		byte[] t = new byte[b.length/2];
		System.arraycopy(b, 0, t, 0, t.length);

		return t;
	}

	public static byte[] restHalf(byte[] b) {
		byte[] t = new byte[b.length/2];
		System.arraycopy(b, b.length/2, t, 0, t.length);

		return t;
	}
}
