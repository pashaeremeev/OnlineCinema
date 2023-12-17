package com.example.onlinecinema.requests;

import androidx.annotation.NonNull;

import com.example.onlinecinema.entities.Movie;
import com.example.onlinecinema.entities.User;
import com.example.onlinecinema.repos.MovieRepo;
import com.example.onlinecinema.repos.UserRepo;
import com.example.onlinecinema.requests.json.MovieJson;
import com.example.onlinecinema.requests.json.MoviesJsonModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadUserFromDB {

    private static final int REFRESH_TIME = 900000;
    private static ScheduledExecutorService scheduler;

    public static void downloadMovies(UserRepo userRepo) {
        scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(new Runnable()
        {
            public void run() {
                RestClient client = RestClient.getInstance();
                Request request = client.createGetRequest("/users/" + userRepo.getLastUser().getId());
                client.getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String jsonData = response.body().string();
                            User user = new Gson().fromJson(jsonData, User.class);
                            userRepo.setUser(user);
                        }
                    }
                });
            }
        }, 0, REFRESH_TIME, TimeUnit.MILLISECONDS);
    }
}
