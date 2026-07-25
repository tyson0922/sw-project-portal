package kopo.swprojectportal.controller;

import kopo.swprojectportal.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/students")
@RequiredArgsConstructor
public class StudentAdminController {

    private final StudentService studentService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("students", studentService.getAll());
        return "admin/student-list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("formAction", "/admin/students");
        return "admin/student-form";
    }

    @PostMapping
    public String create(@RequestParam String name, @RequestParam String cohort) {
        studentService.createStudent(name, cohort);
        return "redirect:/admin/students";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("student", studentService.getById(id));
        model.addAttribute("formAction", "/admin/students/" + id);
        return "admin/student-form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @RequestParam String name, @RequestParam String cohort) {
        studentService.updateStudent(id, name, cohort);
        return "redirect:/admin/students";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return "redirect:/admin/students";
    }
}