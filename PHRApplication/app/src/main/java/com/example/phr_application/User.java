package com.example.phr_application;

public class User {

    public String email, fullName, password, walletName;


    public User(){

    }

    public User(String fullName, String email, String password, String walletName) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.walletName = walletName;
    }
}
