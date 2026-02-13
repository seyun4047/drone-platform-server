package com.mutzin.droneplatform.infrastructure.accesguard;

import com.mutzin.droneplatform.domain.drone.Drone;
import com.mutzin.droneplatform.dto.drone.AccessResponse;
import com.mutzin.droneplatform.repository.drone.DroneRepository;
import com.mutzin.droneplatform.repository.drone.RedisHeartbeatRepository;
import com.mutzin.droneplatform.repository.drone.RedisTokenRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class AccessGuard {

    private final DroneRepository droneRepository;
    private final RedisTokenRepository redisTokenRepository;
    private final RedisHeartbeatRepository redisHeartbeatRepository;

    public AccessGuard(
            DroneRepository droneRepository,
            RedisTokenRepository redisTokenRepository, RedisHeartbeatRepository redisHeartbeatRepository
    ) {
        this.droneRepository = droneRepository;
        this.redisTokenRepository = redisTokenRepository;
        this.redisHeartbeatRepository = redisHeartbeatRepository;
    }

    @Transactional(readOnly = true)
    public AccessResponse handle(String token, String serial) {

        Optional<Drone> optionalDrone = droneRepository.findBySerial(serial);
        if (optionalDrone.isEmpty()) {
            return AccessResponse.fail("DRONE_NOT_FOUND");
        }
        Drone drone = optionalDrone.get();
////        0.req.token is in redisTokenRepository? -> NO_AUTH_TOKEN
        if (token == null) {
            return AccessResponse.fail("NO_AUTH_TOKEN");
        }
////        1. IS CONNECTING?
        if (!redisHeartbeatRepository.isAlive(serial)) {
            return AccessResponse.fail("DRONE_NOT_ALIVE");
        }
////        2.req.token is valid -> INVALID_OR_EXPIRED_TOKEN
        String redisSerial = redisTokenRepository.getSerialByToken(token);
        if (redisSerial == null) {
            return AccessResponse.fail("INVALID_OR_EXPIRED_TOKEN");
        }
////        3.req.serial == redis.serial? -> SERIAL_MISMATCH
        if (!redisSerial.equals(serial)) {
            return AccessResponse.fail("SERIAL_MISMATCH");
            }
////        4.VALID
        return AccessResponse.success(drone);
    }
}
