package com.epam.dockercompscan.util;

import com.epam.dockercompscan.scan.domain.LayerScanResult;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.TimeUnit;

public class LayerScanCache {

    private static final long MAX_NUMBER_OF_LAYERS_PER_IMAGE = 127;
    private static final long SECONDS_IN_HOUR = 3600;

    private final Cache<String, String> byParents;

    private final Cache<String, LayerScanResult> byNames;

    public LayerScanCache(int expireCacheTime, int numberOfCachedScans) {
        byParents = CacheBuilder.newBuilder()
                .maximumSize(MAX_NUMBER_OF_LAYERS_PER_IMAGE * numberOfCachedScans)
                .expireAfterWrite(expireCacheTime * SECONDS_IN_HOUR + 1, TimeUnit.SECONDS)
                .build();
        byNames = CacheBuilder.newBuilder()
                .maximumSize(MAX_NUMBER_OF_LAYERS_PER_IMAGE * numberOfCachedScans)
                .expireAfterWrite(expireCacheTime * SECONDS_IN_HOUR + 1, TimeUnit.SECONDS)
                .build();
    }


    @Nullable
    public LayerScanResult getIfPresent(LayerKey key) {
        if (key.getName() != null) {
            return byNames.getIfPresent(key.getName());
        } else {
            String nameByParent = byParents.getIfPresent(key.getParentName());
            return nameByParent != null ? byNames.getIfPresent(nameByParent) : null;
        }
    }

    public synchronized void put(LayerKey key, LayerScanResult value) {
        if (key.getParentName() != null) {
            byParents.put(key.getParentName(), value.getLayerId());
        }
        if (key.getName() != null){
            byNames.put(key.getName(), value);
        }
    }

    public long size() {
        return byParents.size();
    }

    public void cleanUp() {
        byParents.cleanUp();
        byNames.cleanUp();
    }

}
