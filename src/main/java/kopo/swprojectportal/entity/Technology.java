package kopo.swprojectportal.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "technology")
@Cacheable                                          // opt-in to L2 cache (sharedCache.mode = ENABLE_SELECTIVE)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)  // READ_WRITE, not READ_ONLY — this entity can be updated
@DynamicInsert
@DynamicUpdate
@Getter                                             // no @Setter — mutation only via changeX() methods below
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA needs this, but blocks `new Technology()` elsewhere
public class Technology {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)  // store enum name, not ordinal — safe if enum order changes later
    @Column(nullable = false)
    private TechnologyCategory category;

    @Builder  // scoped to this constructor only — id isn't a param, so it can't be set via builder
    private Technology(String name, TechnologyCategory category) {
        this.name = name;
        this.category = category;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeCategory(TechnologyCategory category) {
        this.category = category;
    }
}