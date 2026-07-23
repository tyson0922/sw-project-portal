package kopo.swprojectportal.repository;

import kopo.swprojectportal.entity.Technology;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechnologyRepository extends JpaRepository<Technology, Long> {
}