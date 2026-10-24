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

### Run on Windows, macOS and Linux

Requires JDK 21.

#### Windows

From PowerShell:

```powershell
.\gradlew.bat bootRun
```

To enable live currency conversion:

```powershell
$env:CURRENCY_API_KEY="your-key"
.\gradlew.bat bootRun
```

#### macOS

From the repository root:

```bash
chmod +x gradlew  # only needed if the executable bit was lost, e.g. after extracting a ZIP
./gradlew bootRun
```

To enable live currency conversion:

```bash
export CURRENCY_API_KEY="your-key"
./gradlew bootRun
```

#### Linux

Use the same commands as macOS:

```bash
chmod +x gradlew  # only if needed
./gradlew bootRun
```

The server starts at [http://localhost:8082](http://localhost:8082). The H2 console is at [http://localhost:8082/h2-console](http://localhost:8082/h2-console), using `jdbc:h2:file:./data/auctionsdb`. To reset the example data, stop the server and remove the files matching `./data/auctionsdb*`.

The Gradle wrapper is included; no local Gradle installation is required. In Eclipse or Spring Tool Suite on any supported operating system, import the folder as an existing Gradle project and run `AuctionsApplication`.

### Tests and continuous integration

```bash
./gradlew test
```

The suite includes unit tests for authentication and currency conversion, H2 persistence tests for the auction service, and `MockMvc` tests for the REST controllers. Integration tests also start the complete application with an isolated in-memory H2 database and verify Swagger/OpenAPI, login/bid/logout, concurrent bids and initialization rollback. Gateway tests use a local HTTP server for valid, invalid and unavailable responses; no real API key is required.

The [CI workflow](.github/workflows/ci.yml) runs the test suite for pushes to `master` and pull requests. The `master` branch requires the `test` check to pass before changes are integrated.

### Classroom notes and dependency review

Stable versions checked on **8 October 2026**:

| Component | Version | Official reference |
|:--|:--|:--|
| Spring Boot | 4.1.1 | [Releases](https://github.com/spring-projects/spring-boot/releases) |
| springdoc OpenAPI | 3.1.1 (Spring Boot 4 compatible branch) | [Documentation](https://springdoc.org/) |
| Spring dependency management plugin | 1.1.7 | [Releases](https://github.com/spring-gradle-plugins/dependency-management-plugin/releases) |
| Apache Commons Codec | 1.22.1 (explicit override) | [Release history](https://commons.apache.org/proper/commons-codec/changes.html) |
| H2 | 2.5.252 (explicit override) | [Releases](https://github.com/h2database/h2database/releases) |
| Hibernate ORM | 7.4.12.Final (managed series, explicit maintenance override) | [Releases](https://hibernate.org/orm/releases/) |
| Gradle wrapper | 9.8.0 | [Releases](https://gradle.org/releases/) |
| CI checkout / Java setup | 7.0.1 / 6.0.1 | [Checkout](https://github.com/actions/checkout/releases), [Java setup](https://github.com/actions/setup-java/releases) |

Java 21 remains the classroom requirement. Spring Boot manages the remaining library versions as a compatible set; they are not individually pinned to their newest upstream release. See [Spring Boot dependency management](https://docs.spring.io/spring-boot/gradle-plugin/managing-dependencies.html).

Send the returned token as a **plain text body** when bidding or logging out. Article IDs are generated by H2; retrieve them through the category endpoints rather than assuming V1's IDs.

The initial data is inserted atomically and only when no categories exist. Articles and bids survive restarts, while login tokens remain in memory and are lost on restart. Demo auctions end on 31 December of their initialization year: restarting an existing database does not renew their dates. To start a fresh classroom dataset, stop the server, keep a backup of the database files, and follow the reset instructions above.

Without an API key, EUR/USD/GBP work locally. Valid external rates take priority; unavailable or invalid responses use the fallback for USD/GBP, and unsupported currencies return 400. External requests have five-second connection/request timeouts. Automated tests use a local HTTP server rather than FreeCurrencyAPI; the live account/key has not been validated. SHA-1, non-expiring sessions and `double` monetary values remain teaching simplifications.

### License and authorship

This project is licensed under the [MIT License](LICENSE).

Faculty of Engineering, University of Deusto — Academic year 2026–27.

### AI assistance and review disclosure

The application code was initially generated with ChatGPT 4o (OpenAI) and adapted using GitHub Copilot. The codebase and documentation were reviewed and updated with assistance from Claude Opus 4.8 (Anthropic) in July 2026 and ChatGPT-6.1 Sol (OpenAI) in October 2026.

All automated tests and the continuous integration workflow were entirely generated with ChatGPT-6.1 (OpenAI) in September and October 2026.

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

### Ejecución en Windows, macOS y Linux

Requiere JDK 21.

#### Windows

Desde PowerShell:

```powershell
.\gradlew.bat bootRun
```

Para activar la conversión de moneda real:

```powershell
$env:CURRENCY_API_KEY="your-key"
.\gradlew.bat bootRun
```

#### macOS

Desde la raíz del repositorio:

```bash
chmod +x gradlew  # solo si se ha perdido el permiso, por ejemplo tras extraer un ZIP
./gradlew bootRun
```

Para activar la conversión de moneda real:

```bash
export CURRENCY_API_KEY="your-key"
./gradlew bootRun
```

#### Linux

Utiliza los mismos comandos que en macOS:

```bash
chmod +x gradlew  # solo si fuera necesario
./gradlew bootRun
```

El servidor queda disponible en [http://localhost:8082](http://localhost:8082). La consola H2 está en [http://localhost:8082/h2-console](http://localhost:8082/h2-console), con la URL JDBC `jdbc:h2:file:./data/auctionsdb`. Para reiniciar los datos del ejemplo, detén el servidor y elimina los ficheros que coincidan con `./data/auctionsdb*`.

Se incluye el wrapper de Gradle, por lo que no es necesario instalar Gradle localmente. En Eclipse o Spring Tool Suite, en cualquiera de los sistemas operativos admitidos, importa la carpeta como proyecto Gradle existente y ejecuta `AuctionsApplication`.

### Pruebas e integración continua

```bash
./gradlew test
```

La batería incluye pruebas unitarias de autenticación y conversión de moneda, pruebas de persistencia H2 del servicio de subastas y pruebas `MockMvc` de los controladores REST. Las pruebas de integración arrancan también la aplicación completa con una base H2 aislada en memoria y verifican Swagger/OpenAPI, login/puja/logout, pujas simultáneas y rollback de la inicialización. Las pruebas del gateway usan un servidor HTTP local para respuestas válidas, inválidas y no disponibles; no requieren una clave de API real.

El [flujo de CI](.github/workflows/ci.yml) ejecuta las pruebas en cada cambio a `master` y en cada pull request. La rama `master` requiere que la comprobación `test` sea correcta antes de integrar cambios.

### Notas para clase y revisión de dependencias

Versiones estables comprobadas el **8 de octubre de 2026**:

| Componente | Versión | Referencia oficial |
|:--|:--|:--|
| Spring Boot | 4.1.1 | [Versiones](https://github.com/spring-projects/spring-boot/releases) |
| springdoc OpenAPI | 3.1.1 (rama compatible con Spring Boot 4) | [Documentación](https://springdoc.org/) |
| Plugin de gestión de dependencias Spring | 1.1.7 | [Versiones](https://github.com/spring-gradle-plugins/dependency-management-plugin/releases) |
| Apache Commons Codec | 1.22.1 (versión explícita) | [Historial](https://commons.apache.org/proper/commons-codec/changes.html) |
| H2 | 2.5.252 (versión explícita) | [Versiones](https://github.com/h2database/h2database/releases) |
| Hibernate ORM | 7.4.12.Final (misma rama, revisión explícita) | [Versiones](https://hibernate.org/orm/releases/) |
| Wrapper de Gradle | 9.8.0 | [Versiones](https://gradle.org/releases/) |
| CI checkout / configuración Java | 7.0.1 / 6.0.1 | [Checkout](https://github.com/actions/checkout/releases), [Java setup](https://github.com/actions/setup-java/releases) |

Se mantiene Java 21 como requisito docente. Spring Boot gestiona las versiones del resto de librerías como un conjunto compatible; no se fija individualmente cada una a su última versión publicada. Véase la [gestión de dependencias de Spring Boot](https://docs.spring.io/spring-boot/gradle-plugin/managing-dependencies.html).

Envía el token recibido como **cuerpo de texto plano** para pujar o cerrar sesión. H2 genera los identificadores de artículos; consúltalos mediante los endpoints de categorías en lugar de asumir los de V1.

Los datos iniciales se insertan en una única transacción y solo cuando no existen categorías. Los artículos y pujas sobreviven al reinicio; los tokens de sesión se mantienen en memoria y se pierden al reiniciar. Las subastas de ejemplo cierran el 31 de diciembre del año de inicialización: reiniciar una base existente no renueva sus fechas. Para comenzar con datos nuevos en clase, detén el servidor, conserva una copia de los ficheros de la base y sigue las instrucciones anteriores de reinicio de datos.

Sin clave de API, EUR/USD/GBP funcionan localmente. Las tasas externas válidas tienen prioridad; las respuestas inválidas o no disponibles activan el respaldo para USD/GBP, y las monedas no admitidas devuelven 400. Las peticiones externas tienen límites de cinco segundos para conexión/petición. Las pruebas automáticas utilizan un servidor HTTP local en lugar de FreeCurrencyAPI; no se ha validado una cuenta/clave real. SHA-1, las sesiones sin caducidad y los importes con `double` siguen siendo simplificaciones docentes.

### Licencia y autoría

Este proyecto se distribuye bajo la [licencia MIT](LICENSE).

Facultad de Ingeniería, Universidad de Deusto — Curso académico 2026–27.

### Declaración sobre asistencia de IA y revisión

El código de la aplicación se generó inicialmente con ChatGPT 4o (OpenAI) y se adaptó utilizando GitHub Copilot. El código y la documentación se revisaron y actualizaron con asistencia de Claude Opus 4.8 (Anthropic) en julio de 2026 y de ChatGPT-6.1 Sol (OpenAI) en octubre de 2026.

Todas las pruebas automáticas y el flujo de integración continua se generaron íntegramente con ChatGPT-6.1 (OpenAI) en septiembre y octubre de 2026.
