package com.mutzin.droneplatform.dto;
import lombok.Data;
import java.util.Map;

@Data
public class TelemetryRequest {
    private Integer event;
    private String serial;
    private String device;
    private String token;
    private Map<String, Object> data;
}