package io.camunda.client.dentist.dto;

import java.io.Serializable;

public class User implements Serializable {

    private Long id;

    private String email;

    private String username;

    private String password;

    private String phoneNumber;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, String name, String password, String phoneNumber) {
        this.email = email;
        this.username = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public User() {
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password){
        this.password=password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
