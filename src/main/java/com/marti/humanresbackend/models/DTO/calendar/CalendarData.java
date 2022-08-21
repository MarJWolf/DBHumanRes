package com.marti.humanresbackend.models.DTO.calendar;

import com.marti.humanresbackend.models.entities.Holiday;

import java.util.List;

public record CalendarData(List<CalendarUser> users, List<Holiday> holidays) {


}

