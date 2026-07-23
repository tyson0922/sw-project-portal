package kopo.swprojectportal.controller;

import kopo.swprojectportal.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/project/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("project", projectService.getProjectDetail(id));
        return "project-detail";
    }
}