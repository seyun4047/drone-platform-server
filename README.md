Korean version: [한국어 문서](https://github.com/seyun4047/drone-platform-server/blob/main/README.kr.md)

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
- Authorized Drone Database (MySQL)<br>
Maintains the list of registered and approved drones.
- Drone Authentication Tokens (Redis)<br>
Issues and validates access tokens for authenticated drones.
- Drone Heartbeat Tracking (Redis)<br>
Tracks real-time connectivity status using time-indexed heartbeat data.
- Amazon S3 STS Temporary Credential(AWS)<br>
Generates STS temporary credentials to enable event images to be uploaded to S3.

### 2. Telemetry & Event Processing
The server receives and processes telemetry and event data transmitted from [drone-client](https://github.com/seyun4047/drone-platform-client), including:
- Telemetry Data<br>
Processes real-time operational status data from active drones.
- Event Data<br>
Processes event data, such as Human detection and other mission-triggered activities.

### 3. Dashboard Authentication
The server handles user registration and approval for dashboard access:
- User Registration & Approval Workflow<br>
Users can register for dashboard access. Only approved users can log in.
- Access Control Enforcement<br>
Only authenticated and authorized users can access dashboard APIs, ensuring data integrity and privacy.

### 4. Dashboard API Access
The server provides secure, JWT-based access for the web dashboard and other frontend clients:
- JWT Token Generation & Verification<br>
Upon successful login, the server issues a JWT token which clients must include in API requests.<br>The server validates the token for all protected endpoints.
Secured Dashboard Endpoints<br>
- Fetching alive drones from Redis heartbeat data.
- Retrieving latest telemetry and event data for drones.
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
## Test-Drone Data
### Flow Test with Mock Drone Data
```bash
# Test
./gradlew test
```
### Flow Test with Real Drone Data
> If you want to test with real drone data, check it out here: [Drone Data Tester](https://github.com/seyun4047/drone-platform-trans-tester)   

## Test-Dashboard Auth
### Flow Test with Real User Data
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
## DB GUIDE
### MYSQL DB USAGE GUIDE
>  If you want to know MySQL usage guide, check it out here: [DB GUIDE](https://github.com/seyun4047/drone-platform-docs/blob/main/components/server/DB_GUIDE.md)
---
## AMAZON S3 GUIDE
### S3 & IAM SETUP GUIDE
> If you want to know S3 guide, check it out here: [S3 GUIDE](https://github.com/seyun4047/drone-platform-docs/blob/main/components/server/S3_GUIDE.md)
---
## Dashboard(Front-End) Communication

<div align="center">
<img height="600" alt="AWS Upload Presigned URL-2026-02-13-144904" src="https://github.com/user-attachments/assets/4e956658-5ef2-4c1d-972d-ea669aa09b67" />
</div>


---

<div align="center">
 
# PROJECT OVERVIEW

  <a href="https://youtu.be/7IdtRp_fe1U" target="_blank">
    <img width="900" src="https://github.com/user-attachments/assets/7bc575a8-27f7-4e64-b04e-1e33d4a7848e" alt="MAIN_DRONE_LOGO"/>
  </a>
   <p><strong>Click & Watch the Introduction Video</strong></p> 

---


It is a **Manufacturer-Independent Drone Monitoring Platform.**

It is designed to manage various drones within a single environment, enabling both **high-end professional drones
<br>and commercially available hobby camera drones**
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
| Dashboard | Drone platform's front-end | [GitHub](https://github.com/seyun4047/drone-platform-dashboard)            |
| Docs | Platform Documents, API's | [GitHub](https://github.com/seyun4047/drone-platform-docs)|



---



## Background

</div>

Although custom drones, commercial drones, and consumer drones share similar basic control mechanisms,
<br>their operational methods and **command-and-control structures** in real-world environments vary significantly.

In practice, drones are often utilized as tools that depend heavily on:
- Specific equipment
- Highly trained personnel

Recently, many institutions and companies have attempted to build drone systems integrated with AI technologies.  
However, these systems have clear limitations.
<br>They typically rely on tuning specific drone models or operating a single type of custom-built drone,
<br>which results in strong dependency on specialized personnel and proprietary technologies.

Such dependency is particularly critical in **life-saving and disaster response operations**.

---

<div align="center">

 ## Project Goal

</div>

- A manufacturer-independent drone monitoring platform that supports lifesaving and disaster response operations.

---

<div align="center">
 
## Objectives

</div>

- A drone monitoring and management system deployable regardless of drone model or manufacturer
- A system that can be immediately deployed in the field without complex control procedures
- A system that does not rely on the performance capabilities of specific drone hardware
- A system that allows non-professional drone hobbyists to contribute effectively in emergency situations

---

<div align="center">

 ## Expected Impact

</div>

In life-saving and disaster response scenarios, before professional equipment<br>
or rescue teams arrive on site, any available drone—if operable by anyone—can be immediately deployed to:
- Assess victims
- Identify hazards
- Estimate damage

By securing this critical **golden time**, the system enables faster decision-making<br>
and more effective deployment of advanced rescue resources, ultimately leading to more sophisticated<br>
and impactful drone-assisted emergency response systems.

---
<div align="center">
 
## System Architecture

### Overall System Architecture

<img width="8192" height="6302" alt="AWS Upload Presigned URL-2026-02-20-144917" src="https://github.com/user-attachments/assets/687f81a5-f03c-4f28-acc3-338f4d78a00a" />

---

## Core System Flows
<details>
  <summary>Click to expand</summary>

|                                                                           Auth Logic                                                                            |                                          Control Data From Drone                                          |                                           Drone's Token Validation                                              |                                             Monitoring Server                                             |
|:---------------------------------------------------------------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------:|
|  <img width="450" alt="Auth logic flow" src="https://github.com/user-attachments/assets/cf0e6a9e-eeae-4525-aaf1-198c98e61c90" />  |<img width="450" alt="Control data flow" src="https://github.com/user-attachments/assets/f8c4acf7-0cbe-4ca3-b7fe-6aee4f3e854c" /> | <img alr="Token validation flow" src="https://github.com/user-attachments/assets/456dc993-64a0-4ac8-9138-0f5446aaad07" width="450"/>  |<img width="450" alt="Monitoring server flow" src="https://github.com/user-attachments/assets/6eea1ba2-663d-4bf1-be1d-c729e3bda2f7" />|
|                                                   **Redis-based authentication and connection control flow.**                                                   |                    **Processing of control and telemetry data after authentication.**                     |                         **Validation of Redis tokens for incoming drone data.**                          |                              **Periodic drone connection state monitoring.**                             |

| Back-End <-> Front-End |
|:---:|
| <img width="700" alt="AWS Upload Presigned URL-2026-02-13-144904" src="https://github.com/user-attachments/assets/97c1dbf0-3e24-4b4d-8669-65f076a0ffe5" /> |
| **Communication between Back-End Server and Front-End Dashboard** |

</details>

</div>
