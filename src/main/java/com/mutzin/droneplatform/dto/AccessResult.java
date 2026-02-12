package com.mutzin.droneplatform.dto;

import com.mutzin.droneplatform.domain.Drone;

public class AccessResult {
    private final boolean success;
    private final String message;
    private final Drone drone;

    private AccessResult(boolean success, String message, Drone drone) {
        this.success = success;
        this.message = message;
        this.drone = drone;
    }

    public static AccessResult fail(String message) {
        return new AccessResult(false, message, null);
    }

    public static AccessResult success(Drone drone) {
        return new AccessResult(true, "ACCESS COMPLETE", drone);
    }
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Drone getDrone() {
        return drone;
    }
}
