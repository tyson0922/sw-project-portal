package kopo.swprojectportal.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "admin")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;  // BCrypt hash — never store plaintext

    @Column(nullable = false, length = 20)
    private String role;  // e.g. "ADMIN" — single role is enough for now

    @Builder
    private Admin(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public void changePassword(String password) { this.password = password; }
}
