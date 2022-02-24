package com.marti.humanresbackend.controllers;


import com.marti.humanresbackend.models.entities.User;
import com.marti.humanresbackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "users")
public class UserControler {

    private final UserService userService;

    @Autowired
    public UserControler(UserService userService) {
        this.userService = userService;
    }


    @PostMapping(path = "create")
    public User createUser(@RequestBody User u){
        return userService.createUser(u);
    }

    @GetMapping(path = "all")
    public List<User> getAll(){
        return userService.getAll();
    }

    @PutMapping(path = "delete")
    public void deleteUser(@RequestBody User u){
        u.setJobTitle(null);
        userService.updateUser(u);
    }

    @PutMapping(path = "update")
    public void updateUser(@RequestBody User u){

        userService.updateUser(u);
    }

}
