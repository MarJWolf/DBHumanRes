package com.marti.humanresbackend.repositories;

import com.marti.humanresbackend.models.entities.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    boolean existsByHoliday(LocalDate localDate);

}
