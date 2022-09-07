package com.marti.humanresbackend.repositories;

import com.marti.humanresbackend.models.entities.Days;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DaysRepository extends JpaRepository<Days, Long> {
    @Query("""
        select d
        from Days d
        where d.userDaysId = ?1
                    order by d.year desc
            """)
    List<Days> getDaysByUserId(Long userID);

    @Query("""
                    select sum(d.days)
                    from Days d
                    where d.userDaysId = ?1
                    and d.use = true
            """)
    Integer getUsableDaysByUserId(Long userID);
}
