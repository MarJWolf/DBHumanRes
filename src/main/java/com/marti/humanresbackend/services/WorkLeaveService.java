package com.marti.humanresbackend.services;

import com.marti.humanresbackend.models.DTO.WorkLeaveDTO;
import com.marti.humanresbackend.models.entities.User;
import com.marti.humanresbackend.models.entities.WorkLeave;
import com.marti.humanresbackend.models.enums.Status;
import com.marti.humanresbackend.models.enums.Type;
import com.marti.humanresbackend.models.views.UpdateWorkLeaveView;
import com.marti.humanresbackend.repositories.WorkLeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@Service
public class WorkLeaveService {

    private final WorkLeaveRepository workRep;
    private final UserService userService;
    private final ManagerService manService;

    private final HolidayService holidayService;
    private Set<LocalDate> HOLIDAYS;


    @Autowired
    public WorkLeaveService(WorkLeaveRepository workRep, UserService userService, ManagerService manService, HolidayService holidayService, Set<LocalDate> holidays) {
        this.workRep = workRep;
        this.userService = userService;
        this.manService = manService;
        this.holidayService = holidayService;
    }

    private void resetHolidays(){
        HOLIDAYS = new HashSet<>();
        holidayService.getAllHolidays().forEach(holiday -> {
            HOLIDAYS.add(holiday.getHoliday());
        });
    }

    private List<WorkLeaveDTO> createWLDTO(List<WorkLeave> leaves){
        List<WorkLeaveDTO> leavesFinal = new ArrayList<>();
        for (WorkLeave leave : leaves) {
            leavesFinal.add(new WorkLeaveDTO(leave, userService.getUserById(leave.getUserId()).getFullName()));
        }
        return leavesFinal;
    }

    public WorkLeave createLeave(WorkLeave w){
        User u = userService.getUserById(w.getUserId());
        if(w.getType() == Type.Paid){
            if(getBusinessDays(w) > u.getLastYearPaidDays()+u.getThisYearPaidDays()){
                throw new RuntimeException("Нямате достатъчно дни!");
            }
        }
        return workRep.save(w);
    }

    public int getBusinessDays(WorkLeave w) {
        resetHolidays();
        int businessDays = 0;
        LocalDate d = w.getStartDate();
        while(!d.isAfter(w.getEndDate())){
            DayOfWeek dw = d.getDayOfWeek();
            if (!HOLIDAYS.contains(d)
                    && dw != DayOfWeek.SATURDAY
                    && dw != DayOfWeek.SUNDAY) {
                businessDays++;
            }
            d = d.plusDays(1);
        }
        return businessDays;
    }

    public List<WorkLeave> getAll() { return workRep.findAll();}
    public List<WorkLeaveDTO> getAllSimplified() {
        List<WorkLeave> leaves = workRep.findAll();
        return createWLDTO(leaves);}

    public List<WorkLeave> getAllByUser(Long id){
        return workRep.findAllByUserIdEqualsOrderByFillDateDesc(id);
    }

    public List<WorkLeaveDTO> getAllByUserSimplified(Long userId){
        List<WorkLeave> leaves = workRep.findAllByUserIdEqualsOrderByFillDateDesc(userId);
        return createWLDTO(leaves);
    }

    public List<WorkLeaveDTO> getAllByUserAndAdminStatSimplified(Long userId, Status stat){
        List<WorkLeave> leaves = workRep.findAllByUserIdEqualsAndStatusAdminOrderByFillDateDesc(userId, stat);
        return createWLDTO(leaves);
    }

    public List<WorkLeaveDTO> getAllByUserAndMStatSimplified(Long userId, Status stat){
        List<User> subUsers= manService.getManagerByUserManager(userId).getAllWorkers();
        List<WorkLeave> leaves =  new ArrayList<>();
        for (User subUser : subUsers) {
            leaves.addAll(workRep.findAllByUserIdEqualsAndStatusManagerOrderByFillDateDesc(subUser.getId(), stat));
        }
        return createWLDTO(leaves);
    }

    public List<WorkLeaveDTO> getAllPendingWithoutManager(){
        List<User> noSubUsers = userService.getAllByManager(null);
        List<WorkLeave> leaves =  new ArrayList<>();
        for (User subUser : noSubUsers) {
            leaves.addAll(workRep.findAllByUserIdEqualsAndStatusManagerOrderByFillDateDesc(subUser.getId(), Status.Pending));
        }
        return createWLDTO(leaves);
    }

    public List<WorkLeaveDTO> getAllByAdminStatSimplified(Status stat){
        List<WorkLeave> leaves = workRep.findByStatusAdminEqualsAndNoManager(stat);
        return createWLDTO(leaves);
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
        return new UpdateWorkLeaveView(u.getId(), u.getUserId(), u.getType(), u.getStartDate(), u.getEndDate(), u.getFillDate(), u.getStatusManager(), u.getStatusAdmin());
    }

    public void cancelWorkleave(Long WorkleaveId, Status status){
        WorkLeave workLeave = getById(WorkleaveId);
        if(status == Status.Cancelled) {
            workLeave.setStatusAdmin(Status.Cancelled);
            workLeave.setStatusManager(Status.Cancelled);
        }
        updateWorkLeave(workLeave);
    }

    public void updateStatus(Long WorkleaveId, Status status){
        WorkLeave workLeave = getById(WorkleaveId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("Manager"))){
            workLeave.setStatusManager(status);
        }else if(auth.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("Admin"))){
            workLeave.setStatusAdmin(status);
            if(userService.getUserById(workLeave.getUserId()).getManagerId() == null){
                workLeave.setStatusManager(status);
            }
        }
        takeDays(workLeave);
        updateWorkLeave(workLeave);
    }

    private void takeDays(WorkLeave workLeave) {
        if(workLeave.getStatusAdmin() == Status.Confirmed && workLeave.getStatusManager() == Status.Confirmed && workLeave.getType() == Type.Paid)
        {
            int businessDays = getBusinessDays(workLeave);
            User u = userService.getUserById(workLeave.getUserId());
            if(businessDays > u.getLastYearPaidDays()+u.getThisYearPaidDays()){
                throw new RuntimeException("Потребителя няма достатъчно дни!");
            }else{
                if((u.getLastYearPaidDays() - businessDays) >= 0){
                    u.setLastYearPaidDays(u.getLastYearPaidDays() - businessDays);
                }else{
                    businessDays = businessDays - u.getLastYearPaidDays();
                    u.setLastYearPaidDays(0);
                    u.setThisYearPaidDays(u.getThisYearPaidDays() - businessDays);
                }
                userService.updateUser(u);
            }

        }
    }
}
