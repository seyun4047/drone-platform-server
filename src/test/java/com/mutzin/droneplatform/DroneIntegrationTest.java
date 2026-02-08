package com.mutzin.droneplatform;

import com.mutzin.droneplatform.controller.AuthController;
import com.mutzin.droneplatform.controller.TelemetryController;
import com.mutzin.droneplatform.dto.AuthResponse;
import com.mutzin.droneplatform.dto.TelemetryRequest;
import com.mutzin.droneplatform.dto.TelemetryResponse;
import com.mutzin.droneplatform.service.AuthService;
import com.mutzin.droneplatform.service.TelemetryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import java.util.Map;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({AuthController.class, TelemetryController.class})
@AutoConfigureMockMvc(addFilters = false)
public class DroneIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private TelemetryService telemetryService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String SERIAL = "tests-serial";
    private final String DRONE_NAME = "test-drone";
    private final String TOKEN = "testtoken";

    // ===============================
    // 1. CONNECT
    // ===============================
    @Test
    void connectTest() throws Exception {
        AuthResponse connectRes = new AuthResponse(true, "SUCCESS_CONNECT", TOKEN);
        when(authService.connectDrone(SERIAL, DRONE_NAME)).thenReturn(connectRes);

        mockMvc.perform(post("/auth/connect")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of("serial", SERIAL, "device_name", DRONE_NAME)
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("SUCCESS_CONNECT"))
                .andExpect(jsonPath("$.token").value(TOKEN));
    }

    // ===============================
    // 2. TELEMETRY DATA (event=0)
    // ===============================
    @Test
    void telemetryTest() throws Exception {
        TelemetryRequest telemetryReq = new TelemetryRequest();
        telemetryReq.setEvent(0);
        telemetryReq.setSerial(SERIAL);
        telemetryReq.setDevice(DRONE_NAME);
        telemetryReq.setToken(TOKEN);
        telemetryReq.setData(Map.of("angle", 90, "position", "(2,200)"));

        TelemetryResponse telemetryRes = new TelemetryResponse(true, "OK");
        when(telemetryService.handle(any(TelemetryRequest.class))).thenReturn(telemetryRes);

        mockMvc.perform(post("/api/telemetry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(telemetryReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    // ===============================
    // 3. EVENT DATA (event=1)
    // ===============================
    @Test
    void eventTelemetryTest() throws Exception {
        TelemetryRequest eventReq = new TelemetryRequest();
        eventReq.setEvent(1);
        eventReq.setSerial(SERIAL);
        eventReq.setDevice(DRONE_NAME);
        eventReq.setToken(TOKEN);
        eventReq.setData(Map.of(
                "angle", 180,
                "position", "(2,200)",
                "event_detail", Map.of("message", "human detected", "position", "(12,100)")
        ));

        TelemetryResponse eventRes = new TelemetryResponse(true, "EVENT_RECEIVED");
        when(telemetryService.handle(any(TelemetryRequest.class))).thenReturn(eventRes);

        mockMvc.perform(post("/api/telemetry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("EVENT_RECEIVED"));
    }

    // ===============================
    // 4. UPDATE TOKEN
    // ===============================
    @Test
    void updateTokenTest() throws Exception {
        Map<String, Object> updateReq = Map.of(
                "serial", SERIAL,
                "token", TOKEN
        );

        AuthResponse updateRes = new AuthResponse(true, "UPDATE TOKEN", TOKEN);
        when(authService.update(any())).thenReturn(updateRes);

        mockMvc.perform(post("/auth/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("UPDATE TOKEN"))
                .andExpect(jsonPath("$.token").value(TOKEN));
    }

    // ===============================
    // 5. DISCONNECT
    // ===============================
    @Test
    void disconnectTest() throws Exception {
        Map<String, Object> disconnectReq = Map.of(
                "serial", SERIAL,
                "token", TOKEN
        );

        AuthResponse disconnectRes = new AuthResponse(true, "DISCONNECT:" + SERIAL, TOKEN);
        when(authService.disconnect(any())).thenReturn(disconnectRes);

        mockMvc.perform(post("/auth/disconnect")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(disconnectReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("DISCONNECT:" + SERIAL))
                .andExpect(jsonPath("$.token").value(TOKEN));
    }
}