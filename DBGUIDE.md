# DB SETUP
## 1. ENTER THE MYSQL AS ROOT
```bash
docker exec -it drone-platform-mysql mysql -u root -p
```
## 2. CREATE DB
```bash
CREATE DATABASE drone;
```
## 3. CREATE USER
```bash
CREATE USER IF NOT EXISTS 'YOUR_USERNAME'@'%' IDENTIFIED BY 'YOUR_PASSWORD';
GRANT ALL PRIVILEGES ON drone.* TO 'YOUR_USERNAME'@'%';
FLUSH PRIVILEGES;
```
## 4. ENTER THE MYSQL AS YOUR ACCOUNT
```bash
docker exec -it drone-platform-mysql mysql -u YOUR_MYSQL_USERNAME -p
```

## 5. CREATE DRONE DB
```bash
CREATE DATABASE IF NOT EXISTS drone;
```
## 6. USE DB
```bash
USE drone;
```
## 7. CREATE DRONE TABLE
```bash
CREATE TABLE drone (
id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
serial VARCHAR(50) NOT NULL UNIQUE,
name VARCHAR(50) NOT NULL,
recent_log VARCHAR(255) DEFAULT NULL,
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
level INT NOT NULL DEFAULT 0,
connecting TINYINT(1) NOT NULL DEFAULT 0,
log_path VARCHAR(255) NOT NULL DEFAULT '/',
last_connect_time TIMESTAMP DEFAULT NULL,
PRIMARY KEY (id)
);
```
## 8. INSERT ACCESS DRONE
```bash
insert into drone(serial, name, recent_log, level, connecting) values('test-serial','drone-test1020','X',1,false);
```