package com.marti.humanresbackend.repositories;

import com.marti.humanresbackend.models.entities.TakenDays;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TakenDaysRepository extends JpaRepository<TakenDays, Long> {
    @Query("""
                    select days
                    from TakenDays days
                    where days.workleaveId = ?1
            """)
    List<TakenDays> findByWorkleaveId(Long workleaveId);
}
