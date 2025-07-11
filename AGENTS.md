# AGENTS

> **NOTE:** We track current and planned development tasks in `python_app/TODO.md`.  \
> That file is the source of truth for which feature‐parity tasks are in progress and upcoming.

This document provides guidelines for agents on handling the Python environment and project tasks
in the `python_app` directory using the `uv` CLI tool, including invoking Python via `uv`.

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

Install the Ruff tool if you haven't already:

```bash
uv tool ruff
```

Use Ruff for both linting and formatting:

```bash
uv run ruff .        # lint checks
uv run ruff --fix .  # auto-format and fix issues
```

### Export requirements

To regenerate or update `requirements.txt` from the lockfile:

```bash
uv export --format=requirements.txt --output-file requirements.txt
```

## Additional `uv` commands

Use `uv help` to discover other available commands (e.g., `uv tree`, `uv python`, `uv build`, etc.).

## Commit Guidelines

- Propose a separate commit for each set of changes you make.
- Write verbose, descriptive commit messages that follow the Conventional Commits specification (e.g., `feat: add new feature`, `fix: correct typo`). For more information, see https://www.conventionalcommits.org/.

---

> **Note:** Always prefer `uv` over direct `pip`, `python`, or `python3` commands (e.g., use `uv python` or `uv run python`) to ensure consistent environments and reproducible dependency management.
> Do not use raw `pip` or a `requirements.txt`‑based workflow; use `uv pip` for all pip operations, as it fully implements the pip API within the managed environment.
> Projects in this repo follow the `pyproject.toml` specification as the primary manifest for dependencies and configuration; rely on it rather than `requirements.txt`.
