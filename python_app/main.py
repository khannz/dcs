from fastapi import FastAPI, HTTPException

from app.config import Settings
from app.models import ScanRequest, ImageScanResult, LayerScanResult
from app.cache import LayerScanCache
from app.docker_registry import DockerRegistryService
from app.dependency_check import DependencyCheckService
from app.scan_service import ScanService

settings = Settings()

app = FastAPI()

cache = LayerScanCache(
    expire_hours=settings.cache_expire_hours,
    number_cached_scans=settings.cache_number_cached_scans,
)
registry = DockerRegistryService(insecure=settings.ssl_insecure_enable)
checker = DependencyCheckService()
scan_service = ScanService(
    cache,
    registry,
    checker,
    settings.working_dir,
    settings.concurrency,
)

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
