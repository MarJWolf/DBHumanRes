package com.marti.humanresbackend.controllers;

import com.marti.humanresbackend.models.DTO.UserDTO;
import com.marti.humanresbackend.models.entities.*;
import com.marti.humanresbackend.models.views.UpdateUserView;
import com.marti.humanresbackend.models.views.UserView;
import com.marti.humanresbackend.services.CompanyInfoService;
import com.marti.humanresbackend.services.DaysService;
import com.marti.humanresbackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "users")
public class UserController {

    private final UserService userService;
    private final DaysService daysService;
    private final CompanyInfoService companyService;

    @Autowired
    public UserController(UserService userService, DaysService daysService, CompanyInfoService companyService) {

        this.userService = userService;
        this.daysService = daysService;
        this.companyService = companyService;
    }


    @PostMapping(path = "create")
    public User createUser(@RequestBody UserView uv){
        User u = new User(uv);
        return userService.createUser(u);
    }

    @GetMapping(path = "all")
    public List<User> getAll(){
        return userService.getAll();
    }

    @GetMapping(path = "allInactive")
    public List<User> getAllInactive(){
        return userService.getAllInactive();
    }

    @GetMapping(path = "allSimplified")
    public List<UserDTO> getAllSimplified(){return userService.getAllSimplified();}

    @GetMapping(path = "allInactiveSimplified")
    public List<UserDTO> getAllInactiveSimplified(){return userService.getAllInactiveSimplified();}

    @GetMapping(path = "byEmail")
    public User getOne(@RequestParam String email) {return userService.getUserByEmail(email);}

    @GetMapping(path = "byManagerId")
    public List<User> getByManagerId(@RequestParam Long id){
        return userService.getAllByManager(id);
    }

    @PutMapping(path = "dismiss")
    public void deleteUser(@RequestBody User u){
        userService.dismissUser(u);
    }

    @PutMapping(path = "update")
    public void updateUser(@RequestBody UpdateUserView uuv){ userService.updateUser(uuv);}

    @GetMapping(path = "byId")
    public User getById(@RequestParam Long Id) { return userService.getUserById(Id);}

    @GetMapping(path = "EditById")
    public UpdateUserView getUUV(@RequestParam Long Id){
        return userService.getUpdateUserView(Id);
    }

    //JobTitle

    @GetMapping(path = "allJobTitles")
    public List<JobTitle> allJobTitles(){return userService.allJobTitles();}

    @DeleteMapping(path = "deleteJobTitle")
    public void deleteJobTitle(@RequestParam Long Id){userService.deleteJobTitle(Id);}

    @PostMapping(path = "createJobTitle")
    public void createJobTitle(@RequestParam String name){userService.createJobTitle(name);}

    @GetMapping(path = "getJobTitleById")
    public JobTitle getJobTitleById(@RequestParam Long Id){return userService.getJobTitleById(Id);}

    //Workplace

    @GetMapping(path = "allWorkplaces")
    public List<Workplace> allWorkplaces(){return userService.allWorkplaces();}

    @DeleteMapping(path = "deleteWorkplace")
    public void deleteWorkplace(@RequestParam Long Id){userService.deleteWorkplace(Id);}

    @PostMapping(path = "createWorkplace")
    public void createWorkplace(@RequestParam String name){userService.createWorkplace(name);}

    //days

    @GetMapping(path = "allDays")
    public List<Days> allDays() {
        return daysService.allDays();
    }

    @GetMapping(path = "allDaysByUserId")
    public List<Days> allDaysByUserId(@RequestParam Long userID) {
        return daysService.allDaysByUser(userID);
    }

    @PostMapping(path = "createDays")
    public List<Days> createDays(@RequestBody Days days){
        return daysService.createDays(days);
    }

    @PutMapping(path = "updateDays")
    public void updateDays(@RequestParam Long daysID, @RequestParam int days,@RequestParam int year,@RequestParam boolean use){
        daysService.updateDays(daysID, days, year, use);
    }

    @DeleteMapping(path = "deleteDays")
    public void deleteDays(@RequestParam Long Id){
        daysService.deleteDays(Id);
    }

    //ComapnyInfo

    @PutMapping(path = "updateCompanyInfo")
    public void updateCompanyInfo(@RequestParam String Cname, @RequestParam String Oname) {
        companyService.updateCompanyInfo(Cname, Oname);
    }

    @GetMapping(path = "getCompanyInfo")
    public CompanyInfo getCompanyInfo() {
        return companyService.getCompanyInfo();
    }
}
