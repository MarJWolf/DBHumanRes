package com.marti.humanresbackend.unittests;


import com.marti.humanresbackend.models.entities.User;
import com.marti.humanresbackend.models.entities.WorkLeave;
import com.marti.humanresbackend.models.enums.Role;
import com.marti.humanresbackend.models.enums.Status;
import com.marti.humanresbackend.models.enums.Type;
import com.marti.humanresbackend.models.views.UpdateWorkLeaveView;
import com.marti.humanresbackend.repositories.UserRepository;
import com.marti.humanresbackend.repositories.WorkLeaveRepository;
import com.marti.humanresbackend.services.UserService;
import com.marti.humanresbackend.services.WorkLeaveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

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

    @BeforeEach
    void setUp() {
        userRep.deleteAll();
        workLeaveRep.deleteAll();
    }

    @Test
    void createWorkLeaveShouldWork() {
        userService.createUser(new User("example@email.com","examplePass","Full Name", 1L, 1L, 22, 22,0, Role.User, null));
        WorkLeave wl = new WorkLeave(Type.Paid, LocalDate.now(),LocalDate.now(), LocalDate.now().plusDays(3L), Status.Pending, Status.Pending);
        wl.setUserId(1L);
        WorkLeave savewl = workLeaveService.createLeave(wl);
        assert savewl.getId()!=null;
    }



    @Test
    void updateWorkLeaveShouldWork() {
        User user = userService.createUser(new User("example@email.com","examplePass","Full Name", 1L, 1L, 22, 22,0, Role.User, null));
        WorkLeave u = new WorkLeave(Type.Paid, LocalDate.now(),LocalDate.now(), LocalDate.now().plusDays(5L), Status.Pending, Status.Pending);
        u.setUserId(user.getId());
        workLeaveService.createLeave(u);
        workLeaveService.updateWorkLeave(new UpdateWorkLeaveView(u.getId(), u.getUserId(), u.getType(), u.getStartDate(), u.getEndDate(), u.getFillDate(), Status.Confirmed, u.getStatusAdmin()));
        WorkLeave wl = workLeaveService.getById(u.getId());
        assert wl.getStatusManager().equals(Status.Confirmed);
    }
}

