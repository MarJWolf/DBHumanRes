package com.marti.humanresbackend.models.DTO;


import com.marti.humanresbackend.models.DTO.calendar.CalendarDayStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class CalendarDTO {
    private String workplace;
    private String name;
    private int allLeaves;
    private Map<LocalDate, CalendarDayStatus> days;

    public CalendarDTO(String wp, String nm, Map<LocalDate, CalendarDayStatus> Ldays, int allLeaves) {
        this.workplace = wp;
        this.name = nm;
        this.days = Ldays;
        this.allLeaves = allLeaves;
    }

}
