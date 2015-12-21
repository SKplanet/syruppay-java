## JOSE for SyrupPay

Java로 구현한 JOSE(Javascript Object Signing and Encryption) - [RFC 7516](https://tools.ietf.org/html/rfc7516), [RFC 7515](https://tools.ietf.org/html/rfc7515) 규격입니다. 
JOSE 규격은 SyrupPay 결제 데이터 암복호화 및 AccessToken 발행 등에 사용되며 SyrupPay 서비스의 가맹점에 배포하기 위한 목적으로 라이브러리가 구현되었습니다.

## Required
JDK Framework 1.5 or later

## Installation
### maven
```
<dependency>
	<groupId>com.skplanet.syruppay</groupId>
	<artifactId>jose_jdk1.5</artifactId>
	<version>1.3.1</version>
</dependency>
```
### Gradle
```
compile 'com.skplanet.syruppay:jose_jdk1.5:1.3.1'
```

## Usage
### JWE
``` java
import com.skplanet.jose.Jose;
import com.skplanet.jose.JoseHeader;
import com.skplanet.jose.jwa.Jwa;

import static com.skplanet.jose.JoseBuilders.*;

//암호화 할 데이터
String payload = "apple";
//SyrupPay가 발급하는 iss
String kid = "sample";
//SyrupPay가 발급하는 secret
String key = "12345678901234561234567890123456";

/*
 * JWE header 규격
 * alg : key wrap encryption algorithm. 아래 Supported JOSE encryption algorithms 참조
 * enc : content encryption algorithm. 아래 Supported JOSE encryption algorithms 참조
 */
//1. encryption
String jweToken = new Jose().configuration(
        JsonEncryptionCompactSerializationBuilder()
            .header(new JoseHeader(Jwa.A256KW, Jwa.A128CBC_HS256, kid))
            .payload(payload)
            .key(key)
    ).serialization();

//2. verify and decryption		
String decryptedText = new Jose().configuration(
        compactDeserializationBuilder()
            .serializedSource(jweToken)
            .key(key)
        ).deserialization();	
```

### JWS
```java
import com.skplanet.jose.Jose;
import com.skplanet.jose.JoseHeader;
import com.skplanet.jose.jwa.Jwa;

import static com.skplanet.jose.JoseBuilders.*;

//암호화 할 데이터
String payload = "apple";
//SyrupPay가 발급하는 iss
String kid = "sample";
//SyrupPay가 발급하는 secret
String key = "12345678901234561234567890123456";

/*
 * JWS header 규격
 * alg : signature algorithm. 아래 Supported JOSE encryption algorithms 참조
 */
//1. sign
JoseHeader joseHeader = new JoseHeader(Jwa.HS256);
joseHeader.setHeader(JoseHeader.JoseHeaderKeySpec.TYPE, "JWT");

String token = new Jose().configuration(
        JsonSignatureCompactSerializationBuilder()
            .header(joseHeader)
            .payload(payload)
            .key(key)
		).serialization();

//2. verify
String json = new Jose().configuration(
        compactDeserializationBuilder()
            .serializedSource(token)
            .key(key)
        ).deserialization();	
```

## Supported JOSE encryption algorithms

### "alg" (Algorithm) Header Parameter Values For JWE
alg Param Value|Key Management Algorithm
------|------
A128KW|AES Key Wrap with default initial value using 128 bit key
A256KW|AES Key Wrap with default initial value using 256 bit key
RSA1_5|RSAES-PKCS1-v1_5
RSA-OAEP|RSAES OAEP using default parameters
dir|Direct use of a shared symmetric key as the CEK

### "enc" (Encryption Algorithm) Header Parameter Values for JWE
enc Param Value|Content Encryption Algorithm
-------------|------
A128CBC-HS256|AES_128_CBC_HMAC_SHA_256 authenticated encryption algorithm
A256CBC-HS512|AES_256_CBC_HMAC_SHA_512 authenticated encryption algorithm
A128GCM|AES GCM using 128 bit key
A256GCM|AES GCM using 256 bit key

#### A256CBC-HS512 IllegalKeyException 오류
미국 외 지역에서 AES256를 사용하면 IllegalKeyException(Illegal key size)가 발생합니다.
'Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files'를 설치해야만 정상적으로 동작합니다.
'Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files'는 oracle.com 에서 다운로드 받을 수 있습니다.

### "alg" (Algorithm) Header Parameter Values for JWS
alg Param Value|Digital Signature or MAC Algorithm
-----|-------
HS256|HMAC using SHA-256
RS256|RSASSA-PKCS1-v1_5 using SHA-256
ES256|ECDSA using P-256 and SHA-256

## release note
### 1.3.1
- A256CBC-HS256 추가
- A128GCM 추가
- A256GCM 추가

### 1.3.1
- CryptoUtils exception 처리 변경
- AsymmetricShaAlgorithm class, RSAEncryptionAlgorithm class 추가

### 0.3.6
- JoseBuilders.compactDeserializationBuilder 추가
- JoseBuilders deprecated method 추가
    JoseBuilders.JsonSignatureCompactDeserializationBuilder
    JoseBuilders.JsonEncryptionCompactDeserializationBuilder

### 0.3.5
- JoseHeader VER 추가


