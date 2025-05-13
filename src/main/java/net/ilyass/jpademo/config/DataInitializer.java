package net.ilyass.jpademo.config;

import net.ilyass.jpademo.entity.Role;
import net.ilyass.jpademo.entity.Task;
import net.ilyass.jpademo.entity.User;
import net.ilyass.jpademo.repository.TaskRepository;
import net.ilyass.jpademo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository,
                                   TaskRepository taskRepository,
                                   PasswordEncoder passwordEncoder) {
        return args -> {

            User admin = new User();
            admin.setName("Admin User");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);


            User user = new User();
            user.setName("Regular User");
            user.setEmail("user@example.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRole(Role.USER);
            userRepository.save(user);

            Task task1 = new Task();
            task1.setTitle("pfa");
            task1.setDescription("just for test ");
            task1.setStatus(Task.TaskStatus.À_FAIRE);
            task1.setAssignedUser(user);
            taskRepository.save(task1);

            Task task2 = new Task();
            task2.setTitle("Fix authentication bug");
            task2.setDescription("just for test stage pfa ");
            task2.setStatus(Task.TaskStatus.EN_COURS);
            task2.setAssignedUser(user);
            taskRepository.save(task2);

        };
    }
}