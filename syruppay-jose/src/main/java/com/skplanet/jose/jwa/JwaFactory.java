package com.skplanet.jose.jwa;

import com.skplanet.jose.jwa.alg.*;
import com.skplanet.jose.jwa.enc.AesEncryptionWithHmacSha;
import com.skplanet.jose.exception.IllegalJoseHeaderException;

import java.util.HashMap;

public class JwaFactory {
	private static HashMap<Jwa, JweAlgorithm> supportedJweAlgorithm = new HashMap<Jwa, JweAlgorithm>();
	private static HashMap<Jwa, JwsAlgorithm> supportedJwsAlgorithm = new HashMap<Jwa, JwsAlgorithm>();
	private static HashMap<Jwa, JweEncryption> supportedJweEncryption = new HashMap<Jwa, JweEncryption>();

	static {
		supportedJweAlgorithm.put(Jwa.A128KW, new AesKeyWrapAlgorithm());
		supportedJweAlgorithm.put(Jwa.A256KW, new AesKeyWrapAlgorithm(32));
		supportedJweAlgorithm.put(Jwa.RSA1_5, new RSA15Algorithm());
		supportedJweAlgorithm.put(Jwa.RSA_OAEP, new RSAOAEPAlgorithm());
		supportedJweAlgorithm.put(Jwa.DIR, new DirectKeyAlgorithm());

		supportedJwsAlgorithm.put(Jwa.HS256, new HmacSha256Algorithm());
		supportedJwsAlgorithm.put(Jwa.RS256, new Sha256WithRsaAlgorithm());
		supportedJwsAlgorithm.put(Jwa.ES256, new Sha256WithECDSAUsingP256Algorithm());

		supportedJweEncryption.put(Jwa.A128CBC_HS256, new AesEncryptionWithHmacSha(32, 16));
	}

	public static JweAlgorithm getJweAlgorithm(Jwa jwa) {
		return supportedJweAlgorithm.get(jwa);
	}

	public static JwsAlgorithm getJwsAlgorithm(Jwa jwa) {
		return supportedJwsAlgorithm.get(jwa);
	}
	
	public static JweEncryption getJweEncryption(Jwa jwa) {
		return supportedJweEncryption.get(jwa);
	}
}
