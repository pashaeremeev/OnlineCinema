package com.example.onlinecinema.requests.json;

import com.example.onlinecinema.entities.Movie;

import java.util.ArrayList;

public class MovieJson {

    private int idMovie;
    private String nameRu;
    private String nameOriginal;
    private ArrayList<String> countries;
    private ArrayList<String> genres;
    private int year;
    private String posterUrl;
    private String posterUrlPreview;
    private String stream;
    private String desc;

    public MovieJson(int idMovie, String nameRu, String nameOriginal, ArrayList<String> countries,
                     ArrayList<String> genres, int year, String posterUrl,
                     String posterUrlPreview, String stream, String desc) {
        this.idMovie = idMovie;
        this.nameRu = nameRu;
        this.nameOriginal = nameOriginal;
        this.countries = countries;
        this.genres = genres;
        this.year = year;
        this.posterUrl = posterUrl;
        this.posterUrlPreview = posterUrlPreview;
        this.stream = stream;
        this.desc = desc;
    }

    public int getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(int idMovie) {
        this.idMovie = idMovie;
    }

    public String getNameRu() {
        return nameRu;
    }

    public void setNameRu(String nameRu) {
        this.nameRu = nameRu;
    }

    public String getNameOriginal() {
        return nameOriginal;
    }

    public void setNameOriginal(String nameOriginal) {
        this.nameOriginal = nameOriginal;
    }

    public ArrayList<String> getCountries() {
        return countries;
    }

    public void setCountries(ArrayList<String> countries) {
        this.countries = countries;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getPosterUrlPreview() {
        return posterUrlPreview;
    }

    public void setPosterUrlPreview(String posterUrlPreview) {
        this.posterUrlPreview = posterUrlPreview;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Movie createMovie() {
        return new Movie(idMovie, nameRu, nameOriginal, countries, genres, year, posterUrl,
                posterUrlPreview, stream, desc, false);
    }
}
