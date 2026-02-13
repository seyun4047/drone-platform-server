package com.mutzin.droneplatform.dto.drone;

import com.mutzin.droneplatform.domain.drone.Drone;
import lombok.Getter;

@Getter
public class AccessResponse {
    private final boolean success;
    private final String message;
    private final Drone drone;

    private AccessResponse(boolean success, String message, Drone drone) {
        this.success = success;
        this.message = message;
        this.drone = drone;
    }

    public static AccessResponse fail(String message) {
        return new AccessResponse(false, message, null);
    }

    public static AccessResponse success(Drone drone) {
        return new AccessResponse(true, "ACCESS COMPLETE", drone);
    }
}
