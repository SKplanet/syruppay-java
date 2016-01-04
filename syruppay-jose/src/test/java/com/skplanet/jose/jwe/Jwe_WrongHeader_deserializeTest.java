package com.skplanet.jose.jwe;

import com.skplanet.jose.Jose;
import com.skplanet.jose.JoseBuilders;
import com.skplanet.jose.jwa.Jwa;
import com.skplanet.jose.jwa.crypto.CryptoUtils;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Created by byeongchan.park@sk.com(1000808) on 2016-01-04.
 */
public class Jwe_WrongHeader_deserializeTest {
	RSAPublicKey publicKey;
	RSAPrivateKey privateKey;

	String token = "eyJhbGciOiJSU0ExXzUiLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0.AgUpncShShIWM2UDcqZtZg7J_kownJAIicQj4F5WLHANyXOLvie5x2zftSdrWaVYiL4_yHuXaBtKdyPoZi697nGxwiXePhlAFKkEIewWQOV2kzgjcUc11VnKQOqS07ClKiNC8EbmoQ8x1jIPy6WuCdOq9qubJGFlCRHSZnfhSXI.v392bTlT0uJt9ne59L9PDA.BJPis0dNHHJg0yXJwiVcUt--GOezYSEwyKqfxAZ_feJgNLV6Sst4FZkOKGeEGxgnJZXbHA7VPrR_PHVOvcy_h-AvDiC8N92jmyB8EHaGUwEJ9YEtsmQxjP92gHx7ZD87J_7uBuOY3h1Iq-hEuOOn7QwNGWhwjq3QoK0ite1SmbXfVfpX8fIX1rp7haDo_pwWuMC2dD0xt0aW02JzSOBaeEFOKzBuWuuuFCc8AqTEeF5p99D9isEMoBfe-omL-K9_95jmc_PP7Z6Kptj37uQsXh3zur8NXSHrEa-r2jYOCFcGmuzJa3pMrEjVZVkkiemr_9XQFldgona79Ameier1kywOImevVRfpq3FYxKGb9ZyK7L2hlf7XM_UfM01Y_YtJbDl_lDa8OgHd46CTA-r8yQ73r_Iu2BdynGAhV-6TXFIrXYs7IX-P9htZQfJRnw8umbJvyw-3SAbPX3XAe0ZlNwGp6VXnqZZz1oG8ap_85lae3ywkQ9uJnAWWXlig7PqybjeCLOX-4lvGu6suUymKR4CH_3YuQsc4H71s6Cni0HymiisDnRkYfVSYLFCAPE3Zar5GbjSIWl3OQVjE-Wsn0cIjol99OdPhoi3CPkNz0uzRcldSj_4Qm37KCFwyfvpuCB4Rqt39OzAV76H9sEKK6ElHwwFPTq5WMKFykDGq9Y5DNBNQ5JF5phXLsd0iB4uY.GVga4Q-2p6n0es76l026Ww";

	@Before
	public void setUp() throws Exception {
		String exponent = "5c07c898dd70aa1e8f8197a74f316e4bcb16e5cb7eb737b9d1dc3a2dea98e70c273434ea87b1c98a96997929e686673ccf8436d8102ae8757b96097a483dd2ddaefdb94424def12f777026cf8144992051593fdbb13d0b34a7752e46e26d1469f1bd3b53092d1925b11c983646ab4d20f4145dd7b67782c8d391c2e763f6d921";
		String e = "10001";
		String module = "b075b6b99c8c2524b6459e571cbc7bc51c64308d33666b14d55a895929f6b99aaa598c68977200df22d017fe148dde87a52bff5f78d8d92563a3e248742e9463d589d427450b49c365d631ac392b1e952d6883f344537ec04f4c7b65322234fa28660210ac1125d4d3bcc8f598a331a63dd02c8ee2f4efb69438cc7aa2b15b93";

		BigInteger bn = new BigInteger(module, 16);
		BigInteger be = new BigInteger(e, 16);
		BigInteger bd = new BigInteger(exponent, 16);

		publicKey = CryptoUtils.generateRsaPublicKey(bn, be);
		privateKey = CryptoUtils.generateRsaPrivateKey(bn, bd);
	}

	@Test
	public void testIOSRsaDeserialize() {
		String expected = "{\n"
				+ "  \"auId\" : \"QTIwQTJEQ0QtM0UyRS00NzZDLTlEOTMtRDg1N0NCOUExRTM1KGNvbS5za3BsYW5ldC5TeXJ1cFBheUFnZW50KQ==\",\n"
				+ "  \"publicKey\" : \"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA5tF3CDxWiyQLcP3XcCXWgSJhrkc3nxDW24816LQnxZGuGSZYbztHAMlDhTaxSd0JiD73tu-Kt8Pvg_ZnkNhz20tiB4Tn2ZtRJf_LmCw1dCI80SlUbi0fBQhvN8chLoClSy8i10av5NUrErtQfPk3QwPEhwGugaWfnIU-M9IFPsjjYKKxqfYC7J1JRU_uR7yYQX4IGAUutQleZJzifijT-oJNrQf0LkSiBEwM8fJJDal6fiKBlivLUKP62vATmGQC2Uloq21zyDzQC4PzId9UcG7i26QJ3N6pg0E4j1THBvGpXYaLlqJKLkIEMOwqv962YHwz3dOkNzCaSGCdijAJswIDAQAB\"\n"
				+ "}           ";
		String actual = new Jose().configuration(
				JoseBuilders.compactDeserializationBuilder()
						.userAlgorithm(Jwa.RSA_NONE, Jwa.A128CBC_NONE_HS256)
						.serializedSource(token)
						.key(privateKey)
		).deserialization();

		System.out.println(actual);
	}
}
