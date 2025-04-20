package io.camunda.client.dentist.service;

import io.camunda.client.dentist.dto.ApiResponse;
import io.camunda.client.dentist.dto.ProcessTaskDto;
import io.camunda.client.dentist.dto.Treatment;
import io.camunda.client.dentist.dto.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class UserService {

    private static final String SERVER_URL = "http://localhost:8080"; // Camunda app

    private final RestTemplate restTemplate;

    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<User> registerUser(User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);  // Ensure the Content-Type is JSON
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));  // Accept JSON response

        HttpEntity<User> requestEntity = new HttpEntity<>(user, headers);
        return restTemplate.exchange(SERVER_URL + "/client/register", HttpMethod.POST, requestEntity, User.class);
    }

    public boolean authenticateUser(User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);  // Ensure the Content-Type is JSON
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));  // Accept JSON response
        HttpEntity<User> requestEntity = new HttpEntity<>(user, headers);
        Boolean result = restTemplate.exchange(SERVER_URL + "/client/authenticate", HttpMethod.POST, requestEntity, Boolean.class).getBody();

        return Boolean.TRUE.equals(result);
    }

    public ResponseEntity<ApiResponse<User>> loginUser(User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<User> request = new HttpEntity<>(user, headers);

        return restTemplate.exchange(
                SERVER_URL + "/client/login",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<ApiResponse<User>>() {}
        );
    }

    public ResponseEntity<ApiResponse<ProcessTaskDto>> getTaskInfo(String processInstanceId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(processInstanceId, headers);
        return restTemplate.exchange(
                SERVER_URL + "/client/task",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<ApiResponse<ProcessTaskDto>>() {}
        );
    }

    public ResponseEntity<String> requestTreatment(Treatment treatment) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);  // Ensure the Content-Type is JSON
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));  // Accept JSON response
        HttpEntity<Treatment> requestEntity = new HttpEntity<>(treatment, headers);
        return restTemplate.exchange(SERVER_URL + "/client/request-treatment", HttpMethod.POST, requestEntity, String.class);
    }



}
