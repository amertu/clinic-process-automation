package io.camunda.client.dentist.service;

import io.camunda.client.dentist.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Service
public class UserService {

    private static final String SERVER_URL = "http://localhost:8080"; // Camunda app

    private final RestTemplate restTemplate;

    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void registerUser(User user) {
        restTemplate.postForObject(SERVER_URL + "/client/register", user, User.class);
    }

    public void authenticateUser() {
        restTemplate.postForObject(SERVER_URL + "/client/authenticate", true, Boolean.class);
    }

    public void startProcess(User user) {
        restTemplate.postForObject(SERVER_URL + "/client/start", user, User.class);
    }

    public void login() {
        restTemplate.postForObject(SERVER_URL + "/client/login", true, Boolean.class);
    }

    public ResponseEntity<HashMap> startLogin(User user) {
        return restTemplate.getForEntity(SERVER_URL + "/client/login?name=" + user.getName(), HashMap.class);
    }





}
