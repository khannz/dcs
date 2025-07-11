from fastapi import FastAPI, HTTPException
from pathlib import Path

from app.models import ScanRequest
from app.models import ImageScanResult, LayerScanResult
from app.cache import LayerScanCache
from app.docker_registry import DockerRegistryService
from app.dependency_check import DependencyCheckService
from app.scan_service import ScanService

app = FastAPI()

cache = LayerScanCache(expire_hours=24, number_cached_scans=50)
registry = DockerRegistryService()
checker = DependencyCheckService()
scan_service = ScanService(cache, registry, checker, Path("/tmp"))

@app.post("/scan", response_model=LayerScanResult)
async def scan(request: ScanRequest):
    try:
        return await scan_service.scan(request)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.get("/scan/{layer_id}", response_model=ImageScanResult)
async def image_scan(layer_id: str):
    try:
        return await scan_service.load_image_scan(layer_id)
    except ValueError as e:
        # layer not found: client error
        raise HTTPException(status_code=404, detail=str(e))
    except Exception as e:
        # unexpected missing parent or other server error
        raise HTTPException(status_code=500, detail=str(e))
