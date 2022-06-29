package com.marti.humanresbackend.models.views;

import com.marti.humanresbackend.models.enums.Role;

public record UpdateUserView(Long id,String email, String pass, String fullName, Long jobTitle, Long workplace, int contractPaidDays, int thisYearPaidDays, int lastYearPaidDays, Role role, Long managerId) {
}
