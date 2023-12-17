package com.example.onlinecinema.entities;

import androidx.annotation.Nullable;

import com.example.onlinecinema.requests.json.MovieJson;

import java.util.ArrayList;
import java.util.Objects;

public class Movie {

    private Integer idMovie;
    private String nameRu;
    private String nameOriginal;
    private ArrayList<String> countries;
    private ArrayList<String> genres;
    private Integer year;
    private String posterUrl;
    private String posterUrlPreview;
    private String stream;
    private String desc;
    private String duration;
    private Double rating;

    public Movie(String nameRu, String nameOriginal, ArrayList<String> countries,
                 ArrayList<String> genres, Integer year, String posterUrl, String posterUrlPreview,
                 String stream, String desc, String duration, Double rating) {
        this.idMovie = null;
        this.nameRu = nameRu;
        this.nameOriginal = nameOriginal;
        this.countries = countries;
        this.genres = genres;
        this.year = year;
        this.posterUrl = posterUrl;
        this.posterUrlPreview = posterUrlPreview;
        this.stream = stream;
        this.desc = desc;
        this.duration = duration;
        this.rating = rating;
    }

    public Integer getId() {
        return idMovie;
    }

    public void setId(Integer idMovie) {
        this.idMovie = idMovie;
    }

    public String getNameRu() {
        return nameRu;
    }

    public void setFirstName(String firstName) {
        this.nameRu = firstName;
    }

    public String getOrigName() {
        return nameOriginal;
    }

    public void setOrigName(String nameOriginal) {
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

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getPoster() {
        return posterUrl;
    }

    public void setPoster(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getPreview() {
        return posterUrlPreview;
    }

    public void setPreviewUrl(String preview) {
        this.posterUrlPreview = preview;
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

    public void setDescription(String description) {
        this.desc = description;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Double getRating() {
        return rating;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMovie, nameRu, nameOriginal, countries, genres, year, posterUrl, posterUrlPreview, stream);
    }

    public MovieJson toJson() {
        return new MovieJson(this.idMovie,  this.nameRu, this.nameOriginal,
                this.countries,
                this.genres, this.year, this.posterUrl,
                this.posterUrlPreview, this.stream, this.desc, this.duration, this.rating);
    }

    public int compareTo(Movie movie) {
        return Double.compare(this.getRating(), movie.getRating());
    }
}
