package kopo.swprojectportal.mapper;

import kopo.swprojectportal.dto.ProjectDetailDto;
import kopo.swprojectportal.dto.ProjectResponseDto;
import kopo.swprojectportal.dto.TechnologyOptionDto;
import kopo.swprojectportal.entity.Project;
import kopo.swprojectportal.entity.Student;
import kopo.swprojectportal.entity.Technology;
import kopo.swprojectportal.util.YoutubeUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = {YoutubeUtils.class, Student.class})
public interface ProjectMapper {

    @Mapping(target = "thumbnailUrl", expression = "java(YoutubeUtils.extractThumbnailUrl(project.getYoutubeUrl()))")
    @Mapping(target = "studentNames", expression = "java(mapStudentNames(project))")
    @Mapping(target = "technologyNames", expression = "java(mapTechnologyNames(project))")
    @Mapping(target = "usesAi", expression = "java(project.usesAi())")
    ProjectResponseDto toResponseDto(Project project);

    @Mapping(target = "youtubeEmbedUrl", expression = "java(YoutubeUtils.extractEmbedUrl(project.getYoutubeUrl()))")
    @Mapping(target = "studentNames", expression = "java(mapStudentNames(project))")
    @Mapping(target = "studentIds", expression = "java(project.getStudents().stream().map(Student::getId).toList())")
    @Mapping(target = "technologiesByCategory", expression = "java(mapTechnologiesByCategory(project))")
    @Mapping(target = "technologies", expression = "java(mapTechnologyOptions(project))")
    @Mapping(target = "usesAi", expression = "java(project.usesAi())")
    ProjectDetailDto toDetailDto(Project project);

    default List<String> mapStudentNames(Project project) {
        return project.getStudents().stream().map(Student::getName).toList();
    }

    default List<String> mapTechnologyNames(Project project) {
        return project.getTechnologies().stream().map(Technology::getName).toList();
    }

    default Map<String, List<String>> mapTechnologiesByCategory(Project project) {
        return project.getTechnologies().stream()
                .collect(Collectors.groupingBy(
                        t -> t.getCategory().name(),
                        LinkedHashMap::new,
                        Collectors.mapping(Technology::getName, Collectors.toList())
                ));
    }

    default List<TechnologyOptionDto> mapTechnologyOptions(Project project) {
        return project.getTechnologies().stream()
                .map(t -> new TechnologyOptionDto(t.getId(), t.getName()))
                .toList();
    }
}