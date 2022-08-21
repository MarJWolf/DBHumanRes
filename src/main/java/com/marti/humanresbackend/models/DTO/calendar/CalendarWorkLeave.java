package com.marti.humanresbackend.models.DTO.calendar;

import com.marti.humanresbackend.models.entities.WorkLeave;
import com.marti.humanresbackend.models.enums.Status;
import com.marti.humanresbackend.models.enums.Type;

import java.time.LocalDate;

public record CalendarWorkLeave(
        Type type,
        LocalDate startDate,
        LocalDate endDate,
        LocalDate fillDate,
        Status status) {
    public CalendarWorkLeave(WorkLeave workLeave){
        this(workLeave.getType(),workLeave.getStartDate(),workLeave.getEndDate(),workLeave.getFillDate(),workLeave.getStatus());
    }
}
