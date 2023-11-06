package com.example.onlinecinema.repos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.onlinecinema.entities.Movie;

import java.util.ArrayList;

public class MovieRepo {

    private static MutableLiveData<ArrayList<Movie>> movieMutableLiveData = new MutableLiveData<>(new ArrayList<Movie>());

    public LiveData<ArrayList<Movie>> getMovieMutableLiveData() {
        return movieMutableLiveData;
    }

    public void setMovies(ArrayList<Movie> movies ) {
        movieMutableLiveData.postValue(movies);
    }

    public Movie getById(int id) {
        ArrayList<Movie> movies = movieMutableLiveData.getValue();
        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            if (movie.getId() == id) {
                return movie;
            }
        }
        return null;
    }
}
