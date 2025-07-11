import tempfile
import tarfile
import io
from pathlib import Path
from typing import List

from .analyzers import analyze_all
from .models import Dependency

class DependencyCheckService:
    async def run_scan(self, blob: bytes) -> List[Dependency]:
        with tempfile.TemporaryDirectory() as tmpdir:
            with tarfile.open(fileobj=io.BytesIO(blob), mode="r:gz") as tar:
                tar.extractall(path=tmpdir)
            deps = analyze_all(Path(tmpdir))
            return deps
