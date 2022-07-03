package com.marti.humanresbackend.repositories;

import com.marti.humanresbackend.models.entities.WorkLeave;
import com.marti.humanresbackend.models.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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

}
