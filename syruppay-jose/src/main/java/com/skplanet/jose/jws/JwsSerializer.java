package com.skplanet.jose.jws;

import com.skplanet.jose.JoseAction;
import com.skplanet.jose.JoseActionType;
import com.skplanet.jose.JoseHeader;
import com.skplanet.jose.commons.codec.binary.Base64;
import com.skplanet.jose.exception.IllegalSignatureToken;
import com.skplanet.jose.jwa.Jwa;
import com.skplanet.jose.jwa.JwaFactory;
import com.skplanet.jose.jwa.JwsAlgorithm;
import com.skplanet.jose.jwa.crypto.Algorithm;
import com.skplanet.jose.jwa.crypto.CryptoUtils;
import com.skplanet.jose.jwa.crypto.KeyAlgorithm;
import com.skplanet.jose.util.StringUtils;

import java.security.Key;

/**
 * Created by 박병찬 on 2015-07-17.
 */
public class JwsSerializer implements JoseAction {
	private byte[] key;

	private JwsParts jwsParts = new JwsParts();

	public JwsSerializer(JoseHeader header, String payload, byte[] key) {
		jwsParts.joseHeader = header;
		jwsParts.payload = StringUtils.stringtoBytes(payload);
		this.key = key;
	}

	public JwsSerializer(String src, byte[] key) {
		jwsParts.parse(src);
		this.key = key;
	}

	public String compactSerialization() {
		Jwa alg = jwsParts.joseHeader.getAlgorithm();
		JwsAlgorithm jwsAlgorithm = JwaFactory.getJwsAlgorithm(alg);

		jwsParts.signature = jwsAlgorithm.sign(key, jwsParts.getSignatureSource());

		return jwsParts.toString();
	}

	public String compactDeserialization() {
		Jwa alg = jwsParts.joseHeader.getAlgorithm();
		JwsAlgorithm jwsAlgorithm = JwaFactory.getJwsAlgorithm(alg);

		byte[] signSource = jwsParts.getSignatureSource();
		byte[] expected = jwsParts.signature;

		if (!jwsAlgorithm.verify(key, signSource, expected)) {
			throw new IllegalSignatureToken("signature fail");
		}

		return StringUtils.byteToString(jwsParts.payload);
	}

	public JoseHeader getJoseHeader() {
		return jwsParts.joseHeader;
	}

	private class JwsParts {
		public JoseHeader joseHeader;
		public byte[] payload;
		public byte[] signature;

		@Override
		public String toString() {
			return String.format("%s.%s.%s",
					joseHeader.getEncoded(),
					Base64.encodeBase64URLSafeString(payload),
					Base64.encodeBase64URLSafeString(signature)
					);
		}

		public byte[] getSignatureSource() {
			return StringUtils.stringtoBytes(
					String.format("%s.%s", joseHeader.getEncoded(), Base64.encodeBase64URLSafeString(payload)));
		}

		public void parse(String src) {
			if (src == null || src.length() == 0) {
				throw new IllegalSignatureToken();
			}

			String[] token = src.split("\\.");
			if (token == null || token.length != 3) {
				throw new IllegalSignatureToken("Unknown jws signature format");
			}

			joseHeader = new JoseHeader();
			joseHeader.setEncoded(token[0]);
			payload = Base64.decodeBase64(token[1]);
			signature = Base64.decodeBase64(token[2]);
		}
	}
}
