package kopo.swprojectportal.repository;

import kopo.swprojectportal.entity.Technology;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TechnologyRepository extends JpaRepository<Technology, Long> {
    List<Technology> findByNameContainingIgnoreCase(String query);
    Optional<Technology> findByNameIgnoreCase(String name);
}