package com.marti.humanresbackend.models.views;

import com.marti.humanresbackend.models.entities.Days;
import com.marti.humanresbackend.models.enums.Role;

import java.util.List;

public record UpdateUserView(Long id,String email, String pass, String fullName, Long jobTitleId, Long workplaceId, int contractPaidDays, Role role, Long managerId, List<Days> days) {
}
