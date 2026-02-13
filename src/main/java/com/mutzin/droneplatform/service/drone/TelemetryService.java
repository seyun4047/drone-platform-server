package com.mutzin.droneplatform.service.drone;

import com.mutzin.droneplatform.domain.drone.Drone;
import com.mutzin.droneplatform.dto.drone.AccessResponse;
import com.mutzin.droneplatform.dto.drone.TelemetryRequest;
import com.mutzin.droneplatform.dto.drone.TelemetryResponse;
import com.mutzin.droneplatform.infrastructure.accesguard.AccessGuard;
import com.mutzin.droneplatform.infrastructure.logging.LogAppender;
import com.mutzin.droneplatform.state.drone.DroneEventStore;
import com.mutzin.droneplatform.repository.drone.RedisHeartbeatRepository;
import com.mutzin.droneplatform.state.drone.DroneTelemetryStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TelemetryService {

    private final DroneTelemetryStore droneTelemetryStore;
    private final DroneEventStore droneEventStore;
    private final AccessGuard accessGuard;
    private final RedisHeartbeatRepository redisHeartbeatRepository;

    public TelemetryService(
            DroneTelemetryStore droneTelemetryStore,
            DroneEventStore droneEventStore,
            AccessGuard accessGuard, RedisHeartbeatRepository redisHeartbeatRepository
    ) {
        this.droneTelemetryStore = droneTelemetryStore;
        this.droneEventStore = droneEventStore;
        this.accessGuard = accessGuard;
        this.redisHeartbeatRepository = redisHeartbeatRepository;
    }

    @Transactional
    public TelemetryResponse handle(TelemetryRequest req) {
        String token = req.getToken();
        String serial = req.getSerial();
//      Valid Access
        AccessResponse AccessResponse = accessGuard.handle(token, serial);
        if (!AccessResponse.isSuccess()) {
            return new TelemetryResponse(false, AccessResponse.getMessage());
        }
//        UPDATE HEARTBEAT
        redisHeartbeatRepository.heartbeat(serial);
        Drone drone = AccessResponse.getDrone();
////        req.event == 1 -> save event data store
        if (req.getEvent() == 1) {
            String logPath = drone.getLogPath();
            LogAppender.prepend(logPath, req.toString());
            System.out.println(req.toString());
            droneEventStore.addEvent(req);
            return new TelemetryResponse(
                    true,
                    "UPDATE_EVENT_DATA_serial: " + serial
            );
        }
////        req.event == 0 -> save telemetry data store
        droneTelemetryStore.update(req);
        System.out.println(req.toString());
        return new TelemetryResponse(
                true,
                "UPDATE_TELEMETRY_serial: " + serial
        );
    }
}