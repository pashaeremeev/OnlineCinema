package com.example.onlinecinema.requests;

import com.example.onlinecinema.requests.json.MoviesJsonModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface InterfaceAPI {

//    @GET("uO-l-H")
//        Call<MoviesJsonModel> getData();

    @GET("0bf8812caa54a565ce54")
    Call<MoviesJsonModel> getData();
}
