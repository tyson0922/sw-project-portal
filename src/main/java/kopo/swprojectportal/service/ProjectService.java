package kopo.swprojectportal.service;

import kopo.swprojectportal.dto.ProjectDetailDto;
import kopo.swprojectportal.dto.ProjectFormRequestDto;
import kopo.swprojectportal.dto.ProjectResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {
    Page<ProjectResponseDto> getProjects(Integer year, String studentName, Boolean usesAi,
                                         List<Long> technologyIds, Pageable pageable);
    ProjectDetailDto getProjectDetail(Long id);
    void createProject(ProjectFormRequestDto form);
    void updateProject(Long id, ProjectFormRequestDto form);
    void deleteProject(Long id);
    List<ProjectResponseDto> getAllForAdmin();
}