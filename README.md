🇪🇸 *Scroll down for the Spanish version / Descripción en castellano a continuación.*

# 🚀 Auctions Server - Version 2

## 📘 Description

This repository contains the **second version** of the *Auctions Server*, an **evolution of [Version 1](https://github.com/rcarball/auctions-server-1)**. It extends the functionality introduced in the first version by adding **database persistence** and **external service integration** for currency conversion — creating a more realistic simulation of an online auction platform.

This version maintains the **Spring Boot REST API** architecture while incorporating additional design patterns:

- 🗄️ **Data Access Object (DAO)**
- 🌐 **Service Gateway**

The server now interacts with:
- 🧠 **H2 database** (via JPA) for data persistence.  
- 💱 **Free Currency Conversion API** ([https://freecurrencyapi.com/](https://freecurrencyapi.com/)) for displaying prices in different currencies.

### 🌐 REST API Endpoints

| Method | Endpoint | Description |
|:--------|:----------------------------------|:----------------------------------------------|
| **POST** | `/auth/login` | Log in to the system |
| **POST** | `/auth/logout` | Log out from the system |
| **GET**  | `/auctions/categories` | Retrieve all available categories |
| **GET**  | `/auctions/categories/{categoryName}/articles` | Get all articles by category |
| **GET**  | `/auctions/articles/{articleId}/details` | Retrieve detailed article information |
| **POST** | `/auctions/articles/{articleId}/bid` | Place a bid on an article |

💡 Swagger UI: [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)  
📄 OpenAPI Docs: [http://localhost:8081/v3/api-docs](http://localhost:8081/v3/api-docs)

---

## 📘 Descripción

Este repositorio contiene la **segunda versión** del *Servidor de Subastas*, una **evolución de [la Versión 1](https://github.com/rcarball/auctions-server-1)**. Amplía la funcionalidad previamente desarrollada incorporando **persistencia en base de datos** y **comunicación con un servicio externo** para la conversión de divisas, ofreciendo una simulación más realista de una plataforma de subastas en línea.

Mantiene la arquitectura **Spring Boot REST API** e introduce nuevos patrones de diseño:

- 🗄️ **Data Access Object (DAO)**  
- 🌐 **Service Gateway**

El servidor se comunica con:
- 🧠 Una **base de datos H2** (usando JPA) para la persistencia de datos.  
- 💱 La **Free Currency Conversion API** ([https://freecurrencyapi.com/](https://freecurrencyapi.com/)) para mostrar precios en distintas divisas.

### 🌐 Endpoints del API REST

| Método | Endpoint | Descripción |
|:--------|:----------------------------------|:----------------------------------------------|
| **POST** | `/auth/login` | Iniciar sesión |
| **POST** | `/auth/logout` | Cerrar sesión |
| **GET**  | `/auctions/categories` | Consultar categorías disponibles |
| **GET**  | `/auctions/categories/{categoryName}/articles` | Obtener artículos por categoría |
| **GET**  | `/auctions/articles/{articleId}/details` | Consultar detalles de un artículo |
| **POST** | `/auctions/articles/{articleId}/bid` | Realizar una puja |

💡 Swagger UI: [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)  
📄 OpenAPI Docs: [http://localhost:8081/v3/api-docs](http://localhost:8081/v3/api-docs)

---

## ⚙️ Tech Stack & Build

### 🔧 Java & Build
- ☕ **Java**: 21
- 🧱 **Build**: Gradle  
- 🔌 **Plugins**:
  - `org.springframework.boot` **3.5.7**
  - `io.spring.dependency-management` **1.1.6**

### 📦 Dependencies
- `org.springframework.boot:spring-boot-starter-web`
- `org.springframework.boot:spring-boot-starter-data-jpa`
- `com.h2database:h2`
- `org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.13`
- `org.springframework.boot:spring-boot-starter-test`

---

## ✒️ Authors / Autoría

**Carballedo, R. & Cortázar, R.**  
*Faculty of Engineering – University of Deusto*

---

> 🧠 *This description was generated with the assistance of ChatGPT 5 and has been reviewed and validated to ensure accuracy and correctness.*
