package kopo.swprojectportal.service;

import kopo.swprojectportal.dto.TechnologyOptionDto;

import java.util.List;
import java.util.Map;

public interface TechnologyService {
    Map<String, List<TechnologyOptionDto>> getAllGroupedByCategory();
}