package com.marti.humanresbackend.controllers;

import com.marti.humanresbackend.models.DTO.WorkLeaveDTO;
import com.marti.humanresbackend.models.entities.WorkLeave;
import com.marti.humanresbackend.models.enums.Status;
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

    @PutMapping(path = "changeStatus")
    public void changeStatus(@RequestParam Long workleaveId, Status status){
        workLeaveService.updateStatus(workleaveId, status);
    }

    @PutMapping(path = "cancelWorkleave")
    public void cancelWorkLeave(@RequestParam Long workleaveId, Status status){
        workLeaveService.cancelWorkleave(workleaveId, status);
    }

    @GetMapping(path = "all")
    public List<WorkLeave> getAll(){
        return workLeaveService.getAll();
    }

    @GetMapping(path = "allSimplified")
    public List<WorkLeaveDTO> getAllSimplified(){
        return workLeaveService.getAllSimplified();
    }

    @GetMapping(path = "byUser")
    public List<WorkLeave> getAllByUser(@RequestParam Long userId){
        return workLeaveService.getAllByUser(userId);
    }

    @GetMapping(path = "byUserSimplified")
    public List<WorkLeaveDTO> getAllByUserSimplified(@RequestParam Long userId){
        return workLeaveService.getAllByUserSimplified(userId);
    }

    @GetMapping(path = "byUserAndAdminStatSimplified")
    public List<WorkLeaveDTO> getAllByUserAndAdminStatSimplified(@RequestParam Long userId, Status status){
        return workLeaveService.getAllByUserAndAdminStatSimplified(userId, status);
    }

    @GetMapping(path = "pendingWithoutManager")
    public List<WorkLeaveDTO> pendingWithoutManager(){
        return workLeaveService.getAllPendingWithoutManager();
    }

    @GetMapping(path = "byUserAndMStatSimplified")
    public List<WorkLeaveDTO> getAllByUserAndMStatSimplified(@RequestParam Long userId, Status status){
        return workLeaveService.getAllByUserAndMStatSimplified(userId, status);
    }

    @GetMapping(path = "byAdminStatSimplified")
    public List<WorkLeaveDTO> getAllByAdminStatSimplified(@RequestParam Status status){
        return workLeaveService.getAllByAdminStatSimplified(status);
    }

    @PutMapping(path = "update")
    public void updateWorkLeave(@RequestBody UpdateWorkLeaveView uwv){
        workLeaveService.updateWorkLeave(uwv);
    }

    @GetMapping(path = "edit")
    public UpdateWorkLeaveView getById(@RequestParam Long Id){
        return workLeaveService.getUpdateWorkLeaveView(Id);}
}
