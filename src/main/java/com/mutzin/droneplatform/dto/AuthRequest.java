package com.mutzin.droneplatform.dto;

import lombok.Getter;

@Getter
public class AuthRequest {
    private String serial;
    private String device_name;
    private String token;
}