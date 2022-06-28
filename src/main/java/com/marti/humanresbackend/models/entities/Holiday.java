package com.marti.humanresbackend.models.entities;

import com.marti.humanresbackend.models.views.APIHolidayView;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Table(name = "holidays")
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Holiday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate holiday;
    private String name;

    public Holiday(LocalDate holiday, String name) {
        this.holiday = holiday;
        this.name = name;
    }

    public Holiday(APIHolidayView holiday) {
        this.holiday = holiday.date();
        this.name = holiday.localName();
    }


}
