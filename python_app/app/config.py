from pathlib import Path

from pydantic_settings import BaseSettings


class Settings(BaseSettings):
    """
    Application settings loaded from environment variables or a .env file.
    """
    concurrency: int = 4
    cache_expire_hours: int = 24
    cache_number_cached_scans: int = 50
    working_dir: Path = Path("/tmp")
    ssl_insecure_enable: bool = False

    class Config:
        # Environment variables prefixed with DCS_ (e.g. DCS_CONCURRENCY)
        env_prefix = "DCS_"
        env_file = ".env"
        case_sensitive = False
