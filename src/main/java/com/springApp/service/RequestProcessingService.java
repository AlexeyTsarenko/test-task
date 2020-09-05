package com.springApp.service;

import com.springApp.config.PropertyReader;
import com.springApp.entities.RequestEntity;
import com.springApp.repositories.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Service
public class RequestProcessingService{
    private final RequestRepository requestRepository;
    private final WebClient webClient;
    private final TaskExecutor taskExecutor;
    @Autowired
    RequestProcessingService(RequestRepository requestRepository, WebClient.Builder webClientBuilder, TaskExecutor taskExecutor){
        //this.start();
        this.taskExecutor = taskExecutor;
        this.requestRepository = requestRepository;
        this.webClient = webClientBuilder.baseUrl(PropertyReader.getProperties("server.url")).build();
        this.taskExecutor.execute(this::run);

        Thread requestRepair = new Thread(this::repair);
    }
    private void repair(){
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(;;) {
            List<RequestEntity> reqList= requestRepository.findAllByStatus("STARTEDTOPROCESS");
            reqList.forEach(this::makeCallToProcessingService);
        }
    }
    @Async
    public void run(){
        for(;;) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Optional<RequestEntity> optional = requestRepository.findFirstByStatusOrStatus("UNPROCESSED", "PROCESSING");

            if (optional.isPresent()) {
                System.out.println("in the thread");
                RequestEntity requestEntity = optional.get();
                requestEntity.setStatus("STARTEDTOPROCESS");
                makeCallToProcessingService(requestEntity);
            }else{
                System.out.println("no requests " + Thread.currentThread().getName());
            }
        }
    }
    private void makeCallToProcessingService(RequestEntity requestEntity) {
        String response = this.webClient.get().uri(PropertyReader.getProperties("server.url")+"/process").retrieve().bodyToMono(String.class).block();
        requestEntity.setStatus(response);
        requestRepository.save(requestEntity);
    }
}
