package com.springApp.service.impl;

import com.springApp.service.ProcessService;
import org.springframework.stereotype.Service;

@Service
public class ProcessServiceImpl implements ProcessService {

    @Override
    public String getStatus() {
        return "wtf";
    }
}
