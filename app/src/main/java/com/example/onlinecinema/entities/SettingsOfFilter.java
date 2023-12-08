package com.example.onlinecinema.entities;

public class SettingsOfFilter {

    private String genre;

    private String country;

    private Integer year;

    private Float minRating;

    private Float maxRating;

    private Integer typeOfSort;

    public SettingsOfFilter() {

    }

    public SettingsOfFilter(String genre, String country, Integer year,
                            Float minRating, Float maxRating, Integer typeOfSort) {
        this.genre = genre;
        this.country = country;
        this.year = year;
        this.minRating = minRating;
        this.maxRating = maxRating;
        this.typeOfSort = typeOfSort;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Float getMinRating() {
        return minRating;
    }

    public void setMinRating(Float minRating) {
        this.minRating = minRating;
    }

    public Float getMaxRating() {
        return maxRating;
    }

    public void setMaxRating(Float maxRating) {
        this.maxRating = maxRating;
    }

    public Integer getTypeOfSort() {
        return typeOfSort;
    }

    public void setTypeOfSort(Integer typeOfSort) {
        this.typeOfSort = typeOfSort;
    }

}
