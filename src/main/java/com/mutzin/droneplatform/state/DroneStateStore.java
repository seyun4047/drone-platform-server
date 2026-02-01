package com.mutzin.droneplatform.state;

import com.mutzin.droneplatform.dto.TelemetryRequest;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DroneStateStore {

    private final Map<String, TelemetryRequest> latest = new ConcurrentHashMap<>();

    public void update(TelemetryRequest req) {
        latest.put(req.getSerial(), req);
    }

    public TelemetryRequest get(String serial) {
        return latest.get(serial);
    }
}