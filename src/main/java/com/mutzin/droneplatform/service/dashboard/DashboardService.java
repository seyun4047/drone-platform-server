package com.mutzin.droneplatform.service.dashboard;

import com.mutzin.droneplatform.dto.DroneEventResponse;
import com.mutzin.droneplatform.dto.DroneTelemetryResponse;
import com.mutzin.droneplatform.dto.TelemetryRequest;
import com.mutzin.droneplatform.state.DroneEventStore;
import com.mutzin.droneplatform.state.DroneTelemetryStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DashboardService {
    private DroneTelemetryStore droneTelemetryStore;
    private DroneEventStore droneEventStore;

    public DashboardService(DroneTelemetryStore droneTelemetryStore, DroneEventStore droneEventStore) {
        this.droneTelemetryStore = droneTelemetryStore;
        this.droneEventStore = droneEventStore;
    }

//    Get Drone State(Telemetry)
    public DroneTelemetryResponse getDroneState(String serial) {
        TelemetryRequest req = droneTelemetryStore.get(serial);
        if (req == null) return null;

        return new DroneTelemetryResponse(
                req.getData(),
                Long.parseLong(req.getUpdatedAt())
        );
    }
//    Get Drone Event Data
    public DroneEventResponse getLatestEvent(String serial) {
        List<TelemetryRequest> events = droneEventStore.getEvents(serial);

        if (events.isEmpty()) return null;

        TelemetryRequest latest = events.get(events.size() - 1);

        return new DroneEventResponse(
                latest.getData(),
                Long.parseLong(latest.getUpdatedAt())
        );
    }
}
