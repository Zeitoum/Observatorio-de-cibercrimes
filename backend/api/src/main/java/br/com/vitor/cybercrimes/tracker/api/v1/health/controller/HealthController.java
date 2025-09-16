package br.com.vitor.cybercrimes.tracker.api.v1.health.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1/health")
public class HealthController {

    @GetMapping("/status-check")
    public String statusCheck() {
        return "OK";
    }
}
