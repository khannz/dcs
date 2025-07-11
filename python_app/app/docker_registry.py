import httpx
import os

from .models import ScanRequestLayer

class DockerRegistryService:
    """
    Service for fetching Docker layer blobs, with optional SSL verification control.
    """
    def __init__(self) -> None:
        # Allow disabling SSL certificate verification via environment variable
        insecure = os.getenv("SSL_INSECURE_ENABLE", "false").lower() in ("1", "true", "yes")
        # httpx verify flag: False to disable cert check
        self._verify = not insecure

    async def get_layer_blob(self, layer: ScanRequestLayer) -> bytes:
        headers = layer.headers or {}
        async with httpx.AsyncClient(verify=self._verify) as client:
            response = await client.get(layer.path, headers=headers)
            response.raise_for_status()
            return response.content
