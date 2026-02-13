package com.mutzin.droneplatform.dto.dashboard;

public record AuthResponse(boolean status, String message, Object data) {
    public AuthResponse(boolean status, String message) {
        this(status, message, null);
    }
}