package com.example.onlinecinema.entities;

public class MarkOfMovie {

    private Integer id;
    private Integer idMovie;
    private Integer idUser;
    private Integer mark;

    public MarkOfMovie(Integer idMovie, Integer idUser, Integer mark) {
        this.id = null;
        this.idMovie = idMovie;
        this.idUser = idUser;
        this.mark = mark;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public Integer getIdMovie() {
        return idMovie;
    }

    public Integer getMark() {
        return mark;
    }
}
