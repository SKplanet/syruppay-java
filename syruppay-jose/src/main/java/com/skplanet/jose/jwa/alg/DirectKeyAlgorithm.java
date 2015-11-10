package com.skplanet.jose.jwa.alg;

import com.skplanet.jose.jwa.JweAlgorithm;
import com.skplanet.jose.jwa.enc.ContentEncryptKeyGenerator;

/**
 * Created by 박병찬 on 2015-07-30.
 */
public class DirectKeyAlgorithm implements JweAlgorithm {
	public JweAlgResult encryption(byte[] key, ContentEncryptKeyGenerator cekGenerator) {
		return new JweAlgResult(key, null);
	}

	public byte[] decryption(byte[] key, byte[] cek) {
		return key;
	}
}
