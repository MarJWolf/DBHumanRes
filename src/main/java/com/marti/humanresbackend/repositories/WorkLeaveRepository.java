package com.marti.humanresbackend.repositories;

import com.marti.humanresbackend.models.DTO.calendar.CalendarWorkLeave;
import com.marti.humanresbackend.models.entities.WorkLeave;
import com.marti.humanresbackend.models.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkLeaveRepository extends JpaRepository<WorkLeave, Long> {

    List<WorkLeave> findAllByUserIdEqualsOrderByFillDateDesc(Long id);

    List<WorkLeave> findAllByUserIdEqualsAndStatusAdminOrderByFillDateDesc(Long id, Status status);

    List<WorkLeave> findAllByUserIdEqualsAndStatusManagerOrderByFillDateDesc(Long id, Status status);

    @Query("""
    From WorkLeave w
    Join User u on w.userId = u.id
    where w.statusAdmin = ?1 and u.managerId is not null
    ORDER BY w.fillDate desc
    """)
    List<WorkLeave> findByStatusAdminEqualsAndNoManager(Status status);

    @Query("""
    select new com.marti.humanresbackend.models.DTO.calendar.CalendarWorkLeave(w)
    From WorkLeave w
    where w.userId = ?1
    ORDER BY w.startDate asc
    """)
    List<CalendarWorkLeave> getAllWorkLeaves(Long id);
    @Query("""
    select new com.marti.humanresbackend.models.DTO.calendar.CalendarWorkLeave(w)
    From WorkLeave w
    where w.userId = ?1
    and (w.startDate between ?2 and ?3 or w.endDate between ?2 and ?3)
    ORDER BY w.startDate asc
    """)
    List<CalendarWorkLeave> getAllWorkLeaves(Long id, LocalDate start, LocalDate end);

    @Query("""
       from WorkLeave w
       where w.userId = ?1
       and w.statusAdmin = 1
       and w.statusManager = 1
       and (w.startDate between ?2 and ?3 or w.endDate between ?2 and ?3)
    """)
    List<WorkLeave> getConfirmedWorkleaves(Long id, LocalDate startDate, LocalDate endDate);
}
