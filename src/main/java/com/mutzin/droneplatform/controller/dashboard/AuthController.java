package com.mutzin.droneplatform.controller.dashboard;

import com.mutzin.droneplatform.dto.dashboard.AuthResponse;
import com.mutzin.droneplatform.dto.dashboard.LoginRequest;
import com.mutzin.droneplatform.service.dashboard.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /// Dashboard register
    /// @return {"status":status,"message":"message","data":{"id":"username"}}%
    @Tag(name = "REGISTER")
    @Operation(summary = "Register user")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.register(request.getUsername(), request.getPassword());
        return ResponseEntity.status(authResponse.status() ? 200 : 400).body(authResponse);
    }

    /// Dashboard login
    /// @return {"status":status,"message":"message","data":{"token":"jwt"}}%
    @Tag(name = "LOGIN")
    @Operation(summary = "Login user")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.status(authResponse.status() ? 200 : 401).body(authResponse);
    }
}
