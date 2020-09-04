package com.springApp.service.impl;

import com.springApp.entities.RequestEntity;
import com.springApp.repositories.RequestRepository;
import com.springApp.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository){
        this.requestRepository = requestRepository;
    }

    @Override
    public Integer save(RequestEntity requestEntity) {
        return requestRepository.save(requestEntity).getId();
    }
}
