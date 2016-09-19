## Overview
시럽페이 서비스와 통신하기 위한 프로토콜을 정의, 관리한다.

### 개선 항목
1. 데이터 전송 구간 구현에 대한 복잡도
1. 시럽페이 규격(도메인)에 대한 복잡도

## Getting Start
### 개발 환경
* Java 1.7 이상

### Gradle 빌드 시

### Maven 빌드 시

### 승인요청
#### Java Sample Code
```java
    // 접속 환경 설정
    SyrupPayClient syrupPayClient = new SyrupPayClient(SyrupPayEnvironment.DEVELOPMENT);
    syrupPayClient.basicAuthentication("가맹점 ID", "가맹점 Basic Authentication Key");
    syrupPayClient.useJweWhileCommunicating("가맹점 ID", "가맹점 Secret");

    // 요청 데이터 설정
    ApproveEvent.RequestApprove request = new ApproveEvent.RequestApprove();
    request.setRequestIdOfMerchant("가맹점 거래 승인요청 ID");
    request.setRequestTimeOfMerchant(1448870110);
    request.setOrderIdOfMerchant("가맹점 거래인증 ID");
    request.setPaymentAmount(10000);
    request.setTaxFreeAmount(0);
    request.setOcTransAuthId("TA20151130000000000020083");
    request.setTransactionAuthenticationValue("y7we9C6TA_k-nEiYGnkeCUN8INuVCeyNJWcxbNmaKSI");

    // 요청 & 응답
    ApproveEvent.ResponseApprove response = syrupPayClient.approve(request);
    
    // 응답 데이터 처리
```