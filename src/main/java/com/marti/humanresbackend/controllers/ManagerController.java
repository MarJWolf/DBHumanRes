package com.marti.humanresbackend.controllers;

import com.marti.humanresbackend.models.DTO.WorkLeaveDTO;
import com.marti.humanresbackend.models.entities.Manager;
import com.marti.humanresbackend.models.views.ManagerShortView;
import com.marti.humanresbackend.models.views.ManagerView;
import com.marti.humanresbackend.services.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "managers")
public class ManagerController {

    private final ManagerService manSer;

    @Autowired
    public ManagerController(ManagerService manSer) {
        this.manSer = manSer;
    }

    @PutMapping(path = "update")
    public void updateManager(@RequestBody ManagerView mv){ manSer.updateManager(mv); }

    @GetMapping(path = "all")
    public void allManagers(){ manSer.allManagers(); }

    @GetMapping(path = "byId")
    public Manager getById(@RequestParam Long Id) { return manSer.getManagerById(Id);}

    @GetMapping(path = "byUserId")
    public Manager getByUserId(@RequestParam Long Id) { return manSer.getManagerByUserManager(Id);}

    @GetMapping(path = "workleavesByUserId")
    public List<WorkLeaveDTO> getWorkleavesByUserId(@RequestParam Long Id) { return manSer.getAllSubWorkleaves(Id);}

    @GetMapping(path = "managerNames")
    public List<ManagerShortView> getManagerNames() { return manSer.managerNames();}



}
