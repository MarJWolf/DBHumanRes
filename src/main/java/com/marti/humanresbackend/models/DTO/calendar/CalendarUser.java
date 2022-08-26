package com.marti.humanresbackend.models.DTO.calendar;

import com.marti.humanresbackend.models.DTO.UserDTO;

import java.util.List;

public record CalendarUser(UserDTO user, String workplace, List<CalendarWorkLeave> workLeaves) {

}
