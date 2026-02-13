package com.mutzin.droneplatform.controller.dashboard;

import com.mutzin.droneplatform.dto.drone.DroneEventResponse;
import com.mutzin.droneplatform.dto.drone.DroneTelemetryResponse;
import com.mutzin.droneplatform.repository.drone.RedisHeartbeatRepository;
import com.mutzin.droneplatform.service.dashboard.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

     /// Find alive drones
     /// @return serials
    @Tag(name = "GET ALIVE DRONES")
    @Operation(summary = "Alive drone")
    @GetMapping("/alive-drones")
    public Set<String> getAliveDrones() {
        return heartbeatRepo.findAliveDrones();
    }

    /// Get drone's Event data
    /// @return {"data":{},"updatedAt":currentTimeMillis}%
    @Tag(name = "GET TELEMETRY DATA")
    @Operation(summary = "Drone's Event Data")
    @GetMapping("/drone/event/{serial}")
    public DroneEventResponse getDroneEventData(@PathVariable String serial) {
        return dashboardService.getLatestEvent(serial);

    }
    /// Get drone's Event data
    /// @return {"data":{},"updatedAt":currentTimeMillis}%
    @Tag(name = "GET EVENT DATA")
    @Operation(summary = "Drone's Event Data")
    @GetMapping("/drone/telemetry/{serial}")
    public DroneTelemetryResponse getDroneTelemetryData(@PathVariable String serial) {
        return dashboardService.getDroneState(serial);
    }
}
