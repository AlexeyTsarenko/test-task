package com.springApp.service.impl;

import com.springApp.entities.RequestEntity;
import com.springApp.exceptions.RequestException;
import com.springApp.models.RequestModel;
import com.springApp.repositories.RequestRepository;
import com.springApp.service.interfaces.RequestService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

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
    public ResponseEntity<String> save(RequestModel requestModel) {
        RequestEntity requestEntity = modelMapper.map(requestModel, RequestEntity.class);
        Optional<RequestEntity> optional = requestRepository.findFirstByTicket(requestModel.getTicket());
        if (optional.isEmpty()) {
            requestEntity.setStatus("UNPROCESSED");
            requestEntity = requestRepository.save(requestEntity);
            return new ResponseEntity<>(requestEntity.getId().toString(),HttpStatus.OK);
        }
        return new ResponseEntity<>("0", HttpStatus.CONFLICT);
    }

    @Override
    public RequestEntity findById(int id) {
        return requestRepository.findById(id).orElseThrow(() -> new RequestException("no such request"));
    }

    @Override
    public List<RequestEntity> getAllRequestsByClientId(int id) {
        return requestRepository.findAllByClient(id).stream().filter(x -> x.getDate().getTime() > System.currentTimeMillis())
                .collect(Collectors.toList());
    }

}
