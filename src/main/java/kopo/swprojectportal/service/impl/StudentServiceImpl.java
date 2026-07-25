package kopo.swprojectportal.service.impl;

import kopo.swprojectportal.dto.StudentFormDto;
import kopo.swprojectportal.dto.StudentOptionDto;
import kopo.swprojectportal.entity.Student;
import kopo.swprojectportal.repository.StudentRepository;
import kopo.swprojectportal.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public List<String> getAllStudentNames() {
        return studentRepository.findAll().stream()
                .map(Student::getName)
                .sorted()
                .toList();
    }

    @Override
    public List<StudentOptionDto> getAll() {
        return studentRepository.findAll().stream()
                .map(s -> new StudentOptionDto(s.getId(), s.getName()))
                .toList();
    }

    @Override
    public StudentFormDto getById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found: " + id));
        return new StudentFormDto(student.getId(), student.getName(), student.getCohort());
    }

    @Override
    @Transactional
    public void createStudent(String name, String cohort) {
        studentRepository.save(Student.builder().name(name).cohort(cohort).build());
    }

    @Override
    @Transactional
    public void updateStudent(Long id, String name, String cohort) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found: " + id));
        student.changeName(name);
        student.changeCohort(cohort);
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}