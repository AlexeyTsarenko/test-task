package com.springApp.services;

import com.springApp.config.PropertyReader;
import com.springApp.entities.RequestEntity;
import com.springApp.repositories.RequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
public class RequestProcessingService {
    private final RequestRepository requestRepository;
    private final WebClient webClient;
    private final int timeToWait = 60000;
    private final Logger logger = LoggerFactory.getLogger(RequestProcessingService.class);


    @Autowired
    RequestProcessingService(RequestRepository requestRepository, WebClient.Builder webClientBuilder, TaskExecutor taskExecutor) {
        this.requestRepository = requestRepository;
        this.webClient = webClientBuilder.baseUrl(PropertyReader.getProperties("server.url")).build();
        int threadAmount = Integer.parseInt(PropertyReader.getProperties("thread.amount"));

        for (int i = 0; i < threadAmount; i++) {
            taskExecutor.execute(this::runProcessingTask);
        }
    }


    synchronized public void runProcessingTask() {
        for (; ; ) {
            Optional<RequestEntity> optional;
            optional = requestRepository.findFirstByStatusOrStatusOrStatus("UNPROCESSED", "PROCESSING", "STARTEDTOPROCESS");
            if (optional.isPresent()) {
                logger.info("processing request " + Thread.currentThread().getName());
                RequestEntity requestEntity = optional.get();
                requestEntity.setStatus("STARTEDTOPROCESS");
                requestRepository.save(requestEntity);
                makeCallToProcessingService(optional.get());
            }
            try {
                logger.info(Thread.currentThread().getName() + " sleeping");
                Thread.sleep(timeToWait);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void makeCallToProcessingService(RequestEntity requestEntity) {
        String response = this.webClient.get().uri("/process").retrieve().bodyToMono(String.class).block();
        requestEntity.setStatus(response);
        requestRepository.save(requestEntity);
    }
}
