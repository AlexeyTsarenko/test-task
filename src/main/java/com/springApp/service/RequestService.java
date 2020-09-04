package com.springApp.service;

import com.springApp.entities.RequestEntity;
import org.springframework.stereotype.Service;


public interface RequestService {
    Integer save(RequestEntity requestEntity);
}
