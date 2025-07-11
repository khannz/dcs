package com.epam.dockercompscan.scan.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class ScanRequest {

    private Layer layer;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Layer {
        private String name;
        private String path;
        private Map<String, String> headers;
        private String parentName;

        @Override
        public String toString() {
            return "Layer{" +
                    "name='" + name + '\'' +
                    ", path='" + path + '\'' +
                    ", parentName='" + parentName + '\'' +
                    '}';
        }
    }

}
