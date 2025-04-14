package me.jooie.minicafe.repository;

import me.jooie.minicafe.domain.CafeOrder;
import me.jooie.minicafe.domain.CafeUser;
import me.jooie.minicafe.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<CafeUser, Long> {
    Optional<CafeUser> findByUsername(String username);
    Boolean existsByRole(String userRole);
}
