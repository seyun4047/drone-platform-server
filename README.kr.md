
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

# PROJECT OVERVIEW
# Manufacturer-Independent Drone Platform
---
It is a **manufacturer-independent integrated drone monitoring platform!**

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
| <img src="https://github.com/user-attachments/assets/456dc993-64a0-4ac8-9138-0f5446aaad07" width="450"/>  |<img width="450" alt="Untitled diagram-2026-02-11-173920" src="https://github.com/user-attachments/assets/6eea1ba2-663d-4bf1-be1d-c729e3bda2f7" />
  |
|                          **Validation of Redis tokens for incoming drone data.**                          |                              **Periodic drone connection state monitoring.**                              |
