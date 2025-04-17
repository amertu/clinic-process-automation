package io.camunda.client.dentist.service;

import io.camunda.client.dentist.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {

    private static final String SERVER_URL = "http://localhost:8080"; // Camunda app

    private final RestTemplate restTemplate;

    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<User> registerUser(User user) {
        return restTemplate.postForEntity(SERVER_URL + "/client/register", user, User.class);
    }

    public ResponseEntity<Boolean> authenticateUser(User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> entity = new HttpEntity<>(user, headers);
        return restTemplate.postForEntity(SERVER_URL + "/client/authenticate", entity, Boolean.class);
    }

    public void startProcess(User user) {
        restTemplate.postForObject(SERVER_URL + "/client/start", user, User.class);
    }

    public ResponseEntity<User> startLogin(User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> entity = new HttpEntity<>(user, headers);

        try {
            return restTemplate.postForEntity(
                    SERVER_URL + "/client/login",
                    entity,
                   User.class
            );
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }





}
