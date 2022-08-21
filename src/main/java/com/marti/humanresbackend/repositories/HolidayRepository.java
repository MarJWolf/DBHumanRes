package com.marti.humanresbackend.repositories;

import com.marti.humanresbackend.models.entities.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    boolean existsByHoliday(LocalDate localDate);

    @Query("""
    from Holiday h
    where h.holiday between ?1 and ?2
    order by h.holiday asc
""")
    List<Holiday> findAllBetween(LocalDate startDate, LocalDate endDate);
}
