package com.mutzin.droneplatform.service;

import com.mutzin.droneplatform.domain.Drone;
import com.mutzin.droneplatform.dto.TelemetryRequest;
import com.mutzin.droneplatform.dto.TelemetryResponse;
import com.mutzin.droneplatform.infrastructure.logging.LogAppender;
import com.mutzin.droneplatform.repository.DroneRepository;
import com.mutzin.droneplatform.repository.RedisTokenRepository;
import com.mutzin.droneplatform.state.DroneEventStore;
import com.mutzin.droneplatform.state.DroneStateStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TelemetryService {
    private final DroneStateStore droneStateStore;
    private final DroneRepository droneRepository;
    private final RedisTokenRepository redisTokenRepository;
    private final DroneEventStore droneEventStore;
    public TelemetryService(DroneStateStore droneStateStore, DroneRepository droneRepository, RedisTokenRepository redisTokenRepository, DroneEventStore droneEventStore) {
        this.droneStateStore = droneStateStore;
        this.droneRepository = droneRepository;
        this.redisTokenRepository = redisTokenRepository;
        this.droneEventStore = droneEventStore;
    }
    @Transactional
    public TelemetryResponse handle(TelemetryRequest req) {
        String token = req.getToken();
        String serial = req.getSerial();
        Optional<Drone> optionalDrone = droneRepository.findBySerial(serial);
//        0. IS CONNECTING?
        Drone drone = optionalDrone.get();
        if(!drone.isConnecting()){
            return new TelemetryResponse(false, "NO_CONNECTING_DEVICE");
        }

//        1.req.token is in redisTokenRepository? -> NO_AUTH_TOKEN
        if (token == null) {
            return new TelemetryResponse(false, "NO_AUTH_TOKEN");
        }

//        2.req.token is valid -> INVALID_OR_EXPIRED_TOKEN
        String redisSerial = redisTokenRepository.getSerialByToken(token);
        if (redisSerial == null) {
            return new TelemetryResponse(false, "INVALID_OR_EXPIRED_TOKEN");
        }

//        3.req.serial == redis.serial? -> SERIAL_MISMATCH
        if (!redisSerial.equals(serial)) {
            return new TelemetryResponse(false, "SERIAL_MISMATCH");
        }

//        4.VALID
//        update updateAt
        drone.setUpdatedAt(LocalDateTime.now());
        droneRepository.save(drone);
//        req.event == 1 -> save event data store
        if (req.getEvent() == 1) {
//            APPEND log
//      where logfile?
            String logPath = drone.getLogPath();
            LogAppender.prepend(logPath, req.toString());
            droneEventStore.addEvent(req);
            return new TelemetryResponse(true, "UPDATE_EVENT_DATA_serial: "+serial);
        }
//
//        req.event == 0 -> save telemetry data store
        droneStateStore.update(req);
        return new TelemetryResponse(true, "UPDATE_TELEMETRY_serial: "+serial);
    }

}