package kopo.swprojectportal.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kopo.swprojectportal.entity.Project;
import kopo.swprojectportal.entity.QProject;
import kopo.swprojectportal.entity.TechnologyCategory;
import kopo.swprojectportal.repository.ProjectRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Project> search(Integer year, String studentName, Boolean usesAi,
                                List<Long> technologyIds, Pageable pageable) {
        QProject project = QProject.project;

        BooleanBuilder builder = new BooleanBuilder();
        if (year != null) {
            builder.and(project.year.eq(year));
        }
        if (studentName != null && !studentName.isBlank()) {
            builder.and(project.students.any().name.containsIgnoreCase(studentName));
        }
        if (usesAi != null && usesAi) {
            builder.and(project.technologies.any().category.eq(TechnologyCategory.AI));
        }
        if (technologyIds != null && !technologyIds.isEmpty()) {
            // matches if the project uses AT LEAST ONE of the checked technologies (OR across selections)
            builder.and(project.technologies.any().id.in(technologyIds));
        }

        List<Project> content = queryFactory
                .selectFrom(project)
                .distinct()
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(project.year.desc(), project.id.desc())
                .fetch();

        long total = Optional.ofNullable(
                queryFactory.select(project.count())
                        .from(project)
                        .where(builder)
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(content, pageable, total);
    }
}