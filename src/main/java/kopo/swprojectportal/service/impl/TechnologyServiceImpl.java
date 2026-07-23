package kopo.swprojectportal.service.impl;

import kopo.swprojectportal.dto.TechnologyOptionDto;
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
}