package com.springApp;

import com.springApp.entities.RequestEntity;
import com.springApp.models.RequestModel;
import com.springApp.services.impl.RequestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotSame;

@SpringBootTest
public class RequestProcessingTest {

    @Autowired
    private RequestServiceImpl requestService;

    private RequestModel requestModel;

    @BeforeEach
    void setUp() {

        requestModel = new RequestModel();
        requestModel.setClient(1);
        requestModel.setDate(new Date());
        requestModel.setRoot(1);
        requestModel.setTicket(1);
    }

    @Test
    public void processingTest() throws InterruptedException {
        requestService.save(requestModel);
        Thread.sleep(63000);
        RequestEntity requestEntity = requestService.findById(1);
        assertNotSame("UNPROCESSED", requestEntity.getStatus());
    }
}
