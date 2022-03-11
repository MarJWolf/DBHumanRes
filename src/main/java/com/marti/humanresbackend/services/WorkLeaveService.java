package com.marti.humanresbackend.services;

import com.marti.humanresbackend.models.entities.WorkLeave;
import com.marti.humanresbackend.repositories.WorkLeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkLeaveService {

    private final WorkLeaveRepository workRep;

    @Autowired
    public WorkLeaveService(WorkLeaveRepository workRep) {
        this.workRep = workRep;
    }

    public WorkLeave createLeave(WorkLeave w){
        return workRep.save(w);
    }

    public List<WorkLeave> getByUser(Long id){
        return workRep.findAllByUserIdEquals(id);
    }

    public void updateWorkLeave(WorkLeave w){
        workRep.save(w);
    }

}
