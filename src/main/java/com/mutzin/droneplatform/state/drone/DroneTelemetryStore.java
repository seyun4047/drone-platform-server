package com.mutzin.droneplatform.state.drone;

import com.mutzin.droneplatform.dto.drone.TelemetryRequest;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DroneTelemetryStore {

    private final Map<String, TelemetryRequest> latest = new ConcurrentHashMap<>();

    public void update(TelemetryRequest req) {
        latest.put(req.getSerial(), req);
    }

    public TelemetryRequest get(String serial) {
        return latest.get(serial);
    }
}