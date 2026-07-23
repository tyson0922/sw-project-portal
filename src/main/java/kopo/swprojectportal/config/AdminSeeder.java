package kopo.swprojectportal.config;

import kopo.swprojectportal.entity.Admin;
import kopo.swprojectportal.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;


    @Value("${app.admin.seed-username}")
    private String seedUsername;

    @Value("${app.admin.seed-password}")
    private String seedPassword;

    @Override
    public void run(String... args) {
        if (adminRepository.count() == 0) {
            Admin admin = Admin.builder()
                    .username(seedUsername)
                    .password(passwordEncoder.encode(seedPassword))
                    .role("ADMIN")
                    .build();
            adminRepository.save(admin);
        }
    }
}
