package com.mutzin.droneplatform.controller;

import com.mutzin.droneplatform.dto.TelemetryRequest;
import com.mutzin.droneplatform.dto.TelemetryResponse;
import com.mutzin.droneplatform.service.TelemetryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/// REST controller responsible for receiving and processing telemetry data
/// sent from connected drones.
/// - Accepts telemetry data such as angles, status, or sensor values.
/// - Delegates all processing and validation logic to TelemetryService.
/// This controller only handles HTTP request/response mapping.

@RestController
@RequestMapping("/api")
public class TelemetryController {
    /// Constructor-based dependency injection for TelemetryService.
    ///
    /// @param telemetryService service responsible for handling
    ///                         drone telemetry processing logic
    private final TelemetryService telemetryService;

    public TelemetryController(TelemetryService telemetryService) {
        this.telemetryService = telemetryService;
    }

    /// Handles incoming telemetry data from a drone.
    /// - Receives telemetry information from the drone.
    /// - Passes the data to TelemetryService for validation and processing.
    ///
    /// @param telemetryRequest telemetry data sent by the drone
    /// @return TelemetryResponse containing processing result and status
    @PostMapping("/telemetry")
    public ResponseEntity<TelemetryResponse> getTelemetry(
            @RequestBody TelemetryRequest telemetryRequest) {

//        System.out.println("get Telemetry From " + telemetryRequest.getSerial());
        TelemetryResponse result = telemetryService.handle(telemetryRequest);
        System.out.println("CONNECT: " + result);
        return ResponseEntity.ok(result);
    }

}