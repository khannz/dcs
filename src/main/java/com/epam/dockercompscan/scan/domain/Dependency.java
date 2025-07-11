package com.epam.dockercompscan.scan.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Dependency {

    private String layerId;
    private String version;
    private String name;
    private String ecosystem;
    private String description;

    public static Dependency convert(org.owasp.dependencycheck.dependency.Dependency dependency){
        Dependency converted = new Dependency();
        converted.setName(dependency.getName());
        converted.setVersion(dependency.getVersion());
        converted.setEcosystem(dependency.getEcosystem());
        converted.setDescription(dependency.getDescription());
        return converted;
    }

}
