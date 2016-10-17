## JOSE for SyrupPay

Java로 구현한 JOSE(Javascript Object Signing and Encryption) - [RFC 7516](https://tools.ietf.org/html/rfc7516), [RFC 7515](https://tools.ietf.org/html/rfc7515) 규격입니다. 
JOSE 규격은 SyrupPay 결제 데이터 암복호화 및 AccessToken 발행 등에 사용되며 SyrupPay 서비스의 가맹점에 배포하기 위한 목적으로 라이브러리가 구현되었습니다.

## Required
JDK 1.5 or later

## Installation
### maven
```
<dependency>
	<groupId>com.skplanet.syruppay</groupId>
	<artifactId>jose_jdk1.5</artifactId>
	<version>1.3.5</version>
</dependency>
```
### Gradle
```
compile 'com.skplanet.syruppay:jose_jdk1.5:1.3.5'
```

## Usage
### JWE
``` java
import com.skplanet.jose.Jose;
import com.skplanet.jose.JoseHeader;
import com.skplanet.jose.JoseBuilders;
import com.skplanet.jose.jwa.suites.JweAlgorithmSuites;
import com.skplanet.jose.jwe.JweHeader;

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
    JoseBuilders.JsonEncryptionCompactSerializationBuilder()
            .header(new JweHeader(JweAlgorithmSuites.A256KWAndA128CBC_HS256, kid))
            .payload(payload)
            .key(key)
).serialization();

//2. verify and decryption
String decryptedText = new Jose().configuration(
    JoseBuilders.compactDeserializationBuilder()
            .serializedSource(jweToken)
            .key(key)
).deserialization();
```

### JWS
```java
import com.skplanet.jose.Jose;
import com.skplanet.jose.JoseHeader;
import com.skplanet.jose.JoseBuilders;
import com.skplanet.jose.jwa.suites.JwsAlgorithmSuites;
import com.skplanet.jose.jws.JwsHeader;

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
String token = new Jose().configuration(
		JoseBuilders.JsonSignatureCompactSerializationBuilder()
    		.header(new JwsHeader(JwsAlgorithmSuites.HS256, kid))
			.payload(payload)
			.key(key)
).serialization();

//2. verify
String json = new Jose().configuration(
        JoseBuilders.compactDeserializationBuilder()
            .serializedSource(token)
            .key(key)
).deserialization();
```

### Header
암호화 또는 서명을 생성할 때 JOSE에서 지원하는 알고리즘을 설정 파라미터로 명시하여 처리합니다.
이 알고리즘 설정에 대해서 이 라이브러리는 두가지 방법을 제공합니다.

#### 개별 설정
각 알고리즘 조합을 사용자가 지정하여 사용할 수 있습니다.
```java
String jweToken = new Jose().configuration(
    JoseBuilders.JsonEncryptionCompactSerializationBuilder()
            .header(new JoseHeader(Jwa.A256KW, Jwa.A128CBC_HS256, kid))
            .payload(payload)
            .key(key)
).serialization();
```

#### Suites 설정
자주 사용하는 알로리즘만 사용할 수 있습니다. 
```java
String jweToken = new Jose().configuration(
    JoseBuilders.JsonEncryptionCompactSerializationBuilder()
            .header(new JweHeader(JweAlgorithmSuites.A256KWAndA128CBC_HS256, kid))
            .payload(payload)
            .key(key)
).serialization();
```

## Supported JOSE encryption algorithms
### JWE
JWE는 입력한 payload를 아래에서 지원하는 alg와 enc에서 명시한 알고리즘으로 암호화합니다.
alg는 발행된(기 공유된) key를 이용하여 내부적으로 random하게 생성된 CEK(content encryption key)를 암호화 하는 알고리즘이며,
enc는 내부적으로 생성된 CEK를 사용하여 명시한 암호화 알고리즘으로 payload를 암호화하는 알고리즘입니다.

JWE에서 정의된 alg, enc 중 SyrupPay 서비스에서 자주 사용하는 암호화 알고리즘은 아래와 같이 suites를 지원합니다.

#### Supported JWE algorithm suites
enum class |alg Param Value|enc Param Value
------|------|------
JweAlgorithmSuites.A128KWAndA128CBC_HS256|A128KW|A128CBC-HS256
JweAlgorithmSuites.A256KWAndA128CBC_HS256|A256KW|A128CBC-HS256
JweAlgorithmSuites.RSA1_5AndA128CBC_HS256|RSA1_5|A128CBC-HS256
JweAlgorithmSuites.RSA_OAEPAndA128CBC_HS256|RSA-OAEP|A128CBC-HS256

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

### JWS
JWS는 입력한 payload를 아래에서 지원하는 alg에서 명시한 알고리즘으로 서명을 생성합니다.
alg는 발행된(기 공유된) key를 이용하여 입력한 payload를 서명하는 알고리즘입니다.

JWS에서 정의된 alg 중에서 SyrupPay 서비스에서 자주 사용하는 서명 알고리즘은 아래와 같이 suites를 지원합니다.

#### Supported JWE algorithm suites
enum class |alg Param Value
------|------|------
JwsAlgorithmSuites.HS256|HS256
JwsAlgorithmSuites.RS256|RS256

### "alg" (Algorithm) Header Parameter Values for JWS
alg Param Value|Digital Signature or MAC Algorithm
-----|-------
HS256|HMAC using SHA-256
RS256|RSASSA-PKCS1-v1_5 using SHA-256
ES256|ECDSA using P-256 and SHA-256

## release note
### 1.3.5
- Json 라이브러리 변경 : codehaus > fasterxml 

### 1.3.4
- AESWrap/AESUnWrap 하위호환성을 위한 원복 (jdk 1.8.0_45 이상에서 AESWarp/AESUnWrap key size check 처리 추가됨)

### 1.3.3
- AESWrap/AESUnWrap JCE provider로 변경
- JoseSupport class deprecated
- 기타버그 수정

### 1.3.2
- JWE content encryption algorithms 추가
  * A256CBC-HS256 algorithm
  * A128GCM algorithm
  * A256GCM algorithm
- 불필요한 apache common codec sources 삭제
- JWE, JWS specific header class 추가
- JWE, JWS favorite algorithm suites 기능 추가
- OpenSSL 알고리즘 호환성 추가 (복호화 기능 제공)
  * RSA/NONE/PKCS1PADDING
  * AES/CBC/NOPADDING
- v0.2.1 JWS header bugs compatibility is added

### 1.3.1
- version up for syruppay-token matching
- CryptoUtils exception 처리 변경
- AsymmetricShaAlgorithm class, RSAEncryptionAlgorithm class 추가

### 0.3.6
- JoseBuilders.compactDeserializationBuilder 추가
- JoseBuilders deprecated method 추가
    JoseBuilders.JsonSignatureCompactDeserializationBuilder
    JoseBuilders.JsonEncryptionCompactDeserializationBuilder

### 0.3.5
- JoseHeader VER 추가


