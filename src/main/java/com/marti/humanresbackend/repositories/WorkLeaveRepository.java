package com.marti.humanresbackend.repositories;

import com.marti.humanresbackend.models.DTO.WorkLeaveDTO;
import com.marti.humanresbackend.models.DTO.calendar.CalendarWorkLeave;
import com.marti.humanresbackend.models.entities.WorkLeave;
import com.marti.humanresbackend.models.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("""
                    select new com.marti.humanresbackend.models.DTO.WorkLeaveDTO(w, u.fullName)
                    from WorkLeave w
                    join User u on u.id = w.userId
                    where (:userId is null or w.userId = :userId)
                    and  (cast(:start as date) is null or w.startDate >= :start or w.endDate >= :start)
                    and  (cast(:end as date) is null or w.startDate <= :end or w.endDate <= :end)
                    order by w.startDate desc
            """)
    List<WorkLeaveDTO> findAllByUserIdAndPeriod(@Param("userId") Long userId, @Param("start") LocalDate startDate, @Param("end") LocalDate endDate);

    @Query("""
                    select w
                    from WorkLeave w
                    where (w.statusManager = 1 or w.statusAdmin = 1)
                    and  w.startDate <= ?1
                    and w.startDate > current_date
            """)
    List<WorkLeave> findAllPendingOnDate(LocalDate date);
}
