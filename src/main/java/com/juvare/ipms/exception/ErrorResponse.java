package com.juvare.ipms.exception;

import lombok.Data;

import java.util.List;

@Data
public class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }
}
