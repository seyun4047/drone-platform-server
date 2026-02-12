package com.mutzin.droneplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class DroneEventResponse {
    private Map<String, Object> data;
    private long updatedAt;
}

