package com.example.onlinecinema.requests;

import java.nio.charset.StandardCharsets;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RestClient {

    private static String baseUrl = "http://192.168.0.19:8080";
    private static String headers = "application/json; charset=utf-8";
    private OkHttpClient client;
    private static RestClient restClient;

    public static RestClient getInstance() {
        if (restClient == null) {
            restClient = new RestClient();
        }
        return restClient;
    }

    private RestClient() {
        this.client = new OkHttpClient();
    }

    public RequestBody createRequestBody(String jsonData) {
        byte[] bytes = jsonData.getBytes(StandardCharsets.UTF_8);
        return RequestBody.create(bytes,MediaType.parse(headers));
    }

    public Request createPostRequest(String endpoint, RequestBody body) {
        return new Request.Builder()
                .url(baseUrl + endpoint)
                .post(body)
                .build();
    }

    public Request createPutRequest(String endpoint, RequestBody body) {
        return new Request.Builder()
                .url(baseUrl + endpoint)
                .put(body)
                .build();
    }

    public Request createGetRequest(String endpoint) {
        return new Request.Builder()
                .url(baseUrl + endpoint)
                .build();
    }

    public OkHttpClient getClient() {
        return client;
    }
}
