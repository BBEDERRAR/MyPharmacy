package esi.tdm.mypharmacy.models;

import esi.tdm.mypharmacy.entity.User;

public class LoginResponse {

    private String token;
    private String message;
    private boolean error;
    private User user;


    public LoginResponse(String message, boolean error, User user, String token) {
        this.token = token;
        this.message = message;
        this.error = error;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
