package com.springApp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@AutoConfigureMockMvc
@SpringBootTest
public class ProcessStatusTest {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void getStatusTest() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/process")
                .contentType("application/json"))
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        assertTrue(result.equals("PROCESSED") || result.equals("ERROR") || result.equals("PROCESSING"));
    }
}
