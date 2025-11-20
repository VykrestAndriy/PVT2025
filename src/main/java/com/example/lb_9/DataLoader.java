package com.example.lb_9;

import com.example.lb_9.model.Role;
import com.example.lb_9.model.User;
import com.example.lb_9.repository.RoleRepository;
import com.example.lb_9.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataLoader {

    @Bean
    @Transactional
    public CommandLineRunner loadData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // 1. Створення або перевірка ролей
            Role adminRole = roleRepository.findByName("ROLE_ADMIN");
            if (adminRole == null) {
                adminRole = roleRepository.save(new Role("ROLE_ADMIN"));
                System.out.println(">>> Створено роль: ROLE_ADMIN");
            }

            Role salesRole = roleRepository.findByName("ROLE_SALES");
            if (salesRole == null) {
                salesRole = roleRepository.save(new Role("ROLE_SALES"));
                System.out.println(">>> Створено роль: ROLE_SALES");
            }

            Role userRole = roleRepository.findByName("ROLE_USER");
            if (userRole == null) {
                userRole = roleRepository.save(new Role("ROLE_USER"));
                System.out.println(">>> Створено роль: ROLE_USER");
            }

            // 2. Створення адміністратора, якщо він не існує
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@tickets.com");

                String encodedPassword = passwordEncoder.encode("admin");
                admin.setPassword(encodedPassword);

                Set<Role> adminRoles = new HashSet<>();
                adminRoles.add(adminRole);
                adminRoles.add(salesRole);

                admin.setRoles(adminRoles);
                userRepository.save(admin);

                System.out.println("******************************************************************");
                System.out.println(">>> УВАГА! Створено користувача 'admin' з паролем 'admin'.");
                System.out.println(">>> Закодований пароль: " + encodedPassword);
                System.out.println("******************************************************************");
            }
        };
    }
}