package com.epam.dockercompscan.scan;

import com.epam.dockercompscan.scan.domain.ImageScanResult;
import com.epam.dockercompscan.scan.domain.LayerScanResult;
import com.epam.dockercompscan.scan.domain.ScanRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScanController {

    @Autowired
    private ScanService scanService;

    @GetMapping("/scan/{id:.+}")
    public ImageScanResult getImageScanResult(@PathVariable String id) {
        return scanService.loadImageScan(id);
    }

    @PostMapping("/scan")
    public LayerScanResult scan(@RequestBody ScanRequest request) {
        return scanService.scan(request);
    }

}
