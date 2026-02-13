package com.mutzin.droneplatform.dto.drone;

public record DroneAuthResponse(boolean status, String message, String token) {
    public DroneAuthResponse(boolean status, String message) {
        this(status, message, null);
    }
}
