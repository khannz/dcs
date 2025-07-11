package com.epam.dockercompscan.scan.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class LayerScanResult {

    public LayerScanResult(String layerId, Status status, String parentId) {
        this.layerId = layerId;
        this.status = status;
        this.parentId = parentId;
        this.dependencies = Collections.emptyList();
    }

    private String layerId;
    private Status status;
    private String parentId;

    private List<Dependency> dependencies;

    public enum Status {
        RUNNING, SUCCESSFUL, FAILURE
    }

}
