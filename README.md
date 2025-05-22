# 🎓 SchoolLang — Microservices-Based Language School Platform

**SchoolLang** is a microservices-based web application designed to manage language courses, teachers, students, and payments. It offers features such as course registration, automatic lesson scheduling based on start date, number of sessions, and preferred time slots (morning, afternoon, evening). The system integrates with Keycloak for authentication and PayU for payment processing.

> 🚀 Live demo: [https://frontend-angular-repo-production-3d55.up.railway.app](https://frontend-angular-repo-production-3d55.up.railway.app)

---

## 🧩 Features

### 👨‍🎓 Students:
- User registration and login with Keycloak
- Browse and enroll in available courses
- Browse personal course schedules
- Process payments with PayU

### 👨‍🏫 Teachers:
- Manage assigned courses and schedules
- Browse student enrollments

### 🛠️ Administrators:
- Create and manage courses
- Assign teachers to courses
- Monitor payments and salaries

---

## 🧪 Used technologies

**Backend:**
- Java
- Spring Boot (microservice architecture)
- Spring Cloud (Eureka, Gateway)
- MySQL and MongoDB
- Keycloak (authentication and authorization)
- PayU (payment integration)
- Docker and Docker Compose

**Frontend:**
- Angular
- TypeScript
- RxJS
- SCSS

**DevOps / Deployment:**
- Railway (deployment platform)
- Docker

---

## 🧱 Architecture Overview

The application consists of multiple microservices:

- `calendar-service`: Manages lesson creation and generates schedules
- `course-service`: Manages course creation and enrollment
- `email-service`: Sends notifications and confirmations
- `eureka-service`: Service registry for microservices
- `gateway-service`: API gateway for routing requests
- `keycloak-service`: Authentication server based on Keycloak for managing users, roles, and access tokens
- `keycloak-client-service`: Communicates with Keycloak to register and manage application users
- `order-service`: Handles course enrollment
- `payment-service`: Processes payments via PayU
- `salary-service`: Calculates and manages teacher salaries
- `student-service`: Handles student information
- `teacher-service`: Manages teacher data