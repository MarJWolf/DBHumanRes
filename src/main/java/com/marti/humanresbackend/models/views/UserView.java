package com.marti.humanresbackend.models.views;

import com.marti.humanresbackend.models.enums.Role;

public record UserView(String email, String pass, String fullName, String jobTitle, String workplace, int paidDays, Role role, Long manager_id) {
}