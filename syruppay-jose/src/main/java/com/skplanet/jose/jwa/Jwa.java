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

package com.skplanet.jose.jwa;

/**
 * JWE의 alg, enc에서 지원하는 암호화 알고리즘에 대한 enum class 
 * 
 * @author 1000808 (byeongchan.park@sk.com)
 *
 */
public enum Jwa {
	RSA1_5("RSA1_5"),
	RSA_OAEP("RSA-OAEP"),
	A128KW("A128KW"),
	A256KW("A256KW"),
	DIR("dir"),

	A128CBC_HS256("A128CBC-HS256"),
	A256CBC_HS512("A256CBC-HS512"),
	A128GCM("A128GCM"),
	A256GCM("A256GCM"),

	HS256("HS256"),
	RS256("RS256"),
	ES256("ES256")
	;

	private String value;

	private Jwa(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	public boolean equals(Jwa jwa){
		return (jwa == null)?false:this.value.equals(jwa.getValue());
    }
}
