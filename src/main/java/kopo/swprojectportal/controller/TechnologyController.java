package kopo.swprojectportal.controller;

import kopo.swprojectportal.dto.TechnologyOptionDto;
import kopo.swprojectportal.entity.TechnologyCategory;
import kopo.swprojectportal.service.TechnologyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/api/technologies")
@RequiredArgsConstructor
public class TechnologyController {

    private final TechnologyService technologyService;

    @GetMapping("/search")
    public List<TechnologyOptionDto> search(@RequestParam String q) {
        return technologyService.search(q);
    }

    @PostMapping
    public TechnologyOptionDto create(@RequestBody NewTechnologyRequest request) {
        return technologyService.createOrReuse(request.name(), request.category());
    }

    public record NewTechnologyRequest(String name, TechnologyCategory category) {}
}