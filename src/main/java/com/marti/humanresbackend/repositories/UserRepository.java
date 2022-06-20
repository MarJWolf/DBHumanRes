package com.marti.humanresbackend.repositories;

import com.marti.humanresbackend.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByJobTitleIsNotNull();

    Optional<User> findByEmail(String email);

    List<User> findByManagerId(Long id);
}
