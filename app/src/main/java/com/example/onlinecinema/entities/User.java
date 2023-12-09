package com.example.onlinecinema.entities;

public class User {

    private Integer idUser;
    private String username;
    private String password;
    private Boolean isActive;
    private Integer idRole;


    public User(String username, String password) {
        this.idUser = null;
        this.username = username;
        this.password = password;
        this.isActive = true;
        this.idRole = 2;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return idUser;
    }

    public void setId(int idUser) {
        this.idUser = idUser;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Integer getIdRole() {
        return idRole;
    }

    public void setIdRole(Integer idRole) {
        this.idRole = idRole;
    }
}
