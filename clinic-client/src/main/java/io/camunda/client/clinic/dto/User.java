package io.camunda.client.clinic.dto;

import java.io.Serializable;

public class User implements Serializable {

    private Long id;

    private String email;

    private String userName;

    private String password;

    private String phoneNumber;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, String name, String password, String phoneNumber) {
        this.email = email;
        this.userName = name;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        this.userName = name;
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
