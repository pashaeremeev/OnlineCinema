package com.example.onlinecinema.repos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.onlinecinema.entities.Movie;

import java.util.ArrayList;

public class MovieRepo {

    private static MutableLiveData<ArrayList<Movie>> movieMutableLiveData = new MutableLiveData<>(new ArrayList<Movie>());
    private static MutableLiveData<ArrayList<Movie>> downloadedMovieMutableLiveData = new MutableLiveData<>(new ArrayList<Movie>());
    private static MutableLiveData<ArrayList<Integer>> favMovieMutableLiveData = new MutableLiveData<>(new ArrayList<Integer>());

    public LiveData<ArrayList<Movie>> getMovieMutableLiveData() {
        return movieMutableLiveData;
    }

    public void setMovies(ArrayList<Movie> movies) {
        movieMutableLiveData.postValue(movies);
    }

    public LiveData<ArrayList<Movie>> getDownloadedMovieMutableLiveData() {
        return movieMutableLiveData;
    }

    public void setDownloadedMovies(ArrayList<Movie> movies) {
        downloadedMovieMutableLiveData.postValue(movies);
    }

    public LiveData<ArrayList<Integer>> getFavMovieMutableLiveData() {
        return favMovieMutableLiveData;
    }

    public void setFavMovies(ArrayList<Integer> movies) {
        favMovieMutableLiveData.postValue(movies);
    }

    public Movie getById(Integer id) {
        ArrayList<Movie> movies = movieMutableLiveData.getValue();
        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            if (movie.getId() == id.intValue()) {
                return movie;
            }
        }
        return null;
    }
}
