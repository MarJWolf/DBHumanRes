package com.marti.humanresbackend.controllers;

import com.marti.humanresbackend.models.DTO.UserDTO;
import com.marti.humanresbackend.models.entities.CompanyInfo;
import com.marti.humanresbackend.models.entities.JobTitle;
import com.marti.humanresbackend.models.entities.User;
import com.marti.humanresbackend.models.entities.Workplace;
import com.marti.humanresbackend.models.views.UpdateUserView;
import com.marti.humanresbackend.models.views.UserView;
import com.marti.humanresbackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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

    //ComapnyInfo

    @PutMapping(path = "updateCompanyInfo")
    public void updateCompanyInfo(@RequestParam String Cname, @RequestParam String Oname){userService.updateCompanyInfo(Cname, Oname);}

    @GetMapping(path = "getCompanyInfo")
    public CompanyInfo getCompanyInfo(){return userService.getCompanyInfo();}
}
