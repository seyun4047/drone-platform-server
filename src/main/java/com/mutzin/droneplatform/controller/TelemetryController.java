package com.mutzin.droneplatform.controller;

import com.mutzin.droneplatform.dto.TelemetryRequest;
import com.mutzin.droneplatform.dto.TelemetryResponse;
import com.mutzin.droneplatform.service.TelemetryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
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
@Tag(name = "Telemetry")
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
    @Operation(summary = "Receive telemetry data")
    @PostMapping("/telemetry")
    public ResponseEntity<TelemetryResponse> getTelemetry(
            @RequestBody TelemetryRequest telemetryRequest) {

        try {
            TelemetryResponse result = telemetryService.handle(telemetryRequest);
            System.out.println(result);
            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new TelemetryResponse(false, e.getMessage()));

        } catch (IllegalStateException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new TelemetryResponse(false, e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new TelemetryResponse(false, "INTERNAL_SERVER_ERROR"));
        }
    }
}