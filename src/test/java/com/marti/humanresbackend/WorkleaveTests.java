package com.marti.humanresbackend;

import com.marti.humanresbackend.models.entities.Days;
import com.marti.humanresbackend.models.entities.User;
import com.marti.humanresbackend.models.entities.WorkLeave;
import com.marti.humanresbackend.models.enums.Role;
import com.marti.humanresbackend.models.enums.Status;
import com.marti.humanresbackend.models.enums.Type;
import com.marti.humanresbackend.repositories.DaysRepository;
import com.marti.humanresbackend.repositories.UserRepository;
import com.marti.humanresbackend.repositories.WorkLeaveRepository;
import com.marti.humanresbackend.services.EmailService;
import com.marti.humanresbackend.services.UserService;
import com.marti.humanresbackend.services.WorkLeaveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Set;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY, connection = EmbeddedDatabaseConnection.H2)
class WorkleaveTests {
    @MockBean
    EmailService emailService;
    @Autowired
    UserService userService;
    @Autowired
    WorkLeaveService workLeaveService;

    @Autowired
    UserRepository userRep;
    @Autowired
    WorkLeaveRepository workLeaveRepository;
    @Autowired
    DaysRepository daysRepository;

    User user;
    User manager;
    User admin;

    @BeforeEach
    void setUp() {
        Mockito.doNothing().when(emailService).sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyList());

        workLeaveRepository.deleteAll();
        userRep.deleteAll();
        daysRepository.deleteAll();

        manager = userRep.save(new User("example1@email.com", "examplePass", "manager", 1L, 1L, 22, Role.Manager, null));
        user = userRep.save(new User("example@email.com", "examplePass", "Full Name", 1L, 1L, 22, Role.User, manager.getId()));
        admin = userRep.save(new User("example2@email.com", "examplePass", "admin", 1L, 1L, 22, Role.Admin, null));

        daysRepository.save(new Days(user.getId(), user.getContractPaidDays(), LocalDate.now().getYear(), true));
        daysRepository.save(new Days(user.getId(), user.getContractPaidDays(), LocalDate.now().minusYears(1L).getYear(), true));
    }

    //    Expected result: days should be taken from the last year
    @Test
    void createWorkleaveAndApproveIt() {
        WorkLeave workleave = new WorkLeave(Type.Paid, LocalDate.now().plusDays(5L), LocalDate.now(), LocalDate.now().plusDays(10L), Status.Pending, Status.Pending);
        WorkLeave leave = workLeaveService.createLeave(workleave);

        setUserInSession(manager);
        workLeaveService.updateStatus(leave.getId(), Status.Confirmed);
        setUserInSession(admin);
        workLeaveService.updateStatus(leave.getId(), Status.Confirmed);


    }

    void setUserInSession(User user) {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user.getId(), user.getPass(), Set.of(new SimpleGrantedAuthority(user.getRole().toString()))));
    }
}