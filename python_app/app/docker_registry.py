import httpx

from .models import ScanRequestLayer

class DockerRegistryService:
    """
    Service for fetching Docker layer blobs, with optional SSL verification control.
    """
    def __init__(self, insecure: bool = False) -> None:
        """
        :param insecure: if True, disable SSL certificate verification
        """
        # httpx verify flag: False to disable certificate checking
        self._verify = not insecure

    async def get_layer_blob(self, layer: ScanRequestLayer) -> bytes:
        headers = layer.headers or {}
        async with httpx.AsyncClient(verify=self._verify) as client:
            response = await client.get(layer.path, headers=headers)
            response.raise_for_status()
            return response.content
