package com.juvare.ipms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juvare.ipms.contract.AuthenticationRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({SpringExtension.class})
@AutoConfigureMockMvc
@SpringBootTest
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldThrowExceptionWhenLoginInfoIsNull() throws Exception {
        var request = new AuthenticationRequest();

        mockMvc.perform(
                post("/auth")
                        .content(new ObjectMapper().writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowExceptionWhenLoginInfoIsEmpty() throws Exception {
        var request = new AuthenticationRequest("", "");

        mockMvc.perform(
                post("/auth")
                        .content(new ObjectMapper().writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowExceptionWhenLoginInfoIsIncorrect() throws Exception {
        var request = new AuthenticationRequest("incorrect", "incorrect");

        mockMvc.perform(
                post("/auth")
                        .content(new ObjectMapper().writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{'message':'Incorrect credentials'}"));
    }

    @Test
    void shouldReturnOkWhenLoginInfoIsCorrect() throws Exception {
        var request = new AuthenticationRequest("admin", "admin");

        mockMvc.perform(
                post("/auth")
                        .content(new ObjectMapper().writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").exists())
                .andExpect(jsonPath("$.expiration_in_minutes").exists());
    }
}