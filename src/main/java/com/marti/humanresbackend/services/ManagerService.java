package com.marti.humanresbackend.services;

import com.marti.humanresbackend.models.entities.Manager;
import com.marti.humanresbackend.models.views.ManagerView;
import com.marti.humanresbackend.repositories.ManagerRepository;
import com.marti.humanresbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManagerService {

    private final UserRepository userRep;
    private final ManagerRepository manRep;

    @Autowired
    public ManagerService(UserRepository userRep, ManagerRepository manRep) {
        this.userRep = userRep;
        this.manRep = manRep;
    }

    public Manager getManagerById(Long Id){
        if(manRep.findById(Id).isEmpty())
            throw new RuntimeException("A manager with such Id does not exist!");
        else
            return manRep.findById(Id).get();
    }

    public void updateManager(ManagerView mv){
        Manager man = manRep.getById(mv.id());
        man.setAllWorkers(userRep.findAllById(mv.allUsers()));
        manRep.save(man);
    }

}
