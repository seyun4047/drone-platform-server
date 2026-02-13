package com.mutzin.droneplatform.state.drone;

import lombok.Builder;
import lombok.Getter;
import java.util.Map;

@Getter
@Builder
public class DroneState {
    private int event;
    private String serial;
    private String device;
    private long lastUpdatedAt;
    private Map<String, Object> data;
}
