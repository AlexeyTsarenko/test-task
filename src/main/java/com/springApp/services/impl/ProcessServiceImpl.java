package com.springApp.services.impl;

import com.springApp.services.interfaces.ProcessService;
import org.springframework.stereotype.Service;

@Service
public class ProcessServiceImpl implements ProcessService {

    @Override
    public String getStatus() {
        int a = (int) (Math.random() * 3);
        switch (a) {
            case 0:
                return "PROCESSING";
            case 1:
                return "ERROR";
            case 2:
                return "PROCESSED";
        }
        return null;
    }
}

