package com.springApp.service;

import com.springApp.entities.RequestEntity;
import com.springApp.repositories.RequestRepository;
import com.springApp.service.impl.RequestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

@Service
public class RequestProcessingService extends Thread {
    private final RequestRepository requestRepository;
    private final WebClient webClient;

    @Autowired
    RequestProcessingService(RequestRepository requestRepository, WebClient.Builder webClientBuilder){
        this.start();
        this.requestRepository = requestRepository;
        this.webClient = webClientBuilder.baseUrl(getProperties()).build();
    }
    private String getProperties() {
        Properties prop = null;
        try {
            prop = RequestServiceImpl.readPropertiesFile("src/main/resources/application.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert prop != null;
        return prop.getProperty("server.url");
    }

    @Override
   public void run(){
        for(;;) {
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Optional<RequestEntity> optional = requestRepository.findFirstByStatusOrStatusOrStatus("UNPROCESSED", "PROCESSING", "STARTEDTOPROCESS");

            if (optional.isPresent()) {
                System.out.println("in the thread");
                RequestEntity requestEntity = optional.get();
                requestEntity.setStatus("STARTEDTOPROCESS");
                String response = this.webClient.get().uri(getProperties()+"/process").retrieve().bodyToMono(String.class).block();
                requestEntity.setStatus(response);
                requestRepository.save(requestEntity);
            }else{
                System.out.println("no requests");
            }
        }
    }
}
