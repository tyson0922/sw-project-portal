package kopo.swprojectportal.service.impl;

import kopo.swprojectportal.dto.ProjectDetailDto;
import kopo.swprojectportal.dto.ProjectFormRequestDto;
import kopo.swprojectportal.dto.ProjectResponseDto;
import kopo.swprojectportal.entity.Project;
import kopo.swprojectportal.mapper.ProjectMapper;
import kopo.swprojectportal.repository.ProjectRepository;
import kopo.swprojectportal.repository.StudentRepository;
import kopo.swprojectportal.repository.TechnologyRepository;
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
    private final StudentRepository studentRepository;
    private final TechnologyRepository technologyRepository;

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

    @Override
    @Transactional
    public void createProject(ProjectFormRequestDto form) {
        Project project = Project.builder()
                .title(form.title())
                .description(form.description())
                .youtubeUrl(form.youtubeUrl())
                .githubUrl(form.githubUrl())
                .devlogUrl(form.devlogUrl())
                .liveUrl(form.liveUrl())
                .year(form.year())
                .build();
        linkRelations(project, form);
        projectRepository.save(project);
    }

    @Override
    @Transactional
    public void updateProject(Long id, ProjectFormRequestDto form) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        project.changeTitle(form.title());
        project.changeDescription(form.description());
        project.changeYoutubeUrl(form.youtubeUrl());
        project.changeGithubUrl(form.githubUrl());
        project.changeDevlogUrl(form.devlogUrl());
        project.changeLiveUrl(form.liveUrl());
        project.changeYear(form.year());

        project.getStudents().clear();
        project.getTechnologies().clear();
        linkRelations(project, form);
    }

    @Override
    @Transactional
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    private void linkRelations(Project project, ProjectFormRequestDto form) {
        studentRepository.findAllById(form.studentIds()).forEach(project::addStudent);
        technologyRepository.findAllById(form.technologyIds()).forEach(project::addTechnology);
    }

    @Override
    public List<ProjectResponseDto> getAllForAdmin() {
        return projectRepository.findAll().stream()
                .map(projectMapper::toResponseDto)
                .toList();
    }
}