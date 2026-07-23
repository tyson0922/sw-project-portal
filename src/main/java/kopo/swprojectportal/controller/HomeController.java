package kopo.swprojectportal.controller;

import kopo.swprojectportal.service.ProjectService;
import kopo.swprojectportal.service.TechnologyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProjectService projectService;
    private final TechnologyService technologyService;

    @GetMapping("/")
    public String home(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String studentName,
            @RequestParam(required = false) Boolean usesAi,
            @RequestParam(required = false) List<Long> technologyIds,
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, 9);
        model.addAttribute("projectPage", projectService.getProjects(year, studentName, usesAi, technologyIds, pageable));
        model.addAttribute("technologiesByCategory", technologyService.getAllGroupedByCategory());
        model.addAttribute("selectedYear", year);
        model.addAttribute("selectedStudentName", studentName);
        model.addAttribute("selectedUsesAi", usesAi);
        model.addAttribute("selectedTechnologyIds", technologyIds);
        return "index";
    }
}