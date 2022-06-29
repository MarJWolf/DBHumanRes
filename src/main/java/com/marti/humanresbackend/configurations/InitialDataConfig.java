package com.marti.humanresbackend.configurations;

import com.marti.humanresbackend.models.entities.User;
import com.marti.humanresbackend.models.entities.WorkLeave;
import com.marti.humanresbackend.models.enums.Role;
import com.marti.humanresbackend.models.enums.Status;
import com.marti.humanresbackend.models.enums.Type;
import com.marti.humanresbackend.models.views.WorkLeaveView;
import com.marti.humanresbackend.services.HolidayService;
import com.marti.humanresbackend.services.UserService;
import com.marti.humanresbackend.services.WorkLeaveService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class InitialDataConfig implements ApplicationRunner {

    private final UserService userService;
    private final WorkLeaveService workService;
    private final HolidayService holidayService;

    public InitialDataConfig(UserService userService, WorkLeaveService workService, HolidayService holidayService) {
        this.userService = userService;
        this.workService = workService;
        this.holidayService = holidayService;
    }

    @Override
    public void run(ApplicationArguments args) {
        holidayService.getAllAPIHolidays(LocalDate.now().getYear());
        userService.createCompanyInfo("GoldenTulip OOD", "Henry Cavil");
        userService.createJobTitle("Junior developer");
        userService.createJobTitle("CEO");
        userService.createJobTitle("CFO");
        userService.createWorkplace("Varna");
        userService.createWorkplace("Sofia");
        userService.createWorkplace("WFH");
        userService.createUser(new User("example@email.com", "password1", "Kate Peterson", 3L, 1L,20, 12,0, Role.Manager, null));
        userService.createUser(new User("example2@email.com", "password12", "Kate second Peterson", 1L, 1L,22, 22,0, Role.User, 1L));
        userService.createUser(new User("example3@email.com", "password13", "Kate third Peterson", 2L, 1L,30, 27, 11, Role.Admin, null));
        userService.createUser(new User("example4@email.com", "password14", "Kate fourth Peterson", null, 1L, 30, 27,9, Role.Admin, null));
        workService.createLeave(new WorkLeave(new WorkLeaveView(1L, Type.Paid, LocalDate.of(2022,6,27),LocalDate.of(2022,6,27),LocalDate.of(2022,6,27), Status.Pending, Status.Pending)));
        workService.createLeave(new WorkLeave(new WorkLeaveView(2L, Type.Paid, LocalDate.of(2022,6,27),LocalDate.of(2022,6,27),LocalDate.of(2022,6,27), Status.Pending, Status.Pending)));
        workService.createLeave(new WorkLeave(new WorkLeaveView(3L, Type.Paid, LocalDate.of(2022,6,27),LocalDate.of(2022,6,27),LocalDate.of(2022,6,27), Status.Pending, Status.Confirmed)));
    }
}
