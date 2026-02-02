package com.mutzin.droneplatform.service;

import com.mutzin.droneplatform.domain.Drone;
import com.mutzin.droneplatform.dto.AuthRequest;
import com.mutzin.droneplatform.dto.AuthResponse;
import com.mutzin.droneplatform.infrastructure.logging.LogAppender;
import com.mutzin.droneplatform.infrastructure.logging.LogPathCreator;
import com.mutzin.droneplatform.repository.DroneRepository;
import com.mutzin.droneplatform.repository.RedisTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    public enum AuthResult {
        OK("CONNECTED"),
        DRONE_NOT_FOUND("DRONE_NOT_FOUND"),
        DEVICE_NAME_MISMATCH("Device name mismatch"),
        DRONE_BLOCKED("Drone is blocked"),
        DEVICE_ALREADY_CONNECTED("Device already connected");

        private final String message;

        AuthResult(String message) {
            this.message = message;
        }

        public String message() {
            return message;
        }
    }
    private final DroneRepository droneRepository;
    private final RedisTokenRepository redisTokenRepository;
    private final long TOKEN_TTL_SECONDS = 3600;

    public AuthService(DroneRepository droneRepository, RedisTokenRepository redisTokenRepository){
        this.droneRepository = droneRepository;
        this.redisTokenRepository = redisTokenRepository;
    }

    @Transactional
    public AuthResponse connectDrone(String serial, String deviceName) {
///      1.  serial in DB?
        Optional<Drone> optionalDrone = droneRepository.findBySerial(serial);
        if(optionalDrone.isEmpty()) return new AuthResponse(false, "DRONE_NOT_FOUND");

        Drone drone = optionalDrone.get();

///        2. device name == db device name?
        if(!drone.getName().equals(deviceName)) return new AuthResponse(false, "DEVICE_NAME_MISMATCH");

///        3. check level | 0->block 1->good
        if(drone.getLevel()==0) return new AuthResponse(false, "DRONE_BLOCKED");

///        4. Token in REDIS?
        String existingToken = redisTokenRepository.getTokenlBySerial(serial);
        if(existingToken != null) {
///            5. Token in REDIS but Connecting==0? -> ghost
            if(!drone.isConnecting()) redisTokenRepository.deleteBySerial(serial);
            else return new AuthResponse(false, "DEVICE_ALREADY_CONNECTED");
        }
///        6. CREATE NEW TOKEN
        String newToken = UUID.randomUUID().toString();
        redisTokenRepository.saveToken(newToken, serial, TOKEN_TTL_SECONDS);

///        7. DB UPDATE Connecting -> 1
        drone.setConnecting(true);
        drone.setLastConnectTime(LocalDateTime.now());

///        8.save Log
        Path logPath = LogPathCreator.create("logs/", serial, true);
        LogAppender.prepend(logPath.toString(), "CONNECT");
///        9. Connecting Success
        drone.setLogPath(logPath.toString());
        drone.setUpdatedAt(LocalDateTime.now());
        droneRepository.save(drone);
        return new AuthResponse(true, "SUCCESS_CONNECT", newToken);
    }

    @Transactional
    public AuthResponse disconnected(AuthRequest authRequest) {
        String serial = authRequest.getSerial();
        String token = authRequest.getToken();
        Optional<Drone> optionalDrone = droneRepository.findBySerial(serial);

///         if you don't have monitoring server->cannot use this method(It can invalid->ghost session)
//            if(token != redisTokenRepository.getTokenlBySerial(serial)){
//            return "TOKEN INVALID";
//            return new AuthResponse(false, "TOKEN INVALID", token);
//        }
        if(optionalDrone.isPresent()) {
            Drone drone = optionalDrone.get();
            drone.setConnecting(false);
            droneRepository.save(drone);

            redisTokenRepository.deleteBySerial(serial);
            System.out.println("Drone disconnected: " + serial);
            return new AuthResponse(true, "DISCONNECT");
        }
        return new AuthResponse(false, "DISCONNECT ERROR", token);
    }

}
