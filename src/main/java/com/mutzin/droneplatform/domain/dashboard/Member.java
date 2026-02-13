package com.mutzin.droneplatform.domain.dashboard;

import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String role;

//0: REJECT 1: PENDING 2: ACCESS
    private Integer state;
}