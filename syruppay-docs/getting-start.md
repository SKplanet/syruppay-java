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

## 결제 수행
Syrup Pay로 결제를 수행하기 위해 가맹점에서 수행해야될 Task는 다음과 같습니다.

#### 1. Syrup Pay로 부터 가맹점 ID 및 Secret을 부여 받고 서버정보를 Syrup Pay에 전달합니다.
 * 가맹점 ID : merchant_id, 가맹점 Secret : WXpUuHbArT8G0aAyobieCQ4x9cxWH3cE

#### 2. [가맹점 서버] 거래 인증을 위해 가맹점 거래인증 Token을 생성합니다.
 * 자세한 가맹점 Token 생성은 syruppay-token 라이브러리를 참조하시기 바랍니다.
 * Syrup Pay와 Single-Sign-On 기능을 적용하려면 Syrup Pay 인증서버와 기 가입회원 확인 및 자격증명 발행요청 API를 개발하여 개맹점 사용자별 SSO Credential을 발급 받습니다.

##### 개발 환경
* Java 1.5 이상

##### Gradle 빌드 시
```groovy
dependencies {
     compile 'com.skplanet.syruppay:syruppay-token:1.3.0'
}

```

##### Maven 빌드 시
```xml
<dependencies>
	<dependency>
		<groupId>com.skplanet.syruppay</groupId>
		<artifactId>syruppay-token</artifactId>
		<version>1.3.0</version>
	</dependency>
</dependencies>
```

###### SSO로 자동 로그인 후 결제가 가능한 토큰 생성
```java
String token = new SyrupPayTokenBuilder().of("merchant_id")
                    .login()
                        .withMerchantUserId("가맹점의 회원 ID 또는 식별자")
                        .withSsoCredential("발급 받은 SSO가 있을 경우 입력")
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

##### 팝업 API 호출
``` javascript
skplanet.syruppay.getPaymentAuth("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Im1lcmNoYW50X2lkIiwidmVyIjoiMS4zLjEifQ.eyJhdWQiOiJodHRwczovL3BheS5zeXJ1cC5jby5rciIsInR5cCI6Impvc2UiLCJpc3MiOiJtZXJjaGFudF9pZCIsImV4cCI6MTQ0ODg2ODA3NiwiaWF0IjoxNDQ4ODY3NDc2LCJqdGkiOiIwNzc1Yzg5MC0xYjViLTQ2NTEtYTQwZS0xMWM5NmU2NzQwNjEiLCJuYmYiOjAsImxvZ2luSW5mbyI6eyJtY3RVc2VySWQiOiLqsIDrp7nsoJDsnZgg7ZqM7JuQIElEIOuYkOuKlCDsi53rs4TsnpAiLCJTU09DcmVkZW50aWFsIjoi67Cc6riJIOuwm-ydgCBTU0_qsIAg7J6I7J2EIOqyveyasCDsnoXroKUifSwidHJhbnNhY3Rpb25JbmZvIjp7Im1jdFRyYW5zQXV0aElkIjoiZmEzMDIxYzEtY2RmYy00MWI2LThiYmUtMzk2NjAwZjdiMzYwIiwicGF5bWVudEluZm8iOnsiY2FyZEluZm9MaXN0IjpbXSwicHJvZHVjdFRpdGxlIjoi7KCc7ZKI66qFIiwicHJvZHVjdFVybHMiOltdLCJsYW5nIjoiS08iLCJjdXJyZW5jeUNvZGUiOiJLUlciLCJwYXltZW50QW10IjoxMDAwMCwiaXNFeGNoYW5nZWFibGUiOmZhbHNlfSwicGF5bWVudFJlc3RyaWN0aW9ucyI6eyJjYXJkSXNzdWVyUmVnaW9uIjoiQUxMT1dFRDpLT1IifX19.eM6AytBdK_2OLgcEej8wRVcx-E8aHddrPyFw6MDjC7M", returnUrl, paymentSuccessCallback, paymentFailureCallback );
```

##### 페이지 전환 API
``` javascript
skplanet.syruppay.getPaymentAuth("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Im1lcmNoYW50X2lkIiwidmVyIjoiMS4zLjEifQ.eyJhdWQiOiJodHRwczovL3BheS5zeXJ1cC5jby5rciIsInR5cCI6Impvc2UiLCJpc3MiOiJtZXJjaGFudF9pZCIsImV4cCI6MTQ0ODg2ODA3NiwiaWF0IjoxNDQ4ODY3NDc2LCJqdGkiOiIwNzc1Yzg5MC0xYjViLTQ2NTEtYTQwZS0xMWM5NmU2NzQwNjEiLCJuYmYiOjAsImxvZ2luSW5mbyI6eyJtY3RVc2VySWQiOiLqsIDrp7nsoJDsnZgg7ZqM7JuQIElEIOuYkOuKlCDsi53rs4TsnpAiLCJTU09DcmVkZW50aWFsIjoi67Cc6riJIOuwm-ydgCBTU0_qsIAg7J6I7J2EIOqyveyasCDsnoXroKUifSwidHJhbnNhY3Rpb25JbmZvIjp7Im1jdFRyYW5zQXV0aElkIjoiZmEzMDIxYzEtY2RmYy00MWI2LThiYmUtMzk2NjAwZjdiMzYwIiwicGF5bWVudEluZm8iOnsiY2FyZEluZm9MaXN0IjpbXSwicHJvZHVjdFRpdGxlIjoi7KCc7ZKI66qFIiwicHJvZHVjdFVybHMiOltdLCJsYW5nIjoiS08iLCJjdXJyZW5jeUNvZGUiOiJLUlciLCJwYXltZW50QW10IjoxMDAwMCwiaXNFeGNoYW5nZWFibGUiOmZhbHNlfSwicGF5bWVudFJlc3RyaWN0aW9ucyI6eyJjYXJkSXNzdWVyUmVnaW9uIjoiQUxMT1dFRDpLT1IifX19.eM6AytBdK_2OLgcEej8wRVcx-E8aHddrPyFw6MDjC7M", returnUrl);
```

#### 4. [가맹점 웹페이지] Syrup Pay 결제 Page에서 사용자가 유효한 PIN을 입력하면 Syrup Pay에서 거래인증 결과를 가맹점 웹페이지로 전달합니다.
  * 가맹점 웹페이지에서는 가맹점 서버로 부터 거래 인증 결과를 받을 때까지 적절한 UI 표시를 해야 합니다. (ex, 결제 중입니다…Loading Page 노출)

##### 응답결과 조회
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
				window.opener.paymentSuccessCallback( authInfo );
			} else {
				parent.window.opener.paymentSuccessCallback( authInfo );
			}
		} catch(e){
			alert( "해당 주문서 페이지를 이탈하여 결제가 진행되지 않습니다\n다시 결제를 시도해주세요" );
			window.close();
		}
	<% } else { %>
		try{
			if( window.opener ){
				window.opener.paymentFailureCallback( authInfo );
			} else {
				parent.window.opener.paymentFailureCallback( authInfo );
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
		"ssoUpdateRequired": false,
		"pmtAuthDetail": {
			"payMethod": "10",
			"payAmount": 10000,
			"offerAmount": 0,
			"loyaltyAmount": 0,
			"payInstallment": "00",
			"payCurrency": "KRW",
			"payFinanceCode": "17",
			"isCardPointApplied": false
		}
	}
}
```

#### 5. [가맹점 서버] 가맹점 서버는 Syrup Pay에 거래승인 요청 API를 호출 하고 그 결과를 받아 처리합니다.
 * 거래 승인 요청에 대한 응답을 받지 못한 경우 (Timeout등 발생) 정상거래가 일어났는지 알 수 가 없으므로 해당 거래에 대해서는 반드시 거래 망취소 요청을 해야 합니다.


## 회원 가입
Syrup Pay로 회원 가입만 별도로 수행하기 위해서는 다음과 같은 절차를 따르면 됩니다. 
(Editor’s Note: 4.1의 결제 진행과정에서도 Syrup Pay회원이 아닌경우 회원가입을 수행할 수 있습니다. 따라서, 가망점에서 Syrup Pay 회원가입을 위한 별도의 링크를 제공할 때만 아래 내용을 개발하시면 됩니다.)

#### 1. Syrup Pay로 부터 가맹점 ID 및 Secret을 부여 받고 서버정보를 Syrup Pay에 전달합니다.

#### 2. (Server) 가맹점 Client에서 Syrup Pay결제시 요청하는 가맹점 Token 전달 API를 개발합니다.

#### 3. (Server)거래 인증을 위해 가맹점 거래인증 Token을 생성기능을 개발합니다.
 * 가맹점 Token의 규격은 본 규격서에 기술되어 있습니다.

#### 4. (Client) 가맹점 서버로 부터 가맹점 oken을 넘겨받아 시럽페이 회원가입 요청 API를 호출합니다.
 * Syrup Pay의 회원가입 Page는 팝업 형태 또는 Page 전환형태로 띄울 수 있습니다. 일반적으로 PC환경에서는 팝업형태, Mobile에서는 Page 전환형태가 권장됩니다. 팝업형태의 API는 여기, Page 전환형태의 API는 여기를 참고 하세요.

#### 5. (Client, Server) Syrup Pay 회원가입에 대한 결과를 넘겨 받으면 Client에서는 적절한 UI(회원가입 완료 축하 Message 등)처리를 수행합니다.

# Workflow
Syrup Pay의 실행 구조와 workflow를 간단하게 설명합니다.

## Case1) 가맹점이 Syrup Pay의 자동로그인 기능을 기능을 이용할 경우,
가맹점이 자체 회원 체계를 보유하고 있다면 Syrup Pay와 Single-Sign-On 기능을 이용할 수 있 구조는 다음과 같습니다. 

![가맹점이 Syrup Pay의 자동로그인 기능을 이용할 경우](https://raw.githubusercontent.com/skplanet/syruppay-java/release/1.3/doc/images/syrup_pay_case1.png)

시럽페이는 결제시 가맹점과 거래인증및 거래승인 단계를 거쳐 결제를 진행합니다. 
### Step1) 거래인증 (1-1, 1-2 단계)
* 먼저 가맹점에서 거래승인 요청을 하려면 Syrup Pay Client Library에 거래 인증을 위한 Token값을 전달해 야 합니다. 이를 위해 가맹점 Client는 가맹점 서버에게 Token 생성을 요청합니다 (1-1), 
* 가맹점에서는 거래승인을 위한 Token을 생성하는데, Syrup Pay와의 Single-Sign-On 위해서는 Syrup Pay에게 자격증명 발행을 요청합니다. (1-2). 가맹점 서버는 거래 승인을 위한 Token값을 생성하여 가맹점 Client에 전달합니다.  
* 가맹점 Client는 Syrup Pay Client Library에게 거래 승인요청을 하게 되면 Syrup Pay에서 적절한 UI를 Display하고 사용자가 PIN입력하여 거래 승인을 수행하면 거래인증 결과를 가맹점 Client에 전달합니다.

### Step 2) 거래 승인 (3,4 단계)
* 가맹점 Client는 Syrup Pay로 부터 전달받은 거래인증 값을 가맹점 Server로 전달합니다 (3). 
* 가맹점 서버는 거래 Syrup Pay API서버로 거래 승인을 요청합니다, Syrup Pay API서버는 거래 승인을 수행하고 그 결과를 가맹점 서버로 전달합니다(4). 
* 가맹점 서버는 거래승인 성공 또는 실패 여부와 관련정보를 가맹점 Client에 전달합니다

## Case2) 가맹점과 Syrup Pay간의 자동로그인 기능이 필요 없는경우,
위의 경우와 동일하지만 거래인증 단계에서 Syrup Pay와 연동할 필요가 없기 때문에 아래와 같이  1-2단계가 생략 됩니다.

![가맹점과 Syrup Pay간의 자동로그인 기능이 필요 없는 경우](https://raw.githubusercontent.com/skplanet/syruppay-java/release/1.3/doc/images/syrup_pay_case2.png)

# Security Requirements
해당 Interface에서 별도 정의하지 않는 한, Syrup Pay의 모든 요청 및 응답은 HTTPS 기반으로 수행하며 Syrup Pay 서버는 **보안 취약성이 있는 SSL Protocol을 허용하지 않고 TLS Protocol 만**을 허용합니다.

가맹점에서 Syrup Pay로의 모든 요청 및 응답은 JWS(JSON Web Signature) 또는 JWE(JSON Web Encryption) 형식으로 전달되어야 하고 응답 유효성(위/변조 여부) 확인을 수행해야 합니다.
개발 편의를 위해, 가맹점 Server 단의 JWS/JWE Library(Java 기반)를 제공하고, 가맹점의 Web Site에서 Syrup Pay 결제 연동에 필요한 “Syrup Pay Client Library”를 함께 제공합니다.