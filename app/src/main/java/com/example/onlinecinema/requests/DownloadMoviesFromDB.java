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
import okhttp3.RequestBody;
import okhttp3.Response;

public class DownloadMoviesFromDB {

    private static final int REFRESH_TIME = 900000;
    private static ScheduledExecutorService scheduler;

    public static void downloadMovies(MovieRepo movieRepo) {
        scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(new Runnable()
        {
            public void run() {
                RestClient client = RestClient.getInstance();
                Request request = client.createGetRequest("/movies/");
                client.getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            Type type = new TypeToken<ArrayList<Movie>>(){}.getType();
                            String jsonData = response.body().string();
                            ArrayList<Movie> movies = new Gson().fromJson(jsonData, type);
                            movieRepo.setMovies(movies);
                            //DownloadFavMovies.downloadMovies(movieRepo);
                        }
                    }
                });
                /*User user = UserRepo.getCurrentUser().getValue();
                if (user != null) {
                    RestClient favClient = RestClient.getInstance();
                    Request favRequest = favClient.createGetRequest("/movies/fav/" + user.getId());
                    favClient.getClient().newCall(favRequest).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {

                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            if (response.isSuccessful()) {
                                Type type = new TypeToken<ArrayList<Integer>>(){}.getType();
                                String jsonData = response.body().string();
                                ArrayList<Integer> favMovies = new Gson().fromJson(jsonData, type);
                                movieRepo.setFavMovies(favMovies);
                            }
                        }
                    });
                }*/
            }
        }, 0, REFRESH_TIME, TimeUnit.MILLISECONDS);
    }
}
