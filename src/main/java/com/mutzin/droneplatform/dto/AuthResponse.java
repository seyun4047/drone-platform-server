package com.mutzin.droneplatform.dto;


public record AuthResponse(boolean status, String message, String token) {
    public AuthResponse(boolean status, String message) {
        this(status, message, null);
    }
}
