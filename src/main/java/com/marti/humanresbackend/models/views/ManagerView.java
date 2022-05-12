package com.marti.humanresbackend.models.views;

import java.util.List;

public record ManagerView(Long id, List<Long> allUsers) {
}
