package com.epam.dockercompscan;

import com.epam.dockercompscan.util.LayerScanCache;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;


@SpringBootConfiguration
@ComponentScan(basePackages = {"com.epam.dockercompscan.dockerregistry", "com.epam.dockercompscan.owasp",
        "com.epam.dockercompscan.scan"})
@TestPropertySource(value={"classpath:test-application.properties"})
public class TestApplication {

    @Value("${expire.cached.scan.time:24}")
    private int expireCacheTime;

    @Value("${number.cached.scans:50}")
    private int numberOfCachedScans;

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

    @Bean
    public OkHttpClient dockerRegistryClient() {
        return new OkHttpClient();
    }

    @Bean
    public LayerScanCache layerScanCache() {
        return new LayerScanCache(expireCacheTime, numberOfCachedScans);
    }

}

