package io.camunda.client.dentist.controller;

import io.camunda.client.dentist.service.CamundaClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/process")
public class ProcessController {


    private final CamundaClientService camundaClientService;

    public ProcessController(CamundaClientService camundaClientService) {
        this.camundaClientService = camundaClientService;
    }

    @PostMapping("/start")
    public ResponseEntity<String> start() {
        camundaClientService.startProcess();
        return ResponseEntity.ok("Started dentist_bpmn process.");
    }

}
