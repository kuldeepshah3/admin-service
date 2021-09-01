package com.oracle.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthcheckController {

    /**
     * Heathcheck method to verify availability of service
     *
     * @return SUCCESS message
     */
    @GetMapping(value = "/check")
    public String performHealthCheck() {
        return "SUCCESS";
    }

}
