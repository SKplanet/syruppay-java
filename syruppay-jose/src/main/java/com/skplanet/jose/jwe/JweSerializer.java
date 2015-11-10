package com.skplanet.jose.jwe;

import com.skplanet.jose.JoseAction;
import com.skplanet.jose.JoseHeader;
import com.skplanet.jose.commons.codec.binary.Base64;
import com.skplanet.jose.exception.IllegalEncrytionToken;
import com.skplanet.jose.jwa.JwaFactory;
import com.skplanet.jose.jwa.JweAlgorithm;
import com.skplanet.jose.jwa.JweEncryption;
import com.skplanet.jose.jwa.alg.JweAlgResult;
import com.skplanet.jose.jwa.enc.ContentEncryptKeyGenerator;
import com.skplanet.jose.jwa.enc.JweEncResult;
import com.skplanet.jose.util.StringUtils;

public class JweSerializer implements JoseAction {
	private byte[] payload;
	private byte[] key;

	private JweParts jweParts = new JweParts();

	public JweSerializer(JoseHeader header, String payload, byte[] key) {
		jweParts.joseHeader = header;
		this.payload = StringUtils.stringtoBytes(payload);
		this.key = key;
	}

	public JweSerializer(String src, byte[] key) {
		jweParts.parse(src);
		this.key = key;
	}

	public void setUserEncryptionKey(byte[] cek, byte[] iv) {
		jweParts.cek = cek;
		jweParts.iv = iv;
	}

	private byte[] getAdditionalAuthenticatedData() {
		return StringUtils.stringtoBytes(jweParts.joseHeader.getEncoded());
	}

	public String compactSerialization() {
		JweAlgorithm jweAlgorithm = JwaFactory.getJweAlgorithm(jweParts.joseHeader.getAlgorithm());
		JweEncryption jweEncryption = JwaFactory.getJweEncryption(jweParts.joseHeader.getEncrytion());

		ContentEncryptKeyGenerator cekGenerator = jweEncryption.getContentEncryptionKeyGenerator();
		cekGenerator.setUserEncryptionKey(jweParts.cek);

		JweAlgResult jweAlgResult = jweAlgorithm.encryption(key, cekGenerator);
		jweParts.cek = jweAlgResult.getEncryptedCek();

		JweEncResult encResult = jweEncryption.encryptAndSign(jweAlgResult.getCek(),
				jweParts.iv,
				payload,
				getAdditionalAuthenticatedData());

		jweParts.cipherText = encResult.getCipherText();
		jweParts.at = encResult.getAt();
		jweParts.iv = encResult.getIv();

		return jweParts.toString();
	}

	public String compactDeserialization() {
		JweAlgorithm jweAlgorithm = JwaFactory.getJweAlgorithm(jweParts.joseHeader.getAlgorithm());
		JweEncryption jweEncryption = JwaFactory.getJweEncryption(jweParts.joseHeader.getEncrytion());

		byte[] cek = jweAlgorithm.decryption(key, jweParts.cek);
		this.payload = jweEncryption.verifyAndDecrypt(cek, jweParts.iv, jweParts.cipherText,
				getAdditionalAuthenticatedData(), jweParts.at);

		return StringUtils.byteToString(payload);
	}

	public JoseHeader getJoseHeader() {
		return jweParts.joseHeader;
	}

	private class JweParts {
		public JoseHeader joseHeader;
		public byte[] cek;
		public byte[] iv;
		public byte[] cipherText;
		public byte[] at;

		@Override
		public String toString() {
			return String.format("%s.%s.%s.%s.%s",
					joseHeader.getEncoded(),
					StringUtils.isDefaultEmpty(Base64.encodeBase64URLSafeString(cek), ""),
					Base64.encodeBase64URLSafeString(iv),
					Base64.encodeBase64URLSafeString(cipherText),
					Base64.encodeBase64URLSafeString(at));
		}

		public void parse(String src) {
			if (src == null || src.length() == 0) {
				throw new IllegalEncrytionToken();
			}

			String[] token = src.split("\\.");
			if (token == null || token.length != 5) {
				throw new IllegalEncrytionToken();
			}

			joseHeader = new JoseHeader();
			joseHeader.setEncoded(token[0]);
			cek = Base64.decodeBase64(token[1]);
			iv = Base64.decodeBase64(token[2]);
			cipherText = Base64.decodeBase64(token[3]);
			at = Base64.decodeBase64(token[4]);
		}
	}
}
