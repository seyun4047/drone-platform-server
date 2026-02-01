package com.mutzin.droneplatform.controller;

import com.mutzin.droneplatform.dto.AuthRequest;
import com.mutzin.droneplatform.dto.AuthResponse;
import com.mutzin.droneplatform.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
///  REST controller responsible for drone authentication and connection management.
///
///  - Handles initial connection requests from drones.
///  - Manages drone disconnection and cleans up server-side connection state.
///
///  All business logic is delegated to AuthService.
///  This controller focuses only on HTTP request and response handling.
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    /// Constructor-based dependency injection for AuthService.
    /// @param authService service that handles drone authentication
    /// and connection-related business logic

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    /// Handles drone connection requests.
    /// - Authenticates the drone using its serial number and device name.
    /// - Registers the drone as connected on the server if authentication succeeds.
    ///
    /// @param authRequest authentication request containing
    ///                    drone serial and device name
    /// @return AuthResponse containing authentication result and connection status
    @PostMapping("/connect")
    public ResponseEntity<AuthResponse> connect(@RequestBody AuthRequest authRequest) {
        AuthResponse result = authService.connectDrone(authRequest.getSerial(), authRequest.getDevice_name());
        System.out.println("SUCCESS GET TELEMETRY: " + result);
        return ResponseEntity.ok(result);
    }

    /// Handles drone disconnection requests.
    /// - Terminates the drone connection.
    /// - Updates the server-side connection state accordingly.
    ///
    /// @param authRequest disconnection request information
    /// @return AuthResponse containing disconnection result
    @PostMapping("/disconnect")
    public ResponseEntity<AuthResponse> disconnect(@RequestBody AuthRequest authRequest) {
        AuthResponse message = authService.disconnected(authRequest);
        return ResponseEntity.ok(message);
    }
}