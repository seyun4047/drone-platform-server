package com.mutzin.droneplatform.controller.drone;

import com.mutzin.droneplatform.dto.drone.DroneAuthRequest;
import com.mutzin.droneplatform.dto.drone.DroneAuthResponse;
import com.mutzin.droneplatform.service.drone.DroneAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
///  REST controller responsible for drone authentication and connection management.
///
///  - Handles initial connection requests from drones.
///  - Manages drone disconnection and cleans up server-side connection state.
///
///  All business logic is delegated to DroneAuthService.
///  This controller focuses only on HTTP request and response handling.
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication")
public class DroneAuthController {
    private final DroneAuthService droneAuthService;
    /// Constructor-based dependency injection for DroneAuthService.
    /// @param droneAuthService service that handles drone authentication
    /// and connection-related business logic

    public DroneAuthController(DroneAuthService droneAuthService){
        this.droneAuthService = droneAuthService;
    }

    /// Handles drone connection requests.
    /// - Authenticates the drone using its serial number and device name.
    /// - Registers the drone as connected on the server if authentication succeeds.
    ///
    /// @param droneAuthRequest authentication request containing
    ///                    drone serial and device name
    /// @return DroneAuthResponse containing authentication result and connection status
    @Operation(summary = "Connect drone")
    @PostMapping("/connect")
    public ResponseEntity<DroneAuthResponse> connect(@RequestBody DroneAuthRequest droneAuthRequest) {
        try {
            DroneAuthResponse result =
                    droneAuthService.connectDrone(droneAuthRequest.getSerial(), droneAuthRequest.getDevice_name());
            System.out.println(result);
            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new DroneAuthResponse(false, e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new DroneAuthResponse(false, "INTERNAL_SERVER_ERROR"));
        }
    }

    /// Handles drone disconnection requests.
    /// - Terminates the drone connection.
    /// - Updates the server-side connection state accordingly.
    /// @param droneAuthRequest disconnection request information
    /// @return DroneAuthResponse containing disconnection result
    @Operation(summary = "Disconnect drone")
    @PostMapping("/disconnect")
    public ResponseEntity<DroneAuthResponse> disconnect(@RequestBody DroneAuthRequest droneAuthRequest) {
        try {
            DroneAuthResponse result = droneAuthService.disconnect(droneAuthRequest);
            System.out.println(result);
            return ResponseEntity.ok(result);

        } catch (IllegalStateException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new DroneAuthResponse(false, e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new DroneAuthResponse(false, "INTERNAL_SERVER_ERROR"));
        }
    }

    /// Handles drone token update requests.
    /// - Updates the token.
    ///
    /// @param droneAuthRequest update drone request information
    /// @return DroneAuthResponse containing authentication result
    @Operation(summary = "Update drone token")
    @PostMapping("/update")
    public ResponseEntity<DroneAuthResponse> update(@RequestBody DroneAuthRequest droneAuthRequest) {
        try {
            DroneAuthResponse result = droneAuthService.update(droneAuthRequest);
            System.out.println(result);
            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new DroneAuthResponse(false, e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new DroneAuthResponse(false, "INTERNAL_SERVER_ERROR"));
        }
    }
}