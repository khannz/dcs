# python_app Feature Parity Tasks

# This TODO lists the top missing features in the Python FastAPI reference implementation
# that are present in the Java `src/` version, to work on one by one.

## Workflow

- **Start a task**: mark its checkbox `[ ]` as `[In Progress]` or add a sub-checkbox for in-progress steps.
- **Complete a task**: check its box (`[x]`) and, if desired, annotate with `[Done]` after the title.
- I (the assistant) will keep this file up-to-date to reflect the current task and next steps.
- Use this file as our communication channel: you can see what I'm working on and what remains.

## Current Task

- [In Progress] 2. **Add runtime configuration support**

## Tasks

1. **Integrate full OWASP Dependency‑Check analyzers**  
   - Support the same set of analyzers (JAR, Node packages, ComposerLock, CocoaPods, RubyGemspec, .NET/NuGet (.nuspec/.nuget.conf), CMake, Autoconf, central/Nexus, R packages, OS packages, Nvidia)
   - Allow enabling/disabling analyzers via configuration

2. **Add runtime configuration support**  
   - Make thread/concurrency count, cache size/TTL, working directory, SSL‑insecure flag configurable via environment vars or config file
   - Use Pydantic `BaseSettings` or equivalent to manage settings

3. **Expose non‑blocking scan API mode**  
   - Add optional `block` query parameter to POST `/scan` so clients can choose synchronous vs asynchronous scans
   - Implement background task queue or store scan result state for polling via GET `/scan/{layer_id}`

4. **Add automated tests**  
   - Provide unit/integration tests for FastAPI routes, services, and analyzers
   - Mirror Java’s `src/test/java` coverage for scan logic, registry client, and dependency parsing

5. **Support tmpfs‑based working directory helper**  
   - Include `configure_tmpfs.sh` (or equivalent) for mounting workdir to RAM as in the Java README
   - Document tmpfs usage in Python README for consistent performance optimization

6. **Add health and metrics endpoints**  
   - Integrate FastAPI health‑check (e.g. `/health`) and Prometheus metrics endpoint
   - Mirror Spring Boot Actuator functionality (`/actuator/health`, `/actuator/metrics`) for operational visibility.

7. **Use configured working directory for unpacking**  
   - Implement layer unpack into `working_dir/<layer>` and cleanup as in Java's `fetchLayer`
   - Mirror Java's streaming GZIP->tar extraction logic instead of transient tempdir only in dependency_check

8. **Align JSON response schema with Java API**  
   - Rename `layers` field on `ImageScanResult` to `layerScanList` in the API output
   - Update documentation and Pydantic model so Python matches the Java contract in README

9. **Add structured logging**  
   - Integrate Python `logging` across all modules at DEBUG/INFO levels
   - Log key events (request start/end, errors) similarly to Java’s SLF4J logs

10. **Add Python packaging and startup script**  
   - Provide a `docker-comp-scan.sh` (or equivalent) script to launch the Python service similar to the Java wrapper
   - Add a `python_app/README.md` section describing packaging and startup (e.g. `uv build`, `uv run uvicorn`)

11. **Create Dockerfile for Python app**  
   - Define a Dockerfile in `python_app/` to containerize the FastAPI service
   - Mirror Java container distribution guidelines (base image, working_dir mount, tmpfs helper)

12. **Enhance registry client resiliency**  
   - Add configurable HTTP timeouts, retries, and backoff policies to `DockerRegistryService`
   - Surface descriptive error codes/timeouts (e.g. 408/504) instead of generic 500 responses
