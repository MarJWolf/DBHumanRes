package com.marti.humanresbackend.unittests;

import com.marti.humanresbackend.models.entities.Manager;
import com.marti.humanresbackend.models.entities.User;
import com.marti.humanresbackend.models.enums.Role;
import com.marti.humanresbackend.models.views.UpdateUserView;
import com.marti.humanresbackend.repositories.UserRepository;
import com.marti.humanresbackend.services.ManagerService;
import com.marti.humanresbackend.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY,connection = EmbeddedDatabaseConnection.H2)
class UserUnitTest {
    @Autowired
    UserService userService;
    @Autowired
    ManagerService manService;

    @Autowired
    UserRepository userRep;

    @Test
    void createUserShouldWork() {
        User user = new User("example5@email.com","examplePass","Full Name", 1L, 1L,
                22, 22,0, Role.User, null);
        User save = userService.createUser(user);
        assert save.getId()!=null;
    }
    @Test
    void createUserShouldThrowException() {
        Assertions.assertThrows(RuntimeException.class,() -> {
            User user = new User("","examplePass","Full Name", 1L, 1L, 22,
                    22,0, Role.User, null
            );
            userService.createUser(user);
        });
    }

    @Test
    void getByEmailUserShouldWork() {
        User user = userService.getUserByEmail("example@email.com");
        assert user.getId()!=null;
    }
    @Test
    void getByEmailUserShouldThrowException() {
        Assertions.assertThrows(RuntimeException.class,() -> {
            userService.getUserByEmail("No such email in DB");
        });
    }


    @Test
    void getByIdUserShouldWork() {
        User user = userService.getUserById(1L);
        assert user.getId()!=null;
    }
    @Test
    void getByIdUserShouldThrowException() {
        Assertions.assertThrows(RuntimeException.class,() -> {
            User user = userService.getUserById(7L);
        });
    }

    @Test
    void updateUserStatusToManagerShouldWork(){
        User u = userService.getUserById(2L);
        userService.updateUser(new UpdateUserView(u.getId(),u.getEmail(),u.getPass(), u.getFullName(), u.getJobTitleId(), u.getWorkplaceId(), u.getContractPaidDays(), u.getThisYearPaidDays(), u.getLastYearPaidDays(), Role.Manager,u.getManagerId()));
        Manager manager = manService.getManagerByUserManager(u.getId());
        assert manager.getId()!=null;
    }

    @Test
    void updateUserStatusFromManagerShouldWork(){
        User u = userService.getUserById(1L);
        userService.updateUser(new UpdateUserView(u.getId(),u.getEmail(),u.getPass(), u.getFullName(), u.getJobTitleId(), u.getWorkplaceId(), u.getContractPaidDays(), u.getThisYearPaidDays(), u.getLastYearPaidDays(), Role.User,u.getManagerId()));
        Manager manager = manService.getManagerByUserManager(u.getId());
        assert manager == null;
    }

    @Test
    @Transactional
    void dismissUserShouldWork(){
        User u = userRep.findById(2L).get();
        userService.dismissUser(u);
        u = userService.getUserById(2L);
        assert u.getJobTitleId() == null;
    }

}