package kopo.swprojectportal.service;

import kopo.swprojectportal.dto.StudentFormDto;
import kopo.swprojectportal.dto.StudentOptionDto;

import java.util.List;

public interface StudentService {
    List<String> getAllStudentNames();
    List<StudentOptionDto> getAll();
    StudentFormDto getById(Long id);
    void createStudent(String name, String cohort);
    void updateStudent(Long id, String name, String cohort);
    void deleteStudent(Long id);
}
