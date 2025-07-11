from cachetools import TTLCache
from typing import Optional

from .models import LayerKey, LayerScanResult

class LayerScanCache:
    def __init__(self, expire_hours: int, number_cached_scans: int):
        max_layers_per_image = 127
        ttl_seconds = expire_hours * 3600 + 1
        max_size = max_layers_per_image * number_cached_scans
        self.by_parents = TTLCache(maxsize=max_size, ttl=ttl_seconds)
        self.by_names = TTLCache(maxsize=max_size, ttl=ttl_seconds)

    def get_if_present(self, key: LayerKey) -> Optional[LayerScanResult]:
        if key.name:
            return self.by_names.get(key.name)
        if key.parentName:
            name = self.by_parents.get(key.parentName)
            return self.by_names.get(name) if name else None
        return None

    def put(self, key: LayerKey, value: LayerScanResult) -> None:
        if key.parentName:
            self.by_parents[key.parentName] = value.layerId
        if key.name:
            self.by_names[key.name] = value

    def clean_up(self) -> None:
        self.by_parents.expire()
        self.by_names.expire()
