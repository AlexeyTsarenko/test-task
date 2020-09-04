package com.springApp.controllers;

import com.springApp.entities.RequestEntity;
import com.springApp.models.RequestModel;
import com.springApp.service.RequestService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestController {
    private final RequestService requestService;
    private final ModelMapper modelMapper;
    @Autowired
    public RequestController(RequestService requestService, ModelMapper modelMapper){
        this.requestService = requestService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/saveRequest")
    Integer makeOrder(@RequestBody RequestModel requestModel) {
        RequestEntity requestEntity = modelMapper.map(requestModel, RequestEntity.class);
        return requestService.save(requestEntity);
        //return new ResponseEntity<>(HttpStatus.OK);
    }
}

