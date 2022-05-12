package com.marti.humanresbackend.models.DTO;

import com.marti.humanresbackend.models.entities.WorkLeave;
import com.marti.humanresbackend.models.enums.Status;
import com.marti.humanresbackend.models.enums.Type;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class WorkLeaveDTO {
    private Long Id;
    private Long userId;
    private Type type;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate fillDate;
    private Status statusManager;
    private Status statusHr;

    public WorkLeaveDTO(WorkLeave wl){
        this.Id = wl.getId();
        this.userId = wl.getUserId();
        this.type = wl.getType();
        this.startDate = wl.getStartDate();
        this.endDate = wl.getEndDate();
        this.fillDate = wl.getFillDate();
        this.statusManager = wl.getStatusManager();
        this.statusHr = wl.getStatusHr();
    }
}
