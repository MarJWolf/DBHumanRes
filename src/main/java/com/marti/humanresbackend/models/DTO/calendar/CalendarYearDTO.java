package com.marti.humanresbackend.models.DTO.calendar;

import java.util.Map;

public record CalendarYearDTO(String workplace, String name, Map<Integer, Integer> months, Integer daysLeft) {
}
