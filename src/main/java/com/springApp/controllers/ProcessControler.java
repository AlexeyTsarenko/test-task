package com.springApp.controllers;

import com.springApp.services.interfaces.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProcessControler {
    private final ProcessService processService;

    @Autowired
    public ProcessControler(ProcessService processService) {
        this.processService = processService;
    }

    @GetMapping("/process")
    String getStatus() {
        return processService.getStatus();
    }
}
