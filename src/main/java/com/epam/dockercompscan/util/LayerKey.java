package com.epam.dockercompscan.util;

import lombok.Getter;
import lombok.Setter;

import java.net.URLDecoder;

@Getter
@Setter
public final class LayerKey {
    private String name;
    private String parentName;

    private LayerKey(String name, String parentName) {
        this.name =  name != null ? URLDecoder.decode(name) : null;
        this.parentName = parentName != null ? URLDecoder.decode(parentName) : null;
    }

    public static LayerKey withParent(String parentName) {
        return new LayerKey(null, parentName);
    }

    public static LayerKey withName(String name) {
        return new LayerKey(name, null);
    }

    public static LayerKey create(String name, String parentName) {
        return new LayerKey(name, parentName);
    }
}
