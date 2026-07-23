package kopo.swprojectportal.dto;

import java.util.List;

public record ProjectResponseDto(
        Long id,
        String title,
        String youtubeUrl,
        String thumbnailUrl,
        String githubUrl,
        String devlogUrl,
        String liveUrl,
        Integer year,
        List<String> studentNames,
        List<String> technologyNames,
        boolean usesAi
) {
}