# Manufacturer-Independent Drone Platform

The project aims to be a **manufacturer-independent drone platform**, connecting various drone devices with standardized interfaces and performing **Redis-based authentication and status management**.

---

## Overview

This project focuses on building a universal drone control and monitoring platform that can operate independently of drone manufacturers and hardware-specific constraints.

---

## Background

Although custom drones, commercial drones, and consumer drones share similar basic control mechanisms,  
their operational methods and **command and control structures** in real-world environments vary significantly.

In practice, drones are often utilized as tools that depend heavily on:
- Specific equipment
- Highly trained personnel

Recently, many institutions and companies have attempted to build drone systems integrated with AI technologies.  
However, these systems have clear limitations. They typically rely on tuning specific drone models or operating a single type of custom-built drone, which results in strong dependency on specialized personnel and proprietary technologies.

Such dependency is particularly critical in **life-saving and disaster response operations**.

---

## Project Goal

Based on this problem, the goal of this project is defined as follows:

> **A universal drone control and monitoring platform that enables immediate deployment of any camera-equipped drone—including consumer drones—for life-saving and disaster response missions.**

---

## Objectives

- A drone control and monitoring system deployable regardless of drone model or manufacturer
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

## Architecture

### Overall System Architecture

![Overall System Architecture](https://github.com/user-attachments/assets/2024b209-8287-4acf-bcb2-af9d57c9e743)

---

### 1. Auth Logic

This component defines the authentication and connection control flow using Redis-based token management.

![Auth Logic](https://github.com/user-attachments/assets/05b8c887-8fbc-4e45-9aeb-9a1486228f63)

---

### 2. Control Data From Drone

This flow describes how control and telemetry data are received and processed from drones after authentication.

![Control Data From Drone](https://github.com/user-attachments/assets/f2ec9ecc-cfca-4d9d-b95f-127644897329)

---

### 3. Token Validation for Data

This process validates Redis tokens for incoming drone data to ensure integrity and authenticity.

![Token Validation For Data](https://github.com/user-attachments/assets/65a040af-ae23-48ec-9344-8938a9e49704)

---

### 4. Drone State Monitoring Server

The monitoring server periodically checks drone connection states and maintains system consistency.

![Drone State Monitoring Server](https://github.com/user-attachments/assets/d4c4462f-f6eb-48e5-943b-5a1d15b440e3)
