# EasyBank Microservices Architecture

This project is a microservices-based implementation of a banking application called EasyBank. It was originally developed as a Maven project and is now being migrated to Gradle.

## Architecture Overview

The application follows a microservices architecture pattern with the following components:

- **API Gateway**: Entry point for all client requests
- **Service Discovery**: For service registration and discovery
- **Configuration Server**: Centralized configuration management
- **Business Services**: Core banking functionality
- **Messaging**: Notification service for emails and SMS

## Technology Stack

- **Java**: Version 21
- **Spring Boot**: Version 3.4.5
- **Spring Cloud**: Version 2024.0.0
- **Database**: H2 (for development)
- **API Documentation**: SpringDoc OpenAPI
- **Build Tool**: Gradle
- **Containerization**: Docker with JIB
- **Orchestration**: Kubernetes with Helm charts
- **Monitoring**: Grafana, Prometheus, Loki, Tempo

## Microservices

### Business Services

1. **Accounts Service**
   - Manages customer bank accounts
   - Provides account creation, retrieval, and management functionality
   - API Documentation: `/swagger-ui.html`

2. **Cards Service**
   - Manages credit and debit cards
   - Provides card issuance, activation, and management
   - API Documentation: `/swagger-ui.html`

3. **Loans Service**
   - Manages loan products and applications
   - Provides loan application, approval, and management
   - API Documentation: `/swagger-ui.html`

4. **Message Service**
   - Handles notifications via email and SMS
   - Processes account-related events for customer communication

### Infrastructure Services

1. **Config Server**
   - Centralized configuration management
   - Externalized configuration for all microservices

2. **Eureka Server**
   - Service discovery and registration
   - Allows services to find and communicate with each other

3. **Gateway Server**
   - API Gateway using Spring Cloud Gateway
   - Routes requests to appropriate services
   - Implements circuit breaker pattern for fault tolerance
   - Provides rate limiting to protect services
   - Handles request rewriting and load balancing

## Setup and Installation

### Prerequisites

- Java 17 or higher
- Gradle 7.x or higher
- Docker (for containerization)
- Kubernetes (for deployment)
- Helm (for Kubernetes package management)

### Building the Project

```bash
./gradlew clean build
```

### Running Locally

1. Start the infrastructure services first:

```bash
./gradlew :apps:configserver:bootRun
./gradlew :apps:eurekaserver:bootRun
./gradlew :apps:gatewayserver:bootRun
```

2. Start the business services:

```bash
./gradlew :apps:accounts:bootRun
./gradlew :apps:cards:bootRun
./gradlew :apps:loans:bootRun
./gradlew :apps:message:bootRun
```

## Deployment

### Docker Containers

Build Docker images for all services:

```bash
./gradlew jibDockerBuild
```

### Kubernetes Deployment

The project includes Helm charts for deploying to Kubernetes:

```bash
helm install eazybank-common ./helm/eazybank-common
helm install eazybank-services ./helm/eazybank-services
```

### Environment-specific Deployments

The project supports different environments:

- Development: `./helm/environments/dev-env`
- QA: `./helm/environments/qa-env`
- Production: `./helm/environments/prod-env`

## Monitoring

The project includes Helm charts for deploying monitoring tools:

- Grafana: `./helm/grafana`
- Prometheus: `./helm/kube-prometheus`
- Loki (for logs): `./helm/grafana-loki`
- Tempo (for tracing): `./helm/grafana-tempo`

## API Documentation

Each service provides its own Swagger UI for API documentation:

- Accounts: `http://localhost:8080/eazybank/accounts/swagger-ui.html`
- Cards: `http://localhost:8080/eazybank/cards/swagger-ui.html`
- Loans: `http://localhost:8080/eazybank/loans/swagger-ui.html`

## Project Structure

```
easy-bank-msa/
├── apps/
│   ├── accounts/         # Accounts microservice
│   ├── cards/            # Cards microservice
│   ├── configserver/     # Configuration server
│   ├── eazy-bom/         # Bill of Materials for dependency management
│   ├── eurekaserver/     # Service discovery server
│   ├── gatewayserver/    # API Gateway
│   ├── loans/            # Loans microservice
│   └── message/          # Notification service
├── helm/                 # Kubernetes Helm charts
│   ├── eazybank-common/  # Common infrastructure
│   ├── eazybank-services/# Microservices deployment
│   ├── environments/     # Environment-specific configurations
│   ├── grafana/          # Monitoring dashboards
│   ├── grafana-loki/     # Log aggregation
│   ├── grafana-tempo/    # Distributed tracing
│   ├── kafka/            # Event streaming platform
│   ├── keycloak/         # Identity and access management
│   └── kube-prometheus/  # Monitoring and alerting
└── kubernetes/           # Kubernetes manifests
```

## Contributing

Please read the contribution guidelines before submitting pull requests.

## License

This project is licensed under the Apache License 2.0 - see the LICENSE file for details.
