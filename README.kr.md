해당 문서는 gemini-2.5-flash 로 자동 번역되었습니다.<br>정확한 내용은 여기서 확인해주세요: [English Document](https://github.com/seyun4047/drone-platform-server/blob/main/README.md)

---

# 드론 플랫폼 서버

---

## 개요
드론 플랫폼 서버는 제조사 독립형 드론 플랫폼의 핵심 백엔드 서비스입니다.<br>

드론 세션의 전체 수명 주기, 인증, 원격 측정(텔레메트리) 처리 및 플랫폼 전반의 대용량 요청 처리를 관리하는 역할을 담당합니다.<br>

이 서버는 드론, 데이터 저장 시스템 및 외부 서비스 간의 중앙 조정 계층 역할을 합니다.

---

## 핵심 책임

### 1. 드론 인증 및 세션 관리
서버는 다음을 사용하여 안전하고 확장 가능한 드론 세션을 관리합니다:
- 인증된 드론 데이터베이스 (MySQL)
등록 및 승인된 드론 목록을 유지합니다.
- 드론 인증 토큰 (Redis)
인증된 드론에 대한 접근 토큰을 발급하고 유효성을 검사합니다.
- 드론 심박수 추적 (Redis)
시간 기반 심박수 데이터를 사용하여 실시간 연결 상태를 추적합니다.

### 2. 원격 측정 및 이벤트 처리
서버는 [drone-client](https://github.com/seyun4047/drone-platform-client)에서 전송되는 원격 측정(텔레메트리) 및 이벤트 데이터를 수신하고 처리합니다. 포함되는 내용은 다음과 같습니다:
- 원격 측정 데이터
활성 드론으로부터 실시간 운영 상태 데이터를 처리합니다.
- 이벤트 데이터
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
> 실제 드론 데이터를 사용하여 테스트하려면 다음을 참조하십시오: [드론 데이터 테스터](https://github.com/seyun4047/drone-platform-trans-tester)

---
## DB 가이드
### MYSQL DB 사용 가이드
> MySQL 사용 가이드에 대해 알고 싶다면 여기를 참조하십시오: [DB 가이드](https://github.com/seyun4047/drone-platform-docs/blob/main/components/server/DB_GUIDE.md)
---


---

# 프로젝트 개요
# 제조사 독립형 드론 플랫폼

---
이것은 **제조사 독립형 통합 드론 모니터링 플랫폼**입니다.

단일 환경에서 다양한 드론을 관리하도록 설계되었으며,
**고급 전문가용 드론과 시판되는 취미용 카메라 드론** 모두
인명 구조 및 재난 대응에 활용될 수 있도록 합니다.

---

## 프로젝트 구조

이 플랫폼은 여러 독립적인 저장소로 구성됩니다:

| Component | 설명 | Repository |
|---|---|---|
| Server | 코어 드론 플랫폼 서버 (API, 인증, 텔레메트리) | [GitHub](https://github.com/seyun4047/drone-platform-server) |
| Monitoring Server | 실시간 드론 상태 점검 모니터링 서비스 | [GitHub](https://github.com/seyun4047/drone-platform-monitoring-server) |
| Drone Data Tester | 드론 텔레메트리 및 데이터 시뮬레이션 테스트 클라이언트 | [GitHub](https://github.com/seyun4047/drone-platform-trans-tester) |
| Drone Client | 드론 데이터 수집, 전송 및 분석 | [GitHub](https://github.com/seyun4047/drone-platform-client) |
| Docs | 플랫폼 문서 | [GitHub](https://github.com/seyun4047/drone-platform-docs)|

---

## 배경

커스텀 드론, 상업용 드론, 소비자용 드론은 기본적인 제어 메커니즘을 공유하지만,
실제 환경에서의 운용 방식과 **명령 및 제어 구조**는 상당히 다릅니다.

실제로 드론은 다음과 같은 요소에 크게 의존하는 도구로 활용되는 경우가 많습니다:
- 특정 장비
- 고도로 훈련된 인력

최근 많은 기관과 기업이 AI 기술과 통합된 드론 시스템을 구축하려고 시도했습니다.
그러나 이러한 시스템에는 명확한 한계가 있습니다. 일반적으로 특정 드론 모델을 튜닝하거나 단일 유형의 맞춤형 드론을 운용하는 데 의존하며, 이는 전문 인력과 독점 기술에 대한 강력한 의존성을 초래합니다.

이러한 의존성은 특히 **인명 구조 및 재난 대응 작전**에서 매우 중요합니다.

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

## 예상 효과

인명 구조 및 재난 대응 시나리오에서 전문 장비나 구조팀이 현장에 도착하기 전에,
누구나 운용할 수 있다면, 사용 가능한 모든 드론을 즉시 배치하여 다음을 수행할 수 있습니다:
- 피해자 평가
- 위험 식별
- 피해 규모 추정

이 중요한 **골든 타임**을 확보함으로써 시스템은 더 빠른 의사 결정과 고급 구조 자원의 더 효과적인 배치를 가능하게 하며, 궁극적으로 더욱 정교하고 영향력 있는 드론 지원 비상 대응 시스템으로 이어집니다.

---

## 시스템 아키텍처

### 전체 시스템 아키텍처
<img height="900" alt="Untitled diagram-2026-02-11-182634" src="https://github.com/user-attachments/assets/8842dd09-471e-4a75-8804-674f9cff675a" />


---

## 핵심 시스템 흐름

| Auth Logic | Control Data From Drone |
|:---:|:---:|
| <img width="450" alt="Redis Token Connection Flow-2026-02-01-182619" src="https://github.com/user-attachments/assets/cf0e6a9e-eeae-4525-aaf1-198c98e61c90" /> | <img src="https://github.com/user-attachments/assets/669647c3-ee30-4bfb-baea-d02e306070ea" width="450"/> |
| **Redis 기반 인증 및 연결 제어 흐름.** | **인증 후 제어 및 텔레메트리 데이터 처리.** |

| Token Validation | Monitoring Server |
|:---:|:---:|
| <img src="https://github.com/user-attachments/assets/456dc993-64a0-4ac8-9138-0f5446aaad07" width="450"/> |<img width="450" alt="Untitled diagram-2026-02-11-173920" src="https://github.com/user-attachments/assets/6eea1ba2-663d-4bf1-be1d-c729e3bda2f7" />|
| **들어오는 드론 데이터에 대한 Redis 토큰 유효성 검사.** | **주기적인 드론 연결 상태 모니터링.** |

---