package com.marti.humanresbackend.unittests;


import com.marti.humanresbackend.models.entities.User;
import com.marti.humanresbackend.models.entities.WorkLeave;
import com.marti.humanresbackend.models.enums.Role;
import com.marti.humanresbackend.models.enums.Status;
import com.marti.humanresbackend.models.enums.Type;
import com.marti.humanresbackend.models.views.APIHolidayView;
import com.marti.humanresbackend.models.views.UpdateWorkLeaveView;
import com.marti.humanresbackend.repositories.ManagerRepository;
import com.marti.humanresbackend.repositories.UserRepository;
import com.marti.humanresbackend.repositories.WorkLeaveRepository;
import com.marti.humanresbackend.services.HolidayService;
import com.marti.humanresbackend.services.UserService;
import com.marti.humanresbackend.services.WorkLeaveService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Objects;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY,connection = EmbeddedDatabaseConnection.H2)
public class WorkLeaveUnitTest {
    @Autowired
    WorkLeaveService workLeaveService;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRep;
    @Autowired
    WorkLeaveRepository workLeaveRep;
    @Autowired
    ManagerRepository manRep;
    @Autowired
    HolidayService holidayService;

    @BeforeEach
    void setUp() {
        manRep.deleteAll();
        userRep.deleteAll();
        workLeaveRep.deleteAll();
    }

    @Test
    void createWorkLeaveShouldWork() {
        User user = userService.createUser(new User("example@email.com", "examplePass", "Full Name", 1L,
                1L, 22, Role.User, null));
        WorkLeave wl = new WorkLeave(Type.Paid, LocalDate.now(), LocalDate.now(), LocalDate.now().plusDays(3L), Status.Pending, Status.Pending);
        wl.setUserId(user.getId());
        WorkLeave savewl = workLeaveService.createLeave(wl);
        assert savewl.getId()!=null;
    }

    @Test
    void createWorkLeaveShouldThrowException() {
        Assertions.assertThrows(RuntimeException.class,() -> {
            userService.createUser(new User("example@email.com", "examplePass", "Full Name", 1L,
                    1L, 22, Role.User, null));
            WorkLeave wl = new WorkLeave(Type.Paid, LocalDate.now(), LocalDate.now(), LocalDate.now().plusDays(3L), Status.Pending, Status.Pending);
            wl.setUserId(1L);
            workLeaveService.createLeave(wl);
        });
    }


    @Test
    void updateWorkLeaveShouldWork() {
        User user = userService.createUser(new User("example@email.com", "examplePass", "Full Name", 1L, 1L,
                22, Role.User, null));
        WorkLeave u = new WorkLeave(Type.Paid, LocalDate.now(), LocalDate.now(), LocalDate.now().plusDays(5L), Status.Pending, Status.Pending);
        u.setUserId(user.getId());
        workLeaveService.createLeave(u);
        workLeaveService.updateWorkLeave(new UpdateWorkLeaveView(u.getId(), u.getUserId(), u.getType(), u.getStartDate(), u.getEndDate(),
                u.getFillDate(), Status.Confirmed, u.getStatusAdmin()));
        WorkLeave wl = workLeaveService.getById(u.getId());
        assert wl.getStatusManager().equals(Status.Confirmed);
    }

    @Test
    void getByIdShouldWork(){
        User user = userService.createUser(new User("example@email.com", "examplePass", "Full Name", 1L, 1L,
                22, Role.User, null));
        WorkLeave u = new WorkLeave(Type.Paid, LocalDate.now(), LocalDate.now(), LocalDate.now().plusDays(5L), Status.Pending, Status.Pending);
        u.setUserId(user.getId());
        WorkLeave wl = workLeaveService.createLeave(u);
        assert Objects.equals(wl.getId(), workLeaveService.getById(wl.getId()).getId());
    }

    @Test
    void getByIdShouldThrowException(){
        Assertions.assertThrows(RuntimeException.class,() -> {
            User user = userService.createUser(new User("example@email.com", "examplePass", "Full Name", 1L, 1L,
                    22, Role.User, null));
            WorkLeave u = new WorkLeave(Type.Paid, LocalDate.now(), LocalDate.now(), LocalDate.now().plusDays(5L), Status.Pending, Status.Pending);
            u.setUserId(user.getId());
            WorkLeave wl = workLeaveService.createLeave(u);
            workLeaveService.getById(7L);
        });
    }

    @Test
    void checkBusinessDaysShouldWork(){
        User user = userService.createUser(new User("example@email.com", "examplePass", "Full Name", 1L, 1L,
                22, Role.User, null));
        holidayService.createHoliday(new APIHolidayView(LocalDate.of(2022, 12, 16),"SDFasdfasdfeafsDASfe"));
        WorkLeave u = new WorkLeave(Type.Paid, LocalDate.of(2022, 12, 12),LocalDate.now(), LocalDate.of(2022, 12, 19), Status.Pending, Status.Pending);
        u.setUserId(user.getId());
        WorkLeave wl = workLeaveService.createLeave(u);
        assert workLeaveService.getBusinessDays(wl) == 5;
    }
}

