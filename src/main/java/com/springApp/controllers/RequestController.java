package com.springApp.controllers;

import com.springApp.models.RequestModel;
import com.springApp.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class RequestController {
    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService){
        this.requestService = requestService;
    }

    @PostMapping("/saveRequest")
    Integer makeOrder(@RequestBody RequestModel requestModel) {
        return requestService.save(requestModel).getId();
    }
    @GetMapping("/getStatusForRequest")
    String getStatus(@RequestParam int id){
        return requestService.findById(id).getStatus();
    }
}

