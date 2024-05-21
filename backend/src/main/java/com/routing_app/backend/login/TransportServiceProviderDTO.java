package com.routing_app.backend.login;

public class TransportServiceProviderDTO {
    private int id;
    private String companyName;
    private String companyAddress;
    private String email;
    private String password;

    // Constructors
    public TransportServiceProviderDTO() {
    }

    public TransportServiceProviderDTO(int id, String companyName, String companyAddress, String email, String password) {
        this.id = id;
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.email = email;
        this.password = password;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
