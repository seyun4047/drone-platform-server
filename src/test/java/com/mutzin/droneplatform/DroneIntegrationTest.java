package com.mutzin.droneplatform;

import com.mutzin.droneplatform.config.SecurityConfig;
import com.mutzin.droneplatform.controller.drone.DroneAuthController;
import com.mutzin.droneplatform.controller.drone.TelemetryController;
import com.mutzin.droneplatform.dto.drone.DroneAuthRequest;
import com.mutzin.droneplatform.dto.drone.DroneAuthResponse;
import com.mutzin.droneplatform.dto.drone.TelemetryRequest;
import com.mutzin.droneplatform.dto.drone.TelemetryResponse;
import com.mutzin.droneplatform.infrastructure.jwt.JwtAuthenticationFilter;
import com.mutzin.droneplatform.service.drone.DroneAuthService;
import com.mutzin.droneplatform.service.drone.TelemetryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = {DroneAuthController.class, TelemetryController.class},
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
        )
)
@AutoConfigureMockMvc(addFilters = false)
public class DroneIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DroneAuthService droneAuthService;

    @MockitoBean
    private TelemetryService telemetryService;

    // @Autowired 제거 → WebMvcTest 슬라이스에서 ObjectMapper 빈이 등록되지 않음
    // 직접 생성하면 의존성 문제 없이 동일하게 동작
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String SERIAL = "tests-serial";
    private final String DRONE_NAME = "test-drone";
    private final String TOKEN = "testtoken";

    // ===============================
    // 1. CONNECT
    // ===============================
    @Test
    void connectTest() throws Exception {
        DroneAuthResponse connectRes = new DroneAuthResponse(true, "SUCCESS_CONNECT", TOKEN);
        when(droneAuthService.connectDrone(eq(SERIAL), eq(DRONE_NAME))).thenReturn(connectRes);

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

        TelemetryResponse telemetryRes = new TelemetryResponse(true, "UPDATE_TELEMETRY_serial: " + SERIAL);
        when(telemetryService.handle(any(TelemetryRequest.class))).thenReturn(telemetryRes);

        mockMvc.perform(post("/api/telemetry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(telemetryReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("UPDATE_TELEMETRY_serial: " + SERIAL));
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

        TelemetryResponse eventRes = new TelemetryResponse(true, "UPDATE_EVENT_DATA_serial: " + SERIAL);
        when(telemetryService.handle(any(TelemetryRequest.class))).thenReturn(eventRes);

        mockMvc.perform(post("/api/telemetry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("UPDATE_EVENT_DATA_serial: " + SERIAL));
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

        DroneAuthResponse updateRes = new DroneAuthResponse(true, "UPDATE TOKEN", TOKEN);
        when(droneAuthService.update(any(DroneAuthRequest.class))).thenReturn(updateRes);

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

        DroneAuthResponse disconnectRes = new DroneAuthResponse(true, "DISCONNECT:" + SERIAL, TOKEN);
        when(droneAuthService.disconnect(any(DroneAuthRequest.class))).thenReturn(disconnectRes);

        mockMvc.perform(post("/auth/disconnect")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(disconnectReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("DISCONNECT:" + SERIAL))
                .andExpect(jsonPath("$.token").value(TOKEN));
    }
}