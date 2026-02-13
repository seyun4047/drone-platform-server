package com.mutzin.droneplatform.repository.dashboard;

import com.mutzin.droneplatform.domain.dashboard.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
    boolean existsByUsername(String username);
}
