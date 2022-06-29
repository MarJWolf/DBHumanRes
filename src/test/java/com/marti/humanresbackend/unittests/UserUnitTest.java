package com.marti.humanresbackend.unittests;

import com.marti.humanresbackend.models.entities.User;
import com.marti.humanresbackend.models.enums.Role;
import com.marti.humanresbackend.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY,connection = EmbeddedDatabaseConnection.H2)
class UserUnitTest {
    @Autowired
    UserService userService;

    @Test
    void createUserShouldWork() {
        User user = new User("example@email.com","examplePass","Full Name", 1L, 1L, 22, 22,0, Role.User, null);
        User save = userService.createUser(user);
        assert save.getId()!=null;
    }
    @Test
    void createUserShouldThrowException() {
        Assertions.assertThrows(RuntimeException.class,() -> {
            User user = new User("","examplePass","Full Name", 1L, 1L, 22, 22,0, Role.User, null
            );
            userService.createUser(user);
        });
    }
}