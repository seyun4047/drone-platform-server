package com.mutzin.droneplatform.repository;

import com.mutzin.droneplatform.domain.Drone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DroneRepository extends JpaRepository<Drone, Long> {
    Optional<Drone> findBySerial(String serial);
}