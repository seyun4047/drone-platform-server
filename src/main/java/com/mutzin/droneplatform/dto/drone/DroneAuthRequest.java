package com.mutzin.droneplatform.dto.drone;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DroneAuthRequest {
    private String serial;
    private String device_name;
    private String token;
}