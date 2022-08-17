package com.marti.humanresbackend.models.views;

import com.marti.humanresbackend.models.enums.Role;

public record UpdateUserView(Long id,String email, String pass, String fullName, Long jobTitleId, Long workplaceId, int contractPaidDays, Role role, Long managerId) {
}
