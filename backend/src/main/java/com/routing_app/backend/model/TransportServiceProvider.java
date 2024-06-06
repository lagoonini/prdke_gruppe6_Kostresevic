package com.routing_app.backend.model;

import jakarta.persistence.*;
@Entity
@Table(name="transport_service_provider")
public class TransportServiceProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id", length = 45)
    private Long id;

    @Column(name="company_name", length = 255)
    private String companyName;

    @Column(name="company_address", length = 255)
    private String companyAddress;

    @Column(name="email", length = 255)
    private String email;

    @Column(name="password", length = 255)
    private String password;

    // Constructors
    public TransportServiceProvider() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
