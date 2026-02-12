package com.mutzin.droneplatform.controller;

import com.mutzin.droneplatform.dto.DroneEventResponse;
import com.mutzin.droneplatform.dto.DroneTelemetryResponse;
import com.mutzin.droneplatform.repository.RedisHeartbeatRepository;
import com.mutzin.droneplatform.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final RedisHeartbeatRepository heartbeatRepo;
    private final DashboardService dashboardService;

    public DashboardController(
            RedisHeartbeatRepository heartbeatRepo, DashboardService dashboardService
            ) {
        this.heartbeatRepo = heartbeatRepo;
        this.dashboardService = dashboardService;
    }

     /// find alive drones
     /// @return serials
    @Operation(summary = "Alive drone")
    @GetMapping("/alive-drones")
    public Set<String> getAliveDrones() {
        return heartbeatRepo.findAliveDrones();
    }

    /// Get drone's Event data
    /// @return {"data":{},"updatedAt":currentTimeMillis}%
    @Operation(summary = "Drone's Event Data")
    @GetMapping("/drone/event/{serial}")
    public DroneEventResponse getDroneEventData(@PathVariable String serial) {
        return dashboardService.getLatestEvent(serial);

    }
    /// Get drone's Event data
    /// @return {"data":{},"updatedAt":currentTimeMillis}%
    @Operation(summary = "Drone's Event Data")
    @GetMapping("/drone/telemetry/{serial}")
    public DroneTelemetryResponse getDroneTelemetryData(@PathVariable String serial) {
        return dashboardService.getDroneState(serial);
    }
}
