package com.mutzin.droneplatform.infrastructure.accesguard;


import com.mutzin.droneplatform.domain.Drone;
import com.mutzin.droneplatform.dto.AccessResult;
import com.mutzin.droneplatform.repository.DroneRepository;
import com.mutzin.droneplatform.repository.RedisTokenRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class AccessGuard {

    private final DroneRepository droneRepository;
    private final RedisTokenRepository redisTokenRepository;

    public AccessGuard(
            DroneRepository droneRepository,
            RedisTokenRepository redisTokenRepository
    ) {
        this.droneRepository = droneRepository;
        this.redisTokenRepository = redisTokenRepository;
    }

    @Transactional
    public AccessResult handle(String token, String serial) {

        Optional<Drone> optionalDrone = droneRepository.findBySerial(serial);
        if (optionalDrone.isEmpty()) {
            return AccessResult.fail("DRONE_NOT_FOUND");
        }
////        0. IS CONNECTING?
        Drone drone = optionalDrone.get();
        if (!drone.isConnecting()) {
            return AccessResult.fail("NO_CONNECTING_DEVICE");
        }
////        1.req.token is in redisTokenRepository? -> NO_AUTH_TOKEN
        if (token == null) {
            return AccessResult.fail("NO_AUTH_TOKEN");
        }
////        2.req.token is valid -> INVALID_OR_EXPIRED_TOKEN
        String redisSerial = redisTokenRepository.getSerialByToken(token);
        if (redisSerial == null) {
            return AccessResult.fail("INVALID_OR_EXPIRED_TOKEN");
        }
////        3.req.serial == redis.serial? -> SERIAL_MISMATCH
        if (!redisSerial.equals(serial)) {
            return AccessResult.fail("SERIAL_MISMATCH");
        }
////        4.VALID
////        update updateAt
        drone.setUpdatedAt(LocalDateTime.now());
        droneRepository.save(drone);

        return AccessResult.success(drone);
    }
}
