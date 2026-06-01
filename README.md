# 🚀 EduTrack – Enterprise Academic Management Platform built using Spring Boot Microservices.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green)
![Angular](https://img.shields.io/badge/Angular-Frontend-red)
![Microservices](https://img.shields.io/badge/Architecture-Microservices-blue)
![JWT](https://img.shields.io/badge/Security-JWT-yellow)


EduTrack is a full-stack Learning Management System (LMS) built using a microservices architecture. The platform is designed to manage learning programs, course content, student enrollments, assessments, and user access control through independent services communicating via Spring Cloud components.

The project demonstrates how enterprise-grade educational platforms can be designed using distributed services, centralized routing, service discovery, and role-based access control.

---

## 📌 Project Overview

EduTrack follows a domain-driven microservices architecture where each business capability is isolated into a dedicated service.

The platform supports three user roles:

* **Admin**
* **Instructor**
* **Student**

Each role interacts with the platform through a secure authentication system and accesses features based on assigned permissions.

All client requests are routed through a centralized API Gateway, which communicates with backend services using Eureka Service Discovery.

---
## Business Capabilities

EduTrack was designed as an Academic Management System that supports the complete learning lifecycle from program creation to assessment evaluation.
---
### Core Functionalities

- User Management with Role-Based Access Control (RBAC)
- Program, Course, and Module Management
- Enrollment and Attendance Tracking
- Learning Content Delivery
- Assessment and Evaluation System
- Progress Monitoring
- Student Performance Tracking
- Quiz and Submission Management
- Academic Analytics Dashboard

The platform enables institutions to manage learning workflows while providing instructors with visibility into student performance and academic outcomes.
---
## 🎯 Key Features

* Microservices-based architecture
* JWT-based authentication and authorization
* API Gateway for centralized routing
* Eureka Service Discovery
* Role-Based Access Control (RBAC)
* Program → Course → Module learning hierarchy
* Enrollment-based content access
* Learning progress tracking
* Dynamic quiz generation
* Automated assessment evaluation
* Randomized question selection
* Secure service-to-service communication
* Academic Management System
* Role-Based Access Control (RBAC)
* Student Performance Analytics
* Interactive Dashboards
* Assessment Evaluation Workflow
* Attendance Tracking
* Progress Monitoring
* RESTful Service Communication
* JWT Authorization
* Enterprise-Ready Architecture

---
## 🚀 Getting Started

### Clone the Repository

```bash
git clone https://github.com/KurvaJeevan/Edutrack-Full-Stack-Microservices-project.git
cd Edutrack-Full-Stack-Microservices-project
```

### Prerequisites

Ensure the following are installed:

* Java 17+
* Maven
* MySQL
* Node.js
* Angular CLI
* Git

### Run the Application

Start the services in the following order:

```text
1. Eureka Discovery Server
2. Authentication Service
3. Course Service
4. Enrollment Service
5. Content Service
6. Assessment Service
7. API Gateway
8. Angular Frontend
```

### Access the Application

```text
Gateway URL:
http://localhost:8050
```

All requests should be routed through the API Gateway.

## 🏗️ System Architecture

```text
                           Client Application
                                   │
                                   ▼
                     ┌─────────────────────────┐
                     │       API Gateway       │
                     │        Port 8050        │
                     └────────────┬────────────┘
                                  │
      ┌───────────────┬───────────┼──────────────┬──────────────┐
      │               │           │              │              │
      ▼               ▼           ▼              ▼              ▼

Authentication   Course      Enrollment     Content      Assessment
   Service       Service       Service       Service       Service

                                  │
                                  ▼

                        Eureka Discovery Server
```

---

## 🔐 Authentication Service

The Authentication Service acts as the primary security layer of the platform.

### Responsibilities

* User Registration
* User Login
* JWT Token Generation
* User Validation
* Role Assignment
* Access Control

After successful authentication, users receive a JWT token which is used to access protected resources.

Although requests are routed through the Gateway Service, token validation is implemented across services to ensure secure access throughout the system.

---

## 📚 Course Service

The Course Service is the core domain service of the application.

It manages the complete learning structure using a three-level hierarchy.

### Learning Structure

```text
Program
   │
   ├── Course
   │      │
   │      └── Module
```

### Example

```text
Java Full Stack Development
│
├── Core Java
│     ├── OOP Concepts
│     ├── Collections
│     └── Exception Handling
│
├── Spring Boot
│     ├── REST APIs
│     ├── Spring Security
│     └── JPA
```

### Responsibilities

* Program Management
* Course Management
* Module Management
* Learning Progress Tracking

A student must enroll in a Program before accessing its Courses and Modules.

---

## 📝 Enrollment Service

The Enrollment Service manages student participation within learning programs.

### Responsibilities

* Program Enrollment
* Enrollment Tracking
* Student Registration Records
* Attendance Management

This service ensures that only enrolled students can access learning content associated with a program.

---

## 📖 Content Service

The Content Service is responsible for managing and delivering learning materials.

### Responsibilities

* Content Creation
* Content Storage
* Content Retrieval
* Learning Resource Management

When a student selects a module, the corresponding learning content is fetched from this service.

---

## 🎯 Assessment Service

The Assessment Service handles quizzes, evaluations, and result processing.

### Assessment Eligibility Rule

Students can attempt an assessment only after completing all modules associated with a course.

This ensures that learners complete the required content before evaluation.

### Dynamic Quiz Generation

The system supports flexible question banks.

For example:

```text
Question Bank Size : 30 Questions
Quiz Size          : 10 Questions
```

The platform randomly selects 10 questions from the available pool whenever a quiz is generated.

This approach:

* Prevents repetition
* Improves assessment fairness
* Creates unique quiz experiences

### Responsibilities

* Question Management
* Quiz Generation
* Assessment Creation
* Submission Evaluation
* Result Processing
* Performance Analysis

---

## 🌐 API Gateway

The API Gateway acts as the single entry point for all client requests.

### Responsibilities

* Request Routing
* Centralized API Access
* Service Resolution
* Communication with Eureka Discovery

### Routing Examples

```text
/api/auth/**          -> Authentication Service
/api/users/**         -> Authentication Service

/api/programs/**      -> Course Service
/api/courses/**       -> Course Service
/api/modules/**       -> Course Service
/api/progress/**      -> Course Service

/api/enrollments/**   -> Enrollment Service
/api/attendance/**    -> Enrollment Service

/api/content/**       -> Content Service

/api/assessments/**   -> Assessment Service
/api/questions/**     -> Assessment Service
/api/submissions/**   -> Assessment Service
/api/analysis/**      -> Assessment Service
```

All requests are processed through the Gateway Service running on Port **8050**.

---

## 👥 User Roles

### Admin

The Admin has complete control over the platform.

#### Permissions

* Create Programs
* Update Programs
* Delete Programs
* Manage Users
* Manage Courses
* Manage Content
* Manage Assessments
* Manage Enrollments

---

### Instructor

The Instructor is responsible for academic content management.

#### Permissions

* Create Programs
* Create Courses
* Create Modules
* Upload Learning Content
* Create Assessments
* Create Question Banks
* Monitor Student Progress

---

### Student

The Student is the learner within the platform.

#### Permissions

* Enroll in Programs
* Access Courses
* View Module Content
* Track Progress
* Attempt Assessments
* View Results

---

## 🛠️ Technology Stack

### Backend

* Java
* Spring Boot
* Spring Security
* Spring Cloud Gateway
* Eureka Discovery Server
* JWT Authentication
* REST APIs

### Frontend

* Angular
* TypeScript
* HTML
* CSS

### Architecture

* Microservices Architecture
* API Gateway Pattern
* Service Discovery Pattern
* Role-Based Access Control (RBAC)

### Build Tools

* Maven
* Git
* GitHub

---

## 📂 Repository Structure

```text
Edutrack-Full-Stack-Microservices-project

├── authentication-service
├── gateway-service
├── discovery-service
├── course-service
├── enrollment-service
├── content-service
├── assessment-service
└── EduTrackFrontEnd
```


---

## 🤝 Team Project

This project was developed as part of a collaborative team effort focused on designing and implementing a Learning Management Platform using Microservices Architecture.

### Organization Repository

https://github.com/EduTrack-MicroServices

The organization repository contains the collective work and contributions from all team members involved in the project.

---

## 🔮 Future Enhancements

* Docker Containerization
* Kubernetes Deployment
* CI/CD Pipeline Integration
* Distributed Logging
* Centralized Configuration Server
* Notification Service
* Recommendation Engine
* Monitoring with Prometheus and Grafana

---

## 👨‍💻 Author

**Jeevan Kumar**

Software Engineer at Cognizant

Interested in:
- Backend Engineering
- Distributed Systems
- Microservices Architecture
- Cloud-Native Applications
- Full Stack Development

GitHub: https://github.com/KurvaJeevan
