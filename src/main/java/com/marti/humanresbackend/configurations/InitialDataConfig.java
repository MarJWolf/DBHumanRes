package com.marti.humanresbackend.configurations;

import com.marti.humanresbackend.models.entities.User;
import com.marti.humanresbackend.models.entities.WorkLeave;
import com.marti.humanresbackend.models.enums.Role;
import com.marti.humanresbackend.models.enums.Status;
import com.marti.humanresbackend.models.enums.Type;
import com.marti.humanresbackend.models.views.WorkLeaveView;
import com.marti.humanresbackend.services.UserService;
import com.marti.humanresbackend.services.WorkLeaveService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.Date;

@Configuration
public class InitialDataConfig implements ApplicationRunner {

    private final UserService userService;
    private final WorkLeaveService workService;

    public InitialDataConfig(UserService userService, WorkLeaveService workService) {
        this.userService = userService;
        this.workService = workService;
    }

    @Override
    public void run(ApplicationArguments args) {
        userService.createUser(new User("example@email.com", "password1", "Kate Peterson", "junior developer", "Varna", 12, Role.Manager, null));
        userService.createUser(new User("example2@email.com", "password12", "Kate second Peterson", "junior developer", "Varna", 22, Role.User, 1L));
        userService.createUser(new User("example3@email.com", "password13", "Kate third Peterson", "junior developer", "Varna", 27, Role.Admin, null));
        userService.createUser(new User("example4@email.com", "password14", "Kate fourth Peterson", null, "Varna", 27, Role.Admin, null));
        workService.createLeave(new WorkLeave(new WorkLeaveView(1L, Type.Paid, LocalDate.now(),LocalDate.now(),LocalDate.now(), Status.Pending, Status.Pending)));
        workService.createLeave(new WorkLeave(new WorkLeaveView(2L, Type.Paid, LocalDate.now(),LocalDate.now(),LocalDate.now(), Status.Pending, Status.Pending)));
        workService.createLeave(new WorkLeave(new WorkLeaveView(3L, Type.Paid, LocalDate.now(),LocalDate.now(),LocalDate.now(), Status.Pending, Status.Confirmed)));
    }
}
