package com.example.ahmed.movieapp.Models;

/**
 * Created by Ahmed on 1/11/2018.
 */

public class ReviewModel {
    private String auth , rev ;

    public ReviewModel(String auth, String rev) {
        this.auth = auth;
        this.rev = rev;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }
}
