package io.camunda.server.clinic.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email format is invalid")
    @Column(unique = true, nullable = false)
    private String email;

    private String userName;

    private String password;

    private String phoneNumber;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, String username, String password, String phoneNumber) {
        this.email = email;
        this.userName = username;
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

    public void setUserName(String userName) {
        this.userName = userName;
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