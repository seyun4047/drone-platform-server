
# Drone Platform Server
---
## 개요
Drone Platform Server는 제조사 독립형 드론 플랫폼(Manufacturer-Independent Drone Platform)의 핵심 백엔드 서비스입니다.<br>

이 서버는 드론 세션의 전체 라이프사이클 관리, 인증 처리, 텔레메트리 데이터 처리, 그리고 대규모 요청 처리 전반을 담당합니다.<br>

드론, 데이터 저장소, 외부 서비스 간의 중앙 조정 계층(Central Coordination Layer) 역할을 수행합니다.

---

## 주요 기능

### 1. 드론 인증 및 세션 관리
서버는 안전하고 확장 가능한 드론 세션 관리를 위해 다음을 사용합니다:

- Authorized Drone Database (MySQL)  
  등록 및 승인된 드론 목록을 관리합니다.

- Drone Authentication Tokens (Redis)  
  인증된 드론에 대한 액세스 토큰을 발급하고 검증합니다.

- Drone Heartbeat Tracking (Redis)  
  시간 기반 Heartbeat 데이터를 활용하여 드론의 실시간 연결 상태를 추적합니다.

### 2. 텔레메트리 및 이벤트 처리
서버는 [drone-client](https://github.com/seyun4047/drone-platform-client)로부터 전송되는 텔레메트리 및 이벤트 데이터를 수신하고 처리합니다:

- Telemetry Data  
  활성 드론의 실시간 운용 상태 데이터를 처리합니다.

- Event Data  
  사람 탐지 등 운용중 발생하는 이벤트 데이터를 처리합니다.
---

## 사용법
### Local Build
```bash
# Export env
export $(cat .env | xargs)

# Build the project (Gradle)
./gradlew build

# Run locally with 'local' profile
./gradlew bootRun --args='--spring.profiles.active=local'
```
### Docker Build
```bash
# Build Images and Start (MySQL, Redis, App)
docker compose up --build

# Stop and remove containers
docker compose down
```
---
## 테스트 방법
### 모의 데이터를 활용한 플로우 테스트
```bash
# Test
./gradlew test
```
### 실제 데이터를 활용한 플로우 테스트
> 실 데이터를 활용한 플로우 테스트를 위한 데이터 전송기는 다음 레포지토리에서 확인가능합니다. : [Drone Data Tester](https://github.com/seyun4047/drone-platform-trans-tester)   

---
## DB 가이드
### MYSQL DB 사용 가이드
>  MySQL 사용 사이드는 다음 레포지토리에서 확인 가능합니다. : [DB GUIDE](https://github.com/seyun4047/drone-platform-docs/blob/main/components/server/DB_GUIDE.md)
---


---


