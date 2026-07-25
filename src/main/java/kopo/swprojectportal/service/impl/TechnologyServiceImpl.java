package kopo.swprojectportal.service.impl;

import kopo.swprojectportal.dto.TechnologyOptionDto;
import kopo.swprojectportal.entity.Technology;
import kopo.swprojectportal.entity.TechnologyCategory;
import kopo.swprojectportal.repository.TechnologyRepository;
import kopo.swprojectportal.service.TechnologyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TechnologyServiceImpl implements TechnologyService {

    private final TechnologyRepository technologyRepository;

    @Override
    public Map<String, List<TechnologyOptionDto>> getAllGroupedByCategory() {
        return technologyRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        t -> t.getCategory().name(),
                        LinkedHashMap::new,
                        Collectors.mapping(t -> new TechnologyOptionDto(t.getId(), t.getName()), Collectors.toList())
                ));
    }

    @Override
    public List<TechnologyOptionDto> search(String query) {
        if (query == null || query.isBlank()) return List.of();
        return technologyRepository.findByNameContainingIgnoreCase(query).stream()
                .map(t -> new TechnologyOptionDto(t.getId(), t.getName()))
                .limit(10)
                .toList();
    }

    @Override
    @Transactional
    public TechnologyOptionDto createOrReuse(String name, TechnologyCategory category) {
        // Case-insensitive match first — this is the actual anti-duplication check.
        // "gpt" typed against an existing "GPT" row reuses it instead of creating a new one.
        return technologyRepository.findByNameIgnoreCase(name)
                .map(t -> new TechnologyOptionDto(t.getId(), t.getName()))
                .orElseGet(() -> {
                    Technology saved = technologyRepository.save(
                            Technology.builder().name(name).category(category).build()
                    );
                    return new TechnologyOptionDto(saved.getId(), saved.getName());
                });
    }
}