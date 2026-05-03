# Pokédex API

A REST API built with Quarkus that retrieves Pokémon information from [PokéAPI](https://pokeapi.co) and optionally translates the description using [FunTranslations](https://funtranslations.mercxry.me).

---

## Endpoints

| Method | Path | Description                                                                                                        |
|--------|------|--------------------------------------------------------------------------------------------------------------------|
| GET | `/pokemon/{name}` | Returns name, habitat, legendary status and English description                                                    |
| GET | `/pokemon/translated/{name}` | Same as above but with the description translated (Yoda if habitat is cave or is legendary, Shakespeare otherwise) |

---

## How to run

### Prerequisites
For docker 
- [Docker](https://www.docker.com/get-started) 
- [Docker Compose](https://docs.docker.com/compose/install/) 

For local run without docker
- [Java 21](https://adoptium.net/) 
- [Maven 3.9+](https://maven.apache.org/download.cgi) 

No other tools or services need to be installed.

---

### Option 1 — Docker (recommended)

Build the application JAR first, then start the container:

```bash
# 1. Build the JAR (requires Java 21 and Maven)
mvn package -DskipTests

# 2. Build and start the container
docker-compose up --build
```

The API will be available at `http://localhost:8080`.

To stop:

```bash
docker-compose down
```

---

### Option 2 — Local (no Docker)

Requires Java 21 and Maven installed locally.

```bash
mvn quarkus:dev
```

If your default Java version is not 21, set it inline before the command:

**Linux / macOS:**
```bash
JAVA_HOME=/path/to/java21 mvn quarkus:dev
```

**Windows (PowerShell):**
```powershell
$env:JAVA_HOME="C:\path\to\java21"; mvn quarkus:dev
```

The API will be available at `http://localhost:8080`.  
Swagger UI is available at `http://localhost:8080/q/swagger-ui`.

---

## What I would do differently for a production API

**Security**
- Add authentication (e.g. JWT via `quarkus-smallrye-jwt`) so the API is not publicly open.

**Resilience**
- Set explicit timeouts on both REST clients via `application.properties` (`connect-timeout`, `read-timeout`).

**Caching**
- Cache responses from PokeAPI using `@CacheResult` (Quarkus Cache extension) on `getPokemonInfo` to eliminate redundant external calls.

**Configuration**
- Move base URLs of external clients out of `@RegisterRestClient(baseUri = ...)` and into `application.properties` (or environment variables), so they can be changed per environment without recompiling.

**CI/CD**
- Add a pipeline (GitHub Actions or similar) that runs tests, builds the Docker image and pushes it to a registry on every merge to main.
