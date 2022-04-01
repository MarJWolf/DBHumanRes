package com.marti.humanresbackend.models.views;

import com.marti.humanresbackend.models.enums.Status;
import com.marti.humanresbackend.models.enums.Type;

import java.time.LocalDate;

public record WorkLeaveView(Long userId, Type type, LocalDate startDate, LocalDate endDate, LocalDate fillDate, Status statusManager, Status statusHr) {
}
