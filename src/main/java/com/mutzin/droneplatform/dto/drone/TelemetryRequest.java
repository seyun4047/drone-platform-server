package com.mutzin.droneplatform.dto.drone;
import lombok.Data;
import java.util.Map;

@Data
public class TelemetryRequest {
    private Integer event;
    private String serial;
    private String device;
    private String token;
    private String updatedAt;
    private Map<String, Object> data;

    public TelemetryRequest() {
        this.updatedAt = String.valueOf(System.currentTimeMillis());
    }
}