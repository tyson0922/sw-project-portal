package kopo.swprojectportal.controller;

import kopo.swprojectportal.dto.ProjectFormRequestDto;
import kopo.swprojectportal.service.ProjectService;
import kopo.swprojectportal.service.StudentService;
import kopo.swprojectportal.service.TechnologyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/projects")
@RequiredArgsConstructor
public class ProjectAdminController {

    private final ProjectService projectService;
    private final StudentService studentService;
    private final TechnologyService technologyService;

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("allStudents", studentService.getAll());
        model.addAttribute("formAction", "/admin/projects");
        return "admin/project-form";
    }

    @PostMapping
    public String create(ProjectFormRequestDto form) {
        projectService.createProject(form);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("allStudents", studentService.getAll());
        model.addAttribute("project", projectService.getProjectDetail(id));
        model.addAttribute("projectId", id);
        model.addAttribute("formAction", "/admin/projects/" + id);
        return "admin/project-form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, ProjectFormRequestDto form) {
        projectService.updateProject(id, form);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        projectService.deleteProject(id);
        return "redirect:/admin/dashboard";
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("projects", projectService.getAllForAdmin());
        return "admin/project-list";
    }
}