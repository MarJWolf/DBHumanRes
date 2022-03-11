package com.marti.humanresbackend.repositories;

import com.marti.humanresbackend.models.entities.WorkLeave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkLeaveRepository extends JpaRepository<WorkLeave, Long> {

    List<WorkLeave> findAllByUserIdEquals(Long id);

}
