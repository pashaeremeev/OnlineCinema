package com.example.onlinecinema.entities;

public class FavOfMovie {

    private Integer idUser;
    private Integer idMovie;
    private Boolean isFav;

    public FavOfMovie(Integer idUser, Integer idMovie, Boolean isFav) {
        this.idUser = idUser;
        this.idMovie = idMovie;
        this.isFav = isFav;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public Integer getIdMovie() {
        return idMovie;
    }

    public Boolean getFav() {
        return isFav;
    }
}
