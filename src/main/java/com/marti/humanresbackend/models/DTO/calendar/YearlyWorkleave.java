package com.marti.humanresbackend.models.DTO.calendar;

import java.util.Map;

public record YearlyWorkleave( Integer pastYearsWorkleaves, Integer workleavesByContract, Map<String,Integer> monthlyPaidLeaves, Integer remaining) {
}
