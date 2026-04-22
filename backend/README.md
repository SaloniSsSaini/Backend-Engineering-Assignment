# 🚀 Backend Engineering Assignment: Core API & Guardrails

## 📌 Overview

This project is a **high-performance Spring Boot microservice** designed to simulate a social platform backend with strict **guardrails using Redis**.
It ensures safe AI/bot interactions, prevents abuse, and handles high concurrency reliably.

---

## 🧱 Tech Stack

* Java 17
* Spring Boot 3.x
* PostgreSQL
* Redis (Spring Data Redis)
* Redisson (Distributed Locks)
* Micrometer + Prometheus

---

## 🏗 Architecture (Hexagonal / Clean Architecture)

```
Adapter Layer → Controllers, DTOs  
Application Layer → Business Logic (Services)  
Domain Layer → Entities  
Infrastructure → DB + Redis Integration  
```

This separation ensures scalability, maintainability, and testability.

---

## ⚙️ Features

### ✅ Phase 1: Core API

* Create Post → `POST /api/posts`
* Add Comment → `POST /api/posts/{id}/comments`
* Like Post → `POST /api/posts/{id}/like`

---

### ⚡ Phase 2: Redis Virality Engine

#### 📊 Virality Score (Real-time)

Stored in Redis:

```
post:{id}:virality
```

| Action        | Score |
| ------------- | ----- |
| Bot Reply     | +1    |
| Human Like    | +20   |
| Human Comment | +50   |

---

### 🔒 Atomic Guardrails (Concurrency Safe)

#### 1. Horizontal Cap (Bot Limit)

* Max **100 bot comments per post**
* Implemented using **Redis Lua Script (atomic execution)**

---

#### 2. Vertical Cap (Depth Limit)

* Max comment depth = **20**
* Validated before DB write

---

#### 3. Cooldown Cap

* Bot cannot interact repeatedly within 10 minutes

```
cooldown:bot_{id}:post_{id}
```

* Implemented using Redis TTL

---

### 🔔 Phase 3: Notification Engine

#### 🧠 Smart Throttling

* If user already notified → store in Redis list:

```
user:{id}:pending_notifs
```

#### ⏱ CRON Scheduler

* Runs every 5 minutes
* Aggregates notifications
* Logs summary:

```
"Bot X and N others interacted with your posts"
```

---

## 🚀 Concurrency Handling

* Redis Lua Script → ensures atomic operations
* Redisson Distributed Locks → prevents race conditions
* System tested under **200 concurrent requests**

✅ Result: **Bot replies capped exactly at 100 (no overflow)**

---

## 🧪 Testing Strategy

### Race Condition Test

* Simulated 200 bots hitting the same post simultaneously
* Verified strict enforcement of limits

### Statelessness

* No in-memory state (no HashMap/static usage)
* All logic handled via Redis

### Data Integrity

* Redis acts as gatekeeper
* DB writes occur only after validation passes

---

## ⚙️ How to Run (Without Docker)

### 1. Start PostgreSQL

* DB name: `app`
* username: `user`
* password: `pass`

### 2. Start Redis

```
redis-server
```

### 3. Run Project

```
mvn clean install
mvn spring-boot:run
```

---

## 📬 API Examples

### Create Post

```json
POST /api/posts

{
  "authorId": 1,
  "content": "Hello World"
}
```

---

### Add Comment

```json
POST /api/posts/1/comments

{
  "authorId": 2,
  "content": "Nice post!",
  "depthLevel": 1,
  "bot": false
}
```

---

### Like Post

```
POST /api/posts/1/like?userId=2
```

---

## 📊 Metrics

Prometheus endpoint:

```
http://localhost:8080/actuator/prometheus
```

---

## 📦 Deliverables

* ✔ GitHub Repository
* ✔ Postman Collection
* ✔ README

---

## 💡 Key Highlights

* Strong concurrency control using Redis
* Fully stateless microservice
* Clean architecture design
* Real-time scoring system
* Scalable notification batching system

---

## 🧠 Conclusion

This system demonstrates:

* Robust backend design
* Distributed system thinking
* Production-level concurrency handling

It ensures safe, scalable, and efficient handling of user and bot interactions.

---
