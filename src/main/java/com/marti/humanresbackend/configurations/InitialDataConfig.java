package com.marti.humanresbackend.configurations;

import com.marti.humanresbackend.models.entities.Days;
import com.marti.humanresbackend.models.entities.User;
import com.marti.humanresbackend.models.enums.Role;
import com.marti.humanresbackend.services.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class InitialDataConfig implements ApplicationRunner {

    private final UserService userService;
    private final WorkLeaveService workService;
    private final CompanyInfoService companyService;

    private final DaysService daysService;
    private final HolidayService holidayService;

    public InitialDataConfig(UserService userService, WorkLeaveService workService, CompanyInfoService companyService, DaysService daysService, HolidayService holidayService) {
        this.userService = userService;
        this.workService = workService;
        this.companyService = companyService;
        this.daysService = daysService;
        this.holidayService = holidayService;
    }

    //todo: remove initial data except for admin email.
    @Override
    public void run(ApplicationArguments args) {
        holidayService.getAllAPIHolidays(LocalDate.now().getYear());
        companyService.createCompanyInfo("GoldenTulip OOD", "Henry Cavil");
        userService.createJobTitle("Junior developer");
        userService.createJobTitle("CEO");
        userService.createJobTitle("CFO");
        userService.createWorkplace("Varna");
        userService.createWorkplace("Sofia");
        userService.createWorkplace("WFH");
        userService.createUser(new User("angel.g.prodanov@gmail.com", "password1", "Kate Peterson", 3L, 1L, 20, Role.Manager, null));
        userService.createUser(new User("martinavalkova1999@gmail.com", "password12", "Kate second Peterson", 1L, 1L, 22, Role.User, 1L));
        userService.createUser(new User("martinksssss@gmail.com", "password13", "Kate third Peterson", 2L, 1L, 30, Role.Admin, null));
        daysService.createDays(new Days(1L, 20, 2021, true));
        daysService.createDays(new Days(1L, 20, 2022, true));
        daysService.createDays(new Days(2L, 20, 2021, true));
        daysService.createDays(new Days(2L, 20, 2022, true));
        daysService.createDays(new Days(3L, 20, 2021, true));
        daysService.createDays(new Days(3L, 20, 2022, true));
    }
}
