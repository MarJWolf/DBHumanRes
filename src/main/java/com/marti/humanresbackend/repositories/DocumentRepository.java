package com.marti.humanresbackend.repositories;

import com.marti.humanresbackend.models.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Transactional
    Document findByWorkleaveId(Long workleaveId);

    boolean existsByWorkleaveId(Long workleaveId);
}
