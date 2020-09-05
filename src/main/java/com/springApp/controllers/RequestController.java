package com.springApp.controllers;

import com.springApp.entities.RequestEntity;
import com.springApp.models.RequestModel;
import com.springApp.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RequestController {
    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService){
        this.requestService = requestService;
    }

    @PostMapping("/saveRequest")
    ResponseEntity<String> saveRequest(@RequestBody RequestModel requestModel) {
        return requestService.save(requestModel);
    }
    @GetMapping("/getStatusForRequest")
    String getStatus(@RequestParam int id){
        return requestService.findById(id).getStatus();
    }
    @GetMapping("/getAllRequestsByClientId")
    List<RequestEntity> getAllRequestsByClientId(@RequestParam int id){
        return requestService.getAllRequestsByClientId(id);
    }
}

