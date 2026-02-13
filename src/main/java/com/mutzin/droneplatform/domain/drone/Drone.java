package com.mutzin.droneplatform.domain.drone;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Drone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String serial;

    @Column(nullable = false)
    private String name;

    private String recentLog;
    private LocalDateTime updatedAt;
    private int level;
    private boolean connecting;
    private String logPath;
    private LocalDateTime lastConnectTime;

}
