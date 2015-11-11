package com.skplanet.jose.jwa;

import com.skplanet.jose.jwa.alg.JweAlgResult;
import com.skplanet.jose.jwa.enc.ContentEncryptKeyGenerator;

/**
 * JOSE alg를 처리하는 암호화 알고리즘 인터페이스 
 * 
 * @author 1000808 (byeongchan.park@sk.com)
 *
 */
public interface JweAlgorithm {
	JweAlgResult encryption(byte[] key, ContentEncryptKeyGenerator cekGenerator);
	byte[] decryption(byte[] key, byte[] cek);
}
