package com.routing_app.backend.login;

public class LoginMessage {
    private String message;
    private Boolean status;

    // Constructors
    public LoginMessage() {
    }

    public LoginMessage(String message, Boolean status) {
        this.message = message;
        this.status = status;
    }

    // Getters
    public String getMessage() {
        return message;
    }

    public Boolean getStatus() {
        return status;
    }

    // Setters
    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}


