# SplitLoop Backend

Backend REST de **SplitLoop**, una aplicación para gestionar gastos compartidos entre usuarios y grupos.

El proyecto está construido con **Java y Spring Boot** y cubre funcionalidades como autenticación, gestión de usuarios y grupos, gastos compartidos, gastos recurrentes, pagos y cálculo de balances.

El objetivo del proyecto es aplicar una arquitectura backend mantenible y orientada al dominio, separando responsabilidades y manteniendo la lógica de negocio aislada de la infraestructura.

---

## ✨ Features

### 🔐 Authentication & Security

* Registro de usuarios
* Autenticación de usuarios
* JWT access tokens
* Refresh tokens
* Integración con Spring Security
* Protección de endpoints mediante autenticación y autorización

### 👥 Users & Groups

* Gestión de usuarios
* Creación y gestión de grupos
* Gestión de miembros de un grupo
* Relación entre usuarios y grupos
* Gestión de gastos compartidos dentro de grupos

### 💸 Expenses

* Creación y gestión de gastos
* División de gastos entre participantes
* Gestión de participantes
* Cálculo de cantidades individuales
* Gastos recurrentes
* Generación automática de ocurrencias de gastos recurrentes

### ⚖️ Balances

* Cálculo de balances entre usuarios
* Seguimiento de cantidades pendientes
* Gestión de cantidades que cada usuario debe recibir o pagar
* Relación entre obligaciones y pagos

### 💳 Payments

* Registro de pagos
* Gestión de obligaciones de pago
* Seguimiento de pagos pendientes
* Registro de obligaciones saldadas

---

# 🏗️ Architecture

SplitLoop utiliza una **arquitectura orientada al dominio (domain-oriented architecture)**.

En lugar de organizar toda la aplicación exclusivamente por capas técnicas, cada dominio mantiene sus propias responsabilidades y componentes.

```text
src/main/java/com/example/SplitLoop
│
├── auth/
│   ├── application/
│   ├── controller/
│   ├── domain/
│   └── exception/
│
├── balance/
│
├── common/
│
├── expense/
│   ├── application/
│   ├── controller/
│   ├── domain/
│   ├── exception/
│   ├── infrastructure/
│   ├── mapper/
│   └── scheduler/
│
├── group/
│   ├── application/
│   ├── controller/
│   ├── domain/
│   ├── exception/
│   └── mapper/
│
├── payment/
│
├── user/
│
└── SplitLoopApplication.java
```

### Domain-oriented approach

Cada dominio es responsable de sus propias reglas de negocio y casos de uso.

Por ejemplo:

```text
Expense
│
├── Controller
│
├── Application
│   └── Business logic / use cases
│
├── Domain
│   └── Entities / domain rules
│
├── Infrastructure
│   └── Persistence / external concerns
│
├── Mapper
│
├── Exception
│
└── Scheduler
    └── Recurring expenses
```

Esta organización permite mantener el código desacoplado y facilita la incorporación de nuevos dominios y funcionalidades.

---

# 🧩 Main Domains

Los principales dominios de la aplicación son:

```text
                    ┌─────────────┐
                    │    User     │
                    └──────┬──────┘
                           │
                           ▼
                    ┌─────────────┐
                    │    Group    │
                    └──────┬──────┘
                           │
             ┌─────────────┼─────────────┐
             ▼             ▼             ▼
        ┌─────────┐   ┌──────────┐   ┌─────────┐
        │ Expense │   │ Balance  │   │ Payment │
        └────┬────┘   └──────────┘   └─────────┘
             │
             ▼
        ┌──────────────┐
        │  Recurring   │
        │   Expenses   │
        └──────────────┘
```

La aplicación modela las principales relaciones necesarias para gestionar gastos compartidos y calcular las cantidades que cada usuario debe pagar o recibir.

---

# 🛠️ Tech Stack

| Technology                  | Purpose                        |
| --------------------------- | ------------------------------ |
| **Java 26**                 | Backend language               |
| **Spring Boot 4**           | Application framework          |
| **Spring Web MVC**          | REST API                       |
| **Spring Data JPA**         | Data persistence               |
| **Hibernate**               | ORM                            |
| **Spring Security**         | Authentication & authorization |
| **JJWT**                    | JWT handling                   |
| **PostgreSQL 17**           | Relational database            |
| **Flyway**                  | Database migrations            |
| **MapStruct**               | DTO mapping                    |
| **Lombok**                  | Boilerplate reduction          |
| **Docker / Docker Compose** | Development infrastructure     |
| **Testcontainers**          | Integration testing            |
| **Springdoc OpenAPI**       | API documentation              |
| **JaCoCo**                  | Test coverage                  |
| **SonarQube**               | Code quality analysis          |
| **Maven**                   | Build & dependency management  |

---

# 🔐 Authentication & Security

SplitLoop utiliza **JWT** para la autenticación y está integrado con **Spring Security**.

El flujo principal de autenticación es:

```text
                    Login
                      │
                      ▼
              Authentication
                      │
                      ▼
               Access Token
                      │
                      ▼
             Protected endpoints
                      │
                      │
                      ▼
                Refresh Token
```

Los access tokens se utilizan para acceder a los endpoints protegidos, mientras que los refresh tokens permiten obtener nuevos access tokens sin volver a introducir las credenciales.

Los secretos y credenciales se gestionan mediante variables de entorno.

Ejemplo de configuración local:

```env
POSTGRES_USER=postgres
POSTGRES_PASSWORD=your_password
POSTGRES_DB=spring

DATABASE_URL=jdbc:postgresql://localhost:5433/spring
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=your_password

JWT_SECRET=your_jwt_secret
JWT_EXPIRATION=86400000
JWT_REFRESH_EXPIRATION=604800000
```

> **Never commit real credentials, JWT secrets, API keys or other sensitive information to the repository.**

El archivo `.env` debe permanecer fuera del control de versiones:

```gitignore
.env
```

---

# 💸 Expense Management

Los gastos son uno de los principales dominios de SplitLoop.

Un gasto puede estar asociado a un grupo y contar con diferentes participantes, permitiendo representar cómo debe dividirse el importe.

Conceptualmente:

```text
Expense
   │
   ├── Group
   │
   ├── Payer
   │
   └── Participants
          │
          ├── User A → amount
          ├── User B → amount
          └── User C → amount
```

Esto permite posteriormente utilizar la información de los gastos para calcular los balances y obligaciones entre los miembros del grupo.

---

# 🔄 Recurring Expenses

SplitLoop también soporta **gastos recurrentes**.

La definición del gasto recurrente se mantiene separada de sus diferentes ocurrencias:

```text
Recurring Expense
        │
        ▼
    Scheduler
        │
        ▼
Expense Occurrence
        │
        ▼
      Splits
```

Esto permite definir un gasto recurrente una única vez y generar sus ocurrencias automáticamente en función de la configuración correspondiente.

El procesamiento de estos gastos se realiza mediante tareas programadas dentro del backend.

---

# ⚖️ Balances & Payments

Los dominios de balances y pagos permiten representar las cantidades pendientes entre los miembros de un grupo.

El flujo conceptual es:

```text
Expenses
    │
    ▼
Calculate balances
    │
    ▼
Payment obligations
    │
    ▼
Payments
    │
    ▼
Settled balances
```

De esta forma, los gastos registrados pueden utilizarse para determinar quién debe dinero, cuánto debe y qué obligaciones han sido posteriormente saldadas mediante pagos.

---

# 🗄️ Database

SplitLoop utiliza **PostgreSQL 17** como base de datos principal.

El esquema se gestiona mediante **Flyway**, permitiendo versionar los cambios de base de datos junto con el código.

Las migraciones actuales incluyen:

```text
V1__create_users_table.sql
V2__create_groups_table.sql
V3__create_group_members_table.sql
V4__create_recurring_expenses_table.sql
V5__create_recurring_expense_participants.sql
V6__create_expense_occurrences_table.sql
V7__create_expense_occurrence_splits_table.sql
V8__create_payments_table.sql
V9__create_payment_obligations_table.sql
V10__create_tokens_table.sql
```

Las migraciones se encuentran en:

```text
src/main/resources/db/migration/
```

Las migraciones ya ejecutadas no deben modificarse.

Los cambios posteriores deben introducirse mediante una nueva migración:

```text
V11__add_new_feature.sql
V12__another_change.sql
```

---

# 🧪 Testing

El proyecto utiliza **Spring Boot Test** y **Testcontainers** para realizar pruebas, especialmente pruebas de integración.

Testcontainers permite ejecutar las pruebas contra servicios containerizados, evitando depender de una instalación manual de PostgreSQL en el entorno de desarrollo.

Ejecutar la suite de tests:

### Linux / macOS

```bash
  ./mvnw test
```

### Windows

```bash
  mvnw.cmd test
```

El proyecto también incorpora:

* **JaCoCo** para análisis de cobertura
* **SonarQube** para análisis de calidad del código
* Tests de integración utilizando Testcontainers

---

# 📚 API Documentation

La API está documentada mediante **Springdoc OpenAPI**.

Cuando la aplicación está ejecutándose y la documentación está habilitada, Swagger UI está disponible en:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

Esto permite explorar los endpoints disponibles y probar la API durante el desarrollo.

---

# 🐳 Docker

Docker Compose se utiliza actualmente para proporcionar el entorno de desarrollo de PostgreSQL.

```text
┌──────────────────────┐
│   SplitLoop Backend  │
│      Spring Boot     │
└──────────┬───────────┘
           │
           │ JDBC
           ▼
┌──────────────────────┐
│      PostgreSQL      │
│       Docker         │
│    localhost:5433    │
└──────────────────────┘
```

El repositorio también incluye un `Dockerfile` para poder construir una imagen del backend.

---

# ⚙️ Configuration

La aplicación dispone de diferentes configuraciones según el entorno:

```text
src/main/resources/
│
├── application.yml
├── application-dev.yml
├── application-test.yml
└── application-prod.yml
```

La configuración sensible debe proporcionarse mediante variables de entorno.

Entre las principales variables utilizadas se encuentran:

```text
DATABASE_URL
DATABASE_USERNAME
DATABASE_PASSWORD

JWT_SECRET
JWT_EXPIRATION
JWT_REFRESH_EXPIRATION
```

No se deben almacenar credenciales reales dentro de los archivos de configuración ni del repositorio.

---

# ▶️ Running Locally

## Requirements

Antes de ejecutar el proyecto necesitas:

* Java 26
* Maven
* Docker
* Docker Compose

No es necesario instalar PostgreSQL localmente si se utiliza Docker Compose.

---

## 1. Clone the repository

```bash
git clone https://github.com/david160999/splitloop-backend.git

cd splitloop-backend
```

---

## 2. Configure environment variables

Crea un archivo `.env` local con las variables necesarias.

Ejemplo:

```env
POSTGRES_USER=postgres
POSTGRES_PASSWORD=your_password
POSTGRES_DB=spring

DATABASE_URL=jdbc:postgresql://localhost:5433/spring
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=your_password

JWT_SECRET=your_jwt_secret
JWT_EXPIRATION=86400000
JWT_REFRESH_EXPIRATION=604800000
```

No utilices valores reales de producción en este archivo.

---

## 3. Start PostgreSQL

```bash
  docker compose up -d
```

Comprueba que el contenedor está ejecutándose:

```bash
  docker ps
```

PostgreSQL estará disponible desde el host en:

```text
localhost:5433
```

---

## 4. Run the application

### Linux / macOS

```bash
./mvnw spring-boot:run
```

### Windows

```bash
mvnw.cmd spring-boot:run
```

También puedes utilizar Maven directamente:

```bash
mvn spring-boot:run
```

---

# 📁 Project Structure

```text
splitloop-backend/
│
├── .mvn/
│   └── wrapper/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/SplitLoop/
│   │   │       ├── auth/
│   │   │       ├── balance/
│   │   │       ├── common/
│   │   │       ├── expense/
│   │   │       ├── group/
│   │   │       ├── payment/
│   │   │       └── user/
│   │   │
│   │   └── resources/
│   │       ├── db/
│   │       │   └── migration/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       ├── application-test.yml
│   │       └── application-prod.yml
│   │
│   └── test/
│
├── Dockerfile
├── docker-compose.yml
├── pom.xml
├── mvnw
├── mvnw.cmd
└── .gitignore
```

---

# 🔄 Development Workflow

El desarrollo de nuevas funcionalidades sigue una estructura orientada al dominio:

```text
Feature
   │
   ▼
Domain logic
   │
   ▼
Application layer
   │
   ▼
Controller
   │
   ▼
REST API
   │
   ▼
Persistence
   │
   ▼
PostgreSQL
```

Cuando una funcionalidad requiere cambios en la base de datos, estos se introducen mediante una nueva migración Flyway.

Por ejemplo:

```text
V11__add_new_feature.sql
```

Las migraciones anteriores no deben modificarse después de haber sido ejecutadas.

---

# 📡 Planned: Real-time communication

> 🚧 **Not implemented yet**

El proyecto tiene previsto incorporar comunicación en tiempo real mediante **WebSockets** en una futura iteración.

El objetivo es permitir que los usuarios reciban actualizaciones cuando cambien recursos compartidos como:

* Gastos
* Pagos
* Balances
* Miembros de grupos

La idea sería evolucionar desde una comunicación basada exclusivamente en REST hacia un modelo donde determinados eventos puedan notificarse directamente a los clientes.

Conceptualmente:

```text
Client A
   │
   │ REST request
   ▼
SplitLoop Backend
   │
   ├── Update domain
   │
   └── WebSocket event
            │
            ▼
        Client B
```

**Actualmente esta funcionalidad no está implementada.**

---

# 🎯 What this project demonstrates

SplitLoop es un proyecto de portfolio orientado a demostrar conocimientos prácticos de desarrollo backend con el ecosistema Java/Spring.

Entre los principales conceptos y tecnologías aplicados se encuentran:

* Diseño y desarrollo de REST APIs
* Arquitectura orientada al dominio
* Diseño de lógica de negocio
* Programación orientada a objetos
* Spring Boot
* Spring Web MVC
* Spring Security
* JWT authentication
* Access y refresh tokens
* JPA / Hibernate
* Modelado de bases de datos relacionales
* PostgreSQL
* Database migrations con Flyway
* Procesamiento de tareas programadas
* DTO mapping con MapStruct
* Integration testing con Testcontainers
* Containerización con Docker
* API documentation con OpenAPI
* Code coverage con JaCoCo
* Static code analysis con SonarQube

El proyecto está diseñado para poder evolucionar progresivamente a medida que se incorporan nuevos requisitos y funcionalidades.

---

# 🚧 Project Status

**Active development**

### Implemented

* User registration and authentication
* JWT authentication
* Refresh tokens
* User management
* Group management
* Group members
* Expense management
* Expense splitting
* Recurring expenses
* Automatic expense occurrences
* Expense participants
* Balance calculation
* Payment management
* Payment obligations
* PostgreSQL persistence
* Flyway migrations
* Integration testing with Testcontainers
* OpenAPI documentation

### Planned

* WebSocket-based real-time updates
* Expanded notification system
* Additional balance settlement strategies
* More comprehensive end-to-end testing
* CI/CD pipeline
* Production-oriented deployment
* Observability and monitoring

---

# 📌 Future improvements

Algunas de las áreas previstas para futuras iteraciones son:

* **WebSockets** para actualizaciones en tiempo real
* Sistema de notificaciones
* Ampliación de las estrategias de liquidación de balances
* Mayor cobertura de tests end-to-end
* Pipeline de CI/CD
* Despliegue orientado a producción
* Monitoring y observability
* Mejoras de rendimiento
* Ampliación de la documentación de la API

---

# 👨‍💻 About

**SplitLoop Backend** es un proyecto personal desarrollado para poner en práctica el diseño y desarrollo de una aplicación backend completa utilizando tecnologías modernas del ecosistema Java.

El proyecto busca priorizar una arquitectura mantenible, separación de responsabilidades, seguridad, persistencia, testing y evolución progresiva del dominio.

---




