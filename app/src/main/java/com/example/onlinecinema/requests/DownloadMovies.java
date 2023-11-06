package com.example.onlinecinema.requests;

import com.example.onlinecinema.entities.Movie;
import com.example.onlinecinema.repos.MovieRepo;
import com.example.onlinecinema.requests.json.MovieJson;
import com.example.onlinecinema.requests.json.MoviesJsonModel;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadMovies {

    private static final int REFRESH_TIME = 900000;
    private static ScheduledExecutorService scheduler;

    public static void downloadChannels(MovieRepo movieRepo) {
        scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(new Runnable()
        {
            public void run() {
                RetrofitClient.getInstance().getApi().getData().enqueue(new Callback<MoviesJsonModel>() {
                    @Override
                    public void onResponse(Call<MoviesJsonModel> call, Response<MoviesJsonModel> response) {
                        if (response.isSuccessful()) {
                            ArrayList<MovieJson> movieJsons = response.body().getMovieJsons();
                            ArrayList<Movie> movies = new ArrayList<>();
                            for (int i = 0; i < movieJsons.size(); i++) {
                                MovieJson movieJson = movieJsons.get(i);
                                Movie movie = movieJson.createMovie();
                                movies.add(movie);
                            }
                            movieRepo.setMovies(movies);
                        }
                    }

                    @Override
                    public void onFailure(Call<MoviesJsonModel> call, Throwable t) {

                    }
                });
                //Log.d("TAG", "1");
            }
        }, 0, REFRESH_TIME, TimeUnit.MILLISECONDS);
    }
}
