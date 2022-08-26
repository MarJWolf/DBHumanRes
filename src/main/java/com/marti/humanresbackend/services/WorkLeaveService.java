package com.marti.humanresbackend.services;

import com.marti.humanresbackend.models.DTO.calendar.CalendarData;
import com.marti.humanresbackend.models.DTO.calendar.CalendarUser;
import com.marti.humanresbackend.models.DTO.calendar.CalendarWorkLeave;
import com.marti.humanresbackend.models.DTO.UserDTO;
import com.marti.humanresbackend.models.DTO.WorkLeaveDTO;
import com.marti.humanresbackend.models.DTO.calendar.YearlyWorkleave;
import com.marti.humanresbackend.models.entities.Days;
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

    private void resetHolidays() {
        HOLIDAYS = new HashSet<>();
        holidayService.getAllHolidays().forEach(holiday -> {
            HOLIDAYS.add(holiday.getHoliday());
        });
    }

    private List<WorkLeaveDTO> createWLDTO(List<WorkLeave> leaves) {
        List<WorkLeaveDTO> leavesFinal = new ArrayList<>();
        for (WorkLeave leave : leaves) {
            leavesFinal.add(new WorkLeaveDTO(leave, userService.getUserById(leave.getUserId()).getFullName()));
        }
        return leavesFinal;
    }

    //TODO: tezi tri funkcii otgovarqt za dnite i da se nameri pravilno mqsto za vzimane na dnite
    public WorkLeave createLeave(WorkLeave w) {
        if (w.getType() == Type.Paid) {
            int totalDaysCount = getTotalUserDaysCount(w.getUserId());
            if (getBusinessDays(w) > totalDaysCount) {
                throw new RuntimeException("Нямате достатъчно дни!");
            }
        }
        return workRep.save(w);
    }

    private int getTotalUserDaysCount(Long userId) {
        List<Days> days = userService.allDaysByUser(userId);
        int totalDaysCount = 0;
        for (Days day : days) {
            if (day.isUse())
                totalDaysCount += day.getDays();
        }
        return totalDaysCount;
    }

    public int getBusinessDays(WorkLeave w) {
        resetHolidays();
        int businessDays = 0;
        LocalDate d = w.getStartDate();
        while (!d.isAfter(w.getEndDate())) {
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

    //TODO: da smetna i izwadq dnite ot tablicata, da namerq mqsto
    private void takeDays(WorkLeave workLeave) {
        if (workLeave.getStatusAdmin() == Status.Confirmed && workLeave.getStatusManager() == Status.Confirmed && workLeave.getType() == Type.Paid) {
            int businessDays = getBusinessDays(workLeave);
            User u = userService.getUserById(workLeave.getUserId());
            if (businessDays > getTotalUserDaysCount(workLeave.getUserId())) {
                throw new RuntimeException("Потребителя няма достатъчно дни!");
            } else {
                List<Days> days = userService.allDaysByUser(workLeave.getUserId());
                Collections.sort(days);
                for (Days day : days) {
                    if (day.isUse()) {
                        if (day.getDays() > businessDays) {
                            day.setDays(day.getDays() - businessDays);
                            break;
                        } else {
                            businessDays = businessDays - day.getDays();
                            day.setDays(0);
                            day.setUse(false);
                        }
                    }
                }
                for (Days day : days) {
                    userService.updateDays(day.getId(), day.getDays(), day.getYear(), day.isUse());
                }
            }
            userService.updateUser(u);
        }
    }

    public List<WorkLeave> getAll() {
        return workRep.findAll();
    }

    public List<WorkLeaveDTO> getAllSimplified() {
        List<WorkLeave> leaves = workRep.findAll();
        return createWLDTO(leaves);
    }

    public List<WorkLeave> getAllByUser(Long id) {
        return workRep.findAllByUserIdEqualsOrderByFillDateDesc(id);
    }

    public List<WorkLeaveDTO> getAllByUserSimplified(Long userId) {
        List<WorkLeave> leaves = workRep.findAllByUserIdEqualsOrderByFillDateDesc(userId);
        return createWLDTO(leaves);
    }

    public List<WorkLeaveDTO> getAllByUserAndAdminStatSimplified(Long userId, Status stat) {
        List<WorkLeave> leaves = workRep.findAllByUserIdEqualsAndStatusAdminOrderByFillDateDesc(userId, stat);
        return createWLDTO(leaves);
    }

    public List<WorkLeaveDTO> getAllByUserAndMStatSimplified(Long userId, Status stat) {
        List<User> subUsers = manService.getManagerByUserManager(userId).getAllWorkers();
        List<WorkLeave> leaves = new ArrayList<>();
        for (User subUser : subUsers) {
            leaves.addAll(workRep.findAllByUserIdEqualsAndStatusManagerOrderByFillDateDesc(subUser.getId(), stat));
        }
        return createWLDTO(leaves);
    }

    public List<WorkLeaveDTO> getAllPendingWithoutManager() {
        List<User> noSubUsers = userService.getAllByManager(null);
        List<WorkLeave> leaves = new ArrayList<>();
        for (User subUser : noSubUsers) {
            leaves.addAll(workRep.findAllByUserIdEqualsAndStatusManagerOrderByFillDateDesc(subUser.getId(), Status.Pending));
        }
        return createWLDTO(leaves);
    }

    public List<WorkLeaveDTO> getAllByAdminStatSimplified(Status stat) {
        List<WorkLeave> leaves = workRep.findByStatusAdminEqualsAndNoManager(stat);
        return createWLDTO(leaves);
    }

    public void updateWorkLeave(WorkLeave w) {
        workRep.save(w);
    }

    public void updateWorkLeave(UpdateWorkLeaveView uwlv) {
        workRep.save(WorkLeave.updateWorkLeave(getById(uwlv.id()), uwlv));
    }

    public WorkLeave getById(Long Id) {
        if (workRep.findById(Id).isEmpty())
            throw new RuntimeException("A Leave with such id does not exist!");
        else
            return workRep.findById(Id).get();
    }

    public UpdateWorkLeaveView getUpdateWorkLeaveView(Long Id) {
        WorkLeave u = getById(Id);
        return new UpdateWorkLeaveView(u.getId(), u.getUserId(), u.getType(), u.getStartDate(), u.getEndDate(), u.getFillDate(), u.getStatusManager(), u.getStatusAdmin());
    }

    public void cancelWorkleave(Long WorkleaveId, Status status) {
        WorkLeave workLeave = getById(WorkleaveId);
        if (status == Status.Cancelled) {
            workLeave.setStatusAdmin(Status.Cancelled);
            workLeave.setStatusManager(Status.Cancelled);
        }
        updateWorkLeave(workLeave);
    }

    public void updateStatus(Long WorkleaveId, Status status) {
        WorkLeave workLeave = getById(WorkleaveId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("Manager"))) {
            workLeave.setStatusManager(status);
        } else if (auth.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("Admin"))) {
            workLeave.setStatusAdmin(status);
            if (userService.getUserById(workLeave.getUserId()).getManagerId() == null) {
                workLeave.setStatusManager(status);
            }
        }
        takeDays(workLeave);
        updateWorkLeave(workLeave);
    }

    public CalendarData getCalendarData(int year) {
        List<User> users = userService.getAll();
        List<CalendarUser> calendarUsers = new ArrayList<>();
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        for (User user : users) {
            List<CalendarWorkLeave> allWorkLeaves;
            allWorkLeaves = workRep.getConfirmedWorkleaves(user.getId(), startDate, endDate).stream().map(CalendarWorkLeave::new).toList();
            calendarUsers.add(new CalendarUser(new UserDTO(user), userService.getWorkplaceByUserId(user.getId()), allWorkLeaves));
        }
        return new CalendarData(calendarUsers, holidayService.getAllHolidays(startDate, endDate));
    }

    private YearlyWorkleave getYearlyWorkleave(User user, LocalDate startDate, LocalDate endDate) {
        List<Days> days = userService.getUsableDaysByUser(user.getId());
        Days lastYear = days.remove(0);
        int sum = 0;
        for (Days day : days) {
            sum += day.getDays();
        }
        List<WorkLeave> workleaves = workRep.getConfirmedWorkleaves(user.getId(), startDate, endDate);
        for (WorkLeave workleave : workleaves) {
            int businessDays = getBusinessDays(workleave);
//            Todo: Get Workleave days divided by months
        }
        return new YearlyWorkleave(sum, user.getContractPaidDays(), Map.of(), lastYear.getDays());
    }
}
