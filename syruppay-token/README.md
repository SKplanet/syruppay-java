## Overview
시럽페이 서비스에서 가맹점 인증 및 데이터 교환을 위한 규격을 정의하며 전송 구간에 대한 암호화 및 무결성 보장을 위한 토큰을 생성, 관리하는 기능을 수행한다.

### 개선 항목
1. JWT 규격 및 암,복호화에 대한 복잡도
1. 시럽페이 규격(도메인)에 대한 복잡도
1. 시럽페이 서비스 프로세스 구현에 대한 복잡도(Fluent API 지향)

## Getting Start
### 개발 환경
* Java 1.5 이상

### Gradle 빌드 시
```groovy
dependencies {
     compile 'com.skplanet.syruppay:syruppay-token:1.3.11.SNAPSHOT.2'
}

```

### Maven 빌드 시
```xml
<dependencies>
	<dependency>
		<groupId>com.skplanet.syruppay</groupId>
		<artifactId>syruppay-token</artifactId>
		<version>1.3.11.SNAPSHOT.2</version>
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

### 결제 인증을 위한 Syrup Pay Token 생성
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
					.withRestrictionOf(PayConfigurer.PayableLocaleRule.ONLY_ALLOWED_KOR)
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
                    .withIdentityAuthenticationId("bddb74b0-981f-4070-8c02-0cdf324f46f6"); // Optional
                .and()
                .generateTokenBy("가맹점에게 전달한 비밀키");
```

##### token의 결과
```language
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IuqwgOunueygkCIsInZlciI6IjEuMy4yIn0.eyJhdWQiOiJodHRwczovL3BheS5zeXJ1cC5jby5rciIsInR5cCI6Impvc2UiLCJpc3MiOiLqsIDrp7nsoJAiLCJleHAiOjE0NzM3MzkzMTAsImlhdCI6MTQ3MzczODcxMCwianRpIjoiZDczNWU0NmItYmYwYS00MWY3LTkyMzktMDc2ZGZlMTRmZDQ5IiwibmJmIjowLCJsb2dpbkluZm8iOnsibWN0VXNlcklkIjoi6rCA66e57KCQ7J2YIO2ajOybkCBJRCDrmJDripQg7Iud67OE7J6QIiwiZXh0cmFVc2VySWQiOiLtlbjrk5ztj7Dqs7wg6rCZ7J20IO2ajOybkCDrs4Qg7LaU6rCAIElEIOyytOqzhOqwgCDsobTsnqztlaAg6rK97JqwIOyeheugpSIsIlNTT0NyZWRlbnRpYWwiOiJTU08g66W8IOuwnOq4iSDrsJvslZjsnYQg6rK97JqwIOyeheugpSJ9LCJ1c2VySW5mb01hcHBlciI6eyJtYXBwaW5nVHlwZSI6IkNJX01BUFBFRF9LRVkiLCJtYXBwaW5nVmFsdWUiOiI0OTg3MjM0IiwiaWRlbnRpdHlBdXRoZW50aWNhdGlvbklkIjoiYmRkYjc0YjAtOTgxZi00MDcwLThjMDItMGNkZjMyNGY0NmY2In19.kSqHnlkMJx5JXgxh44OovNFAp2Lc-NsdUXn0Zn_7-dM
```

##### token의 내용
```json
{
  "aud": "https://pay.syrup.co.kr",
  "typ": "jose",
  "iss": "가맹점",
  "exp": 1473739310,
  "iat": 1473738710,
  "jti": "d735e46b-bf0a-41f7-9239-076dfe14fd49",
  "loginInfo": {
    "mctUserId": "가맹점의 회원 ID 또는 식별자",
    "extraUserId": "핸드폰과 같이 회원 별 추가 ID 체계가 존재할 경우 입력",
    "SSOCredential": "SSO 를 발급 받았을 경우 입력"
  },
  "userInfoMapper": {
    "mappingType": "CI_MAPPED_KEY",
    "mappingValue": "4987234",
    "identityAuthenticationId": "bddb74b0-981f-4070-8c02-0cdf324f46f6"
  }
}
```

### 자동결제 등록, 변경을 위한 Syrup Pay Token 생성
##### Java Code
```java
String token = syrupPayTokenBuilder.of("가맹점 ID")
                .login()
                    .withMerchantUserId("가맹점의 회원 ID 또는 식별자")
                    .withExtraMerchantUserId("핸드폰과 같이 회원 별 추가 ID 체계가 존재할 경우 입력") // Optional
                    .withSsoCredential("SSO 를 발급 받았을 경우 입력") // 자동 로그인 시 필수 입력
                .and()
                .subscription()
                    .withAutoPaymentId("시럽페이로부터 발급받은 자동결제 ID") //Optional,  자동결제 변경 시에만 필요, 자동결제 등록 시에는 필요 없음
                    .withRestrictionOf(PayConfigurer.MatchedUser.CI_MATCHED_ONLY) // Optional. 가맹점과 시럽페이 사용자 동일 여부 확인 시에만 필요
                    .withMerchantSubscriptionRequestId("가맹점에서 다시 전달받을 ID 문자열") // Optional
                    .with(new SubscriptionConfigurer.Plan(SubscriptionConfigurer.Interval.WEEKLY, "결제명")) // Optional
                    .withPromotionCode("PROMOTION_CODE_001") // Optional
                .and()
                .generateTokenBy("가맹점에게 전달한 비밀키");
```
#### token 결과
```language
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IuqwgOunueygkCIsInZlciI6IjEuMy4yIn0.eyJhdWQiOiJodHRwczovL3BheS5zeXJ1cC5jby5rciIsInR5cCI6Impvc2UiLCJpc3MiOiLqsIDrp7nsoJAiLCJleHAiOjE0NzQ1MjU1ODAsImlhdCI6MTQ3NDUyNDk4MCwianRpIjoiYzZkNTc4NmMtN2I0MS00MzliLTk0NjgtMDNkYzRjMmFhZDc2IiwibmJmIjowLCJsb2dpbkluZm8iOnsibWN0VXNlcklkIjoi6rCA66e57KCQ7J2YIO2ajOybkCBJRCDrmJDripQg7Iud67OE7J6QIiwiZXh0cmFVc2VySWQiOiLtlbjrk5ztj7Dqs7wg6rCZ7J20IO2ajOybkCDrs4Qg7LaU6rCAIElEIOyytOqzhOqwgCDsobTsnqztlaAg6rK97JqwIOyeheugpSIsIlNTT0NyZWRlbnRpYWwiOiJTU08g66W8IOuwnOq4iSDrsJvslZjsnYQg6rK97JqwIOyeheugpSJ9LCJzdWJzY3JpcHRpb24iOnsiYXV0b1BheW1lbnRJZCI6IuyLnOufve2OmOydtOuhnOu2gO2EsCDrsJzquInrsJvsnYAg7J6Q64-Z6rKw7KCcIElEIiwicmVnaXN0cmF0aW9uUmVzdHJpY3Rpb25zIjp7Im1hdGNoZWRVc2VyIjoiQ0lfTUFUQ0hFRF9PTkxZIn0sInBsYW4iOnsiaW50ZXJ2YWwiOiJXRUVLTFkiLCJuYW1lIjoi6rKw7KCc66qFIn0sIm1jdFN1YnNjcmlwdGlvblJlcXVlc3RJZCI6IuqwgOunueygkOyXkOyEnCDri6Tsi5wg7KCE64us67Cb7J2EIElEIOusuOyekOyXtCIsInByb21vdGlvbkNvZGUiOiJQUk9NT1RJT05fQ09ERV8wMDEifX0.8Tu4pOkP4Sdi5j9cKoLopgl_V2MD69MsjF8iAyqIYLo
```

##### token의 내용
```json
{
  "aud": "https://pay.syrup.co.kr",
  "typ": "jose",
  "iss": "가맹점",
  "exp": 1474525580,
  "iat": 1474524980,
  "jti": "c6d5786c-7b41-439b-9468-03dc4c2aad76",
  "nbf": 0,
  "loginInfo": {
    "mctUserId": "가맹점의 회원 ID 또는 식별자",
    "extraUserId": "핸드폰과 같이 회원 별 추가 ID 체계가 존재할 경우 입력",
    "SSOCredential": "SSO 를 발급 받았을 경우 입력"
  },
  "subscription": {
    "autoPaymentId": "시럽페이로부터 발급받은 자동결제 ID",
    "registrationRestrictions": {
      "matchedUser": "CI_MATCHED_ONLY"
    },
    "plan": {
      "interval": "WEEKLY",
      "name": "결제명"
    },
    "mctSubscriptionRequestId": "가맹점에서 다시 전달받을 ID 문자열",
    "promotionCode": "PROMOTION_CODE_001"
  }
}
```

### 토큰 복호화
```java
Token token = SyrupPayTokenBuilder.verify("토큰", "가맹점에게 전달한 비밀키");
```

### 참고 사항
*이용하고자 하는 시럽페이 서비스 기능이 복합적인 경우 중첩하여 사용 가능하다.*
##### 상황 1. 시럽페이에 자동 로그인 후 결제를 하고자 하는 경우 (자동 로그인, 결제 가능 토큰)
```java
String token = new SyrupPayTokenBuilder().of("가맹점 ID")
                 .login()
                     .withMerchantUserId("가맹점의 회원 ID 또는 식별자")
                     .withExtraMerchantUserId("핸드폰과 같이 회원 별 추가 ID 체계가 존재할 경우 입력") // Optional
                     .withSsoCredential("발급 받은 SSO")
                 .and()
                 .mapToSyrupPayUser() // Optional 사용자 개인정보를 이용하여 시럽페이 사용자와 동일 여부 검증 시 사용(암호화된 사용자 정보 전송)
                     .withType(MapToSyrupPayUserConfigurer.MappingType.ENCRYPTED_PERSONAL_INFO)
                     .withValue(new MapToSyrupPayUserConfigurer.Personal()
                             .setUsername("홍길동")
                             .setSsnFirst7Digit("8011221")
                             .setLineNumber("01022223333")
                             .setOperatorCode(MapToSyrupPayUserConfigurer.OperatorCode.SKT)
                             .setCiHash("HHHHHHAAAAAAAAAAAASSSSSSSSSSSSSSHHHHHHHHHHH")
                             .setEmail("test@mail.com")
                             .setPayableCard(
                                     new MapToSyrupPayUserConfigurer.PayableCard()
                                             .setCardNo("카드번호")
                                             .setExpireDate("202012")
                                             .setCardName("카드이름")
                                             .setCardIssuerName("발급사명")
                                             .setCardIssuer("발급사코드")
                                             .setCardAcquirer("매입사코드")
                                             .setCardType(MapToSyrupPayUserConfigurer.CardType.CREDIT))
                             , "가맹점 ID", "가맹점에 전달한 비밀키")
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
                     .withRestrictionOf(PayConfigurer.PayableLocaleRule.ONLY_ALLOWED_KOR) // Optional
                     .withRestrictionPaymentTypeOf("MOBILE;BANK") // Optional
                     .withMerchantDefinedValue("{" +
                             "\"id_1\": \"value\"," +
                             "\"id_2\": 2" +
                             "}") // Optional, JSON 포맷 이용 시 Escape(\) 입력에 주의 필요, 1k 제한
                     .withRestrictionOf(PayConfigurer.MatchedUser.CI_MATCHED_ONLY) // Optional. 가맹점과 시럽페이 사용자 동일 여부 확인 시에만 필요
                 .and()
                 .generateTokenBy("가맹점의 전달한 비밀키");
```

### 주의
1. 한번 토큰을 생성한 SyrupPayTokenBuilder 를 재이용하여 다시 토큰을 빌드하거나 JSON 을 재성성 할 수 없다.
2. 각각의 토큰에 대한 내용(Claim)은 사용자 편의에 따라 입력 후 토큰을 생성할 수 있지만 Required 되는 필드에 대하여 미입력 시 토큰 생성 시점(SyrupPayTokenHandler#generateTokenBy(key) 호출 시점)에 IllegalArgumentException 을 throw 할 수 있다.
3. 토큰이 생성된 이후 유효시간은 **10분**으로 설정되어 있으며 이에 대한 수정이 필요할 경우 Syrup Pay 개발팀과 협의 후 제공하는 가이드를 따라야 한다. 

### 참고자료
1. JOSE RFC - [https://tools.ietf.org/wg/jose](https://tools.ietf.org/wg/jose/)
2. Syrup Pay JOSE - [https://github.com/SyrupPay/jose_java](https://github.com/SyrupPay/jose_java)
3. JWT IO - [http://jwt.io/](http://jwt.io/)
