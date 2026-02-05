package com.mutzin.droneplatform.service;

import com.mutzin.droneplatform.domain.Drone;
import com.mutzin.droneplatform.dto.AccessResult;
import com.mutzin.droneplatform.dto.TelemetryRequest;
import com.mutzin.droneplatform.dto.TelemetryResponse;
import com.mutzin.droneplatform.infrastructure.accesguard.AccessGuard;
import com.mutzin.droneplatform.infrastructure.logging.LogAppender;
import com.mutzin.droneplatform.state.DroneEventStore;
import com.mutzin.droneplatform.state.DroneStateStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TelemetryService {

    private final DroneStateStore droneStateStore;
    private final DroneEventStore droneEventStore;
    private final AccessGuard accessGuard;

    public TelemetryService(
            DroneStateStore droneStateStore,
            DroneEventStore droneEventStore,
            AccessGuard accessGuard
    ) {
        this.droneStateStore = droneStateStore;
        this.droneEventStore = droneEventStore;
        this.accessGuard = accessGuard;
    }

    @Transactional
    public TelemetryResponse handle(TelemetryRequest req) {
        String token = req.getToken();
        String serial = req.getSerial();
//      Valid Access
        AccessResult accessResult = accessGuard.handle(token, serial);
        if (!accessResult.isSuccess()) {
            return new TelemetryResponse(false, accessResult.getMessage());
        }

        Drone drone = accessResult.getDrone();
////        req.event == 1 -> save event data store
        if (req.getEvent() == 1) {
            String logPath = drone.getLogPath();
            LogAppender.prepend(logPath, req.toString());
            droneEventStore.addEvent(req);
            return new TelemetryResponse(
                    true,
                    "UPDATE_EVENT_DATA_serial: " + serial
            );
        }
////        req.event == 0 -> save telemetry data store
        droneStateStore.update(req);
        return new TelemetryResponse(
                true,
                "UPDATE_TELEMETRY_serial: " + serial
        );
    }
}