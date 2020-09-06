package com.springApp.services.interfaces;

import com.springApp.entities.RequestEntity;
import com.springApp.models.RequestModel;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface RequestService {
    ResponseEntity<String> save(RequestModel requestModel);

    RequestEntity findById(int id);

    List<RequestEntity> getAllRequestsByClientId(int id);
}
