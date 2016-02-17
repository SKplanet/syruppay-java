## Overview
시럽페이 서비스에서 가맹점 인증 및 데이터 교환을 위한 규격을 정의하며 전송 구간에 대한 암호화 및 무결성 보장을 위한 토큰을 생성, 관리하는 기능을 수행한다.

### 개선 항목
1. JWT 규격 및 암,복호화에 대한 복잡도
2. 시럽페이 규격(도메인)에 대한 복잡도
3. 시럽페이 서비스 프로세스 구현에 대한 복잡도(Fluent API 지향)
4. 데이터 전송 구간 구현에 대한 복잡도(1.5 버전 예정)

## Getting Start
### 개발 환경
* Java 1.5 이상

### Gradle 빌드 시
```groovy
dependencies {
     compile 'com.skplanet.syruppay:syruppay-token:1.3.6'
}

```

### Maven 빌드 시
```xml
<dependencies>
	<dependency>
		<groupId>com.skplanet.syruppay</groupId>
		<artifactId>syruppay-token</artifactId>
		<version>1.3.6</version>
	</dependency>
</dependencies>
```

### 회원가입, 로그인, 설정과 같은 사용자 정보에 접근하기 위한 Syrup Pay Token 생성
회원가입, 설정, 로그인 기능 등과 같은 Syrup Pay 사용자 정보 접근하기 위해 사용되는 토큰을 설정하고 생성합니다.

##### Java Code
```java
// 사용자 로그인, 환경 설정 접근 시 
String token = new SyrupPayTokenBuilder().of("가맹점 ID")
                    .login()
                        .withMerchantUserId("가맹점의 회원 ID 또는 식별자")
                        .withExtraMerchantUserId("핸드폰과 같이 회원 별 추가 ID 체계가 존재할 경우 입력")
                        .withSsoCredential("SSO 를 발급 받았을 경우 입력")
                    .and()
                    .generateTokenBy("가맹점에게 전달한 비밀키");
// 회원 가입 시 
String token = new SyrupPayTokenBuilder().of("가맹점 ID")
                    .signup()
                        .withMerchantUserId("가맹점의 회원 ID 또는 식별자")
                        .extraId("핸드폰과 같이 회원 별 추가 ID 체계가 존재할 경우 입력")
                        .ssoCredential("SSO 를 발급 받았을 경우 입력")
                    .and()
                    .generateTokenBy("가맹점에게 전달한 비밀키");
```

##### token의 결과
```language
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJodHRwczovL3BheS5zeXJ1cC5jby5rciIsInR5cCI6Impvc2UiLCJpc3MiOiLqsIDrp7nsoJAiLCJleHAiOjE0NDExNjE3OTEsImlhdCI6MTQ0MTE2MTE5MSwianRpIjoiMzRhM2QxNzMtZDY2OS00MzkyLTg2ZjUtY2Q4ZjI0NDRjMzM3IiwibmJmIjowLCJsb2dpbkluZm8iOnsibWN0VXNlcklkIjoi6rCA66e57KCQ7J2YIO2ajOybkCBJRCDrmJDripQg7Iud67OE7J6QIiwiZXh0cmFVc2VySWQiOiLtlbjrk5ztj7Dqs7wg6rCZ7J20IO2ajOybkCDrs4Qg7LaU6rCAIElEIOyytOqzhOqwgCDsobTsnqztlaAg6rK97JqwIOyeheugpSIsInNzb0NyZWRlbnRpYWwiOiJTU08g66W8IOuwnOq4iSDrsJvslZjsnYQg6rK97JqwIOyeheugpSJ9fQ.JZi3pz1CRukrSvXnrBUx1DLE-QY5xxY9NJm1cnpo_7Q
```

##### token의 내용
```json
{
  "aud": "https://pay.syrup.co.kr",
  "typ": "jose",
  "iss": "가맹점",
  "exp": 1441161791,
  "iat": 1441161191,
  "jti": "34a3d173-d669-4392-86f5-cd8f2444c337",
  "loginInfo": {
    "mctUserId": "가맹점의 회원 ID 또는 식별자",
    "extraUserId": "핸드폰과 같이 회원 별 추가 ID 체계가 존재할 경우 입력",
    "ssoCredential": "SSO 를 발급 받았을 경우 입력"
  }
}
```

### 결재 인증을 위한 Syrup Pay Token 생성
##### Java Code
```java
String token = new SyrupPayTokenBuilder().of("가맹점 ID")
				.pay()
					.withOrderIdOfMerchant("가맹점에서 관리하는 주문 ID")
					.withProductTitle("제품명")
					.withProductUrls(
					    "http://deal.11st.co.kr/product/SellerProductDetail.tmall?method=getSellerProductDetail&prdNo=1122841340",
					    "http://deal.11st.co.kr/product/SellerProductDetail.tmall?method=getSellerProductDetail&prdNo=1265508741"
					    )
					.withLanguageForDisplay(PayConfigurer.Language.KO)
					.withAmount(50000)
					.withCurrency(PayConfigurer.Currency.KRW)
					.withShippingAddress(new PayConfigurer.ShippingAddress("137-332", "서초구 잠원동 하나아파트", "1동 1호", "서울", "", "kr"))
					.withDeliveryPhoneNumber("01011112222")
					.withDeliveryName("배송 수신자")
					.withInstallmentPerCardInformation(new PayConfigurer.CardInstallmentInformation("카드구분 코드", "할부정보. ex. NN1;NN2;YY3;YY4;YY5;NH6"))
					.withBeAbleToExchangeToCash(false)
					.withPayableRuleWithCard(PayConfigurer.PayableLocaleRule.ONLY_ALLOWED_KOR)
				.and()
				.generateTokenBy("가맹점에게 전달한 비밀키");
```

##### token의 결과
```language
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJodHRwczovL3BheS5zeXJ1cC5jby5rciIsInR5cCI6Impvc2UiLCJpc3MiOiLqsIDrp7nsoJAiLCJleHAiOjE0NDEyNTcwMjgsImlhdCI6MTQ0MTI1NjQyOCwianRpIjoiYzgxMWI4OWItYzczMi00MDVlLWE0MDctYmMwYjE1MTk3OGFhIiwibmJmIjowLCJ0cmFuc2FjdGlvbkluZm8iOnsibWN0VHJhbnNBdXRoSWQiOiLqsIDrp7nsoJDsl5DshJwg6rSA66as7ZWY64qUIOyjvOusuCBJRCIsInBheW1lbnRJbmZvIjp7ImNhcmRJbmZvTGlzdCI6W3siY2FyZENvZGUiOiLsubTrk5zqtazrtoQg7L2U65OcIiwibW9udGhseUluc3RhbGxtZW50SW5mbyI6Iu2VoOu2gOygleuztC4gZXguIE5OMTtOTjI7WVkzO1lZNDtZWTU7Tkg2In1dLCJwcm9kdWN0VGl0bGUiOiLsoJztkojrqoUiLCJsYW5nIjoiS08iLCJjdXJyZW5jeUNvZGUiOiJLUlciLCJwYXltZW50QW10Ijo1MDAwMCwic2hpcHBpbmdBZGRyZXNzIjoiYTI6a3J8MTM3LTMzMnzshJzstIjqtawg7J6g7JuQ64-ZIO2VmOuCmOyVhO2MjO2KuHwx64-ZIDHtmLh87ISc7Jq4fHwiLCJkZWxpdmVyeVBob25lTnVtYmVyIjoiMDEwMTExMTIyMjIiLCJkZWxpdmVyeU5hbWUiOiLrsLDshqEg7IiY7Iug7J6QIiwiaXNFeGNoYW5nZWFibGUiOmZhbHNlfSwicGF5bWVudFJlc3RyaWN0aW9ucyI6eyJjYXJkSXNzdWVyUmVnaW9uIjoiQUxMT1dFRDpLT1IifX19.fetvv8Th7bTbnrpGgMHHS3Z0K9WrTgW79g2_ajbzHKQ
```

##### token의 내용
```json
{
  "aud": "https://pay.syrup.co.kr",
  "typ": "jose",
  "iss": "가맹점",
  "exp": 1441249587,
  "iat": 1441248987,
  "jti": "3ba6a56e-9277-465d-b743-80933cdbecc6",
  "transactionInfo": {
    "mctTransAuthId": "가맹점에서 관리하는 주문 ID",
    "paymentInfo": {
      "cardInfoList": [
        {
          "cardCode": "카드구분 코드",
          "monthlyInstallmentInfo": "할부정보. ex. NN1;NN2;YY3;YY4;YY5;NH6"
        }
      ],
      "productTitle": "제품명",
      "productUrls" : [
        "http://deal.11st.co.kr/product/SellerProductDetail.tmall?method=getSellerProductDetail&prdNo=1122841340",
        "http://deal.11st.co.kr/product/SellerProductDetail.tmall?method=getSellerProductDetail&prdNo=1265508741"
      ],
      "lang": "KO",
      "currencyCode": "KRW",
      "paymentAmt": 50000,
      "shippingAddress": "a2:kr|137-332|서초구 잠원동 하나아파트|1동 1호|서울||",
      "deliveryPhoneNumber": "01011112222",
      "deliveryName": "배송 수신자",
      "isExchangeable": false
    },
    "paymentRestrictions": {
      "cardIssuerRegion": "ALLOWED:KOR"
    }
  }
}
```

### 토큰 복호화
```java
Token token = SyrupPayTokenBuilder.verify("토큰", "가맹점에게 전달한 비밀키");
```
### 참고 사항
#### 이용하고자 하는 시럽페이 서비스 기능이 복합적인 경우 중첩하여 사용 가능하다.
##### 상황 1. 시럽페이 가입 여부를 모르는 상태에서 결제 하고자 하는 경우 (회원가입, 로그인, 결제 가능 토큰)
```java
String token = new SyrupPayTokenBuilder().of("가맹점 ID")
                    .signUp() 
                        .withMerchantUserId("가맹점의 회원 ID 또는 식별자")
                        .withExtraMerchantUserId("핸드폰과 같이 회원 별 추가 ID 체계가 존재할 경우 입력") // Optional
                    .and()
                    .pay()
                    	.withOrderIdOfMerchant("가맹점에서 관리하는 주문 ID") // 가맹점 Transaction Id = mctTransAuthId
                    	.withProductTitle("제품명")
                    	.withProductUrls(
                            "http://deal.11st.co.kr/product/SellerProductDetail.tmall?method=getSellerProductDetail&prdNo=1122841340",
                            "http://deal.11st.co.kr/product/SellerProductDetail.tmall?method=getSellerProductDetail&prdNo=1265508741"
                            ) // Optional
                        .withLanguageForDisplay(PayConfigurer.Language.KO) 
                        .withAmount(50000)
                        .withCurrency(PayConfigurer.Currency.KRW)
                        .withShippingAddress(new PayConfigurer.ShippingAddress("137-332", "서초구 잠원동 하나아파트", "1동 1호", "서울", "", "kr")) // Optional
                        .withDeliveryPhoneNumber("01011112222") // Optional
                        .withDeliveryName("배송 수신자") // Optional
                        .withInstallmentPerCardInformation(new PayConfigurer.CardInstallmentInformation("카드구분 코드", "할부정보. ex. NN1;NN2;YY3;YY4;YY5;NH6")) // Optional
                        .withBeAbleToExchangeToCash(false) // Optional
                        .withPayableRuleWithCard(PayConfigurer.PayableLocaleRule.ONLY_ALLOWED_KOR) // Optional
                    .and()
                    .generateTokenBy("가맹점에게 전달한 비밀키");
```

##### 상황 2. 시럽페이에 자동 로그인 후 결제를 하고자 하는 경우 (자동 로그인, 결제 가능 토큰)
```java
String token = new SyrupPayTokenBuilder().of("가맹점 ID")
                    .login() 
                        .withMerchantUserId("가맹점의 회원 ID 또는 식별자")
                        .withExtraMerchantUserId("핸드폰과 같이 회원 별 추가 ID 체계가 존재할 경우 입력") // Optional
                        .withSsoCredential("발급 받은 SSO")
                    .and()
                    .pay()
                        .withOrderIdOfMerchant("가맹점에서 관리하는 주문 ID") // 가맹점 Transaction Id = mctTransAuthId
                        .withProductTitle("제품명")
                        .withProductUrls(
                            "http://deal.11st.co.kr/product/SellerProductDetail.tmall?method=getSellerProductDetail&prdNo=1122841340",
                            "http://deal.11st.co.kr/product/SellerProductDetail.tmall?method=getSellerProductDetail&prdNo=1265508741"
                            ) // Optional
                        .withLanguageForDisplay(PayConfigurer.Language.KO)  
                        .withAmount(50000)
                        .withCurrency(PayConfigurer.Currency.KRW)
                        .withShippingAddress(new PayConfigurer.ShippingAddress("137-332", "서초구 잠원동 하나아파트", "1동 1호", "서울", "", "kr")) // Optional
                        .withDeliveryPhoneNumber("01011112222") // Optional
                        .withDeliveryName("배송 수신자") // Optional
                        .withInstallmentPerCardInformation(new PayConfigurer.CardInstallmentInformation("카드구분 코드", "할부정보. ex. NN1;NN2;YY3;YY4;YY5;NH6")) // Optional
                        .withBeAbleToExchangeToCash(false) // Optional
                        .withPayableRuleWithCard(PayConfigurer.PayableLocaleRule.ONLY_ALLOWED_KOR) // Optional
                        .withMerchantDefinedValue("{" +
                                                  "\"id_1\": \"value\"," +
                                                  "\"id_2\": 2" +
                                                  "}") // Optional, JSON 포맷 이용 시 Escape(\) 입력에 주의 필요, 1k 제한
                    .and()
                    .generateTokenBy("가맹점에게 전달한 비밀키");
```

#### 4. 시럽페이에 자동 로그인 후 정기 결제 상품을 구매하고자 하는 경우(자동 로그인, 자동 정기 결제 가능 토큰)
##### - 준비중 -

## Extensional Function 
### 시럽페이 사용자 연동을 위한 Syrup Pay Token 세팅
Syrup Pay 사용자에 대한 정보를 조회하여 Syrup Pay 수동 로그인 시 ID 자동 입력과 같은 추가적인 기능을 수행할 수 있도록 매칭이 되는 정보를 설정하고 토큰을 생성합니다.
##### Java Code
```java
String token = new SyrupPayTokenBuilder().of("가맹점 ID")
                .login()
                    .withMerchantUserId("가맹점의 회원 ID 또는 식별자")
                    .withExtraMerchantUserId("핸드폰과 같이 회원 별 추가 ID 체계가 존재할 경우 입력")
                    .withSsoCredential("SSO 를 발급 받았을 경우 입력")
                .and()
                .mapToSyrupPayUser()
                    .withType(MapToSyrupPayUserConfigurer.MappingType.CI_MAPPED_KEY)
                    .withValue("4987234")
                .and()
                .generateTokenBy("가맹점에게 전달한 비밀키");
```

##### token의 결과
```language
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJodHRwczovL3BheS5zeXJ1cC5jby5rciIsInR5cCI6Impvc2UiLCJpc3MiOiLqsIDrp7nsoJAiLCJleHAiOjE0NDExNjg2NjYsImlhdCI6MTQ0MTE2ODA2NiwianRpIjoiOGQyNGU3NTMtNmZjNS00YmMwLWI4MzktMmVlNTBhYjQ3MGEwIiwibmJmIjowLCJsb2dpbkluZm8iOnsibWN0VXNlcklkIjoi6rCA66e57KCQ7J2YIO2ajOybkCBJRCDrmJDripQg7Iud67OE7J6QIiwiZXh0cmFVc2VySWQiOiLtlbjrk5ztj7Dqs7wg6rCZ7J20IO2ajOybkCDrs4Qg7LaU6rCAIElEIOyytOqzhOqwgCDsobTsnqztlaAg6rK97JqwIOyeheugpSIsInNzb0NyZWRlbnRpYWwiOiJTU08g66W8IOuwnOq4iSDrsJvslZjsnYQg6rK97JqwIOyeheugpSJ9LCJ1c2VySW5mb01hcHBlciI6eyJtYXBwaW5nVHlwZSI6IkNJX01BUFBFRF9LRVkiLCJtYXBwaW5nVmFsdWUiOiI0OTg3MjM0In19.edroOd5__uGm_GU8u9YPwY7Dxkv9Qr7JOtXJuU5KBwY
```

##### token의 내용
```json
{
  "aud": "https://pay.syrup.co.kr",
  "typ": "jose",
  "iss": "가맹점",
  "exp": 1441168666,
  "iat": 1441168066,
  "jti": "8d24e753-6fc5-4bc0-b839-2ee50ab470a0",
  "loginInfo": {
    "mctUserId": "가맹점의 회원 ID 또는 식별자",
    "extraUserId": "핸드폰과 같이 회원 별 추가 ID 체계가 존재할 경우 입력",
    "ssoCredential": "SSO 를 발급 받았을 경우 입력"
  },
  "userInfoMapper": {
    "mappingType": "CI_MAPPED_KEY",
    "mappingValue": "4987234"
  }
}
```

### 참고 사항
#### 이용하고자 하는 시럽페이 서비스 기능이 복합적인 경우 중첩하여 사용 가능하다.
##### 상황 1.
##### 상황 2.

## 시럽페이 체크아웃 기능 사용하기
가맹점의 쿠폰, 사용자 멤버쉽, 사용자의 배송지와 같은 주문 관련 정보와 기존 시럽페이의 간편 결제를 좀 더 편리하게(Seamless) 사용하기 위한 시럽페이의 확장된 기능   

### 주의
쿠폰(Offer)과 멤버쉽 포인트(Loyalty)에 대한 복합 결제를 지원한기 위한 기능으로 해당 서비스를 사용하기 위해서는 사전 협의 단계가 필요하다.

### 시럽페이 체크아웃을 이용하여 가맹점의 쿠폰(Offer)을 함께 결제 인증하기 위한 Syrup Pay Token 생성
##### - 준비중 -

### 시럽페이 체크아웃을 이용하여 멤버쉽 포인트(Loyalty)를 함께 결제 인증하기 위한 Syrup Pay Token 생성
##### - 준비중 -

### 시럽페이 체크아웃을 이용하여 배송지 정보를 멤버쉽 포인트(Loyalty)를 함께 결제 인증하기 위한 Syrup Pay Token 생성
##### - 준비중 -

### 주의
1. 한번 토큰을 생성한 SyrupPayTokenBuilder 를 재이용하여 다시 토큰을 빌드하거나 JSON 을 재성성 할 수 없다.
2. 각각의 토큰에 대한 내용(Claim)은 사용자 편의에 따라 입력 후 토큰을 생성할 수 있지만 Required 되는 필드에 대하여 미입력 시 토큰 생성 시점(SyrupPayTokenHandler#generateTokenBy(key) 호출 시점)에 IllegalArgumentException 을 throw 할 수 있다.
3. 토큰이 생성된 이후 유효시간은 **10분**으로 설정되어 있으며 이에 대한 수정이 필요할 경우 Syrup Pay 개발팀과 협의 후 제공하는 가이드를 따라야 한다. 

### 참고자료
1. JOSE RFC - [https://tools.ietf.org/wg/jose](https://tools.ietf.org/wg/jose/)
2. Syrup Pay JOSE - [https://github.com/SyrupPay/jose_java](https://github.com/SyrupPay/jose_java)
3. JWT IO - [http://jwt.io/](http://jwt.io/)