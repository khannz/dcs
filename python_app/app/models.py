from __future__ import annotations
from typing import Dict, List, Optional
from pydantic import BaseModel

class ScanRequestLayer(BaseModel):
    name: str
    path: str
    headers: Optional[Dict[str, str]] = None
    parentName: Optional[str] = None

class ScanRequest(BaseModel):
    layer: ScanRequestLayer

class Dependency(BaseModel):
    layerId: Optional[str] = None
    version: Optional[str] = None
    name: Optional[str] = None
    ecosystem: Optional[str] = None
    description: Optional[str] = None

class LayerScanResult(BaseModel):
    layerId: str
    status: str
    parentId: Optional[str] = None
    dependencies: List[Dependency] = []

class ImageScanResult(BaseModel):
    name: str
    layers: List[LayerScanResult]

class LayerKey(BaseModel):
    name: Optional[str] = None
    parentName: Optional[str] = None

    @classmethod
    def with_parent(cls, parent_name: str) -> "LayerKey":
        return cls(parentName=parent_name)

    @classmethod
    def with_name(cls, name: str) -> "LayerKey":
        return cls(name=name)

    @classmethod
    def create(cls, name: str, parent_name: Optional[str]) -> "LayerKey":
        return cls(name=name, parentName=parent_name)
