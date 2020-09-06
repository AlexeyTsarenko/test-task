package com.springApp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springApp.models.RequestModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RequestIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    private RequestModel requestModel;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
        requestModel = new RequestModel();
        requestModel.setClient(1);
        requestModel.setDate(new Date());
        requestModel.setRoot(1);
        requestModel.setTicket(1);
    }

    @Order(1)
    @Test
    public void creationTest() throws Exception {
        performPost(requestModel);
    }

    @Order(2)
    @Test
    public void getRequestStatusTest() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/getStatusForRequest")
                .contentType("application/json")
                .param("id", "1")).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        assertEquals(result, "UNPROCESSED");
    }

    @Order(3)
    @Test
    public void postingSameDataShouldReturn409() throws Exception {
        mvc.perform(post("/saveRequest")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(requestModel)))
                .andExpect(status().isConflict());
    }

    @Order(4)
    @Test
    public void getAllRequestsByClientIdTest() throws Exception {
        performSomePosts();
        MvcResult mvcResult = mvc.perform(get("/getAllRequestsByClientId")
                .contentType("application/json")
                .param("id", "1")).andReturn();
        String result = mvcResult.getResponse().getContentAsString();

        List<Date> dateList = getDatesFromResult(result);
        for (int i = 0; i < dateList.size() - 1; i++) {
            assertTrue(dateList.get(i).getTime() < dateList.get(i + 1).getTime());

        }
        assertThatIdIs1(result);

    }

    private void performSomePosts() throws Exception {
        for (int i = 2; i < 5; i++) {
            requestModel.setTicket(i);
            requestModel.setRoot(i);
            requestModel.setClient(1);
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse("21/10/202" + i);
            requestModel.setDate(date);
            performPost(requestModel);
        }
    }

    private void assertThatIdIs1(String result) throws JSONException {
        JSONArray json = new JSONArray(result);

        for (int i = 0; i < json.length(); i++) {
            String str = json.getJSONObject(i).getString("client");
            assertEquals(1, Integer.parseInt(str));
        }
    }

    private List<Date> getDatesFromResult(String result) throws JSONException, ParseException {
        JSONArray json = new JSONArray(result);
        List<Date> dateList = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            String strDate = json.getJSONObject(i).getString("date");
            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(strDate);
            dateList.add(date);
        }
        return dateList;
    }


    private void performPost(RequestModel requestModel) throws Exception {
        mvc.perform(post("/saveRequest")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(requestModel)))
                .andExpect(status().isCreated());
    }
}
