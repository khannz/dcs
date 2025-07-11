# AGENTS

This document provides guidelines for agents on handling the Python environment and project tasks
in the `python_app` directory using the `uv` CLI tool.

## Prerequisites

- Install Python 3.13 or newer.
- Install or upgrade the `uv` CLI:
  ```bash
  pip install --upgrade uv
  ```

## Working directory

All `uv` commands should be run from the root of the `python_app` project:

```bash
cd python_app
```

## Virtual environment and dependencies

### Create or update the virtual environment

```bash
uv venv            # (re)creates the `.venv` environment in this directory
uv sync            # syncs the environment with the lockfile
```

### Install new or remove existing dependencies

- Add a dependency:
  ```bash
  uv add <package>[@<version>]
  ```
- Remove a dependency:
  ```bash
  uv remove <package>
  ```
- Update the lockfile after changing dependencies:
  ```bash
  uv lock
  ```

## Routine project tasks

### Run the application

```bash
uv run uvicorn main:app --reload
```

### Run tests

```bash
uv run pytest
```

### Code formatting and linting

```bash
uv run black .
uv run flake8 .
```

### Export requirements

To regenerate or update `requirements.txt` from the lockfile:

```bash
uv export --format=requirements.txt --output-file requirements.txt
```

## Additional `uv` commands

Use `uv help` to discover other available commands (e.g., `uv tree`, `uv python`, `uv build`, etc.).

---

> **Note:** Always prefer `uv` over direct `pip` or `python -m venv` commands to ensure consistent environments
> and reproducible dependency management across all agents.
