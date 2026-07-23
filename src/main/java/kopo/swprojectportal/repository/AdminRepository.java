package kopo.swprojectportal.repository;

import kopo.swprojectportal.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin,Long> {

    // used by AdminUserDetailsService during login
    Optional<Admin> findByUsername(String username);
}
