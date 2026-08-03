package kopo.swprojectportal.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="project")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Lob // maps to a TEXT/CLOB column - descriptions can run longer than a normal VARCHAR
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "youtube_url", nullable = false, length = 500)
    private String youtubeUrl;

    @Column(name = "github_url", length=500)
    private String githubUrl;

    @Column(name = "devlog_url", length = 500)
    private String devlogUrl;

    @Column(name = "live_url", length = 500)
    private String liveUrl;

    @Column(nullable = false)
    private Integer year;

    @ManyToMany
    @JoinTable(
            name = "project_technology",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "technology_id")
    )
    private List<Technology> technologies = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "project_student",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> students = new ArrayList<>();

    @Builder
    private Project(String title, String description, String youtubeUrl, String githubUrl,
                    String devlogUrl, String liveUrl, Integer year) {
        this.title = title;
        this.description = description;
        this.youtubeUrl = youtubeUrl;
        this.githubUrl = githubUrl;
        this.devlogUrl = devlogUrl;
        this.liveUrl = liveUrl;
        this.year = year;
    }

    public void changeTitle(String title) { this.title = title; }
    public void changeDescription(String description) { this.description = description; }
    public void changeYoutubeUrl(String youtubeUrl) { this.youtubeUrl = youtubeUrl; }
    public void changeGithubUrl(String githubUrl) { this.githubUrl = githubUrl; }
    public void changeDevlogUrl(String devlogUrl) { this.devlogUrl = devlogUrl; }
    public void changeLiveUrl(String liveUrl) { this.liveUrl = liveUrl; }
    public void changeYear(Integer year) { this.year = year; }

    public void addTechnology(Technology technology) { this.technologies.add(technology); }
    public void removeTechnology(Technology technology) { this.technologies.remove(technology); }

    public void addStudent(Student student) { this.students.add(student); }
    public void removeStudent(Student student) { this.students.remove(student); }

    // Derived, not stored — true if any linked technology is tagged AI.
    // Lives on the entity since it's a pure function of existing state, no extra query needed.
    public boolean usesAi() {
        return technologies.stream().anyMatch(t -> t.getCategory() == TechnologyCategory.AI);
    }
}
