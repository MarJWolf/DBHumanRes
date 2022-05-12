package com.marti.humanresbackend.services;

import com.marti.humanresbackend.models.DTO.WorkLeaveDTO;
import com.marti.humanresbackend.models.entities.WorkLeave;
import com.marti.humanresbackend.models.views.UpdateWorkLeaveView;
import com.marti.humanresbackend.repositories.WorkLeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<WorkLeave> getAll() {return workRep.findAll();}

    public List<WorkLeave> getAllByUser(Long id){
        return workRep.findAllByUserIdEquals(id);
    }

    public List<WorkLeaveDTO> getAllByUserSimplified(Long userId){
        List<WorkLeave> leaves = workRep.findAllByUserIdEquals(userId);
        return leaves.stream().map(WorkLeaveDTO::new).collect(Collectors.toList());
    }

    public void updateWorkLeave(WorkLeave w){
        workRep.save(w);
    }

    public void updateWorkLeave(UpdateWorkLeaveView uwlv){workRep.save(WorkLeave.updateWorkLeave(getById(uwlv.id()), uwlv));}

    public WorkLeave getById(Long Id){
        if (workRep.findById(Id).isEmpty())
            throw new RuntimeException("A Leave with such id does not exist!");
        else
            return workRep.findById(Id).get();
    }

    public UpdateWorkLeaveView getUpdateWorkLeaveView(Long Id) {
        WorkLeave u = getById(Id);
        return new UpdateWorkLeaveView(u.getId(), u.getUserId(), u.getType(), u.getStartDate(), u.getEndDate(), u.getFillDate(), u.getStatusManager(), u.getStatusHr());
    }
}
