package kopo.swprojectportal.repository;

import kopo.swprojectportal.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

// Combines Spring Data JPA(simple CRUD) with our custom QueryDSL search method below
public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectRepositoryCustom {
}
