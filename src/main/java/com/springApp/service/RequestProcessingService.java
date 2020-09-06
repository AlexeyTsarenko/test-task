package com.springApp.service;

import com.springApp.config.PropertyReader;
import com.springApp.entities.RequestEntity;
import com.springApp.repositories.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.IntStream;

@Service
@Scope("prototype")
public class RequestProcessingService implements Runnable{
    private final RequestRepository requestRepository;
    private final WebClient webClient;
    private final TaskExecutor taskExecutor;
    private static Object obj = new Object();
    @Autowired
    RequestProcessingService(RequestRepository requestRepository, WebClient.Builder webClientBuilder, TaskExecutor taskExecutor){
        //this.start();
        this.taskExecutor = taskExecutor;
        this.requestRepository = requestRepository;
        this.webClient = webClientBuilder.baseUrl(PropertyReader.getProperties("server.url")).build();
      //  int threadAmount = Integer.parseInt(PropertyReader.getProperties("thread.amount"));

        //for(int i = 0; i < 3; i++) {
          //  this.taskExecutor.execute(this::runTask);\
        //    this.taskExecutor.execute(new SomeTask(requestRepository));
      //  }
      //  Thread requestRepair = new Thread(this::repair);
      //  requestRepair.start();
    }
    /*private void repair(){
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(;;) {
            List<RequestEntity> reqList= requestRepository.findAllByStatus("STARTEDTOPROCESS");
            reqList.forEach(this::makeCallToProcessingService);
        }
    }*/
    @Override
    public void run(){
        for(;;) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Optional<RequestEntity> optional;
            synchronized (obj) {
                 optional = requestRepository.findFirstByStatusOrStatus("UNPROCESSED", "PROCESSING");

                if (optional.isPresent()) {
                    System.out.println("in the thread" + Thread.currentThread().getName());
                    System.out.println("working with " + optional.get().getId());
                    RequestEntity requestEntity = optional.get();
                    requestEntity.setStatus("STARTEDTOPROCESS");
                    System.out.println("end" + Thread.currentThread().getName());
                }
            }
            if(optional.isPresent()) {
                makeCallToProcessingService(optional.get());
            }
            else{
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
