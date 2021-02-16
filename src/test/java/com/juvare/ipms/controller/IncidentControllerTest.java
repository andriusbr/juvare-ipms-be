package com.juvare.ipms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juvare.ipms.contract.AuthenticationRequest;
import com.juvare.ipms.contract.IncidentRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@ExtendWith({SpringExtension.class})
@AutoConfigureMockMvc
@SpringBootTest
class IncidentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldThrowExceptionWhenIncidentInfoIsEmpty() throws Exception {
        var request = new IncidentRequest();
        String accessToken = obtainAccessToken();

        mockMvc.perform(
                post("/incident")
                        .header("Authorization", "Bearer " + accessToken)
                        .content(new ObjectMapper().writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowExceptionWhenIncidentStartDateIsTomorrow() throws Exception {
        var tomorrow = getDate(1);
        var request = new IncidentRequest();
        request.setName("test");
        request.setStartDate(tomorrow);

        String accessToken = obtainAccessToken();

        mockMvc.perform(
                post("/incident")
                        .header("Authorization", "Bearer " + accessToken)
                        .content(new ObjectMapper().writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnOkWhenIncidentStartDateIsToday() throws Exception {
        var today = getDate(0);
        var request = new IncidentRequest();
        request.setName("test");
        request.setStartDate(today);

        String accessToken = obtainAccessToken();

        mockMvc.perform(
                post("/incident")
                        .header("Authorization", "Bearer " + accessToken)
                        .content(new ObjectMapper().writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnOkWhenIncidentStartDateIsYesterday() throws Exception {
        var yesterday = getDate(-1);
        var request = new IncidentRequest();
        request.setName("test");
        request.setStartDate(yesterday);

        String accessToken = obtainAccessToken();

        mockMvc.perform(
                post("/incident")
                        .header("Authorization", "Bearer " + accessToken)
                        .content(new ObjectMapper().writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void unAuthorizedExceptionWhenBearerTokenIsEmptyPost() throws Exception {
        var request = new IncidentRequest();
        String accessToken = "";

        mockMvc.perform(
                post("/incident")
                        .header("Authorization", "Bearer " + accessToken)
                        .content(new ObjectMapper().writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void unAuthorizedWhenBearerTokenIsEmptyGet() throws Exception {
        String accessToken = "";

        mockMvc.perform(
                get("/incident")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnOkWithoutQueryParams() throws Exception {
        String accessToken = obtainAccessToken();

        mockMvc.perform(
                get("/incident")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.numberOfElements").exists())
                .andExpect(jsonPath("$.content").exists());
    }

    @Test
    void shouldReturnOkWithQueryParams() throws Exception {
        String accessToken = obtainAccessToken();

        mockMvc.perform(
                get("/incident?size=20&page=0&query=test&sort=name,desc")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.numberOfElements").exists())
                .andExpect(jsonPath("$.content").exists());
    }

    private Date getDate(int addDays){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, addDays);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    private String obtainAccessToken() throws Exception {
        var request = new AuthenticationRequest("admin", "admin");

        ResultActions result  = mockMvc.perform(
                post("/auth")
                        .content(new ObjectMapper().writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON));

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }

}