package com.mutzin.droneplatform.service.dashboard;

import com.mutzin.droneplatform.domain.dashboard.Member;
import com.mutzin.droneplatform.dto.dashboard.AuthResponse;
import com.mutzin.droneplatform.infrastructure.jwt.JwtUtil;
import com.mutzin.droneplatform.repository.dashboard.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    // Register
    public AuthResponse register(String username, String password) {
        if (memberRepository.existsByUsername(username)) {
            return new AuthResponse(false, "USERNAME_ALREADY_EXISTS", null);
        }

        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role("USER")
                .build();

        memberRepository.save(member);
        return new AuthResponse(true, "USER_REGISTERED", Map.of("id", username));
    }

    // Login
    public AuthResponse login(String username, String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
//            Gen Token with username
            String token = jwtUtil.generateToken(username);
            return new AuthResponse(true, "LOGIN_SUCCESSFUL", Map.of("token", token));

        } catch (Exception e) {
            return new AuthResponse(false, "INVALID_USERNAME_OR_PASSWORD", null);
        }
    }
}
