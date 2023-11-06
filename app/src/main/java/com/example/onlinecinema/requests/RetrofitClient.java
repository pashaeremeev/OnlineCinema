package com.example.onlinecinema.requests;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    //private String BASE_URL = "https://api.jsonserve.com/";
    private String BASE_URL = "https://api.npoint.io/";
    private Retrofit retrofit;
    private static RetrofitClient retrofitClient;

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static RetrofitClient getInstance() {
        if (retrofitClient == null) {
            retrofitClient = new RetrofitClient();
        }
        return retrofitClient;
    }

    public InterfaceAPI getApi() {
        return retrofit.create(InterfaceAPI.class);
    }
}
