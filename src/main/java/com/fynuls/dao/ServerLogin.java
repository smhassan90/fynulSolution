package com.fynuls.dao;

import com.fynuls.entity.login.User;

public class ServerLogin {
    String token;
    User user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
