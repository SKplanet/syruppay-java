package com.skplanet.jose.jwa;

import com.skplanet.jose.jwa.enc.ContentEncryptKeyGenerator;
import com.skplanet.jose.jwa.enc.JweEncResult;

/**
 * JOSE enc를 처리하는 암호화 알고리즘 인터페이스
 * 
 * @author 1000808 (byeongchan.park@sk.com)
 *
 */
public interface JweEncryption {
	JweEncResult encryptAndSign(byte[] cek, byte[] iv, byte[] src, byte[] aad);
	byte[] verifyAndDecrypt(byte[] cek, byte[] iv, byte[] cipherText, byte[] aad, byte[] expected);
	ContentEncryptKeyGenerator getContentEncryptionKeyGenerator();
}
