package com.marti.humanresbackend.configurations;

import com.marti.humanresbackend.models.entities.User;
import com.marti.humanresbackend.models.enums.Role;
import com.marti.humanresbackend.services.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitialDataConfig implements ApplicationRunner {

    private final UserService userService;

    public InitialDataConfig(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        userService.createUser(new User("example@email.com", "password1", "Kate Peterson", "junior developer", "Varna", 22, Role.User, null));
        userService.createUser(new User("example2@email.com", "password12", "Kate second Peterson", "junior developer", "Varna", 12, Role.Manager, null));
        userService.createUser(new User("example3@email.com", "password13", "Kate third Peterson", "junior developer", "Varna", 27, Role.HR, null));
    }
}
