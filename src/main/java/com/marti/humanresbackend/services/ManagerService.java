package com.marti.humanresbackend.services;

import com.marti.humanresbackend.models.DTO.WorkLeaveDTO;
import com.marti.humanresbackend.models.entities.Manager;
import com.marti.humanresbackend.models.entities.User;
import com.marti.humanresbackend.models.views.ManagerShortView;
import com.marti.humanresbackend.models.views.ManagerView;
import com.marti.humanresbackend.repositories.ManagerRepository;
import com.marti.humanresbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ManagerService {

    private final UserRepository userRep;
    private final ManagerRepository manRep;


    @Autowired
    public ManagerService(UserRepository userRep, ManagerRepository manRep) {
        this.userRep = userRep;
        this.manRep = manRep;
    }

    public List<Manager> allManagers(){
        return manRep.findAll();
    }

    public Manager getManagerById(Long Id){
        if(manRep.findById(Id).isEmpty())
            throw new RuntimeException("A manager with such Id does not exist!");
        else
            return manRep.findById(Id).get();
    }

    public Manager getManagerByUserManager(Long Id){
         return manRep.findManagerByUserManager(userRep.getById(Id));
    }

    public void updateManager(ManagerView mv){
        Manager man = manRep.getById(mv.id());
        man.setAllWorkers(userRep.findAllById(mv.allUsers()));
        manRep.save(man);
    }

    public List<WorkLeaveDTO> getAllSubWorkleaves(Long Id){
        Manager man = getManagerByUserManager(Id);
        List<User> subUsers = man.getAllWorkers();
        List<WorkLeaveDTO> WL = new ArrayList<>();
        for (User user: subUsers) {
            for (var wl: user.getAllWorkleaves()) {
                WL.add(new WorkLeaveDTO(wl, user.getFullName()));
            }
        }
        return WL;
    }

    public List<ManagerShortView> managerNames(){
        List<ManagerShortView> names = new ArrayList<>();
        for (Manager manager: allManagers()) {
            names.add(new ManagerShortView(manager.getId(), userRep.getById(manager.getUserManager().getId()).getFullName()));
        }
        return names;
    }

}
