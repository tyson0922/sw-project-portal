package kopo.swprojectportal.dto;

import java.util.List;
import java.util.Map;

public record ProjectDetailDto(
        Long id,
        String title,
        String description,
        String youtubeEmbedUrl,
        String githubUrl,
        String devlogUrl,
        String liveUrl,
        Integer year,
        List<String> studentNames,
        Map<String, List<String>> technologiesByCategory,
        boolean usesAi
) {
}