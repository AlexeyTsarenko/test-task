package com.springApp.service;

import com.springApp.config.PropertyReader;
import com.springApp.entities.RequestEntity;
import com.springApp.repositories.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

@Service
public class RequestProcessingService {
    private final RequestRepository requestRepository;
    private final WebClient webClient;
    private final CyclicBarrier cyclicBarrier;

    @Autowired
    RequestProcessingService(RequestRepository requestRepository, WebClient.Builder webClientBuilder, TaskExecutor taskExecutor) {
        this.requestRepository = requestRepository;
        this.webClient = webClientBuilder.baseUrl(PropertyReader.getProperties("server.url")).build();
        int threadAmount = Integer.parseInt(PropertyReader.getProperties("thread.amount"));

        for (int i = 0; i < threadAmount; i++) {
            taskExecutor.execute(this::runProcessingTask);
        }
        cyclicBarrier = new CyclicBarrier(threadAmount, this::repair);
    }

    private void repair() {
        System.out.println("repairing");
        List<RequestEntity> reqList = requestRepository.findAllByStatus("STARTEDTOPROCESS");
        reqList.forEach(this::makeCallToProcessingService);
    }

    public void runProcessingTask() {
        for (; ; ) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Optional<RequestEntity> optional;
            synchronized (requestRepository) {
                optional = requestRepository.findFirstByStatusOrStatus("UNPROCESSED", "PROCESSING");
                if (optional.isPresent()) {
                    System.out.println("in the thread" + Thread.currentThread().getName());
                    System.out.println("working with " + optional.get().getId());
                    RequestEntity requestEntity = optional.get();
                    requestEntity.setStatus("STARTEDTOPROCESS");
                    requestRepository.save(requestEntity);
                    System.out.println("end" + Thread.currentThread().getName());
                }
            }
            if (optional.isPresent()) {
                makeCallToProcessingService(optional.get());
            } else {
                System.out.println("no requests " + Thread.currentThread().getName());
            }
            try {
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
    private void makeCallToProcessingService(RequestEntity requestEntity) {
        String response = this.webClient.get().uri(PropertyReader.getProperties("server.url") + "/process").retrieve().bodyToMono(String.class).block();
        requestEntity.setStatus(response);
        requestRepository.save(requestEntity);
    }
}
