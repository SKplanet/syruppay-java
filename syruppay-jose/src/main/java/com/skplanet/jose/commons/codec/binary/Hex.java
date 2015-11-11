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

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skplanet.jose.commons.codec.binary;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import com.skplanet.jose.commons.codec.BinaryDecoder;
import com.skplanet.jose.commons.codec.EncoderException;
import com.skplanet.jose.commons.codec.BinaryEncoder;
import com.skplanet.jose.commons.codec.CharEncoding;
import com.skplanet.jose.commons.codec.DecoderException;

/**
 * Converts hexadecimal Strings. The charset used for certain operation can be set, the default is set in
 * {@link #DEFAULT_CHARSET_NAME}
 *
 * This class is thread-safe.
 *
 * @since 1.1
 * @version $Id: Hex.java 1637888 2014-11-10 15:29:02Z ggregory $
 */
public class Hex implements BinaryEncoder, BinaryDecoder {

    /**
     * Default charset name is {@link CharEncoding#UTF_8}
     *
     * @since 1.4
     */
    public static final String DEFAULT_CHARSET_NAME = CharEncoding.UTF_8;

    /**
     * Used to build output as Hex
     */
    private static final char[] DIGITS_LOWER =
        {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Used to build output as Hex
     */
    private static final char[] DIGITS_UPPER =
        {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * Converts an array of characters representing hexadecimal values into an array of bytes of those same values. The
     * returned array will be half the length of the passed array, as it takes two characters to represent any given
     * byte. An exception is thrown if the passed char array has an odd number of elements.
     *
     * @param data
     *            An array of characters containing hexadecimal digits
     * @return A byte array containing binary data decoded from the supplied char array.
     * @throws DecoderException
     *             Thrown if an odd number or illegal of characters is supplied
     */
    public static byte[] decodeHex(final char[] data) throws DecoderException {

        final int len = data.length;

        if ((len & 0x01) != 0) {
            throw new DecoderException("Odd number of characters.");
        }

        final byte[] out = new byte[len >> 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }

    /**
     * Converts an array of bytes into an array of characters representing the hexadecimal values of each byte in order.
     * The returned array will be double the length of the passed array, as it takes two characters to represent any
     * given byte.
     *
     * @param data
     *            a byte[] to convert to Hex characters
     * @return A char[] containing hexadecimal characters
     */
    public static char[] encodeHex(final byte[] data) {
        return encodeHex(data, true);
    }

    /**
     * Converts a byte buffer into an array of characters representing the hexadecimal values of each byte in order.
     * The returned array will be double the length of the passed array, as it takes two characters to represent any
     * given byte.
     *
     * @param data
     *            a byte buffer to convert to Hex characters
     * @return A char[] containing hexadecimal characters
     * @since 1.11
     */
    public static char[] encodeHex(final ByteBuffer data) {
        return encodeHex(data, true);
    }

    /**
     * Converts an array of bytes into an array of characters representing the hexadecimal values of each byte in order.
     * The returned array will be double the length of the passed array, as it takes two characters to represent any
     * given byte.
     *
     * @param data
     *            a byte[] to convert to Hex characters
     * @param toLowerCase
     *            <code>true</code> converts to lowercase, <code>false</code> to uppercase
     * @return A char[] containing hexadecimal characters
     * @since 1.4
     */
    public static char[] encodeHex(final byte[] data, final boolean toLowerCase) {
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    /**
     * Converts a byte buffer into an array of characters representing the hexadecimal values of each byte in order.
     * The returned array will be double the length of the passed array, as it takes two characters to represent any
     * given byte.
     *
     * @param data
     *            a byte buffer to convert to Hex characters
     * @param toLowerCase
     *            <code>true</code> converts to lowercase, <code>false</code> to uppercase
     * @return A char[] containing hexadecimal characters
     * @since 1.11
     */
    public static char[] encodeHex(final ByteBuffer data, final boolean toLowerCase) {
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    /**
     * Converts an array of bytes into an array of characters representing the hexadecimal values of each byte in order.
     * The returned array will be double the length of the passed array, as it takes two characters to represent any
     * given byte.
     *
     * @param data
     *            a byte[] to convert to Hex characters
     * @param toDigits
     *            the output alphabet
     * @return A char[] containing hexadecimal characters
     * @since 1.4
     */
    protected static char[] encodeHex(final byte[] data, final char[] toDigits) {
        final int l = data.length;
        final char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }

    /**
     * Converts a byte buffer into an array of characters representing the hexadecimal values of each byte in order.
     * The returned array will be double the length of the passed array, as it takes two characters to represent any
     * given byte.
     *
     * @param data
     *            a byte buffer to convert to Hex characters
     * @param toDigits
     *            the output alphabet
     * @return A char[] containing hexadecimal characters
     * @since 1.11
     */
    protected static char[] encodeHex(final ByteBuffer data, final char[] toDigits) {
        return encodeHex(data.array(), toDigits);
    }

    /**
     * Converts an array of bytes into a String representing the hexadecimal values of each byte in order. The returned
     * String will be double the length of the passed array, as it takes two characters to represent any given byte.
     *
     * @param data
     *            a byte[] to convert to Hex characters
     * @return A String containing hexadecimal characters
     * @since 1.4
     */
    public static String encodeHexString(final byte[] data) {
        return new String(encodeHex(data));
    }

    /**
     * Converts a byte buffer into a String representing the hexadecimal values of each byte in order. The returned
     * String will be double the length of the passed array, as it takes two characters to represent any given byte.
     *
     * @param data
     *            a byte buffer to convert to Hex characters
     * @return A String containing hexadecimal characters
     * @since 1.11
     */
    public static String encodeHexString(final ByteBuffer data) {
        return new String(encodeHex(data));
    }

    /**
     * Converts a hexadecimal character to an integer.
     *
     * @param ch
     *            A character to convert to an integer digit
     * @param index
     *            The index of the character in the source
     * @return An integer
     * @throws DecoderException
     *             Thrown if ch is an illegal hex character
     */
    protected static int toDigit(final char ch, final int index) throws DecoderException {
        final int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new DecoderException("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }

    private final String charset;

    /**
     * Creates a new codec with the default charset name {@link #DEFAULT_CHARSET}
     */
    public Hex() {
        // use default encoding
        this.charset = DEFAULT_CHARSET_NAME;
    }

    /**
     * Converts an array of character bytes representing hexadecimal values into an array of bytes of those same values.
     * The returned array will be half the length of the passed array, as it takes two characters to represent any given
     * byte. An exception is thrown if the passed char array has an odd number of elements.
     *
     * @param array
     *            An array of character bytes containing hexadecimal digits
     * @return A byte array containing binary data decoded from the supplied byte array (representing characters).
     * @throws DecoderException
     *             Thrown if an odd number of characters is supplied to this function
     * @see #decodeHex(char[])
     */
    public byte[] decode(final byte[] array) throws DecoderException{
        try {
        	return decodeHex(new String(array, getCharset()).toCharArray());			
		} catch (Exception e) {
		}
    	return null;
    }

    /**
     * Converts a buffer of character bytes representing hexadecimal values into an array of bytes of those same values.
     * The returned array will be half the length of the passed array, as it takes two characters to represent any given
     * byte. An exception is thrown if the passed char array has an odd number of elements.
     *
     * @param buffer
     *            An array of character bytes containing hexadecimal digits
     * @return A byte array containing binary data decoded from the supplied byte array (representing characters).
     * @throws DecoderException
     *             Thrown if an odd number of characters is supplied to this function
     * @see #decodeHex(char[])
     * @since 1.11
     */
    public byte[] decode(final ByteBuffer buffer) throws DecoderException {
        try {
			return decodeHex(new String(buffer.array(), getCharset()).toCharArray());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
    }

    /**
     * Converts a String or an array of character bytes representing hexadecimal values into an array of bytes of those
     * same values. The returned array will be half the length of the passed String or array, as it takes two characters
     * to represent any given byte. An exception is thrown if the passed char array has an odd number of elements.
     *
     * @param object
     *            A String, ByteBuffer, byte[], or an array of character bytes containing hexadecimal digits
     * @return A byte array containing binary data decoded from the supplied byte array (representing characters).
     * @throws DecoderException
     *             Thrown if an odd number of characters is supplied to this function or the object is not a String or
     *             char[]
     * @see #decodeHex(char[])
     */
    public Object decode(final Object object) throws DecoderException {
        if (object instanceof String) {
            return decode(((String) object).toCharArray());
        } else if (object instanceof byte[]) {
            return decode((byte[]) object);
        } else if (object instanceof ByteBuffer) {
            return decode((ByteBuffer) object);
        } else {
            try {
                return decodeHex((char[]) object);
            } catch (final ClassCastException e) {
                throw new DecoderException(e.getMessage(), e);
            }
        }
    }

    /**
     * Converts an array of bytes into an array of bytes for the characters representing the hexadecimal values of each
     * byte in order. The returned array will be double the length of the passed array, as it takes two characters to
     * represent any given byte.
     * <p>
     * The conversion from hexadecimal characters to the returned bytes is performed with the charset named by
     * {@link #getCharset()}.
     * </p>
     *
     * @param array
     *            a byte[] to convert to Hex characters
     * @return A byte[] containing the bytes of the hexadecimal characters
     * @since 1.5 No longer throws IllegalStateException if the charsetName is invalid.
     * @see #encodeHex(byte[])
     */
    public byte[] encode(final byte[] array) {
        try {
			return encodeHexString(array).getBytes(this.getCharset());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
    }

    /**
     * Converts byte buffer into an array of bytes for the characters representing the hexadecimal values of each
     * byte in order. The returned array will be double the length of the passed array, as it takes two characters to
     * represent any given byte.
     * <p>
     * The conversion from hexadecimal characters to the returned bytes is performed with the charset named by
     * {@link #getCharset()}.
     * </p>
     *
     * @param array
     *            a byte buffer to convert to Hex characters
     * @return A byte[] containing the bytes of the hexadecimal characters
     * @see #encodeHex(byte[])
     * @since 1.11
     */
    public byte[] encode(final ByteBuffer array) {
        try {
			return encodeHexString(array).getBytes(this.getCharset());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
    }

    /**
     * Converts a String or an array of bytes into an array of characters representing the hexadecimal values of each
     * byte in order. The returned array will be double the length of the passed String or array, as it takes two
     * characters to represent any given byte.
     * <p>
     * The conversion from hexadecimal characters to bytes to be encoded to performed with the charset named by
     * {@link #getCharset()}.
     * </p>
     *
     * @param object
     *            a String, ByteBuffer, or byte[] to convert to Hex characters
     * @return A char[] containing hexadecimal characters
     * @throws EncoderException
     *             Thrown if the given object is not a String or byte[]
     * @see #encodeHex(byte[])
     */
    public Object encode(final Object object) throws EncoderException {
        byte[] byteArray = null;
        if (object instanceof String) {
            try {
				byteArray = ((String) object).getBytes(this.getCharset());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
        } else if (object instanceof ByteBuffer) {
            byteArray = ((ByteBuffer) object).array();
        } else {
            try {
                byteArray = (byte[]) object;
            } catch (final ClassCastException e) {
                throw new EncoderException(e.getMessage(), e);
            }
        }
        return encodeHex(byteArray);
    }

    /**
     * Gets the charset.
     *
     * @return the charset.
     * @since 1.5
     */
    public String getCharset() {
        return DEFAULT_CHARSET_NAME;
    }

    /**
     * Returns a string representation of the object, which includes the charset name.
     *
     * @return a string representation of the object.
     */
    public String toString() {
        return super.toString() + "[charsetName=" + this.charset + "]";
    }
}
