🇪🇸 *Scroll down for the Spanish version / Descripción en castellano a continuación.*

# 🚀 Auctions Server - Version 2

## 📘 Description (English)

This repository contains the **second version** of the *Auctions Server*, an **evolution of [Version 1](https://github.com/rcarball/auctions-server-1)**. It extends the functionality introduced in the first version by adding **database persistence** and **external service integration** for currency conversion — creating a more realistic simulation of an online auction platform.

This version maintains the **Spring Boot REST API** architecture while incorporating additional design patterns:

- 🗄️ **Data Access Object (DAO)**
- 🌐 **Service Gateway**

The server now interacts with:
- 🧠 **H2 database** (via JPA) for data persistence.  
- 💱 **Free Currency Conversion API (https://freecurrencyapi.com/)** for displaying prices in different currencies.

The design builds on the foundations laid in Version 1, expanding both complexity and realism, while preserving modularity and maintainability.

### 🌐 REST API Endpoints

| Method | Endpoint | Description |
|:--------|:----------------------------------|:----------------------------------------------|
| **POST** | `/auth/login` | Log in to the system |
| **POST** | `/auth/logout` | Log out from the system |
| **GET**  | `/auctions/categories` | Retrieve all available categories |
| **GET**  | `/auctions/categories/{categoryName}/articles` | Get all articles by category |
| **GET**  | `/auctions/articles/{articleId}/details` | Retrieve detailed article information |
| **POST** | `/auctions/articles/{articleId}/bid` | Place a bid on an article |
| **GET**  | `/currency/{from}/{to}` | Convert between currencies (via external API) |

💡 Swagger UI: [http://localhost:8081/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)  
📄 OpenAPI Docs: [http://localhost:8081/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 📘 Descripción

Este repositorio contiene la **segunda versión** del *Servidor de Subastas*, una **evolución de [la Versión 1](https://github.com/rcarball/auctions-server-1)**. Amplía la funcionalidad previamente desarrollada incorporando **persistencia en base de datos** y **comunicación con un servicio externo** para la conversión de divisas, ofreciendo una simulación más realista de una plataforma de subastas en línea.

Mantiene la arquitectura **Spring Boot REST API** e introduce nuevos patrones de diseño:

- 🗄️ **Data Access Object (DAO)**  
- 🌐 **Service Gateway**

El servidor se comunica con:
- 🧠 Una **base de datos H2** (usando JPA) para la persistencia de datos.  
- 💱 **Free Currency Conversion API (https://freecurrencyapi.com/)** para mostrar precios en distintas divisas.

Esta versión amplía la base establecida en la primera versión, aumentando la complejidad y la fidelidad del sistema sin comprometer su claridad ni modularidad.

### 🌐 Endpoints del API REST

| Método | Endpoint | Descripción |
|:--------|:----------------------------------|:----------------------------------------------|
| **POST** | `/auth/login` | Iniciar sesión |
| **POST** | `/auth/logout` | Cerrar sesión |
| **GET**  | `/auctions/categories` | Consultar categorías disponibles |
| **GET**  | `/auctions/categories/{categoryName}/articles` | Obtener artículos por categoría |
| **GET**  | `/auctions/articles/{articleId}/details` | Consultar detalles de un artículo |
| **POST** | `/auctions/articles/{articleId}/bid` | Realizar una puja |
| **GET**  | `/currency/{from}/{to}` | Conversión de divisas (vía API externa) |

💡 Swagger UI: [http://localhost:8081/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)  
📄 OpenAPI Docs: [http://localhost:8081/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## ✒️ Authors / Autoría

**Carballedo, R. & Cortázar, R.**  
*Faculty of Engineering – University of Deusto*

---

> 🧠 *This description was generated with the assistance of ChatGPT 5 and has been reviewed and validated to ensure accuracy and correctness.*
