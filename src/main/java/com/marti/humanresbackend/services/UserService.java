package com.marti.humanresbackend.services;


import com.marti.humanresbackend.models.DTO.UserDTO;
import com.marti.humanresbackend.models.entities.*;
import com.marti.humanresbackend.models.enums.Role;
import com.marti.humanresbackend.models.enums.Status;
import com.marti.humanresbackend.models.views.UpdateUserView;
import com.marti.humanresbackend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRep;
    private final ManagerRepository manRep;
    private final JobTitleRepository jobRep;
    private final WorkplaceRepository workRep;
    private final CompanyInfoRepository compRep;

    private final DaysService daysService;

    private final DaysRepository daysRep;

    @Autowired
    public UserService(UserRepository userRep, ManagerRepository manRep, JobTitleRepository jobRep, WorkplaceRepository workRep, CompanyInfoRepository compRep, DaysService daysService, DaysRepository daysRep) {
        this.userRep = userRep;
        this.manRep = manRep;
        this.jobRep = jobRep;
        this.workRep = workRep;
        this.compRep = compRep;
        this.daysService = daysService;
        this.daysRep = daysRep;
    }

    //user
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
        return userRep.findAllByJobTitleIdIsNotNullOrderByFullName();
    }
    public List<User> getAllInactive(){
        return userRep.findAllByJobTitleIdIsNullOrderByFullName();
    }

    public List<UserDTO> getAllSimplified(){
        List<User> users = userRep.findAllByJobTitleIdIsNotNullOrderByFullName();
        return users.stream().map(UserDTO::new).collect(Collectors.toList());
    }

    public List<UserDTO> getAllInactiveSimplified() {
        List<User> users = userRep.findAllByJobTitleIdIsNullOrderByFullName();
        return users.stream().map(UserDTO::new).collect(Collectors.toList());
    }

    public List<User> getAllByManager(Long id) {
        return userRep.findByManagerId(id);
    }

    public List<User> getAdmins() {
        return userRep.findAllByRole(Role.Admin);
    }

    public void updateUser(User u) {
        userRep.save(u);
    }

    public void yearlyDaysUpdate(int year) {
        for (User user : getAll()) {
            if (user.getAllDays().stream().noneMatch(days -> days.getYear() == year)) {
                user.getAllDays().add(new Days(user.getId(), user.getContractPaidDays(), year, true));
                user.updateDays(year);
            }
            daysRep.saveAll(user.getAllDays());
            updateUser(user);
        }
    }

    //used for updating user
    public void updateUser(UpdateUserView uuv) {
        User u = getUserById(uuv.id());
        if (u.getRole() != Role.Manager && uuv.role() == Role.Manager) {
            manRep.save(new Manager(u));
        }
        if (u.getRole() == Role.Manager && uuv.role() != Role.Manager) {
            manRep.delete(manRep.findManagerByUserManager(u));
        }
        for (Days days:uuv.days()) {
            daysService.updateDays(days.getId(), days.getDays(), days.getYear(), days.isUse());
        }
        updateUser(User.updateUser(u,uuv));
    }

    public UpdateUserView getUpdateUserView(Long Id) {
        User u = getUserById(Id);
        return new UpdateUserView(u.getId(), u.getEmail(), u.getPass(), u.getFullName(), u.getJobTitleId(), u.getWorkplaceId(), u.getContractPaidDays(),  u.getRole(), u.getManagerId(),u.getAllDays() );
    }


    public void dismissUser(User u) {
        u.setJobTitleId(null);
        for (WorkLeave workleave : u.getAllWorkleaves()) {
            if(workleave.getStatusAdmin() == Status.Pending || workleave.getStatusManager() == Status.Pending)
            {
                workleave.setStatusManager(Status.Cancelled);
                workleave.setStatusAdmin(Status.Cancelled);
            }
        }
        u.setManagerId(null);
        updateUser(u);
    }
    //job

    public List<JobTitle> allJobTitles(){return jobRep.findAll();}

    public JobTitle getJobTitleById(Long Id){
        Optional<JobTitle> byId = jobRep.findById(Id);
        return byId.orElse(null);
    }
    public void createJobTitle(String name){
        jobRep.save(new JobTitle(name));
    }

    public void updateJobTitle(Long Id, String name){
        JobTitle jt = jobRep.getById(Id);
        jt.setJobTitle(name);
        jobRep.save(jt);
    }

    public void deleteJobTitle(Long Id){
        jobRep.delete(jobRep.getById(Id));
    }

    // workplace

    public List<Workplace> allWorkplaces(){return workRep.findAll();}
    public void createWorkplace(String name){
        workRep.save(new Workplace(name));
    }

    public void updateWorkplace(Long Id, String name){
        Workplace wp = workRep.getById(Id);
        wp.setWorkplace(name);
        workRep.save(wp);
    }

    public void deleteWorkplace(Long Id) {
        workRep.delete(workRep.getById(Id));
    }

    public String getWorkplaceByUserId(Long id) {
        return workRep.findByUserId(id);
    }

    //days


    //companyInfo


}
