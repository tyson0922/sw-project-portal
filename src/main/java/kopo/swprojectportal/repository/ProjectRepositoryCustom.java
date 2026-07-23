package kopo.swprojectportal.repository;

import kopo.swprojectportal.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

// Contract for the dynamic filter query - Implementation lives in ProjectRepositoryimpl
public interface ProjectRepositoryCustom {
    Page<Project> search(Integer year, String studentName, Boolean usesAi,
                         List<Long> technologyIds, Pageable pageable);
}
