# Clinic Workflow Automation System
End-to-end BPMN-driven system for streamlining dental clinic operations. Automates appointment handling, treatment tracking, lab coordination, and billing using containerized services, event-driven architecture, and Kubernetes orchestration.

[![Build](https://img.shields.io/endpoint?url=https://raw.githubusercontent.com/amertu/clinic-process-automation/main/resources/build-status.json&label=build&green&logo=github&color=briightgreen)](https://img.shields.io/endpoint?url=https://raw.githubusercontent.com/amertu/clinic-process-automation/main/resources/build-status.json)

![Docker Compose](https://img.shields.io/badge/Docker-25.0.8-blue?logo=docker&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-EC2-%23FF9900?logo=amazonec2&logoColor=white)
![Jenkins](https://img.shields.io/badge/Jenkins-2.507-blue?logo=jenkins&logoColor=white)
![Java](https://img.shields.io/badge/Java-17-red?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen?logo=spring&logoColor=white)
![Camunda BPM](https://img.shields.io/badge/Camunda-7.23-orange?logo=camunda&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15.2-blue?logo=postgresql&logoColor=white)
![Kafka](https://img.shields.io/badge/Kafka-3.5.0-black?logo=apachekafka&logoColor=white)
![Kubernetes](https://img.shields.io/badge/Kubernetes-1.30-blue?logo=kubernetes&logoColor=white)

## Features
- **Modular BPMN Workflows:** Developed Camunda-based workflows to automate patient processes and optimize resource management.
- **Event-Based Process Integration:** Kafka used for loosely coupled communication between clinic and lab workflows.
- **CI/CD & Orchestration:** Jenkins pipelines with AWS (ECR, EC2, EKS) and Kubernetes for scalable deployments.
- **BPMN Workflow Engine:** Camunda for transparent, model-driven execution and lifecycle management.

## Architecture

```markdown           
                +---------------------------------------------------------------+
                |                         GitHub Repository                     |
                +---------------------------+-----------------------------------+
                                            |
                                            | webhook
                                            ▼
                +---------------------------------------------------------------+
                |                      Nginx (Reverse Proxy / Ingress)          |
                +---------------------------+-----------------------------------+
                                            |
                                            +----------------------------+
                                            |                            |
                                    +-------▼-------+            +-------▼-------+
                                    | Jenkins CI    |            |  AWS ECR      |
                                    | - Run Tests   |            |  (Docker      |
                                    | - Build &     |            |   Registry)   |
                                    |   Push Images |            +---------------+
                                    +-------+-------+
                                            |
                                            | Deploy Docker Images
                                            ▼
                +---------------------------------------------------------------+
                |                 AWS EKS Kubernetes Cluster                    |
                |                                                               |
                |  +---------------------+   +---------------------+            |
                |  | Deployment:         |   | Deployment:         |            |
                |  | Camunda BPM Server  |   | Client Service      |            |
                |  | (Spring Boot)       |   | (Spring Boot + UI)  |            |
                |  +---------------------+   +---------------------+            |
                |                                                               |
                |  +---------------------+   +---------------------+            |
                |  | Deployment:         |   | Deployment:         |            |
                |  | Kafka Broker(s)     |   | Zookeeper           |            |
                |  | (Containers)        |   | (Containers)        |            |
                |  +---------------------+   +---------------------+            |
                |                                                               |
                |  +---------------------+                                      |
                |  | Deployment:         |                                      |
                |  | PostgreSQL          |                                      |
                |  | (StatefulSet + PV)  |                                      |
                |  +---------------------+                                      |
                |                                                               |
                |  +---------------------------------------------------------+  |
                |  | Services: Internal Service Communication & Kafka Topics |  |
                |  +---------------------------------------------------------+  |
                +---------------------------------------------------------------+
                                             ▲
                                             │
                                    Local developer
                                    triggers GitHub push
                                             │
                         +-------------------------------------------+
                         | Local Development (docker-compose)        |
                         +-------------------------------------------+
                         | Camunda BPM Server (Spring Boot)          |
                         | Client Service (Spring Boot + HTML/CSS/JS)|
                         | Kafka + Zookeeper (Container)             |
                         | PostgreSQL (Container)                    | 
                         +-------------------------------------------+

```