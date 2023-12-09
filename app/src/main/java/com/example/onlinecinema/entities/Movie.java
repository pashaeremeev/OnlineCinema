package com.example.onlinecinema.entities;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Objects;

public class Movie {

    private Integer id;
    private String firstName;
    private String origName;
    private ArrayList<String> countries;
    private ArrayList<String> genres;
    private int year;
    private String poster;
    private String preview;
    private String stream;
    private String description;
    private boolean isFavorite;

    public Movie(Integer id, String firstName, String origName, ArrayList<String> countries,
                 ArrayList<String> genres, int year, String poster, String preview,
                 String stream, String description, boolean isFavorite) {
        this.id = null;
        this.firstName = firstName;
        this.origName = origName;
        this.countries = countries;
        this.genres = genres;
        this.year = year;
        this.poster = poster;
        this.preview = preview;
        this.stream = stream;
        this.description = description;
        this.isFavorite = isFavorite;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getOrigName() {
        return origName;
    }

    public void setOrigName(String origName) {
        this.origName = origName;
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

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreviewUrl(String preview) {
        this.preview = preview;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, origName, countries, genres, year, poster, preview, stream);
    }
}
