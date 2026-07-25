package kopo.swprojectportal.service;

import kopo.swprojectportal.dto.TechnologyOptionDto;
import kopo.swprojectportal.entity.TechnologyCategory;

import java.util.List;
import java.util.Map;

public interface TechnologyService {
    Map<String, List<TechnologyOptionDto>> getAllGroupedByCategory();
    List<TechnologyOptionDto> search(String query);
    TechnologyOptionDto createOrReuse(String name, TechnologyCategory category);
}