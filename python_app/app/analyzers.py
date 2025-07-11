import re
from pathlib import Path
from typing import List

from .models import Dependency

OS_RELEASE_FILES = [
    "etc/os-release",
    "usr/lib/os-release",
    "etc/redhat-release",
    "etc/system-release",
    "etc/centos-release",
]

CUDA_PATTERN = re.compile(r"usr/local/cuda-[^/]+/targets/x86_64-linux/lib/libcuda.*\.so.*")


def analyze_os_version(temp_dir: Path) -> List[Dependency]:
    deps: List[Dependency] = []
    for rel in OS_RELEASE_FILES:
        file_path = temp_dir / rel
        if file_path.exists():
            content = file_path.read_text(errors="ignore")
            name = None
            version = None
            # parse os-release files differently from legacy release files
            if file_path.name == "os-release":
                m = re.search(r"^ID=?\"?([^\n\"]*)", content, re.MULTILINE)
                if m:
                    name = m.group(1).strip().lower()
                m = re.search(r"^VERSION_ID=?\"?([^\n\"]*)", content, re.MULTILINE)
                if m:
                    version = m.group(1).strip()
            else:
                # legacy release files: extract name as first token and version number pattern
                m = re.match(r"([^ ]+)", content)
                if m:
                    name = m.group(1).strip().lower()
                m = re.search(r"([\d\.\-_]+)", content)
                if m:
                    version = m.group(1).strip()
            if name and version:
                deps.append(Dependency(name=name, version=version, ecosystem="OS"))
                break
    return deps


def analyze_r_packages(temp_dir: Path) -> List[Dependency]:
    deps: List[Dependency] = []
    for desc_file in temp_dir.rglob("DESCRIPTION"):
        try:
            text = desc_file.read_text(errors="ignore")
        except Exception:
            continue
        if "Built: R" in text:
            # normalize continuation lines and extract metadata
            contents = re.sub(r"\n\s+", " ", text)
            pkg = re.search(r"^Package:\s*([^\n]*)", contents, re.MULTILINE)
            ver = re.search(r"^Version:\s*([^\n]*)", contents, re.MULTILINE)
            desc = re.search(r"^Description:\s*([^\n]*)", contents, re.MULTILINE)
            if pkg and ver:
                deps.append(
                    Dependency(
                        name=pkg.group(1),
                        version=ver.group(1),
                        ecosystem="R.Pkg",
                        description=desc.group(1) if desc else None
                    )
                )
    return deps


def analyze_cuda(temp_dir: Path) -> List[Dependency]:
    deps: List[Dependency] = []
    for path in temp_dir.rglob("*"):
        if CUDA_PATTERN.search(str(path)):
            deps.append(Dependency(name="NvidiaCuda", ecosystem="Nvidia"))
            break
    return deps

def analyze_python_packages(temp_dir: Path) -> List[Dependency]:
    """
    Detect installed Python packages by scanning for .dist-info and .egg-info directories.
    """
    deps: List[Dependency] = []
    # look for Python site-packages directories
    for site_dir in temp_dir.rglob("site-packages"):
        # .dist-info directories
        for dist_info in site_dir.rglob("*.dist-info"):
            pkg = dist_info.name[:-len(".dist-info")]
            if "-" in pkg:
                name, version = pkg.rsplit("-", 1)
                deps.append(Dependency(name=name, version=version, ecosystem="PyPI"))
        # .egg-info directories
        for egg_info in site_dir.rglob("*.egg-info"):
            pkg = egg_info.name[:-len(".egg-info")]
            if "-" in pkg:
                name, version = pkg.rsplit("-", 1)
                deps.append(Dependency(name=name, version=version, ecosystem="PyPI"))
    return deps


def analyze_all(temp_dir: Path) -> List[Dependency]:
    deps: List[Dependency] = []
    deps.extend(analyze_os_version(temp_dir))
    deps.extend(analyze_python_packages(temp_dir))
    deps.extend(analyze_r_packages(temp_dir))
    deps.extend(analyze_cuda(temp_dir))
    return deps
