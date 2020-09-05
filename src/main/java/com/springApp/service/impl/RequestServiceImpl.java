package com.springApp.service.impl;

import com.springApp.entities.RequestEntity;
import com.springApp.exceptions.RequestException;
import com.springApp.models.RequestModel;
import com.springApp.repositories.RequestRepository;
import com.springApp.service.RequestService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Service
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository, ModelMapper modelMapper) {
        this.requestRepository = requestRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public RequestEntity save(RequestModel requestModel) {
        RequestEntity requestEntity = modelMapper.map(requestModel, RequestEntity.class);
        requestEntity.setStatus("UNPROCESSED");
        return requestRepository.save(requestEntity);
    }

    @Override
    public RequestEntity findById(int id) {
        return requestRepository.findById(id).orElseThrow(() -> new RequestException("no such request"));
    }

    public static Properties readPropertiesFile(String fileName) throws IOException {
        FileInputStream fis = null;
        Properties prop = null;
        try {
            fis = new FileInputStream(fileName);
            prop = new Properties();
            prop.load(fis);
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            assert fis != null;
            fis.close();
        }
        return prop;
    }

}
