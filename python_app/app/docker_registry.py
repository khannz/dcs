import httpx
from typing import Optional

from .models import ScanRequestLayer

class DockerRegistryService:
    async def get_layer_blob(self, layer: ScanRequestLayer) -> bytes:
        headers = layer.headers or {}
        async with httpx.AsyncClient() as client:
            response = await client.get(layer.path, headers=headers)
            response.raise_for_status()
            return response.content
