package com.epam.dockercompscan.dockerregistry;

import com.epam.dockercompscan.scan.domain.ScanRequest;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

@Service
public class DockerRegistryService {

    @Autowired
    private OkHttpClient dockerRegistryClient;

    public InputStream getDockerLayerBlob(ScanRequest.Layer layer) throws IOException {

        Request request = new Request.Builder().url(layer.getPath())
                .headers(layer.getHeaders() != null
                        ? Headers.of(layer.getHeaders())
                        : Headers.of(Collections.emptyMap()))
                .build();
        Response response = dockerRegistryClient.newCall(request).execute();

        if (!response.isSuccessful() || response.body() == null) {
            response.close();
            throw new IOException("Couldn't get layer blob from: " + request.url().toString() + "; " +
                            "Cause " + response.message());
        }

        return response.body().byteStream();
    }

}
