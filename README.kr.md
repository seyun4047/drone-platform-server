해당 문서는 gemini-2.5-flash 로 자동 번역되었습니다.<br>정확한 내용은 여기서 확인해주세요: [English Document](https://github.com/seyun4047/drone-platform-server/blob/main/README.md)

---

# 드론 플랫폼 서버

---
## 개요
드론 플랫폼 서버는 제조사 독립형 드론 플랫폼의 핵심 백엔드 서비스입니다.<br>
이 서버는 드론 세션의 전체 수명 주기 관리, 인증, 원격 측정 처리, 그리고 플랫폼 전반에 걸친 대용량 요청 처리를 담당합니다.<br>
이 서버는 드론, 데이터 저장 시스템 및 외부 서비스 간의 중앙 조정 계층 역할을 합니다.

---

## 핵심 책임

### 1. 드론 인증 및 세션 관리
서버는 다음을 사용하여 안전하고 확장 가능한 드론 세션을 관리합니다:
- Authorized Drone Database (MySQL)
등록 및 승인된 드론 목록을 유지합니다.
- Drone Authentication Tokens (Redis)
인증된 드론을 위한 접근 토큰을 발급하고 유효성을 검사합니다.
- Drone Heartbeat Tracking (Redis)
시간 기반 하트비트 데이터를 사용하여 실시간 연결 상태를 추적합니다.

### 2. 원격 측정 및 이벤트 처리
서버는 [drone-client](https://github.com/seyun4047/drone-platform-client)에서 전송되는 원격 측정 및 이벤트 데이터를 수신하고 처리합니다. 여기에는 다음이 포함됩니다:
- Telemetry Data
활성 드론으로부터 실시간 운영 상태 데이터를 처리합니다.
- Event Data
인간 감지 및 기타 임무 트리거 활동과 같은 이벤트 데이터를 처리합니다.

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
### Docker 빌드
```bash
# Build Images and Start (MySQL, Redis, App)
docker compose up --build

# Stop and remove containers
docker compose down
```
---
## 테스트
### Mock 데이터를 사용한 흐름 테스트
```bash
# Test
./gradlew test
```
### 실제 데이터를 사용한 흐름 테스트
> 실제 드론 데이터로 테스트하고 싶다면, 여기를 참조하세요: [Drone Data Tester](https://github.com/seyun4047/drone-platform-trans-tester)   

---
## DB 가이드
### MYSQL DB 사용 가이드
> MySQL 사용 가이드를 알고 싶다면, 여기를 참조하세요: [DB GUIDE](https://github.com/seyun4047/drone-platform-docs/blob/main/components/server/DB_GUIDE.md)
---

---

# 프로젝트 개요
# 제조사 독립형 드론 플랫폼

---
이것은 **제조사 독립형 통합 드론 모니터링 플랫폼**입니다.

다양한 드론을 단일 환경에서 관리하도록 설계되었으며,
**고급 전문 드론과 시중에 판매되는 취미용 카메라 드론** 모두를
인명 구조 및 재난 대응에 활용할 수 있도록 지원합니다.

---

## 프로젝트 구조

이 플랫폼은 여러 독립적인 저장소로 구성됩니다:

| 구성 요소 (Component) | 설명 (Description)                                       | 저장소 (Repository)                                                              |
|-------------------|--------------------------------------------------------|-------------------------------------------------------------------------|
| Server            | 핵심 드론 플랫폼 서버 (API, 인증, 텔레메트리) | [GitHub](https://github.com/seyun4047/drone-platform-server)            |
| Monitoring Server | 실시간 드론 상태 점검 모니터링 서비스   | [GitHub](https://github.com/seyun4047/drone-platform-monitoring-server) |
| Drone Data Tester | 드론 텔레메트리 및 데이터 시뮬레이션 테스트 클라이언트 | [GitHub](https://github.com/seyun4047/drone-platform-trans-tester)       |
| Drone Client      | 드론 데이터 수집, 전송 및 분석 | [GitHub](https://github.com/seyun4047/drone-platform-client)            |
| Docs              | 플랫폼 문서 | [GitHub](https://github.com/seyun4047/drone-platform-docs)|

---

## 배경

커스텀 드론, 상업용 드론, 소비자용 드론은 기본적인 제어 메커니즘이 유사함에도 불구하고,
실제 환경에서의 작동 방식과 **명령 및 제어 구조**는 크게 다릅니다.

실제로 드론은 다음과 같은 요소에 크게 의존하는 도구로 활용되는 경우가 많습니다:
- 특정 장비
- 고도로 훈련된 인력

최근 많은 기관과 기업이 AI 기술과 통합된 드론 시스템을 구축하려 시도했습니다.
그러나 이러한 시스템에는 명확한 한계가 있습니다. 일반적으로 특정 드론 모델을 튜닝하거나 단일 유형의 맞춤형 드론을 운영하는 방식에 의존하여, 전문 인력과 독점 기술에 대한 의존성이 강하게 나타납니다.

이러한 의존성은 **인명 구조 및 재난 대응 작전**에서 특히 중요합니다.

---
## 프로젝트 목표
- 인명 구조 및 재난 대응 작전을 지원하는 제조사 독립형 드론 모니터링 플랫폼.

---
## 목표

- 드론 모델이나 제조사에 관계없이 배포 가능한 드론 모니터링 및 관리 시스템
- 복잡한 제어 절차 없이 현장에 즉시 배포할 수 있는 시스템
- 특정 드론 하드웨어의 성능 기능에 의존하지 않는 시스템
- 비전문 드론 취미 사용자도 비상 상황에서 효과적으로 기여할 수 있도록 하는 시스템

---

## 기대 효과

인명 구조 및 재난 대응 시나리오에서, 전문 장비나 구조팀이 현장에 도착하기 전에,
누구라도 조작 가능한 드론이 있다면 즉시 투입하여 다음을 수행할 수 있습니다:
- 희생자 평가
- 위험 식별
- 피해 규모 추정

이 중요한 **골든 타임**을 확보함으로써, 시스템은 더 빠른 의사 결정과 고급 구조 자원의 더 효과적인 배치를 가능하게 하며, 궁극적으로 더 정교하고 영향력 있는 드론 지원 비상 대응 시스템으로 이어집니다.

---

## 시스템 아키텍처

### 전체 시스템 아키텍처
<img height="900" alt="Untitled diagram-2026-02-11-182634" src="https://github.com/user-attachments/assets/8842dd09-471e-4a75-8804-674f9cff675a" />


---

## 핵심 시스템 흐름

|                                                                           인증 로직 (Auth Logic)                                                                            |                                          드론으로부터의 제어 데이터 (Control Data From Drone)                                          |
|:---------------------------------------------------------------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------:|
|  <img width="450" alt="Redis Token Connection Flow-2026-02-01-182619" src="https://github.com/user-attachments/assets/cf0e6a9e-eeae-4525-aaf1-198c98e61c90" />  | <img width="450" alt="Redis Token Connection Flow-2026-02-01-182708" src="https://github.com/user-attachments/assets/a344e0c5-b12a-45ab-951c-0cefcc87bf2b" />
 |
|                                                   **Redis 기반 인증 및 연결 제어 흐름.**                                                   |                    **인증 후 제어 및 텔레메트리 데이터 처리.**                     |

|                                             토큰 유효성 검사 (Token Validation)                                              |                                             모니터링 서버 (Monitoring Server)                                             |
|:---------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------:|
| <img src="https://github.com/user-attachments/assets/456dc993-64a0-4ac8-9138-0f5446aaad07" width="450"/>  |<img width="450" alt="Untitled diagram-2026-02-11-173920" src="https://github.com/user-attachments/assets/6eea1ba2-663d-4bf1-be1d-c729e3bda2f7" />|
|                          **수신되는 드론 데이터에 대한 Redis 토큰 유효성 검사.**                          |                              **주기적인 드론 연결 상태 모니터링.**                             |
