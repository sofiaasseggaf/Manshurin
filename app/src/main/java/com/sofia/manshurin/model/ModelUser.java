package com.sofia.manshurin.model;

public class ModelUser {

    int id_user;
    String username, password;

    public ModelUser(int id_user, String username, String password) {
        this.id_user = id_user;
        this.username = username;
        this.password = password;
    }


    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
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

}
