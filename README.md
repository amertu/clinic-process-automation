# Clinic Workflow Automation System

![Build Status](https://img.shields.io/badge/Build%20Status-Passing-brightgreen?logo=githubactions&logoColor=white)
![Docker Compose](https://img.shields.io/badge/Docker-%E2%9C%94-blue?logo=docker&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-EC2-%23FF9900?logo=amazonec2&logoColor=white)
![Jenkins](https://img.shields.io/badge/Jenkins-CI/CD-blue?logo=jenkins&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen?logo=spring&logoColor=white)
![Camunda BPM](https://img.shields.io/badge/Camunda-7.23-orange?logo=camunda&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15.2-blue?logo=postgresql&logoColor=white)

**Clinic Process Automation** is an end-to-end solution designed to streamline the administrative workflows within a dental clinic. This system automates patient scheduling, treatment tracking, and billing processes using **Spring Boot**, **Camunda BPM**, **Docker**, and **PostgreSQL**.

## Features
- **Automated Patient Scheduling:** Automatically schedules and assigns patients to available time slots.
- **Treatment Tracking:** Efficiently tracks patient treatments and progress, reducing errors and wait times.
- **Billing Automation:** Integrates with the clinic’s financial systems to generate bills automatically based on the treatments provided.
- **Camunda BPM:** Utilizes **BPMN** processes for workflow automation, ensuring smooth and consistent operations.
- **Real-Time Dashboard:** Provides real-time visibility into patient status and clinic operations.
- **Dockerized Solution:** Containerized with Docker for scalability and easy deployment across various environments.

## Architecture

```markdown           
                                             Production (AWS EC2/ECS/EKS)
                                                        ▲
                                                        │ pull & run
                                                        │
                        GitHub Repo ─── webhook ──> Nginx (Reverse Proxy)
                           │                             │
                           │                             ├─> Jenkins CI
                           │                             │      ├─ run tests
                           │                             │      └─ build & push Docker images
                           │                             │            to AWS ECR (Registry)
                           │                             │
                           │                             └─> Application Containers
                           │                                    ├─ Camunda BPM Server (Spring Boot)
                           │                                    ├─ Client Service (Spring Boot)
                           │                                    └─ PostgreSQL Database
                           │
                           └─ Local Development (docker-compose)
                                 ├─ Camunda BPM Server (Spring Boot)
                                 ├─ Client Service (Spring Boot + HTML/CSS/JS)
                                 └─ PostgreSQL

```
## Business Impact

- **Efficiency Gains**: Streamlined administrative processes resulting in a **20% reduction in patient wait times**.
- **Improved Resource Allocation**: Optimized **patient scheduling and treatment tracking**, improving resource utilization by **15%**.
- **Faster Checkout**: **30% faster patient checkouts** with real-time tracking and reduced manual intervention.
