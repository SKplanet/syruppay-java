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

package com.skplanet.jose;

import com.skplanet.jose.support.JoseSupport;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import static com.skplanet.jose.JoseBuilders.compactDeserializationBuilder;

public class TestJweLibrary {
    @Test
    public void testAes128() throws Exception {
        String data = "apple";
        String symmetricKey = "1234567890123456";

        System.out.println("SRC DATA: " + data);
        String encData = JoseSupport.build_a128kw("test", symmetricKey.getBytes(), data);
        System.out.println("ENC DATA: " + encData);
        String decData = JoseSupport.verify_a128kw(symmetricKey.getBytes(), encData);
        System.out.println("DEC DATA: " + decData);

        /**
         * 결과
         * SRC DATA: apple
         * ENC DATA: eyJhbGciOiJBMTI4S1ciLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0.vvIRHwiN2i0TOHOevL4dc8eStQJ607ugkZz1HZrqV99-Froki_POIQ.q-qLAjHHzJXLJhcnDCvS0A.4ihs9xMj3zt6sXrSszU3WA.sdVuNn6AZL77y2rZS_cyyw
         * DEC DATA: apple
         */
    }

    @Ignore
    @Test
    public void testJweRsa() throws Exception {
        String userHome = System.getProperty("user.home");
        String keystoreFilename = userHome + File.separator + ".keystore";

        char[] password = "dja0221".toCharArray();
        String kid = "jwe";

        /* 암호화 */
        FileInputStream fIn = new FileInputStream(keystoreFilename);
        KeyStore keystore = KeyStore.getInstance("pkcs12");
        keystore.load(fIn, password);
        Certificate cert = keystore.getCertificate(kid);
        byte[] pubKey = cert.getPublicKey().getEncoded();

        String src = "apple"; /* 함호화 할 데이터 */
        String enc = JoseSupport.build_rsa15(kid, pubKey, src);

		/* 복호화 */
        kid = JoseSupport.verify_ras15Header(enc);
        Key key = keystore.getKey(kid, password);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key.getEncoded());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        byte[] priKey = privateKey.getEncoded();

        String target = JoseSupport.verify_rsa15(priKey, enc);

        System.out.println("enc=" + src + ", dec=" + target);
    }

    @Ignore
    @Test
    public void testJwsRsa() throws Exception {
        String userHome = System.getProperty("user.home");
        String keystoreFilename = userHome + File.separator + ".keystore";

        char[] password = "dja0221".toCharArray();
        String kid = "jwe";

		          /* 암호화 */
        FileInputStream fIn = new FileInputStream(keystoreFilename);
        KeyStore keystore = KeyStore.getInstance("pkcs12");
        keystore.load(fIn, password);
        Certificate cert = keystore.getCertificate(kid);
        PublicKey publicKey = cert.getPublicKey();

        Key key = keystore.getKey(kid, password);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key.getEncoded());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        String src = "apple"; /* 함호화 할 데이터 */
        String enc = JoseSupport.Jws_RsaSign(src, privateKey);
        String target = JoseSupport.Jws_RsaVerify(enc, publicKey);

        System.out.println("enc=" + src + ", dec=" + target);
    }

    @Test
    public void testJwsHmac() throws Exception {
        String symmetricKey = "1234567890123456";

        String src = "apple"; /* 함호화 할 데이터 */

        String enc = JoseSupport.Jws_HmacSign(src, symmetricKey);
        String target = JoseSupport.Jws_HmacVerify(enc, symmetricKey);
        System.out.println("enc=" + enc + ", dec=" + target);

        Assert.assertThat(target, CoreMatchers.is(src));

    }


    @Test
    public void testJweRsaOaep() {
        String source = "eyJhbGciOiJSU0EtT0FFUCIsImVuYyI6IkExMjhDQkMtSFMyNTYifQ.CwOCz0OfHRzKkOZffNfcVhLQVi_69IQY13tjDlt_hGV32UyxCVxlkCCInI4fHfuK6u_4QhfVaoWZhuwcUx-4UQBHdrz32vswlpt0elNibo7FLCQ5nfEtNf9AWx1JUWk1rKp7ipXYWYSOvR01xYxAdtjk7uvcqsLltW6srxpOEL8.Sb-u2AreBn202Glz7JnIqA.CeTpbn38nMvwxvhqgIPqvLCpjNYTDoNAyI4J03kvayydfOv-VtSnwcDRDqDbk5NjdpFk_Ihm1ezIshMwV9GRwNhySm1aQsPCwbbgOSqcn7YAUsopTCgYToR__31LkpgRYVWFZgQaZ0IDcB7rRkhQBTFhwVxU6Z88MvyVtNFwp1Q5p-3pEQc1BWbeb97IiwlEmZs49DxlY5id0l78zCYkf0khnIkSssZpWZDfAv9Br1r1gfRFpIpjipAUGrhO7nxKO_WTrQrDUT0c3afqZ1rgE2n3UwrDwQ_dcBajNVPDxUjoH3oMlddEP8yjQJFNxcxReePQNiZOO-pAw63wpLr5qGE9Hs1kW1iRY46rrFFBLQd-H0o7S13XypHF1T6ky8Hbcv9B8tJFJKH0X_kit56RMW6Y8O9etLsb_Nq3IK6BUonCpIucx2GTCXimitHTSasnsAhyPVzE0SWr-lcgXWGfZA.ePd55HrWHOi_VSiZ6QwYfA";
        String s2 = "eyJhbGciOiJSU0EtT0FFUCIsImVuYyI6IkExMjhDQkMtSFMyNTYifQ.azb2VD7WPSQ4hTTq4ms1NyBUDFF0_hxO8jNRBns7uqt9wXj8Yg3OWclY19UVuhGmpUTYPArjj7oJy3vY_8woyw-iJEpeQ_kHwXNs-UfnQEXFxEgE2oMyWSOR6Gc4UkYH969qV_AMNp_iw3EidUQzTagSEDSaZ5t-J2gYaUO5z04.tI7Z9OCdczvfwiPmw6FAQA.rrvRLH003bdILALQNk36W1SD6SmC7AHsnpicsJ9aS3Imlj4FlmYPGK25YJczx95O9EVSZd_L0AtGPtT8LzH-ARam5ay2Z5atqk2uYolo7b05jAUaToAHk1yZB_fx0611Yw044A1EWsNl8kUOL2QHzxM4mEN42QQKDxfQX_CtxK3BH582mSze5Cv1qP_EOG7d0pT4cVOvTqLsYt744wq6AxLDPoKHaDIJfV9xNImh_tDf6dGlOpLB61Zq_UW_g5PvaP5y9XPfP8CjZsWkiHtAz5OTN1p8JTSLjvweP0II_1sLkXqG62Y_KUnQrzhCR0j7mWThalaYlNxo0bJREqbFKs7XzLrgfMq4wkbaD-F4pxtrKVUqzZ9SD7LEMGU7Y1cD9HUGFXodJ2uthwlqQJrRBOZYOD4yj2drIvL4VBFug2Qt7rm_gNXNyyEJNyjWI08sEHHnWJO10vrjC4ZU36ZtQg.mSGHWjxhLPxEPrVeHbd-2w";

        String s3 = "eyJhbGciOiJBMTI4S1ciLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0.KUzuuoJHH6zf1DmgosK-8gnzbNm22rUuIRsKlKfHMrfTsAcpfC_8Ww.85YItk4XCmNMtQx39EweiA.OhZs9p8G4hmyg1-KansPp7mIYJegmCA-THBxsaiLEiD1EIfVh3OSRamlwWdV57PP9Urbw5-p2dijT0xnL8PeBvMma41mu24-8hMHdSSat0JE1zpbPa6XNhjFcbjavWGxXqIpCd1MRF7RZWao1cukKfzsq5jo56xkeqjONlTzmZo.7BPD4tTRjx6RNTTIDzGT4A";

        String s4 ="eyJhbGciOiJSU0EtT0FFUCIsImVuYyI6IkExMjhDQkMtSFMyNTYifQ.MU8acAlDIlGSUI9zi3uUAOEgqMxmKT8LNyAqpRolf3T_uGkYsxstUEiteOpEAbvelvkFCLObj-TVzmclne5WoNqaPRisa9dPiWKzVbuk5eep3Dt96TUsAon01SWUIy12oE10i-2K5hbus8zwxKTvwDDB2KnWOOxv1jc_M3UQU0o.HFGI2vl-_cC2LFvRU5dEfQ.RrQUokUB2OnsXS2YupZjE5PBzzxZBtq5ccJKSPCtzMmChmj1dgaWhHPOQdv6k8RDy6kXB1hQST-AnF7MXYSf4-pmjDFH2yKXDWzPSbvHlA3KfHPtZM4kuf2cEEUm-XbUiCFs0ZAqQVwidldNk22d8taZeZ7wzgHFltKPcIYIT4TfaKtuqBoZ3U73sEWwsZqdhTzIcEqxzdYFgShPSSpBTG1f0yWJsGuFAXIL2LMUIV0_W56acQiCr8Er3ArID9vBOFCzqQ-2dO-_Q-a08Y8gXXV_tQwDz1a9vhN5r7VIfy2A3gvrqDj5Fnn8SYkEijT422VF86P-Ws4JQ2QYTeH0ZuUa-15GtJfDU5vJLYrE-Jxbx-fVFqXo8CqsJDAkbZP8fDSnCFjmAaexCfTm2G7Dy_Ew8VU2hjgORfFXv9ntXTK98NlTi8jA5INdgrdacc4judQKZC63Gy4suBN-tBb_bbZFWIHYs0JNHYW3YZM6CgMxzEu6gbJK-qSxNb3UOR0kfvidSyzWRKp_vqG1zIOfgkUcx2Pq4iYw36MNngcPpSPHoIUpQMpgXHYeSZg1LL4sbgcTnU8Lb27rHqf-0eJNswxlVFbMp6zFeurmuDyhTEvx7GhndL_F_AgZ_PUIuuG_G2N1D-TLOnLQXirx1tjSeaznMqUwPpUhiEubOI5iMAh_C5SB5MbcYDvN5HRjdy_B-4NzonAeFZ7dGzn-Lj7Hwo79r5s_Q1B3d8vuKMJpCoZhxxHFqb_n-3POnpX23OTko5oEeyFhOYMuQEq8DtuY4Kh4oIU7fnwM5i2P4s8LuUUl0bNcd2NbMMUzNX_HKuHkCppM3N9wEfdOoiCdFzaQJIulV334Cd7PjwVoXe0JeHOIGzx_mdQlNipf82HwQFAxHEKLoWC3NrJC9cJ99wH7hxJw_PwSzCKu-KOS04JiufrObREWg4-Jq_dlHPXfY_dbgKUZ7BKIkNgBmzn2fA-NVJq79_rViXgj7PIkl8lTzRlnMIvWrqz-jhwm6IuyYCJUHuVw8DhJRj-rHY1u01-4KfmSEGViT32zZoHFt--FuaixekV_UV3TJjdw2W-RybQHJpouNinASMTNayONIqVk4efr4oiWRwTuh6o0BC8JDqCYymWkokZqFqfFmjhA8Xo-YA_S4BCt0ZfJn9QrFUYeOzhlBj5ITecZaftjowQD-cE.Yb8jQKLPDHVHSnRzWYzt3A";

        String s5 = "eyJhbGciOiJSU0EtT0FFUCIsImVuYyI6IkExMjhDQkMtSFMyNTYifQ.MU8acAlDIlGSUI9zi3uUAOEgqMxmKT8LNyAqpRolf3T_uGkYsxstUEiteOpEAbvelvkFCLObj-TVzmclne5WoNqaPRisa9dPiWKzVbuk5eep3Dt96TUsAon01SWUIy12oE10i-2K5hbus8zwxKTvwDDB2KnWOOxv1jc_M3UQU0o.HFGI2vl-_cC2LFvRU5dEfQ.RrQUokUB2OnsXS2YupZjE5PBzzxZBtq5ccJKSPCtzMmChmj1dgaWhHPOQdv6k8RDy6kXB1hQST-AnF7MXYSf4-pmjDFH2yKXDWzPSbvHlA3KfHPtZM4kuf2cEEUm-XbUiCFs0ZAqQVwidldNk22d8taZeZ7wzgHFltKPcIYIT4TfaKtuqBoZ3U73sEWwsZqdhTzIcEqxzdYFgShPSSpBTG1f0yWJsGuFAXIL2LMUIV0_W56acQiCr8Er3ArID9vBOFCzqQ-2dO-_Q-a08Y8gXXV_tQwDz1a9vhN5r7VIfy2A3gvrqDj5Fnn8SYkEijT422VF86P-Ws4JQ2QYTeH0ZuUa-15GtJfDU5vJLYrE-Jxbx-fVFqXo8CqsJDAkbZP8fDSnCFjmAaexCfTm2G7Dy_Ew8VU2hjgORfFXv9ntXTK98NlTi8jA5INdgrdacc4judQKZC63Gy4suBN-tBb_bbZFWIHYs0JNHYW3YZM6CgMxzEu6gbJK-qSxNb3UOR0kfvidSyzWRKp_vqG1zIOfgkUcx2Pq4iYw36MNngcPpSPHoIUpQMpgXHYeSZg1LL4sbgcTnU8Lb27rHqf-0eJNswxlVFbMp6zFeurmuDyhTEvx7GhndL_F_AgZ_PUIuuG_G2N1D-TLOnLQXirx1tjSeaznMqUwPpUhiEubOI5iMAh_C5SB5MbcYDvN5HRjdy_B-4NzonAeFZ7dGzn-Lj7Hwo79r5s_Q1B3d8vuKMJpCoZhxxHFqb_n-3POnpX23OTko5oEeyFhOYMuQEq8DtuY4Kh4oIU7fnwM5i2P4s8LuUUl0bNcd2NbMMUzNX_HKuHkCppM3N9wEfdOoiCdFzaQJIulV334Cd7PjwVoXe0JeHOIGzx_mdQlNipf82HwQFAxHEKLoWC3NrJC9cJ99wH7hxJw_PwSzCKu-KOS04JiufrObREWg4-Jq_dlHPXfY_dbgKUZ7BKIkNgBmzn2fA-NVJq79_rViXgj7PIkl8lTzRlnMIvWrqz-jhwm6IuyYCJUHuVw8DhJRj-rHY1u01-4KfmSEGViT32zZoHFt--FuaixekV_UV3TJjdw2W-RybQHJpouNinASMTNayONIqVk4efr4oiWRwTuh6o0BC8JDqCYymWkokZqFqfFmjhA8Xo-YA_S4BCt0ZfJn9QrFUYeOzhlBj5ITecZaftjowQD-cE.Yb8jQKLPDHVHSnRzWYzt3A";

        final String deserialization = new Jose().configuration(
                compactDeserializationBuilder()
                        .serializedSource(s5)
                        //.key("8I9G4IPPqkik05WZj0Da3bJ5t7BEmXYj"
                        .key(getPrivateKey()
                        )).deserialization();
        System.out.println(deserialization);
    }

    public static Key getPublicKey() {
        String pubModulus = "b075b6b99c8c2524b6459e571cbc7bc51c64308d33666b14d55a895929f6b99aaa598c68977200df22d017fe148dde87a52bff5f78d8d92563a3e248742e9463d589d427450b49c365d631ac392b1e952d6883f344537ec04f4c7b65322234fa28660210ac1125d4d3bcc8f598a331a63dd02c8ee2f4efb69438cc7aa2b15b93";
        String pubExp = "10001";

        Key publicKey = null;
        try {
            RSAPublicKeySpec pub = new RSAPublicKeySpec(new BigInteger(pubModulus, 16), new BigInteger(pubExp, 16));

            KeyFactory fact = KeyFactory.getInstance("RSA");
            publicKey = fact.generatePublic(pub);
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch(InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return publicKey;
    }

    public static Key getPrivateKey() {
        String privModulus = "b075b6b99c8c2524b6459e571cbc7bc51c64308d33666b14d55a895929f6b99aaa598c68977200df22d017fe148dde87a52bff5f78d8d92563a3e248742e9463d589d427450b49c365d631ac392b1e952d6883f344537ec04f4c7b65322234fa28660210ac1125d4d3bcc8f598a331a63dd02c8ee2f4efb69438cc7aa2b15b93";
        String privExp = "5c07c898dd70aa1e8f8197a74f316e4bcb16e5cb7eb737b9d1dc3a2dea98e70c273434ea87b1c98a96997929e686673ccf8436d8102ae8757b96097a483dd2ddaefdb94424def12f777026cf8144992051593fdbb13d0b34a7752e46e26d1469f1bd3b53092d1925b11c983646ab4d20f4145dd7b67782c8d391c2e763f6d921";
        Key privateKey = null;
        try {
            RSAPrivateKeySpec priv = new RSAPrivateKeySpec(new BigInteger(privModulus, 16), new BigInteger(privExp, 16));

            KeyFactory fact = KeyFactory.getInstance("RSA");
            privateKey = fact.generatePrivate(priv);
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch(InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return privateKey;
    }

}
