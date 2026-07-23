package kopo.swprojectportal.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "student")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private String cohort;  // e.g. "빅데이터과 10기"

    @ManyToMany(mappedBy = "students")
    private List<Project> projects = new ArrayList<>();

    @Builder
    private Student(String name, String cohort) {
        this.name = name;
        this.cohort = cohort;
    }

    public void changeName(String name) { this.name = name; }
    public void changeCohort(String cohort) { this.cohort = cohort; }
}
