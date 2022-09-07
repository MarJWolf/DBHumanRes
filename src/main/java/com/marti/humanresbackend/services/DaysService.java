package com.marti.humanresbackend.services;

import com.marti.humanresbackend.models.entities.Days;
import com.marti.humanresbackend.repositories.DaysRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DaysService {
    private final DaysRepository daysRep;

    public List<Days> allDays() {
        return daysRep.findAll();
    }

    public List<Days> allDaysByUser(Long userID) {
        return daysRep.getDaysByUserId(userID);
    }

    public Integer getUsableDaysByUser(Long userID) {
        return daysRep.getUsableDaysByUserId(userID);
    }

    public void createDays(Long userID, int days, int year, boolean use) {
        daysRep.save(new Days(userID, days, year, use));
    }

    public List<Days> createDays(Days days) {
        Days save = daysRep.save(days);
        return allDaysByUser(save.getUserDaysId());
    }

    public void updateDays(Long daysID, int days, int year, boolean use) {
        Days Days = daysRep.getById(daysID);
        Days.setDays(days);
        Days.setYear(year);
        Days.setUse(use);
        daysRep.save(Days);
    }

    public void deleteDays(Long Id) {
        daysRep.delete(daysRep.getById(Id));
    }

    public Days getDays(Long daysId) {
        return daysRep.getById(daysId);
    }
}

