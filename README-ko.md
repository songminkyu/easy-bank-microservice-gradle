# EasyBank 마이크로서비스 아키텍처

이 프로젝트는 EasyBank라는 은행 애플리케이션의 마이크로서비스 기반 구현입니다. 원래 Maven 프로젝트로 개발되었으며 현재 Gradle로 마이그레이션 중입니다.

## 아키텍처 개요

이 애플리케이션은 다음 구성 요소를 포함하는 마이크로서비스 아키텍처 패턴을 따릅니다:

- **API 게이트웨이**: 모든 클라이언트 요청의 진입점
- **서비스 디스커버리**: 서비스 등록 및 발견
- **구성 서버**: 중앙 집중식 구성 관리
- **비즈니스 서비스**: 핵심 뱅킹 기능
- **메시징**: 이메일 및 SMS용 알림 서비스

## 기술 스택

- **Java**: 버전 17
- **Spring Boot**: 버전 3.4.5
- **Spring Cloud**: 버전 2024.0.0
- **데이터베이스**: H2 (개발용)
- **API 문서화**: SpringDoc OpenAPI
- **빌드 도구**: Gradle
- **컨테이너화**: Docker with JIB
- **오케스트레이션**: Kubernetes with Helm charts
- **모니터링**: Grafana, Prometheus, Loki, Tempo

## 마이크로서비스

### 비즈니스 서비스

1. **계정 서비스(Accounts Service)**
   - 고객 은행 계좌 관리
   - 계좌 생성, 조회 및 관리 기능 제공
   - API 문서: `/swagger-ui.html`

2. **카드 서비스(Cards Service)**
   - 신용 및 직불 카드 관리
   - 카드 발급, 활성화 및 관리 제공
   - API 문서: `/swagger-ui.html`

3. **대출 서비스(Loans Service)**
   - 대출 상품 및 신청 관리
   - 대출 신청, 승인 및 관리 제공
   - API 문서: `/swagger-ui.html`

4. **메시지 서비스(Message Service)**
   - 이메일 및 SMS를 통한 알림 처리
   - 고객 커뮤니케이션을 위한 계정 관련 이벤트 처리

### 인프라 서비스

1. **구성 서버(Config Server)**
   - 중앙 집중식 구성 관리
   - 모든 마이크로서비스에 대한 외부화된 구성

2. **유레카 서버(Eureka Server)**
   - 서비스 발견 및 등록
   - 서비스가 서로를 찾고 통신할 수 있도록 함

3. **게이트웨이 서버(Gateway Server)**
   - Spring Cloud Gateway를 사용한 API 게이트웨이
   - 적절한 서비스로 요청 라우팅
   - 장애 허용을 위한 회로 차단기 패턴 구현
   - 서비스 보호를 위한 속도 제한 제공
   - 요청 재작성 및 부하 분산 처리

## 설정 및 설치

### 사전 요구 사항

- Java 17 이상
- Gradle 7.x 이상
- Docker (컨테이너화용)
- Kubernetes (배포용)
- Helm (Kubernetes 패키지 관리용)

### 프로젝트 빌드

```bash
./gradlew clean build
```

### 로컬에서 실행

1. 먼저 인프라 서비스 시작:

```bash
./gradlew :apps:configserver:bootRun
./gradlew :apps:eurekaserver:bootRun
./gradlew :apps:gatewayserver:bootRun
```

2. 비즈니스 서비스 시작:

```bash
./gradlew :apps:accounts:bootRun
./gradlew :apps:cards:bootRun
./gradlew :apps:loans:bootRun
./gradlew :apps:message:bootRun
```

## 배포

### Docker 컨테이너

모든 서비스에 대한 Docker 이미지 빌드:

```bash
./gradlew jibDockerBuild
```

### Kubernetes 배포

이 프로젝트는 Kubernetes에 배포하기 위한 Helm 차트를 포함합니다:

```bash
helm install eazybank-common ./helm/eazybank-common
helm install eazybank-services ./helm/eazybank-services
```

### 환경별 배포

이 프로젝트는 다양한 환경을 지원합니다:

- 개발: `./helm/environments/dev-env`
- QA: `./helm/environments/qa-env`
- 프로덕션: `./helm/environments/prod-env`

## 모니터링

이 프로젝트는 모니터링 도구 배포를 위한 Helm 차트를 포함합니다:

- Grafana: `./helm/grafana`
- Prometheus: `./helm/kube-prometheus`
- Loki (로그용): `./helm/grafana-loki`
- Tempo (추적용): `./helm/grafana-tempo`

## API 문서

각 서비스는 API 문서화를 위한 자체 Swagger UI를 제공합니다:

- Accounts: `http://localhost:8080/eazybank/accounts/swagger-ui.html`
- Cards: `http://localhost:8080/eazybank/cards/swagger-ui.html`
- Loans: `http://localhost:8080/eazybank/loans/swagger-ui.html`

## 프로젝트 구조

```
easy-bank-msa/
├── apps/
│   ├── accounts/         # 계정 마이크로서비스
│   ├── cards/            # 카드 마이크로서비스
│   ├── configserver/     # 구성 서버
│   ├── eazy-bom/         # 의존성 관리를 위한 BOM(Bill of Materials)
│   ├── eurekaserver/     # 서비스 디스커버리 서버
│   ├── gatewayserver/    # API 게이트웨이
│   ├── loans/            # 대출 마이크로서비스
│   └── message/          # 알림 서비스
├── helm/                 # Kubernetes Helm 차트
│   ├── eazybank-common/  # 공통 인프라
│   ├── eazybank-services/# 마이크로서비스 배포
│   ├── environments/     # 환경별 구성
│   ├── grafana/          # 모니터링 대시보드
│   ├── grafana-loki/     # 로그 집계
│   ├── grafana-tempo/    # 분산 추적
│   ├── kafka/            # 이벤트 스트리밍 플랫폼
│   ├── keycloak/         # 신원 및 접근 관리
│   └── kube-prometheus/  # 모니터링 및 경고
└── kubernetes/           # Kubernetes 매니페스트
```

## 기여

풀 리퀘스트를 제출하기 전에 기여 가이드라인을 읽어주세요.

## 라이선스

이 프로젝트는 Apache License 2.0에 따라 라이선스가 부여됩니다 - 자세한 내용은 LICENSE 파일을 참조하세요.