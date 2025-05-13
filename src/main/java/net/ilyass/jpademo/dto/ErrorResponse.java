package net.ilyass.jpademo.dto;

import lombok.Data;

@Data
public class ErrorResponse {
    private String message;
    private String timestamp;

    public ErrorResponse(String message) {
        this.message = message;
        this.timestamp = new java.util.Date().toString();
    }
}
