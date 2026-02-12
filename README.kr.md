해당 문서는 gemini-2.5-flash 로 자동 번역되었습니다.<br>정확한 내용은 여기서 확인해주세요: [English Document](https://github.com/seyun4047/drone-platform-server/blob/main/README.md)

---

# 드론 플랫폼 서버

---
## 개요
드론 플랫폼 서버는 제조사 독립형 드론 플랫폼의 핵심 백엔드 서비스입니다.<br>

이 서버는 드론 세션의 전체 수명 주기, 인증, <br>텔레메트리 처리, 그리고 플랫폼 전반의 대규모 요청 처리를 담당합니다.<br>

이 서버는 드론, 데이터 저장 시스템 및 외부 서비스 간의 중앙 조정 계층 역할을 합니다.

---

## 핵심 책임

### 1. 드론 인증 및 세션 관리
서버는 다음을 사용하여 안전하고 확장 가능한 드론 세션을 관리합니다:
- 승인된 드론 데이터베이스 (MySQL)
  등록 및 승인된 드론 목록을 유지합니다.
- 드론 인증 토큰 (Redis)
  인증된 드론에 대한 접근 토큰을 발급하고 유효성을 검사합니다.
- 드론 Heartbeat 추적 (Redis)
  시간으로 색인된 Heartbeat 데이터를 사용하여 실시간 연결 상태를 추적합니다.

### 2. 텔레메트리 및 이벤트 처리
서버는 [drone-client](https://github.com/seyun4047/drone-platform-client)에서 전송되는 텔레메트리 및 이벤트 데이터를 수신하고 처리하며, 다음을 포함합니다:
- 텔레메트리 데이터
  활성 드론의 실시간 운영 상태 데이터를 처리합니다.
- 이벤트 데이터
  사람 감지 및 기타 임무 트리거 활동과 같은 이벤트 데이터를 처리합니다.

---

## 사용법
### 로컬 빌드
```bash
# Export env
export $(cat .env | xargs)

# Build the project (Gradle)
./gradlew build

# Run locally with 'local' profile
./gradlew bootRun --args='--spring.profiles.active=local'
```
### 도커 빌드
```bash
# Build Images and Start (MySQL, Redis, App)
docker compose up --build

# Stop and remove containers
docker compose down
```
---
## 테스트
### 목(Mock) 데이터로 플로우 테스트
```bash
# Test
./gradlew test
```
### 실제 데이터로 플로우 테스트
> 실제 드론 데이터로 테스트하려면 여기를 확인하세요: [Drone Data Tester](https://github.com/seyun4047/drone-platform-trans-tester)   

---
## DB 가이드
### MYSQL DB 사용 가이드
> MySQL 사용 가이드를 알고 싶다면 여기를 확인하세요: [DB GUIDE](https://github.com/seyun4047/drone-platform-docs/blob/main/components/server/DB_GUIDE.md)
---


---

