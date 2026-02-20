package com.mutzin.droneplatform.dto.drone;

import java.util.Map;

public record DroneAuthResponse(boolean status, String message, String token, Map<String, Object> sts) {
    public DroneAuthResponse(boolean status, String message) {
        this(status, message, null, null);
    }
    public DroneAuthResponse(boolean status, String message, String token) {
        this(status, message, token, null);
    }
}
