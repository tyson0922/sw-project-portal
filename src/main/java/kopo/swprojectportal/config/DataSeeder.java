package kopo.swprojectportal.config;

import kopo.swprojectportal.entity.*;
import kopo.swprojectportal.repository.ProjectRepository;
import kopo.swprojectportal.repository.StudentRepository;
import kopo.swprojectportal.repository.TechnologyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final ProjectRepository projectRepository;
    private final StudentRepository studentRepository;
    private final TechnologyRepository technologyRepository;

    @Override
    public void run(String... args) {
        if (projectRepository.count() > 0) {
            return;
        }

        Map<String, Technology> tech = Map.ofEntries(
                Map.entry("Spring Boot", save(TechnologyCategory.FRAMEWORK, "Spring Boot")),
                Map.entry("Spring Security", save(TechnologyCategory.FRAMEWORK, "Spring Security")),
                Map.entry("Spring Framework", save(TechnologyCategory.FRAMEWORK, "Spring Framework")),
                Map.entry("Thymeleaf", save(TechnologyCategory.FRAMEWORK, "Thymeleaf")),
                Map.entry("FastAPI", save(TechnologyCategory.FRAMEWORK, "FastAPI")),
                Map.entry("React", save(TechnologyCategory.FRAMEWORK, "React")),
                Map.entry("Redis", save(TechnologyCategory.DATABASE, "Redis")),
                Map.entry("MongoDB", save(TechnologyCategory.DATABASE, "MongoDB")),
                Map.entry("MariaDB", save(TechnologyCategory.DATABASE, "MariaDB")),
                Map.entry("Python", save(TechnologyCategory.LANGUAGE, "Python")),
                Map.entry("Gemini", save(TechnologyCategory.AI, "Google Gemini API")),
                Map.entry("YOLO", save(TechnologyCategory.AI, "Ultralytics YOLO")),
                Map.entry("OpenAI", save(TechnologyCategory.AI, "OpenAI GPT API")),
                Map.entry("AWS EC2", save(TechnologyCategory.CLOUD, "AWS EC2")),
                Map.entry("AWS S3", save(TechnologyCategory.CLOUD, "AWS S3")),
                Map.entry("Nginx", save(TechnologyCategory.WEB_SERVER, "Nginx")),
                Map.entry("Docker", save(TechnologyCategory.WEB_SERVER, "Docker")),
                Map.entry("REST API", save(TechnologyCategory.PROTOCOL, "REST API")),
                Map.entry("HTTPS", save(TechnologyCategory.PROTOCOL, "HTTPS")),
                Map.entry("BCrypt", save(TechnologyCategory.AUTH, "BCrypt")),
                Map.entry("JWT", save(TechnologyCategory.AUTH, "JWT Token")),
                Map.entry("StockAPI", save(TechnologyCategory.EXTERNAL_API, "한국투자증권 Open API")),
                Map.entry("DartAPI", save(TechnologyCategory.EXTERNAL_API, "DART 전자공시 API")),
                Map.entry("PapagoAPI", save(TechnologyCategory.EXTERNAL_API, "Papago API")),
                Map.entry("NinjasAPI", save(TechnologyCategory.EXTERNAL_API, "API Ninjas")),
                Map.entry("FullCalendar", save(TechnologyCategory.ETC, "FullCalendar"))
        );

        Student jiJuWoo = studentRepository.save(
                Student.builder().name("지주우").cohort("빅데이터과 10기").build()
        );
        Student yooYoungSang = studentRepository.save(
                Student.builder().name("유영상").cohort("빅데이터과 10기").build()
        );
        Student yoonGeon = studentRepository.save(
                Student.builder().name("윤건").cohort("빅데이터과 9기").build()
        );

        Project jjikgo = Project.builder()
                .title("찍고먹어요")
                .description("냉장고 사진 한 장으로 식재료를 자동 인식하고 요리를 추천해주는 AI 기반 냉장고 식품 분석 및 요리 추천 서비스입니다.")
                .youtubeUrl("https://youtu.be/6VEgogT6Qo4?si=ttJGxooNHsc_v1-b")
                .year(2026)
                .build();
        jjikgo.addStudent(jiJuWoo);
        List.of("Spring Boot", "Spring Security", "FastAPI", "React", "Redis", "MongoDB",
                        "MariaDB", "Python", "Gemini", "YOLO", "AWS EC2", "AWS S3", "Nginx", "Docker")
                .forEach(key -> jjikgo.addTechnology(tech.get(key)));
        projectRepository.save(jjikgo);

        Project kStock = Project.builder()
                .title("K-Stock Compass")
                .description("한국투자증권 Open API와 DART 전자공시 API를 연동하여 주식 정보를 제공하고 Google Gemini AI로 투자 분석을 지원하는 주식 정보 플랫폼입니다.")
                .youtubeUrl("https://youtu.be/6NU8OIGzxhc?si=-tNIEiic9CeeidH5")
                .year(2026)
                .build();
        kStock.addStudent(yooYoungSang);
        List.of("Spring Boot", "Spring Security", "MariaDB", "Redis", "AWS EC2", "Nginx",
                        "REST API", "JWT", "BCrypt", "StockAPI", "DartAPI", "Gemini")
                .forEach(key -> kStock.addTechnology(tech.get(key)));
        projectRepository.save(kStock);

        Project fitmate = Project.builder()
                .title("FITMATE")
                .description("사용자가 입력한 키워드를 기반으로 AI가 자동으로 일주일치 운동 루틴과 식단, 그리고 신체 상태를 분석한 리포트를 추천해주는 개인 맞춤형 피트니스 서비스입니다.")
                .youtubeUrl("https://youtu.be/uwCI6MIZj5M?si=b6DQO1PByvhKuPma")
                .year(2025)
                .build();
        fitmate.addStudent(yoonGeon);
        List.of("Spring Framework", "MariaDB", "MongoDB", "AWS EC2", "HTTPS",
                        "Thymeleaf", "OpenAI", "PapagoAPI", "NinjasAPI", "FullCalendar")
                .forEach(key -> fitmate.addTechnology(tech.get(key)));
        projectRepository.save(fitmate);
    }

    private Technology save(TechnologyCategory category, String name) {
        return technologyRepository.save(
                Technology.builder().name(name).category(category).build()
        );
    }
}