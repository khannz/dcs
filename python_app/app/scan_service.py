import asyncio
from pathlib import Path

from .models import ScanRequest, LayerScanResult, ImageScanResult, LayerKey
from .cache import LayerScanCache
from .docker_registry import DockerRegistryService
from .dependency_check import DependencyCheckService
from urllib.parse import unquote

class ScanService:
    def __init__(self, cache: LayerScanCache, registry: DockerRegistryService, checker: DependencyCheckService, working_dir: Path, concurrency: int = 4):
        self.cache = cache
        self.registry = registry
        self.checker = checker
        self.semaphore = asyncio.Semaphore(concurrency)
        self.working_dir = working_dir

    async def load_image_scan(self, layer_id: str) -> ImageScanResult:
        # decode any percent-encoded layer identifier (as in Java URLDecoder.decode)
        layer_id = unquote(layer_id)
        layers = []
        layer = self.cache.get_if_present(LayerKey.with_name(layer_id))
        if not layer:
            raise ValueError(f"Layer with id: {layer_id} not found")
        layers.append(layer)
        while layer.parentId:
            parent_id = layer.parentId
            layer = self.cache.get_if_present(LayerKey.with_name(parent_id))
            if not layer:
                raise ValueError(f"Layer with id: {parent_id} not found")
            layers.append(layer)
        return ImageScanResult(name=layer_id, layers=layers)

    async def scan(self, request: ScanRequest) -> LayerScanResult:
        # decode percent-encoded names consistent with Java LayerKey URL decoding
        to_scan = request.layer
        name = unquote(to_scan.name)
        parent = unquote(to_scan.parentName) if to_scan.parentName else None
        # ensure decoded values for cache key and result tracking
        to_scan.name, to_scan.parentName = name, parent
        key = LayerKey.create(name, parent)
        cached = self.cache.get_if_present(key)
        if cached and cached.status != "FAILURE":
            return cached

        result = LayerScanResult(layerId=to_scan.name, status="RUNNING", parentId=to_scan.parentName)
        self.cache.put(key, result)
        async with self.semaphore:
            try:
                blob = await self.registry.get_layer_blob(to_scan)
                deps = await self.checker.run_scan(blob)
                for d in deps:
                    d.layerId = to_scan.name
                result.dependencies = deps
                result.status = "SUCCESSFUL"
            except Exception:
                result.status = "FAILURE"
                raise
        return result
