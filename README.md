# 🚀 Auctions Server — Version 2

[![CI](https://github.com/rcarball/auctions-server-2/actions/workflows/ci.yml/badge.svg?branch=master)](https://github.com/rcarball/auctions-server-2/actions/workflows/ci.yml)

## English

### Overview

Auctions Server V2 evolves [Auctions Server V1](https://github.com/rcarball/auctions-server-1) in the *Auctions Service* teaching case study. It remains intentionally educational rather than production-oriented while adding database persistence and an external currency-conversion integration.

It illustrates the Data Access Object and Service Gateway patterns, alongside the patterns introduced in V1. H2/JPA stores the data, and the currency service can use FreeCurrencyAPI when configured.

### REST API

| Method | Endpoint | Description |
|:--|:--|:--|
| POST | `/auth/login` | Log in and obtain a token |
| POST | `/auth/logout` | Log out using a token |
| GET | `/auctions/categories` | Retrieve categories |
| GET | `/auctions/categories/{categoryName}/articles` | Retrieve a category's articles |
| GET | `/auctions/articles/{articleId}/details` | Retrieve article details |
| POST | `/auctions/articles/{articleId}/bid` | Place a bid (requires login) |

- Swagger UI: [http://localhost:8082/swagger-ui/index.html](http://localhost:8082/swagger-ui/index.html)
- OpenAPI document: [http://localhost:8082/v3/api-docs](http://localhost:8082/v3/api-docs)

### Configuration

- **Database:** H2 file database at `./data/auctionsdb`.
- **Currency service:** no API key is stored in the repository. Without `CURRENCY_API_KEY`, the example uses local EUR/USD/GBP fallback rates.
- **Optional live conversion:** set `CURRENCY_API_KEY`; use `CURRENCY_API_URL` only to select another compatible endpoint.

### Run on macOS and Linux

Requires JDK 21. From the repository root:

```bash
chmod +x gradlew  # only needed if the executable bit was lost, e.g. after extracting a ZIP
./gradlew bootRun
```

To enable live currency conversion in macOS or Linux:

```bash
export CURRENCY_API_KEY="your-key"
./gradlew bootRun
```

The server starts at [http://localhost:8082](http://localhost:8082). The H2 console is at [http://localhost:8082/h2-console](http://localhost:8082/h2-console), using `jdbc:h2:file:./data/auctionsdb`. To reset the example data, stop the server and remove the files matching `./data/auctionsdb*`.

The Gradle wrapper is included; no local Gradle installation is required. On Windows, use `gradlew.bat bootRun` and set the key with `$env:CURRENCY_API_KEY="your-key"` in PowerShell. In Eclipse or Spring Tool Suite, import the folder as an existing Gradle project and run `AuctionsApplication`.

### Tests and continuous integration

```bash
./gradlew test
```

The suite includes unit tests for authentication and currency conversion, H2 persistence tests for the auction service, and `MockMvc` tests for the REST controllers. The controller tests validate the HTTP contract without starting a network server or calling the external currency service.

The [CI workflow](.github/workflows/ci.yml) runs the test suite for pushes to `master` and pull requests. The `master` branch requires the `test` check to pass before changes are integrated.

### License and authorship

This project is licensed under the [MIT License](LICENSE).

Faculty of Engineering, University of Deusto — Academic year 2026–27.

### AI assistance and review disclosure

The initial version of this codebase was developed with partial assistance from ChatGPT (OpenAI) and GitHub Copilot.

From July to September 2026, the codebase and documentation were reviewed and audited using Claude Opus (Anthropic) and Codex (OpenAI). The resulting version was tested and refined to identify and correct issues within the scope of those verification activities.

---

## Español

### Descripción general

Auctions Server V2 evoluciona [Auctions Server V1](https://github.com/rcarball/auctions-server-1) dentro del caso docente *Auctions Service*. Mantiene un propósito deliberadamente educativo, no productivo, e incorpora persistencia en base de datos e integración con un servicio externo de conversión de moneda.

Ilustra los patrones Data Access Object y Service Gateway, además de los introducidos en V1. H2/JPA almacena los datos y el servicio de moneda puede usar FreeCurrencyAPI cuando se configura.

### API REST

| Método | Endpoint | Descripción |
|:--|:--|:--|
| POST | `/auth/login` | Iniciar sesión y obtener un token |
| POST | `/auth/logout` | Cerrar sesión con un token |
| GET | `/auctions/categories` | Consultar categorías |
| GET | `/auctions/categories/{categoryName}/articles` | Consultar los artículos de una categoría |
| GET | `/auctions/articles/{articleId}/details` | Consultar los detalles de un artículo |
| POST | `/auctions/articles/{articleId}/bid` | Realizar una puja (requiere sesión) |

- Swagger UI: [http://localhost:8082/swagger-ui/index.html](http://localhost:8082/swagger-ui/index.html)
- Documento OpenAPI: [http://localhost:8082/v3/api-docs](http://localhost:8082/v3/api-docs)

### Configuración

- **Base de datos:** base de datos H2 en fichero, en `./data/auctionsdb`.
- **Servicio de moneda:** el repositorio no guarda ninguna clave de API. Sin `CURRENCY_API_KEY`, el ejemplo emplea tasas locales de respaldo para EUR/USD/GBP.
- **Conversión real opcional:** define `CURRENCY_API_KEY`; usa `CURRENCY_API_URL` solo para seleccionar otro endpoint compatible.

### Ejecución en macOS y Linux

Requiere JDK 21. Desde la raíz del repositorio:

```bash
chmod +x gradlew  # solo si se ha perdido el permiso, por ejemplo tras extraer un ZIP
./gradlew bootRun
```

Para activar la conversión de moneda real en macOS o Linux:

```bash
export CURRENCY_API_KEY="your-key"
./gradlew bootRun
```

El servidor queda disponible en [http://localhost:8082](http://localhost:8082). La consola H2 está en [http://localhost:8082/h2-console](http://localhost:8082/h2-console), con la URL JDBC `jdbc:h2:file:./data/auctionsdb`. Para reiniciar los datos del ejemplo, detén el servidor y elimina los ficheros que coincidan con `./data/auctionsdb*`.

Se incluye el wrapper de Gradle, por lo que no es necesario instalar Gradle localmente. En Windows, usa `gradlew.bat bootRun` y define la clave en PowerShell con `$env:CURRENCY_API_KEY="your-key"`. En Eclipse o Spring Tool Suite, importa la carpeta como proyecto Gradle existente y ejecuta `AuctionsApplication`.

### Pruebas e integración continua

```bash
./gradlew test
```

La batería incluye pruebas unitarias de autenticación y conversión de moneda, pruebas de persistencia H2 del servicio de subastas y pruebas `MockMvc` de los controladores REST. Las pruebas de controladores validan el contrato HTTP sin iniciar un servidor de red ni invocar al servicio externo de moneda.

El [flujo de CI](.github/workflows/ci.yml) ejecuta las pruebas en cada cambio a `master` y en cada pull request. La rama `master` requiere que la comprobación `test` sea correcta antes de integrar cambios.

### Licencia y autoría

Este proyecto se distribuye bajo la [licencia MIT](LICENSE).

Facultad de Ingeniería, Universidad de Deusto — Curso académico 2026–27.

### Declaración sobre asistencia de IA y revisión

La versión inicial de este código se desarrolló con asistencia parcial de ChatGPT (OpenAI) y GitHub Copilot.

Entre julio y septiembre de 2026, el código y la documentación se revisaron y auditaron con Claude Opus (Anthropic) y Codex (OpenAI). La versión resultante fue probada y refinada para identificar y corregir incidencias dentro del alcance de dichas actividades de verificación.
