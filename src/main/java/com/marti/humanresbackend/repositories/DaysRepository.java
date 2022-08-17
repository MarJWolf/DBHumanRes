package com.marti.humanresbackend.repositories;

import com.marti.humanresbackend.models.entities.Days;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DaysRepository extends JpaRepository<Days, Long> {
    List<Days> getDaysByUserDaysId(Long userID);
}
