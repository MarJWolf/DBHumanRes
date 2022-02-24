package com.marti.humanresbackend.services;


import com.marti.humanresbackend.models.entities.User;
import com.marti.humanresbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRep;

    @Autowired
    public UserService(UserRepository userRep) {
        this.userRep = userRep;
    }

    public User createUser(User u){
        return userRep.save(u);
    }

    public List<User> getAll(){
        return userRep.findAllByJobTitleIsNotNull();
    }

    public void updateUser(User u){
        userRep.save(u);
    }


}
