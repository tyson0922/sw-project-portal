package kopo.swprojectportal.dto;

import java.util.List;

public record ProjectFormRequestDto(
        Long id,
        String title,
        String description,
        String youtubeUrl,
        String githubUrl,
        String devlogUrl,
        String liveUrl,
        Integer year,
        List<Long> studentIds,
        List<Long> technologyIds
) {}