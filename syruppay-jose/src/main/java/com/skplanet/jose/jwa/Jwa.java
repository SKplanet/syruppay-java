package com.skplanet.jose.jwa;

/**
 * JWE의 alg, enc에서 지원하는 암호화 알고리즘에 대한 enum class 
 * 
 * @author 1000808 (byeongchan.park@sk.com)
 *
 */
public enum Jwa {
	A128KW("A128KW"), A128CBC_HS256("A128CBC-HS256"),
	RSA1_5("RSA1_5"), RS256("RS256"), HS256("HS256"),
	RSA_OAEP("RSA-OAEP"), ES256("ES256"), A256KW("A256KW"),
	DIR("dir");

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
