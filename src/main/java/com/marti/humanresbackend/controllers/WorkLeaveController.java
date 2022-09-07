package com.marti.humanresbackend.controllers;

import com.marti.humanresbackend.models.DTO.CalendarDTO;
import com.marti.humanresbackend.models.DTO.WorkLeaveDTO;
import com.marti.humanresbackend.models.DTO.calendar.CalendarYearDTO;
import com.marti.humanresbackend.models.entities.WorkLeave;
import com.marti.humanresbackend.models.enums.Status;
import com.marti.humanresbackend.models.views.UpdateWorkLeaveView;
import com.marti.humanresbackend.models.views.WorkLeaveView;
import com.marti.humanresbackend.services.WorkLeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public WorkLeave createLeave(@RequestBody WorkLeaveView wv) {
        return workLeaveService.createLeave(new WorkLeave(wv));
    }

    @PutMapping(path = "changeStatus")
    public void changeStatus(@RequestParam Long workleaveId, Status status) {
        workLeaveService.updateStatus(workleaveId, status);
    }

    @PutMapping(path = "cancelWorkleave")
    public void cancelWorkLeave(@RequestParam Long workleaveId, Status status) {
        workLeaveService.cancelWorkleave(workleaveId, status);
    }

    @GetMapping(path = "all")
    public List<WorkLeave> getAll(@RequestParam(required = false) Long userId) {
        return workLeaveService.getAll(userId);
    }

    @GetMapping(path = "allSimplified")
    public List<WorkLeaveDTO> getAllSimplified(
            @RequestParam(required = false) Long userId,
            @DateTimeFormat(pattern = "M/d/yyyy") @RequestParam(required = false) LocalDate after,
            @DateTimeFormat(pattern = "M/d/yyyy") @RequestParam(required = false) LocalDate before) {
        return workLeaveService.getAllSimplified(userId, after, before);
    }

    @GetMapping(path = "byUser")
    public List<WorkLeave> getAllByUser(@RequestParam Long userId) {
        return workLeaveService.getAllByUser(userId);
    }

    @GetMapping(path = "byUserSimplified")
    public List<WorkLeaveDTO> getAllByUserSimplified(@RequestParam Long userId) {
        return workLeaveService.getAllByUserSimplified(userId);
    }

    @GetMapping(path = "byUserAndAdminStatSimplified")
    public List<WorkLeaveDTO> getAllByUserAndAdminStatSimplified(@RequestParam Long userId, Status status) {
        return workLeaveService.getAllByUserAndAdminStatSimplified(userId, status);
    }

    @GetMapping(path = "pendingWithoutManager")
    public List<WorkLeaveDTO> pendingWithoutManager() {
        return workLeaveService.getAllPendingWithoutManager();
    }

    @GetMapping(path = "byUserAndMStatSimplified")
    public List<WorkLeaveDTO> getAllByUserAndMStatSimplified(@RequestParam Long userId, Status status) {
        return workLeaveService.getAllByUserAndMStatSimplified(userId, status);
    }

    @GetMapping(path = "byAdminStatSimplified")
    public List<WorkLeaveDTO> getAllByAdminStatSimplified(@RequestParam Status status) {
        return workLeaveService.getAllByAdminStatSimplified(status);
    }

    @PutMapping(path = "update")
    public void updateWorkLeave(@RequestBody UpdateWorkLeaveView uwv) {
        workLeaveService.updateWorkLeave(uwv);
    }

    @GetMapping(path = "edit")
    public UpdateWorkLeaveView getById(@RequestParam Long Id) {
        return workLeaveService.getUpdateWorkLeaveView(Id);
    }

    //angel
//    @GetMapping(path = "calendar")
//    public CalendarData getCalendarData(@RequestParam() int year){
//        return workLeaveService.getCalendarData(year);
//    }
    //marti
    @GetMapping(path = "calendar")
    public List<CalendarDTO> getCalendarData(@RequestParam() int year, @RequestParam() int month) {
        return workLeaveService.getCalendarDTO(year, month);
    }

    @GetMapping(path = "calendar/year")
    public List<CalendarYearDTO> getCalendarYear(@RequestParam() int year) {
        return workLeaveService.getCalendarYearDTO(year);
    }

    @Scheduled(cron = "0 0 9 1 1 *")
    @PostMapping("yearlyDaysUpdate")
    public void yearlyUpdate() {
        int year = LocalDate.now().getYear();
        this.workLeaveService.yearlyDBUpdate(year);
    }
}
