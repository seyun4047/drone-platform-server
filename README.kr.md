Original English Document: [https://github.com/seyun4047/drone-platform-server/blob/main/README.md](https://github.com/seyun4047/drone-platform-server/blob/main/README.md)

---

해당 문서는 gemini-2.5-flash-lite 로 자동 번역되어 부자연스러운 부분이 있을 수 있으니,<br>정확한 내용을 여기서 확인해주세요. : [영어 문서](https://github.com/seyun4047/drone-platform-{component}/blob/main/README.md)

---

# 드론 플랫폼 서버

---
## 개요
드론 플랫폼 서버는 제조사 독립적인 드론 플랫폼의 핵심 백엔드 서비스입니다.<br>

드론 세션의 전체 수명 주기 관리, 인증, <br>원격 측정 데이터 처리 및 플랫폼 전반에 걸친 대규모 요청 처리를 담당합니다.<br>

이 서버는 드론, 데이터 스토리지 시스템 및 외부 서비스 간의 중앙 조정 계층 역할을 합니다.

---

## 핵심 책임

### 1. 드론 인증 및 세션 관리
서버는 다음을 사용하여 안전하고 확장 가능한 드론 세션을 관리합니다.
- 승인된 드론 데이터베이스 (MySQL)
등록되고 승인된 드론 목록을 유지 관리합니다.
- 드론 인증 토큰 (Redis)
인증된 드론에 대한 액세스 토큰을 발급하고 유효성을 검사합니다.
- 드론 하트비트 추적 (Redis)
시간 기반 하트비트 데이터를 사용하여 실시간 연결 상태를 추적합니다.

### 2. 원격 측정 및 이벤트 처리
서버는 [drone-client](https://github.com/seyun4047/drone-platform-client)에서 전송된 원격 측정 및 이벤트 데이터를 수신하고 처리합니다. 여기에는 다음이 포함됩니다.
- 원격 측정 데이터
활성 드론의 실시간 운영 상태 데이터를 처리합니다.
- 이벤트 데이터
인간 감지 및 기타 임무 트리거 활동과 같은 이벤트 데이터를 처리합니다.

---

## 사용법
### 로컬 빌드
```bash
# 환경 변수 내보내기
export $(cat .env | xargs)

# 프로젝트 빌드 (Gradle)
./gradlew build

# 'local' 프로필로 로컬에서 실행
./gradlew bootRun --args='--spring.profiles.active=local'
```
### Docker 빌드
```bash
# 이미지 빌드 및 시작 (MySQL, Redis, App)
docker compose up --build

# 컨테이너 중지 및 제거
docker compose down
```
---
## 테스트
### 모의 데이터를 사용한 흐름 테스트
```bash
# 테스트
./gradlew test
```
### 실제 데이터를 사용한 흐름 테스트
> 실제 드론 데이터로 테스트하려면 다음을 확인하십시오. [드론 데이터 테스터](https://github.com/seyun4047/drone-platform-trans-tester)

---
## DB 가이드
### MySQL DB 사용 가이드
> MySQL 사용 가이드에 대해 알아보려면 다음을 확인하십시오. [DB 가이드](https://github.com/seyun4047/drone-platform-docs/blob/main/components/server/DB_GUIDE.md)
---