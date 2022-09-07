package com.marti.humanresbackend.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Table(name = "taken_days")
@Getter
@Setter
@Entity
@NoArgsConstructor
public class TakenDays {
    @Id
    @GeneratedValue
    private Long id;

    private Long daysId;
    private Long workleaveId;
    private Integer takenDays;
    private LocalDateTime timestamp;

    public TakenDays(Long daysId, Long workleaveId, Integer takenDays, LocalDateTime timestamp) {
        this.daysId = daysId;
        this.workleaveId = workleaveId;
        this.takenDays = takenDays;
        this.timestamp = timestamp;
    }
}
