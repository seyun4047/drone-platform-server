package com.mutzin.droneplatform.state;

import com.mutzin.droneplatform.dto.TelemetryRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DroneEventStore {

    private final Map<String, List<TelemetryRequest>> events = new ConcurrentHashMap<>();

    public void addEvent(TelemetryRequest req) {
        events
                .computeIfAbsent(req.getSerial(), k -> new ArrayList<>())
                .add(req);
    }

    public List<TelemetryRequest> getEvents(String serial) {
        return events.getOrDefault(serial, List.of());
    }
}
