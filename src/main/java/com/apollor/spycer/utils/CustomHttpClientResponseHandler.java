package com.apollor.spycer.utils;

import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;

import java.io.IOException;

public class CustomHttpClientResponseHandler implements HttpClientResponseHandler<ClassicHttpResponse> {

    @Override
    public ClassicHttpResponse handleResponse(ClassicHttpResponse response) throws IOException {

        return response;
    }
}
