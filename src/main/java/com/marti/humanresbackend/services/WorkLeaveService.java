package com.marti.humanresbackend.services;

import com.marti.humanresbackend.models.DTO.CalendarDTO;
import com.marti.humanresbackend.models.DTO.WorkLeaveDTO;
import com.marti.humanresbackend.models.DTO.calendar.CalendarDayStatus;
import com.marti.humanresbackend.models.DTO.calendar.CalendarYearDTO;
import com.marti.humanresbackend.models.entities.Days;
import com.marti.humanresbackend.models.entities.TakenDays;
import com.marti.humanresbackend.models.entities.User;
import com.marti.humanresbackend.models.entities.WorkLeave;
import com.marti.humanresbackend.models.enums.Status;
import com.marti.humanresbackend.models.enums.Type;
import com.marti.humanresbackend.models.views.UpdateWorkLeaveView;
import com.marti.humanresbackend.repositories.TakenDaysRepository;
import com.marti.humanresbackend.repositories.WorkLeaveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class WorkLeaveService {

    private final WorkLeaveRepository workRep;
    private final UserService userService;
    private final ManagerService manService;
    private final HolidayService holidayService;
    private final EmailService emailService;
    private final DaysService daysService;
    private final TakenDaysRepository takenDaysRepository;
    private Set<LocalDate> HOLIDAYS;


    @Autowired
    public WorkLeaveService(WorkLeaveRepository workRep, UserService userService, ManagerService manService, HolidayService holidayService, EmailService emailService, DaysService daysService, TakenDaysRepository takenDaysRepository) {
        this.workRep = workRep;
        this.userService = userService;
        this.manService = manService;
        this.holidayService = holidayService;
        this.emailService = emailService;
        this.daysService = daysService;
        this.takenDaysRepository = takenDaysRepository;
    }

    private void resetHolidays() {
        HOLIDAYS = new HashSet<>();
        holidayService.getAllHolidays().forEach(holiday -> HOLIDAYS.add(holiday.getHoliday()));
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
        WorkLeave save = workRep.save(w);
        log.info(String.format("Workleave %s has been created for user with Id = %s", save.getId(), save.getUserId()));
        emailService.sendEmailCreatedWorkleave(w);
        return save;
    }

    private int getTotalUserDaysCount(Long userId) {
        return daysService.getUsableDaysByUser(userId);
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

    private void takeDays(WorkLeave workLeave) {
        if (workLeave.getStatus() == Status.Confirmed && workLeave.getType() == Type.Paid) {
            int businessDays = getBusinessDays(workLeave);
            User u = userService.getUserById(workLeave.getUserId());
            if (businessDays > getTotalUserDaysCount(workLeave.getUserId())) {
                throw new RuntimeException("Потребителя няма достатъчно дни!");
            } else {
                List<Days> days = daysService.allDaysByUser(workLeave.getUserId());
                Collections.sort(days);
                for (Days day : days) {
                    if (day.isUse()) {
                        if (day.getDays() > businessDays) {
                            takeDays(day, businessDays, workLeave);
                            break;
                        } else {
                            businessDays = businessDays - day.getDays();
                            takeDays(day, day.getDays(), workLeave);
                        }
                    }
                }
                for (Days day : days) {
                    daysService.updateDays(day.getId(), day.getDays(), day.getYear(), day.isUse());
                }
                log.info(u.getFullName() + "'s days have been updated");
            }
            userService.updateUser(u);
        }
    }

    private void takeDays(Days day, int businessDays, WorkLeave workLeave) {
        day.setDays(day.getDays() - businessDays);
        if (day.getDays() <= 0)
            day.setUse(false);
        TakenDays takenDays = new TakenDays(day.getId(), workLeave.getId(), businessDays, LocalDateTime.now());
        takenDaysRepository.save(takenDays);
    }

    public List<WorkLeave> getAll(Long userId) {
        WorkLeave example = new WorkLeave();
        example.setUserId(userId);
        return workRep.findAll(Example.of(example));
    }

    public List<WorkLeaveDTO> getAllSimplified(Long userId, LocalDate after, LocalDate before) {
        return workRep.findAllByUserIdAndPeriod(userId, after, before);
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
        updateStatus(w.getId(), w.getStatus());
        workRep.save(w);
        log.info("Workleave with id '" + w.getId() + "' has been updated");
    }

    public void updateWorkLeave(UpdateWorkLeaveView uwlv) {
        updateWorkLeave(WorkLeave.updateWorkLeave(getById(uwlv.id()), uwlv));
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
        if (status == Status.Cancelled && workLeave.getStatus() != Status.Cancelled) {
            workLeave.setStatusAdmin(Status.Cancelled);
            workLeave.setStatusManager(Status.Cancelled);
            emailService.sendEmailCancelledWorkleave(workLeave);
        }
        updateWorkLeave(workLeave);
    }

    public void updateStatus(Long WorkleaveId, Status status) {
        WorkLeave workLeave = getById(WorkleaveId);
        Status initialStatus = workLeave.getStatus();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("Manager"))) {
            workLeave.setStatusManager(status);
        } else if (auth.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("Admin"))) {
            workLeave.setStatusAdmin(status);
            if (userService.getUserById(workLeave.getUserId()).getManagerId() == null) {
                workLeave.setStatusManager(status);
            }
        }
        if (workLeave.getStatus() != Status.Pending) {
            emailService.sendEmailUpdatedWorkleave(workLeave);
            takeDays(workLeave);
        }
        if (initialStatus == Status.Confirmed && workLeave.getStatus() != Status.Confirmed) {
//            todo: send email that days have been returned?
            returnDays(workLeave);
        }
        updateWorkLeave(workLeave);
    }

    private void returnDays(WorkLeave workLeave) {
        List<TakenDays> takenDaysList = takenDaysRepository.findByWorkleaveId(workLeave.getId());
        for (TakenDays takenDays : takenDaysList) {
            Days days = daysService.getDays(takenDays.getDaysId());
            days.setDays(takenDays.getTakenDays());
            if (days.getDays() > 0 && takenDays.getTimestamp().isAfter(LocalDateTime.now().minusYears(2)))
                days.setUse(true);
            takenDaysRepository.deleteById(takenDays.getId());
        }
    }

    public void saveCalDays(WorkLeave w, CalendarDayStatus status, Map<LocalDate, CalendarDayStatus> days, int month) {
        LocalDate d = w.getStartDate();
        while (!d.isAfter(w.getEndDate())) {
            if (d.getMonth().getValue() == month) {
                days.putIfAbsent(d, status);
            }
            d = d.plusDays(1);
        }
    }

    public List<CalendarDTO> getCalendarDTO(List<User> users, LocalDate startDate, LocalDate endDate) {
        List<CalendarDTO> calendarInfo = new ArrayList<>();
        Map<LocalDate, CalendarDayStatus> monthDays = fillPeriodWithNonWorkingDays(startDate, endDate);

        for (User user : users) {
            List<WorkLeave> wls = workRep.getConfirmedWorkleaves(user.getId(), startDate, endDate);
            Map<LocalDate, CalendarDayStatus> days = new HashMap<>(monthDays);
            for (WorkLeave wl : wls) {
                if (wl.getType() == Type.Paid) {
                    saveCalDays(wl, CalendarDayStatus.Paid, days, startDate.getMonthValue());
                } else if (wl.getType() == Type.Unpaid) {
                    saveCalDays(wl, CalendarDayStatus.Unpaid, days, startDate.getMonthValue());
                } else {
                    saveCalDays(wl, CalendarDayStatus.Special, days, startDate.getMonthValue());
                }
            }
            CalendarDTO calendarDTO = new CalendarDTO(userService.getWorkplaceByUserId(user.getId()), user.getFullName(), days, days.size() - monthDays.size());
            fillEmptyDaysInMonth(startDate, calendarDTO.getDays());
            calendarInfo.add(calendarDTO);
        }
        return calendarInfo;
    }

    private void fillEmptyDaysInMonth(LocalDate startDate, Map<LocalDate, CalendarDayStatus> days) {
        LocalDate localDate = startDate;
        while (localDate.getMonthValue() == startDate.getMonthValue()) {
            days.putIfAbsent(localDate, CalendarDayStatus.none);
            localDate = localDate.plusDays(1L);
        }
    }

    private Map<LocalDate, CalendarDayStatus> fillPeriodWithNonWorkingDays(LocalDate startDate, LocalDate endDate) {
        resetHolidays();

        Map<LocalDate, CalendarDayStatus> result = new HashMap<>();
        LocalDate localDate = startDate;
        while (!localDate.isAfter(endDate)) {
            DayOfWeek dw = localDate.getDayOfWeek();
            if (HOLIDAYS.contains(localDate))
                result.put(localDate, CalendarDayStatus.Holiday);
            if (dw == DayOfWeek.SATURDAY || dw == DayOfWeek.SUNDAY)
                result.putIfAbsent(localDate, CalendarDayStatus.NonWorkingDay);
            localDate = localDate.plusDays(1L);
        }

        return result;
    }

    public List<CalendarDTO> getCalendarDTO(int year, int month) {
        List<User> users = userService.getAll();
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = LocalDate.of(year, month, startDate.lengthOfMonth());
        return getCalendarDTO(users, startDate, endDate);
    }

    public List<CalendarYearDTO> getCalendarYearDTO(int year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);

        List<User> users = userService.getAll();
        Map<LocalDate, CalendarDayStatus> freeDays = fillPeriodWithNonWorkingDays(startDate, endDate);
        List<CalendarYearDTO> result = new ArrayList<>();
        for (User user : users) {
            List<WorkLeave> workleaves = workRep.getConfirmedWorkleaves(user.getId(), startDate, endDate);
            Map<Integer, Integer> yearlyWorkleaves = new HashMap<>();
            for (int i = 1; i <= 12; i++) {
                int workleavesByMonth = countWorkleavesInMonth(workleaves, freeDays, LocalDate.of(year, i, 1));
                yearlyWorkleaves.put(i, workleavesByMonth);
            }
            result.add(new CalendarYearDTO(userService.getWorkplaceByUserId(user.getId()), user.getFullName(), yearlyWorkleaves, getTotalUserDaysCount(user.getId())));
        }
        return result;
    }

    private int countWorkleavesInMonth(List<WorkLeave> workleaves, Map<LocalDate, CalendarDayStatus> freeDays, LocalDate of) {
        int count = 0;
        for (WorkLeave workLeave : workleaves) {
            if (workLeave.getType() == Type.Paid) {
                LocalDate date = workLeave.getStartDate();
                while (!date.isAfter(workLeave.getEndDate()) && date.getMonth() == of.getMonth()) {
                    if (!freeDays.containsKey(date))
                        count++;
                    date = date.plusDays(1L);

                }
            }
        }
        return count;
    }

    @Scheduled(cron = "0 0 9 * * *")
    public void unconfirmedWorkleaveEmailReminder() {
        log.info("Daily unconfirmed workleave reminder schedule has been activated");
        List<WorkLeave> allPendingWorkleaves = workRep.findAllPendingOnDate(LocalDate.now().plusDays(3L));
        int sentEmailsCount = 0;
        for (WorkLeave workLeave : allPendingWorkleaves) {
            if (workLeave.getStatusAdmin().equals(Status.Pending)) {
                List<User> admins = userService.getAdmins();
                emailService.sendEmailUpcomingWorkleave(workLeave, admins);
                sentEmailsCount++;
            }
            if (workLeave.getStatusAdmin().equals(Status.Pending)) {
                User user = userService.getUserById(workLeave.getUserId());
                User manager = userService.getUserById(user.getManagerId());

                emailService.sendEmailUpcomingWorkleave(workLeave, List.of(manager));
                sentEmailsCount++;
            }
        }
        log.info("Daily unconfirmed workleave reminder schedule has completed its job. Sent emails: " + sentEmailsCount);
    }

    //    Pulls the new year's holidays and updates every user's paid days.
    public void yearlyDBUpdate(int newYear) {
        holidayService.getAllAPIHolidays(newYear);
        userService.yearlyDaysUpdate(newYear);
    }
}
