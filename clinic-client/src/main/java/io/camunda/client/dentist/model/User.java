package io.camunda.client.dentist.model;

public class User {

    private Long id;

    private String username;

    private String password;

    private String phoneNumber;

    public User(String name, String password) {
        this.username = name;
        this.password = password;
    }

    public User(String name, String password, String phoneNumber) {
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
