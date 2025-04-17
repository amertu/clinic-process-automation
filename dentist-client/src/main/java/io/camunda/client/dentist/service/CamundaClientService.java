package io.camunda.client.dentist.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class CamundaClientService {

    private final RestTemplate restTemplate;

    public CamundaClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${camunda.base-url}")
    private String camundaBaseUrl;

    public void startProcess() {
        String processKey = "user_process";
        String url = camundaBaseUrl + "/process-definition/key/" + processKey + "/start";

        Map<String, Object> variables = new HashMap<>();
        variables.put("patientName", Map.of("value", "John Doe", "type", "String"));
        variables.put("appointmentType", Map.of("value", "Checkup", "type", "String"));

        Map<String, Object> body = new HashMap<>();
        body.put("variables", variables);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
}
