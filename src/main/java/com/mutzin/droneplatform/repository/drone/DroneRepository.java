package com.mutzin.droneplatform.repository.drone;

import com.mutzin.droneplatform.domain.drone.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DroneRepository extends JpaRepository<Drone, Long> {
    Optional<Drone> findBySerial(String serial);
}