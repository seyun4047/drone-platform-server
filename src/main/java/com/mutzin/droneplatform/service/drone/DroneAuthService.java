package com.mutzin.droneplatform.service.drone;

import com.mutzin.droneplatform.domain.drone.Drone;
import com.mutzin.droneplatform.dto.drone.AccessResponse;
import com.mutzin.droneplatform.dto.drone.DroneAuthRequest;
import com.mutzin.droneplatform.dto.drone.DroneAuthResponse;
import com.mutzin.droneplatform.infrastructure.accesguard.AccessGuard;
import com.mutzin.droneplatform.infrastructure.logging.LogAppender;
import com.mutzin.droneplatform.infrastructure.logging.LogPathCreator;
import com.mutzin.droneplatform.infrastructure.aws.S3StsClient;
import com.mutzin.droneplatform.repository.drone.DroneRepository;
import com.mutzin.droneplatform.repository.drone.RedisTokenRepository;
import com.mutzin.droneplatform.repository.drone.RedisHeartbeatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class DroneAuthService {
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
    private final AccessGuard accessGuard;
    private final RedisHeartbeatRepository redisHeartbeatRepository;
    private final S3StsClient s3StsClient;

    public DroneAuthService(DroneRepository droneRepository, RedisTokenRepository redisTokenRepository, AccessGuard accessGuard, RedisHeartbeatRepository redisHeartbeatRepository, S3StsClient s3StsClient){
        this.droneRepository = droneRepository;
        this.redisTokenRepository = redisTokenRepository;
        this.accessGuard = accessGuard;
        this.redisHeartbeatRepository = redisHeartbeatRepository;
        this.s3StsClient = s3StsClient;
    }

///     Connect Drone
    @Transactional
    public DroneAuthResponse connectDrone(String serial, String deviceName) {
///      1.  serial in DB?
        Optional<Drone> optionalDrone = droneRepository.findBySerial(serial);
        if(optionalDrone.isEmpty()) return new DroneAuthResponse(false, "DRONE_NOT_FOUND");

        Drone drone = optionalDrone.get();

///        2. device name == db device name?
        if(!drone.getName().equals(deviceName)) return new DroneAuthResponse(false, "DEVICE_NAME_MISMATCH");

///        3. check level | 0->block 1->good
        if(drone.getLevel()==0) return new DroneAuthResponse(false, "DRONE_BLOCKED");

///        4. Token in REDIS?
        String existingToken = redisTokenRepository.getTokenlBySerial(serial);
        if(existingToken != null) {
///            5. Token in REDIS but Connecting==0? -> ghost
            if(!drone.isConnecting()) redisTokenRepository.deleteBySerial(serial);
            else return new DroneAuthResponse(false, "DEVICE_ALREADY_CONNECTED");
        }

///         STS client
        Map<String, Object> newSts = s3StsClient.getTemporaryCredentials(serial);
        if(newSts == null){
            return new DroneAuthResponse(false, "S3_STS_ERROR");
        };

///        6. CREATE NEW TOKEN
        String newToken = redisTokenRepository.saveToken(serial, TOKEN_TTL_SECONDS);

///        7. DB UPDATE Connecting -> 1
        drone.setConnecting(true);
        drone.setLastConnectTime(LocalDateTime.now());

///        8.save Log
        Path logPath = LogPathCreator.create("logs/", serial, true);
        LogAppender.prepend(logPath.toString(), "CONNECT");
///        9. Connecting Success
        drone.setLogPath(logPath.toString());
        droneRepository.save(drone);
        //        UPDATE HEARTBEAT
        redisHeartbeatRepository.heartbeat(serial);
        return new DroneAuthResponse(true, "SUCCESS_CONNECT", newToken, newSts);
    }

///         Disconnect Drone
    @Transactional
    public DroneAuthResponse disconnect(DroneAuthRequest droneAuthRequest) {
        String serial = droneAuthRequest.getSerial();
        String token = droneAuthRequest.getToken();
        Optional<Drone> optionalDrone = droneRepository.findBySerial(serial);

///         if you don't have monitoring server->cannot use this method(It can invalid->ghost session)
        if(!token.equals(redisTokenRepository.getTokenlBySerial(serial))){
        return new DroneAuthResponse(false, "TOKEN INVALID", token);
        }

        if(optionalDrone.isPresent()) {
            Drone drone = optionalDrone.get();
            drone.setConnecting(false);
            droneRepository.save(drone);

            redisTokenRepository.deleteBySerial(serial);
            return new DroneAuthResponse(true, "DISCONNECT:"+serial, token);
        }
        Path logPath = LogPathCreator.create("logs/", serial, true);
        LogAppender.prepend(logPath.toString(), "DISCONNECT");
        return new DroneAuthResponse(false, "DISCONNECT ERROR", token);
    }

///         Update Token
    @Transactional
    public DroneAuthResponse update(DroneAuthRequest droneAuthRequest) {
        if (droneAuthRequest == null ||
                droneAuthRequest.getSerial() == null ||
                droneAuthRequest.getToken() == null) {
            return new DroneAuthResponse(false, "INVALID_REQUEST");
        }
        String serial = droneAuthRequest.getSerial();
        String token = droneAuthRequest.getToken();
        AccessResponse AccessResponse = accessGuard.handle(token, serial);
        if (!AccessResponse.isSuccess()) {
            return new DroneAuthResponse(false, AccessResponse.getMessage());
        }
        String newToken = redisTokenRepository.updateTokenBySerial(serial, TOKEN_TTL_SECONDS);
        //        UPDATE HEARTBEAT
        redisHeartbeatRepository.heartbeat(serial);
        return new DroneAuthResponse(true, "UPDATE TOKEN", newToken);
    }
}

