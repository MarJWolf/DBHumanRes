package com.marti.humanresbackend.repositories;

import com.marti.humanresbackend.models.entities.Workplace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkplaceRepository extends JpaRepository<Workplace, Long> {
    @Query("""
        select w.workplace
        from Workplace w
        join User u on u.workplaceId = w.id
        where u.id = ?1
""")
    String findByUserId(Long id);
}
