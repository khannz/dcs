package com.epam.dockercompscan.scan.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageScanResult {
    private String name;
    private List<LayerScanResult> layers;
}
