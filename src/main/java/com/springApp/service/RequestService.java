package com.springApp.service;

import com.springApp.entities.RequestEntity;
import com.springApp.models.RequestModel;
import org.springframework.stereotype.Service;


public interface RequestService {
    RequestEntity save(RequestModel requestModel);

    RequestEntity findById(int id);
}
