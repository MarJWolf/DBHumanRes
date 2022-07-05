package com.marti.humanresbackend.models.views;

import com.marti.humanresbackend.models.enums.Role;

public record UserView(String email, String pass, String fullName, Long jobTitleId, Long workplaceId, int contractPaidDays, int thisYearPaidDays, int lastYearPaidDays, Role role, Long managerId) {
}
