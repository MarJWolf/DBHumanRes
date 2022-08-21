package com.marti.humanresbackend.models.entities;

import com.marti.humanresbackend.models.enums.Status;
import com.marti.humanresbackend.models.enums.Type;
import com.marti.humanresbackend.models.views.UpdateWorkLeaveView;
import com.marti.humanresbackend.models.views.WorkLeaveView;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Table(name = "workleave")
@Getter
@Setter
@Entity
@NoArgsConstructor
public class WorkLeave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Type type;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate fillDate;
    private Status statusManager;
    private Status statusAdmin;

    public WorkLeave(Type type, LocalDate startDate,LocalDate fillDate, LocalDate endDate, Status statusManager, Status statusAdmin) {
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.fillDate = fillDate;
        this.statusManager = statusManager;
        this.statusAdmin = statusAdmin;
    }

    public WorkLeave(WorkLeaveView wv) {
        this.userId = wv.userId();
        this.type = wv.type();
        this.startDate = wv.startDate();
        this.endDate = wv.endDate();
        this.fillDate = wv.fillDate();
        this.statusManager = wv.statusManager();
        this.statusAdmin = wv.statusAdmin();
    }

    public static WorkLeave updateWorkLeave(WorkLeave wl, UpdateWorkLeaveView uwlv){
        wl.setType(uwlv.type());
        wl.setStartDate(uwlv.startDate());
        wl.setEndDate(uwlv.endDate());
        wl.setFillDate(uwlv.fillDate());
        wl.setStatusAdmin(uwlv.statusAdmin());
        wl.setStatusManager(uwlv.statusManager());
        return wl;
    }

    //Method to get the combined status.
    public Status getStatus() {
        if(statusAdmin == Status.Confirmed && statusManager == Status.Confirmed)
            return Status.Confirmed;
        if(statusAdmin == Status.Denied && statusManager == Status.Denied)
            return Status.Denied;
        if(statusAdmin == Status.Cancelled || statusManager == Status.Cancelled)
            return Status.Cancelled;
        return Status.Pending;
    }
}
