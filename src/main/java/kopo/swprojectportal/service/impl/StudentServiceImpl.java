package kopo.swprojectportal.service;

import kopo.swprojectportal.entity.Student;
import kopo.swprojectportal.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}