Korean version: [https://github.com/seyun4047/drone-platform-server/blob/main/README.kr.md](https://github.com/seyun4047/drone-platform-server/blob/main/README.kr.md)

---

Korean version: [한국어 문서](https://github.com/seyun4047/drone-platform-docs/blob/main/components/server/server.kr.md)

---

# Drone Platform Server

---
## Overview
The Drone Platform Server is the core backend service of the Manufacturer-Independent Drone Platform.<br>

It is responsible for managing the entire lifecycle of drone sessions, authentication, <br>telemetry processing, and high-volume request handling across the platform.<br>

This server acts as the central coordination layer between drones, data storage systems, and external services.

---

## Core Responsibilities

### 1. Drone Authentication & Session Management
The server manages secure and scalable drone sessions using:
- Authorized Drone Database (MySQL)
Maintains the list of registered and approved drones.
- Drone Authentication Tokens (Redis)
Issues and validates access tokens for authenticated drones.
- Drone Heartbeat Tracking (Redis)
Tracks real-time connectivity status using time-indexed heartbeat data.

### 2. Telemetry & Event Processing
The server receives and processes telemetry and event data transmitted from [drone-client](https://github.com/seyun4047/drone-platform-client), including:
- Telemetry Data
Processes real-time operational status data from active drones.
- Event Data
Processes event data, such as Human detection and other mission-triggered activities.

---

## Usage
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
## Test
### Flow Test with Mock Data
```bash
# Test
./gradlew test
```
### Flow Test with Real Data
> If you want to test with real drone data, check it out here: [Drone Data Tester](https://github.com/seyun4047/drone-platform-trans-tester)   

---
## DB QUIDE
### MYSQL DB USAGE QUIDE
>  If you want to know MySQL usage guide, check it out here: [DB GUIDE](https://github.com/seyun4047/drone-platform-docs/blob/main/components/server/DB_GUIDE.md)
---
 