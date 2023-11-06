package com.example.onlinecinema.requests.json;

import java.util.ArrayList;

public class MoviesJsonModel {

    private ArrayList<MovieJson> movies;

    public MoviesJsonModel(ArrayList<MovieJson> movieJsons) {
        this.movies = movieJsons;
    }

    public ArrayList<MovieJson> getMovieJsons() {
        return movies;
    }

    public void setMovieJsons(ArrayList<MovieJson> movieJsons) {
        this.movies = movies;
    }
}
