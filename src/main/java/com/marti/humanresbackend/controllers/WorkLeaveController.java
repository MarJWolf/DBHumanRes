package com.marti.humanresbackend.controllers;

import com.marti.humanresbackend.models.DTO.WorkLeaveDTO;
import com.marti.humanresbackend.models.entities.WorkLeave;
import com.marti.humanresbackend.models.views.UpdateWorkLeaveView;
import com.marti.humanresbackend.models.views.WorkLeaveView;
import com.marti.humanresbackend.services.WorkLeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "workleaves")
public class WorkLeaveController {
    
    private final WorkLeaveService workLeaveService;

    @Autowired
    public WorkLeaveController(WorkLeaveService workLeaveService) {
        this.workLeaveService = workLeaveService;
    }

    @PostMapping(path = "create")
    public WorkLeave createLeave(@RequestBody WorkLeaveView wv){
        return workLeaveService.createLeave(new WorkLeave(wv));
    }

    @GetMapping(path = "all")
    public List<WorkLeave> getAll(){
        return workLeaveService.getAll();
    }

    @GetMapping(path = "byUser")
    public List<WorkLeave> getAllByUser(Long userId){
        return workLeaveService.getAllByUser(userId);
    }

    @GetMapping(path = "byUserSimplified")
    public List<WorkLeaveDTO> getAllByUserSimplified(Long userId){
        return workLeaveService.getAllByUserSimplified(userId);
    }

    @PutMapping(path = "update")
    public void updateWorkLeave(@RequestBody UpdateWorkLeaveView uwv){
        workLeaveService.updateWorkLeave(uwv);
    }

    @GetMapping(path = "edit")
    public UpdateWorkLeaveView getById(Long Id){
        return workLeaveService.getUpdateWorkLeaveView(Id);}
}
