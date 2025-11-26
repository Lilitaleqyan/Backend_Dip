package org.example.backend_dip.component;

import org.example.backend_dip.entity.AdminForControl;
import org.example.backend_dip.entity.enums.Role;
import org.example.backend_dip.securityConfig.SecurityConfig;
import org.example.backend_dip.service.AdminService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminComponent implements ApplicationRunner {
    private final AdminService service;
    private final SecurityConfig securityConfig;

    @Value("${app.admin.username}")
    private String username;

    @Value("${app.admin.password}")
    private String password;

    public AdminComponent(AdminService service, SecurityConfig securityConfig) {
        this.service = service;
        this.securityConfig = securityConfig;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (service.existsByUsername(username)) {
            return;
        }

        AdminForControl admin = new AdminForControl();
        admin.setRole(Role.ADMIN);
        admin.setUsername(username);
        admin.setPassword(securityConfig.passwordEncoder().encode(password));
        System.out.println(admin);
        service.save(admin);
    }

}
