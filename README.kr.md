해당 문서는 gemini-2.5-flash 로 자동 번역되었습니다.<br>정확한 내용은 여기서 확인해주세요: [English Document](https://github.com/seyun4047/drone-platform-server/blob/main/README.md)

---

# 드론 플랫폼 서버

---
## 개요
드론 플랫폼 서버는 제조사 독립형 드론 플랫폼의 핵심 백엔드 서비스입니다.
이 서버는 드론 세션의 전체 수명 주기 관리, 인증, 텔레메트리 처리, 그리고 플랫폼 전반에 걸친 대용량 요청 처리를 담당합니다.
이 서버는 드론, 데이터 저장 시스템 및 외부 서비스 간의 중앙 조정 계층 역할을 합니다.

---

## 주요 책임

### 1. 드론 인증 및 세션 관리
서버는 다음을 사용하여 안전하고 확장 가능한 드론 세션을 관리합니다:
- 인증된 드론 데이터베이스 (MySQL)
  등록 및 승인된 드론 목록을 유지합니다.
- 드론 인증 토큰 (Redis)
  인증된 드론에 대한 접근 토큰을 발급하고 유효성을 검사합니다.
- 드론 하트비트 추적 (Redis)
  시간 색인된 하트비트 데이터를 사용하여 실시간 연결 상태를 추적합니다.

### 2. 텔레메트리 및 이벤트 처리
서버는 [drone-client](https://github.com/seyun4047/drone-platform-client)로부터 전송된 텔레메트리 및 이벤트 데이터를 수신하고 처리하며, 다음을 포함합니다:
- 텔레메트리 데이터
  활성 드론의 실시간 작동 상태 데이터를 처리합니다.
- 이벤트 데이터
  인간 감지 및 기타 임무 트리거 활동과 같은 이벤트 데이터를 처리합니다.

### 3. 대시보드 인증
서버는 대시보드 접근을 위한 사용자 등록 및 승인을 처리합니다:
- 사용자 등록 및 승인 워크플로우
  사용자는 대시보드 접근을 위해 등록할 수 있습니다. 승인된 사용자만 로그인할 수 있습니다.
- 접근 제어 강제
  인증되고 권한이 부여된 사용자만 대시보드 API에 접근할 수 있어 데이터 무결성 및 개인 정보 보호를 보장합니다.

### 4. 대시보드 API 접근
서버는 웹 대시보드 및 기타 프론트엔드 클라이언트를 위한 안전한 JWT 기반 접근을 제공합니다:
- JWT 토큰 생성 및 검증
  로그인 성공 시, 서버는 클라이언트가 API 요청에 포함해야 하는 JWT 토큰을 발급합니다.
  서버는 모든 보호된 엔드포인트에 대해 토큰의 유효성을 검사합니다.
- 보호된 대시보드 엔드포인트
  - Redis 하트비트 데이터로부터 활성 드론 가져오기.
  - 드론의 최신 텔레메트리 및 이벤트 데이터 검색.

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
## 테스트 - 드론 데이터
### 모의 드론 데이터로 흐름 테스트
```bash
# Test
./gradlew test
```
### 실제 드론 데이터로 흐름 테스트
> 실제 드론 데이터로 테스트하려면 다음을 참조하세요: [Drone Data Tester](https://github.com/seyun4047/drone-platform-trans-tester)   

## 테스트 - 대시보드 인증
### 실제 사용자 데이터로 흐름 테스트
```bash
# register
curl -X POST http://<your_url>/dashboard/register \
  -H "Content-Type: application/json" \
  -d '{
        "username": "testid",
        "password": "testpw"
      }'
# return {"status":status,"message":"message","data":{"id":"testid"}}%
```
```bash
# login
curl -X POST http://<your_url>/dashboard/login \
  -H "Content-Type: application/json" \
  -d '{
        "username": "testid",
        "password": "testpw"
      }'
# return {"status":status,"message":"message","data":{"token":"jwt"}}%
```
```bash
# Get Drone Data
 curl -i --location --request GET 'http://<your_url>/api/dashboard/drone/<telemetry_or_event> /<testid>' \
-H "Auth: Bearer <token>"
# return {"data":{},"updatedAt":currentTimeMillis}%
```
 
---
## DB 가이드
### MYSQL DB USAGE GUIDE
> MySQL 사용 가이드를 알고 싶다면 다음을 참조하세요: [DB GUIDE](https://github.com/seyun4047/drone-platform-docs/blob/main/components/server/DB_GUIDE.md)
---

## 대시보드(프론트엔드) 통신
<img height="900" alt="AWS Upload Presigned URL-2026-02-13-144904" src="https://github.com/user-attachments/assets/4e956658-5ef2-4c1d-972d-ea669aa09b67" />

---

# PROJECT OVERVIEW
# Manufacturer-Independent Drone Platform

---
It is a **Manufacturer-Independent Drone Monitoring Platform.**

It is designed to manage various drones within a single environment,
enabling both **high-end professional drones and commercially available hobby camera drones**
to be used for lifesaving and disaster response.

---

## Introduction
- Click & Watch the Introduction Video
[![MAIN-DRONE](https://img.youtube.com/vi/7IdtRp_fe1U/maxresdefault.jpg)](https://youtu.be/7IdtRp_fe1U)

---

## Project Structure

This platform consists of multiple independent repositories:

| Component | Description                                       | Repository                                                              |
|---------|---------------------------------------------------|-------------------------------------------------------------------------|
| Server | Core drone platform server (API, Auth, Telemetry) | [GitHub](https://github.com/seyun4047/drone-platform-server)            |
| Monitoring Server | Real-time Drone health check monitoring service   | [GitHub](https://github.com/seyun4047/drone-platform-monitoring-server) |
| Drone Data Tester | Test client for drone telemetry & data simulation | [GitHub](https://github.com/seyun4047/drone-platform-trans-tester)       |
| Drone Client | Drone Data Collection, Transmission & Analysis | [GitHub](https://github.com/seyun4047/drone-platform-client)            |
| Dashboard | Drone platform's front-end | [GitHub](https://github.com/seyun4047/drone-platform-dashboard)            |
| Docs | Platform Documents, API's | [GitHub](https://github.com/seyun4047/drone-platform-docs)|

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
<img width="900" alt="AWS Upload Presigned URL-2026-02-18-150556" src="https://github.com/user-attachments/assets/0d62033e-cc5c-4809-a666-0eb3c626c08f"/>

---

## Core System Flows

|                                                                           Auth Logic                                                                            |                                          Control Data From Drone                                          |
|:---------------------------------------------------------------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------:|
|  <img width="450" alt="Redis Token Connection Flow-2026-02-01-182619" src="https://github.com/user-attachments/assets/cf0e6a9e-eeae-4525-aaf1-198c98e61c90" />  | <img width="450" alt="Redis Token Connection Flow-2026-02-01-182708" src="https://github.com/user-attachments/assets/a344e0c5-b12a-45ab-951c-0cefcc87bf2b" />
 |
|                                                   **Redis-based authentication and connection control flow.**                                                   |                    **Processing of control and telemetry data after authentication.**                     |

|                                             Token Validation                                              |                                             Monitoring Server                                             |
|:---------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------:|
| <img src="https://github.com/user-attachments/assets/456dc993-64a0-4ac8-9138-0f5446aaad07" width="450"/>  |<img width="450" alt="Untitled diagram-2026-02-11-173920" src="https://github.com/user-attachments/assets/6eea1ba2-663d-4bf1-be1d-c729e3bda2f7" />|
|                          **Validation of Redis tokens for incoming drone data.**                          |                              **Periodic drone connection state monitoring.**                             |

| Back-End <-> Front-End |
|:---:|
| <img width="700" alt="AWS Upload Presigned URL-2026-02-13-144904" src="https://github.com/user-attachments/assets/97c1dbf0-3e24-4b4d-8669-65f076a0ffe5" /> |
| **Communication between Back-End Server and Front-End Dashboard** |
