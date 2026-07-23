package kopo.swprojectportal.service.impl;

import kopo.swprojectportal.dto.ProjectDetailDto;
import kopo.swprojectportal.dto.ProjectResponseDto;
import kopo.swprojectportal.entity.Project;
import kopo.swprojectportal.mapper.ProjectMapper;
import kopo.swprojectportal.repository.ProjectRepository;
import kopo.swprojectportal.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    @Override
    public Page<ProjectResponseDto> getProjects(Integer year, String studentName, Boolean usesAi,
                                                List<Long> technologyIds, Pageable pageable) {
        return projectRepository.search(year, studentName, usesAi, technologyIds, pageable)
                .map(projectMapper::toResponseDto);
    }

    @Override
    public ProjectDetailDto getProjectDetail(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found: " + id));
        return projectMapper.toDetailDto(project);
    }
}