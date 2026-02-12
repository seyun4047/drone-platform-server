해당 문서는 gemini-2.5-flash 로 자동 번역되었습니다.<br>정확한 내용은 여기서 확인해주세요: [English Document](https://github.com/seyun4047/drone-platform-server/blob/main/README.md)

---

# 드론 플랫폼 서버

---
## 개요
드론 플랫폼 서버는 제조사 독립적인 드론 플랫폼의 핵심 백엔드 서비스입니다.<br>

이 서버는 드론 세션의 전체 생명 주기 관리, 인증, <br>텔레메트리 처리 및 플랫폼 전반의 대량 요청 처리를 담당합니다.<br>

이 서버는 드론, 데이터 저장 시스템 및 외부 서비스 간의 중앙 조정 계층 역할을 합니다.

---

## 핵심 책임

### 1. 드론 인증 & 세션 관리
서버는 다음을 사용하여 안전하고 확장 가능한 드론 세션을 관리합니다:
- 승인된 드론 데이터베이스 (MySQL)
등록 및 승인된 드론 목록을 유지 관리합니다.
- 드론 인증 토큰 (Redis)
인증된 드론에 대한 액세스 토큰을 발급하고 검증합니다.
- 드론 상태 추적 (Redis)
시간 기반 상태 데이터를 사용하여 실시간 연결 상태를 추적합니다.

### 2. 텔레메트리 & 이벤트 처리
서버는 [drone-client](https://github.com/seyun4047/drone-platform-client)에서 전송된 텔레메트리 및 이벤트 데이터를 수신하고 처리합니다. 여기에는 다음이 포함됩니다:
- 텔레메트리 데이터
활성 드론의 실시간 운영 상태 데이터를 처리합니다.
- 이벤트 데이터
사람 감지 및 기타 임무 트리거 활동과 같은 이벤트 데이터를 처리합니다.

---

## 사용법
### 로컬 빌드
```bash
# 환경 변수 내보내기
export $(cat .env | xargs)

# 프로젝트 빌드 (Gradle)
./gradlew build

# 'local' 프로파일로 로컬에서 실행
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
> 실제 드론 데이터로 테스트하려면 여기에서 확인하십시오: [Drone Data Tester](https://github.com/seyun4047/drone-platform-trans-tester)

---
## DB 가이드
### MySQL DB 사용 가이드
> MySQL 사용 가이드에 대해 알아보려면 여기에서 확인하십시오: [DB GUIDE](https://github.com/seyun4047/drone-platform-docs/blob/main/components/server/DB_GUIDE.md)
---

---

# PROJECT OVERVIEW
# Manufacturer-Independent Drone Platform

---
It is a **manufacturer-independent integrated drone monitoring platform.**

It is designed to manage various drones within a single environment,
enabling both **high-end professional drones and commercially available hobby camera drones**
to be used for lifesaving and disaster response.

---

## Project Structure

This platform consists of multiple independent repositories:

| Component | Description                                       | Repository                                                              |
|---------|---------------------------------------------------|-------------------------------------------------------------------------|
| Server | Core drone platform server (API, Auth, Telemetry) | [GitHub](https://github.com/seyun4047/drone-platform-server)            |
| Monitoring Server | Real-time Drone health check monitoring service   | [GitHub](https://github.com/seyun4047/drone-platform-monitoring-server) |
| Drone Data Tester | Test client for drone telemetry & data simulation | [GitHub](https://github.com/seyun4047/drone-platform-trans-tester)       |
| Drone Client | Drone Data Collection, Transmission & Analysis | [GitHub](https://github.com/seyun4047/drone-platform-client)            |
| Docs | Platform Documents | [GitHub](https://github.com/seyun4047/drone-platform-docs)|

---

## Background

Although custom drones, commercial drones, and consumer drones share similar basic control mechanisms,
their operational methods and **command-and-control structures** in real-world environments vary significantly.

In practice, drones are often utilized as tools that depend heavily on:
- Specific equipment
- Highly trained personnel

Recently, many institutions and companies have attempted to build drone systems integrated with AI technologies.  
However, these systems have clear limitations. They typically rely on tuning specific drone models or operating a single type of custom-built drone, which results in strong dependency on specialized personnel and proprietary technologies.

Such dependency is particularly critical in **life-saving and disaster response operations**.

---
## Project Goal
- A manufacturer-independent drone monitoring platform that supports lifesaving and disaster response operations.

---
## Objectives

- A drone monitoring and management system deployable regardless of drone model or manufacturer
- A system that can be immediately deployed in the field without complex control procedures
- A system that does not rely on the performance capabilities of specific drone hardware
- A system that allows non-professional drone hobbyists to contribute effectively in emergency situations

---

## Expected Impact

In life-saving and disaster response scenarios, before professional equipment or rescue teams arrive on site,  
any available drone—if operable by anyone—can be immediately deployed to:
- Assess victims
- Identify hazards
- Estimate damage

By securing this critical **golden time**, the system enables faster decision-making and more effective deployment of advanced rescue resources, ultimately leading to more sophisticated and impactful drone-assisted emergency response systems.

---

## System Architecture

### Overall System Architecture
<img height="900" alt="Untitled diagram-2026-02-11-182634" src="https://github.com/user-attachments/assets/8842dd09-471e-4a75-8804-674f9cff675a" />


---

## Core System Flows

|                                                                           Auth Logic                                                                            |                                          Control Data From Drone                                          |
|:---------------------------------------------------------------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------:|
|  <img width="450" alt="Redis Token Connection Flow-2026-02-01-182619" src="https://github.com/user-attachments/assets/cf0e6a9e-eeae-4525-aaf1-198c98e61c90" />  | <img src="https://github.com/user-attachments/assets/669647c6-ee30-4bfb-baea-d02e306070ea" width="450"/>  |
|                                                   **Redis-based authentication and connection control flow.**                                                   |                    **Processing of control and telemetry data after authentication.**                     |

|                                             Token Validation                                              |                                             Monitoring Server                                             |
|:---------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------:|
| <img src="https://github.com/user-attachments/assets/456dc993-64a0-4ac8-9138-0f5446aaad07" width="450"/>  |<img width="450" alt="Untitled diagram-2026-02-11-173920" src="https://github.com/user-attachments/assets/6eea1ba2-663d-4bf1-be1d-c729e3bda2f7" />|
|                          **Validation of Redis tokens for incoming drone data.**                          |                              **Periodic drone connection state monitoring.**                             |

---
