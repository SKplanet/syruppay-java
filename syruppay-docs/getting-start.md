# Introduction
본 문서는 “Syrup Pay Payment Service” (이하 “Syrup Pay”)를 통한 결제 서비스를 제공하기 위해 필요한 외부 구성요소와의 연동 규격을 정의하고 Web(PC/Mobile 포함), App(Android/iOS) 환경에서의 Syrup Pay 연결을 위한 방법 및 Sample Codes를 포함합니다. 

# 용어정리
### 가맹점 (Merchant, mct)
사용자의 요청에 따라 Syrup Pay를 사용하는 Online/Offline Merchant를 의미합니다. 가맹점이 되기 위해서는, 사전 가맹점 계약체결을 통해 Syrup Pay에 유효 가맹점으로 등록되어 가맹점 ID와 Secret을 부여 받아야 합니다.

### 가맹점 Client
가맹점에서 제공하는 사용자 접점을 의미합니다. 가맹점에서 제공하는 Web Site나 Mobile Application 등이 이에 해당합니다.

### 가맹점 토큰
가맹점과 Syrup Pay 간의 거래인증, 회원가입, 설정 연결 시 필요한 토큰입니다. 거래 시에는 토큰에 최종 결제금액과 상품정보 등 거래에 필요한 최종 값을 가맹점에서 Syrup Pay로 전달할 때 사용하며 Syrup Pay는 해당 토큰이 유효한 경우에만 가맹점에서의 요청을 정상처리 합니다. 

### 자격증명 (SSOCredential, Single-Sign-On Credential)
가맹점 계약에 따라 Single-Sign-On 협약이 된 경우, 가맹점에서는 Syrup Pay에게 해당 가맹점의 사용자별 Unique한 SSOCredential 발급을 요청할 수 있습니다. Syrup Pay는 발급된 SSO Credetial 값이 유효한 가맹점 토큰에 포함되어 있을 경우, 정의된 SSO 정책에 따라 추가 Syrup Pay 로그인 절차 없이 결제 PIN 입력만으로 Syrup Pay 결제가 가능합니다. 만약 가맹점이 회원을 보유하고 있지 않은 경우는 자격증명관련 연동 부분은 skip할 수 있습니다.

# Supported Platform
Syrup Pay는 Web 기반의 Javascript Library 또는 Native Code 기반의 SDK (Android, iOS)모두를 제공합니다.

# Getting Started
Syrup Pay는 Web 기반의 Javascript Library 또는 Native SDK (Android, iOS)모두를 기본 제공합니다. Web 기반 연동일 경우 아래 내용을 참고하여 개발을 진행하면 됩니다. Native SDK 기반도 아래 Step과 동일하나  Client API를 호출하는 부분만 다릅니다. Native SDK 기반 연동 내용은 여기를 참고 하시면 됩니다.

## 가맹점 ID & 가맹점 Secret & Basic Authentication Key
시럽페이 서비스 이용을 위해서는 사전에 Syrup Pay로 부터 가맹점 ID 및 가맹점 Secret 그리고 가맹점 Basic Authentication Key 를 전달 받습니다.
 * 가맹점 ID : merchant_id
 * 가맹점 Secret : WXpUuHbArT8G0aAyobieCQ4x9cxWH3cE
 * 가맹점 Basic Authentication Key : G3aIW7hYmlTjag3FDc63OGLNWwvagVUU

## 결제 수행
Syrup Pay로 결제를 수행하기 위해 가맹점에서 수행해야될 Task는 다음과 같습니다.

#### 1. [가맹점 서버] Syrup Pay 자동 로그인을 사용하는 경우, Syrup Pay로 부터 가맹점 회원에 대한 SSO 발급 여부를 조회합니다.

##### 개발 환경
* Java 1.5 이상

##### Gradle 빌드 시
```groovy
dependencies {
     compile 'com.skplanet.syruppay:syruppay-client:0.1'
}

```

##### Maven 빌드 시
```xml
<dependencies>
	<dependency>
		<groupId>com.skplanet.syruppay</groupId>
		<artifactId>syruppay-client</artifactId>
		<version>0.1</version>
	</dependency>
</dependencies>
```

```java
// 시럽페이 클라이언트 객체 초기화
SyrupPayClient syrupPayClient = new SyrupPayClient(SyrupPayEnvironment.DEVELOPMENT)
syrupPayClient.basicAuthentication("merchant_id", "G3aIW7hYmlTjag3FDc63OGLNWwvagVUU");
syrupPayClient.useJweWhileCommunicating("merchant_id", "WXpUuHbArT8G0aAyobieCQ4x9cxWH3cE");

// SSO 요청 호출
GetSsoCredentialEvent.RequestGettingSso request = new GetSsoCredentialEvent.RequestGettingSso().setSsoIdentifier(new GetSsoCredentialEvent.SsoIdentifier().setUserIdOfMerchant("6733b40f-4b6c-48b7-8c98-f218156a0086")); // 가맹점의 회원 구분 ID 값 : 6733b40f-4b6c-48b7-8c98-f218156a0086

// 응답값 확인
GetSsoCredentialEvent.ResponseGettingSso response = syrupPayClient.getSso(request);
String ssoCredentail = responset.getSsoCredential();
```

#### 2. [가맹점 서버] 거래 인증을 위해 가맹점 거래인증 Token을 생성합니다.

##### 개발 환경
* Java 1.5 이상

##### Gradle 빌드 시
```groovy
dependencies {
     compile 'com.skplanet.syruppay:syruppay-token:1.3'
}

```

##### Maven 빌드 시
```xml
<dependencies>
	<dependency>
		<groupId>com.skplanet.syruppay</groupId>
		<artifactId>syruppay-token</artifactId>
		<version>1.3</version>
	</dependency>
</dependencies>
```

###### SSO로 자동 로그인 후 결제가 가능한 토큰 생성
```java
String token = new SyrupPayTokenBuilder().of("merchant_id")
                    .login()
                        .withMerchantUserId("가맹점의 회원 ID 또는 식별자")
                        .withSsoCredential(ssoCredentail) // SSO 를 잘급 받은 경우 입력하며 없을 경우 withSsoCredential()를 호출하지 않음
                    .and()
                    .pay()
                        .withOrderIdOfMerchant("fa3021c1-cdfc-41b6-8bbe-396600f7b360")
                        .withProductTitle("제품명")
                        .withLanguageForDisplay(PayConfigurer.Language.KO)
                        .withAmount(10000)
                        .withCurrency(PayConfigurer.Currency.KRW)
                    .and()
                    .generateTokenBy("WXpUuHbArT8G0aAyobieCQ4x9cxWH3cE");
```

###### 생성된 토큰 값
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Im1lcmNoYW50X2lkIiwidmVyIjoiMS4zLjEifQ.eyJhdWQiOiJodHRwczovL3BheS5zeXJ1cC5jby5rciIsInR5cCI6Impvc2UiLCJpc3MiOiJtZXJjaGFudF9pZCIsImV4cCI6MTQ0ODg2ODA3NiwiaWF0IjoxNDQ4ODY3NDc2LCJqdGkiOiIwNzc1Yzg5MC0xYjViLTQ2NTEtYTQwZS0xMWM5NmU2NzQwNjEiLCJuYmYiOjAsImxvZ2luSW5mbyI6eyJtY3RVc2VySWQiOiLqsIDrp7nsoJDsnZgg7ZqM7JuQIElEIOuYkOuKlCDsi53rs4TsnpAiLCJTU09DcmVkZW50aWFsIjoi67Cc6riJIOuwm-ydgCBTU0_qsIAg7J6I7J2EIOqyveyasCDsnoXroKUifSwidHJhbnNhY3Rpb25JbmZvIjp7Im1jdFRyYW5zQXV0aElkIjoiZmEzMDIxYzEtY2RmYy00MWI2LThiYmUtMzk2NjAwZjdiMzYwIiwicGF5bWVudEluZm8iOnsiY2FyZEluZm9MaXN0IjpbXSwicHJvZHVjdFRpdGxlIjoi7KCc7ZKI66qFIiwicHJvZHVjdFVybHMiOltdLCJsYW5nIjoiS08iLCJjdXJyZW5jeUNvZGUiOiJLUlciLCJwYXltZW50QW10IjoxMDAwMCwiaXNFeGNoYW5nZWFibGUiOmZhbHNlfSwicGF5bWVudFJlc3RyaWN0aW9ucyI6eyJjYXJkSXNzdWVyUmVnaW9uIjoiQUxMT1dFRDpLT1IifX19.eM6AytBdK_2OLgcEej8wRVcx-E8aHddrPyFw6MDjC7M
```
#### 3. [가맹점 웹페이지] 가맹점 서버로 부터 가맹점 거래인증 Token을 넘겨받아 시럽페이 거래인증 요청 API를 호출하여 Syrup Pay 결제 Page를 띄웁니다.
 * Syrup Pay의 결제 Page는 팝업 형태 또는 Page 전환 형태로 띄울 수 있습니다. 일반적으로 PC환경에서는 팝업형태, Mobile에서는 Page 전환형태가 권장됩니다.
 * Iframe을 이용한 Page 전환은 피해 주시기 바랍니다.

##### 팝업 API 호출
``` javascript
skplanet.syruppay.open(); // * Popup Blocker 에 걸리지 않기 위해 수행 필요

/*------ 토큰을 받아오는 기능 수행 ------*/

var token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Im1lcmNoYW50X2lkIiwidmVyIjoiMS4zLjEifQ.eyJhdWQiOiJodHRwczovL3BheS5zeXJ1cC5jby5rciIsInR5cCI6Impvc2UiLCJpc3MiOiJtZXJjaGFudF9pZCIsImV4cCI6MTQ0ODg2ODA3NiwiaWF0IjoxNDQ4ODY3NDc2LCJqdGkiOiIwNzc1Yzg5MC0xYjViLTQ2NTEtYTQwZS0xMWM5NmU2NzQwNjEiLCJuYmYiOjAsImxvZ2luSW5mbyI6eyJtY3RVc2VySWQiOiLqsIDrp7nsoJDsnZgg7ZqM7JuQIElEIOuYkOuKlCDsi53rs4TsnpAiLCJTU09DcmVkZW50aWFsIjoi67Cc6riJIOuwm-ydgCBTU0_qsIAg7J6I7J2EIOqyveyasCDsnoXroKUifSwidHJhbnNhY3Rpb25JbmZvIjp7Im1jdFRyYW5zQXV0aElkIjoiZmEzMDIxYzEtY2RmYy00MWI2LThiYmUtMzk2NjAwZjdiMzYwIiwicGF5bWVudEluZm8iOnsiY2FyZEluZm9MaXN0IjpbXSwicHJvZHVjdFRpdGxlIjoi7KCc7ZKI66qFIiwicHJvZHVjdFVybHMiOltdLCJsYW5nIjoiS08iLCJjdXJyZW5jeUNvZGUiOiJLUlciLCJwYXltZW50QW10IjoxMDAwMCwiaXNFeGNoYW5nZWFibGUiOmZhbHNlfSwicGF5bWVudFJlc3RyaWN0aW9ucyI6eyJjYXJkSXNzdWVyUmVnaW9uIjoiQUxMT1dFRDpLT1IifX19.eM6AytBdK_2OLgcEej8wRVcx-E8aHddrPyFw6MDjC7M";

var returnURL = "https://client.co.kr/syruppay/bridge-page.html";

skplanet.syruppay.getPaymentAuth(
	  token
	, returnURL
	, successHandler: function ( result ) {
      // 가맹점 Business Logic. 거래인증정보 획득 후 결제 요청 수행
      // var authValue = JSON.parse(result).tranAuthValue
    }
	, failuerHandler: function (result ) {
      // 실패
    }
)
```

##### 페이지 전환 API
``` javascript
/*------ 토큰을 받아오는 기능 수행 ------*/

var token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Im1lcmNoYW50X2lkIiwidmVyIjoiMS4zLjEifQ.eyJhdWQiOiJodHRwczovL3BheS5zeXJ1cC5jby5rciIsInR5cCI6Impvc2UiLCJpc3MiOiJtZXJjaGFudF9pZCIsImV4cCI6MTQ0ODg2ODA3NiwiaWF0IjoxNDQ4ODY3NDc2LCJqdGkiOiIwNzc1Yzg5MC0xYjViLTQ2NTEtYTQwZS0xMWM5NmU2NzQwNjEiLCJuYmYiOjAsImxvZ2luSW5mbyI6eyJtY3RVc2VySWQiOiLqsIDrp7nsoJDsnZgg7ZqM7JuQIElEIOuYkOuKlCDsi53rs4TsnpAiLCJTU09DcmVkZW50aWFsIjoi67Cc6riJIOuwm-ydgCBTU0_qsIAg7J6I7J2EIOqyveyasCDsnoXroKUifSwidHJhbnNhY3Rpb25JbmZvIjp7Im1jdFRyYW5zQXV0aElkIjoiZmEzMDIxYzEtY2RmYy00MWI2LThiYmUtMzk2NjAwZjdiMzYwIiwicGF5bWVudEluZm8iOnsiY2FyZEluZm9MaXN0IjpbXSwicHJvZHVjdFRpdGxlIjoi7KCc7ZKI66qFIiwicHJvZHVjdFVybHMiOltdLCJsYW5nIjoiS08iLCJjdXJyZW5jeUNvZGUiOiJLUlciLCJwYXltZW50QW10IjoxMDAwMCwiaXNFeGNoYW5nZWFibGUiOmZhbHNlfSwicGF5bWVudFJlc3RyaWN0aW9ucyI6eyJjYXJkSXNzdWVyUmVnaW9uIjoiQUxMT1dFRDpLT1IifX19.eM6AytBdK_2OLgcEej8wRVcx-E8aHddrPyFw6MDjC7M";

var returnURL = "https://client.co.kr/syruppay/bridge-page.html";

skplanet.syruppay.getPaymentAuth(token, returnUrl);
```

#### 4. [가맹점 웹페이지] Syrup Pay 결제 Page에서 사용자가 유효한 PIN을 입력하면 Syrup Pay에서 거래인증 결과를 가맹점 웹페이지로 전달합니다.
  * 가맹점 웹페이지에서는 가맹점 서버로 부터 거래 인증 결과를 받을 때까지 적절한 UI 표시를 해야 합니다. (ex, 결제 중입니다…Loading Page 노출)

##### 팝업인 경우 응답결과 조회 샘플 코드
```jsp
/**
 * resultCode 성공 및 실패를 구분합니다.
 * 200: 정상요청
 * 400: 유효한 요청이 아닙니다.
 * 500: Syrup Pay 시스템 오류
 * 600: 사용자 취소 (Syrup Pay 화면에서 닫기 버튼을 누른경우)
 */
String resultCode = request.getParameter("resultCode");
String authInfo = request.getParameter("authInfo");

<script type="text/javascript">
 var authInfo = '<%=authInfo%>';
	<% if( resultCode.equals("200") ){ %>
		try{
			if( window.opener ){
				window.opener.skplanet.syruppay.successHandler( authInfo );
			} else {
				parent.window.opener.skplanet.syruppay.successHandler( authInfo );
			}
		} catch(e){
			alert( "해당 주문서 페이지를 이탈하여 결제가 진행되지 않습니다\n다시 결제를 시도해주세요" );
			window.close();
		}
	<% } else { %>
		try{
			if( window.opener ){
				window.opener.skplanet.syruppay.failureHandler( authInfo );
			} else {
				parent.window.opener.skplanet.syruppay.failureHandler( authInfo );
			}
		} catch(e){
			alert( "해당 주문서 페이지를 이탈하여 결제가 진행되지 않습니다\n다시 결제를 시도해주세요" );
			window.close();
		}
	<% } %>
	window.close();
</script>
```

##### 응답결과(authInfo:String)

```json
{
	"syrupPayError": null,
	"pinFailureCnt": 0,
	"ocAuthInfo": {
		"tranAuthValue": "ArG6u36eUkC4lbdjoLkF2TyOS7uAKCquMU3sxfuG788",
		"mctTransAuthId": "fa3021c1-cdfc-41b6-8bbe-396600f7b360", // 가맹점의 주문 ID
		"ocTransAuthId": "TA20151130000000000015687",
		"ssoUpdateRequired": false // true 일 경우 SSO 조회를 시도해야 함 (1번 절차 참고)
	}
}
```

#### 5. [가맹점 서버] 가맹점 서버는 Syrup Pay에 거래승인 요청 API를 호출 하고 그 결과를 받아 처리합니다.
 * 거래 승인 요청에 대한 응답을 받지 못한 경우 (Timeout등 발생) 정상거래가 일어났는지 알 수 가 없으므로 해당 거래에 대해서는 반드시 거래 망취소 요청을 해야 합니다.

```java
// 시럽페이 클라이언트 객체 초기화
SyrupPayClient syrupPayClient = new SyrupPayClient(SyrupPayEnvironment.DEVELOPMENT)
syrupPayClient.basicAuthentication("merchant_id", "G3aIW7hYmlTjag3FDc63OGLNWwvagVUU");
syrupPayClient.useJweWhileCommunicating("merchant_id", "WXpUuHbArT8G0aAyobieCQ4x9cxWH3cE");

// 거래 승인 객체 구성
ApproveEvent.RequestApprove request = new ApproveEvent.RequestApprove();
request.setRequestIdOfMerchant("4e0f618e9603497f8aa40ec182c36b12");
request.setRequestTimeOfMerchant(1448870110);
request.setOrderIdOfMerchant("가맹점 거래인증 ID");
request.setPaymentAmount(10000);
request.setTaxFreeAmount(0);
request.setOcTransAuthId("TA20151130000000000020083");
request.setTransactionAuthenticationValue("y7we9C6TA_k-nEiYGnkeCUN8INuVCeyNJWcxbNmaKSI");

// 거래 승인 요청
try {
	ApproveEvent.ResponseApprove reeponse = syrupPayClient.approve(request);
	if(reeponse.isExcept()) {
		// 결제 실패 처리
	} else {
		// 결제 성공 처리
	}
} catch(Exception e) {
	// 예외 발생 시에만 망상 취소
	syrupPayClient.cancel(new CancelEvent.RequestCancel()
		.setRequestIdIfMerchant("2398fksdjhf872q1kj3h598gfshkdjhr93") // 현재 요청 ID
		.setRequestTimeOfMerchant(1449671502)	// 현재 요청 시간
		.setApprovedRequestIdOfMerchant("4e0f618e9603497f8aa40ec182c36b12") // 취소 하려는 결제 ID
		.setApprovedRequestTimeOfMerchant(1448870110)	// 취소 하려는 결제 시간
	);
}
```
