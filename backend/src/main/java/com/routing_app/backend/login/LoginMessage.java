package com.routing_app.backend.login;

public class LoginMessage {
    private String message;
    private boolean success;
    private Long providerId;

    // Constructors
    public LoginMessage() {
    }

    public LoginMessage(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public LoginMessage(String message, boolean success, Long providerId) {
        this.message = message;
        this.success = success;
        this.providerId = providerId;
    }

    // Getters
    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public Long getProviderId() {
        return providerId;
    }

    // Setters
    public void setMessage(String message) {
        this.message = message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }
}
