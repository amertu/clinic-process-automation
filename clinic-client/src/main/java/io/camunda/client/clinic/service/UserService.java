package io.camunda.client.clinic.service;

import io.camunda.client.clinic.dto.ApiResponse;
import io.camunda.client.clinic.dto.ProcessTask;
import io.camunda.client.clinic.dto.Treatment;
import io.camunda.client.clinic.dto.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class UserService {

    private final WebClient webClient;

    public UserService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<ResponseEntity<User>> registerUser(User user) {
        return webClient.post()
                .uri("/client/register")
                .bodyValue(user)
                .retrieve()
                .toEntity(User.class);
    }

    public Mono<Boolean> authenticateUser(User user) {
        return webClient.post()
                .uri("/client/authenticate")
                .bodyValue(user)
                .retrieve()
                .bodyToMono(Boolean.class)
                .defaultIfEmpty(false);
    }

    public Mono<ResponseEntity<ApiResponse<User>>> loginUser(User user) {
        return webClient.post()
                .uri("/client/login")
                .bodyValue(user)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<ApiResponse<User>>() {
                });
    }

    public Mono<ResponseEntity<ApiResponse<ProcessTask>>> getTaskInfo(String processInstanceId) {
        Map<String, String> payload = Map.of("processInstanceId", processInstanceId);
        return webClient.post()
                .uri("/client/task")
                .bodyValue(payload)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<ApiResponse<ProcessTask>>() {
                });
    }

    public Mono<ResponseEntity<String>> requestTreatment(Treatment treatment) {
        return webClient.post()
                .uri("/client/request-treatment")
                .bodyValue(treatment)
                .retrieve()
                .toEntity(String.class);
    }

}
