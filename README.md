## Overview
해당 프로젝트는 Syrup Pay의 간편결제 서비스를 이용하고자 하는 가맹점(11번가, Syrup Order 등)의 개발자를 대상으로하며 가맹점과 Syrup Pay 서비스 연동 기능을 구현할 때의 복잡도(Complexity)를 개선하고자 하는 것을 목표로 한다.

### Syrup Pay Token Library
시럽페이 서비스에서 가맹점 인증 및 데이터 교환을 위한 규격을 정의하며 전송 구간에 대한 암호화 및 무결성 보장을 위한 토큰을 생성, 관리하는 기능을 수행하며 자세한 내용은 해당 프로젝트 [syruppay-token](https://github.com/skplanet/syruppay-java/tree/release/1.3/syruppay-token)를 참조하시기 바랍니다.

### Syrup Pay JOSE Library 
SyrupPay 결제 데이터 암복호화 및 AccessToken 발행 등에 사용되며 SyrupPay 서비스의 가맹점에 배포하기 위한 목적으로 라이브러리가 구현되었으며 자세한 내용은 해당 프로젝트 [syruppay-jose](https://github.com/skplanet/syruppay-java/tree/release/1.3/syruppay-jose)를 참조하시기 바랍니다.

### Syrup Pay Client Library (준비중)
