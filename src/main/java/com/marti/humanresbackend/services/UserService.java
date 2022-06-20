package com.marti.humanresbackend.services;


import com.marti.humanresbackend.models.DTO.UserDTO;
import com.marti.humanresbackend.models.entities.Manager;
import com.marti.humanresbackend.models.entities.User;
import com.marti.humanresbackend.models.enums.Role;
import com.marti.humanresbackend.models.views.UpdateUserView;
import com.marti.humanresbackend.repositories.ManagerRepository;
import com.marti.humanresbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRep;
    private final ManagerRepository manRep;

    @Autowired
    public UserService(UserRepository userRep, ManagerRepository manRep) {
        this.userRep = userRep;
        this.manRep = manRep;
    }

    public User createUser(User u){
        if(u.getEmail().isEmpty()){
            throw new RuntimeException("Email should not be empty!");
        }
        var temp = userRep.save(u);
        if(u.getRole() == Role.Manager){
            manRep.save(new Manager(temp));
        }
        return temp;
    }

    public User getUserByEmail(String email){
        Optional<User> byEmail = userRep.findByEmail(email);
        if (byEmail.isEmpty())
            throw new RuntimeException("A user with such email does not exist!");
        else
            return byEmail.get();
    }


    public User getUserByEmailOptional(String email){
        Optional<User> byEmail = userRep.findByEmail(email);
        return byEmail.orElse(null);
    }

    public User getUserById(Long Id){
        Optional<User> byId = userRep.findById(Id);
        if(byId.isEmpty())
            throw new RuntimeException("A user with such Id does not exist!");
        else
            return byId.get();
    }

    public List<User> getAll(){
        return userRep.findAllByJobTitleIsNotNull();
    }

    public List<UserDTO> getAllSimplified(){
        List<User> users = userRep.findAllByJobTitleIsNotNull();
        return users.stream().map(UserDTO::new).collect(Collectors.toList());
    }

    public List<User> getAllByManager(Long id){
        return userRep.findByManagerId(id);
    }


    //used for "deleting" user
    public void updateUser(User u){
        userRep.save(u);
    }

    //used for updating user
    public void updateUser(UpdateUserView uuv){
        User u = getUserById(uuv.id());
        if(u.getRole() != Role.Manager && uuv.role() == Role.Manager)
        {
            manRep.save(new Manager(u));
        }
        if(u.getRole() == Role.Manager && uuv.role() != Role.Manager)
        {
            manRep.delete(manRep.findManagerByUserManager(u));
        }
        updateUser(User.updateUser(u,uuv));
    }

    public UpdateUserView getUpdateUserView(Long Id) {
        User u = getUserById(Id);
        return new UpdateUserView(u.getId(), u.getEmail(), u.getPass(), u.getFullName(), u.getJobTitle(), u.getWorkplace(), u.getPaidDays(), u.getRole(), u.getManagerId());
    }



}
