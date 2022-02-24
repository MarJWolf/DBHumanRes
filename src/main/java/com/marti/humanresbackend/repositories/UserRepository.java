package com.marti.humanresbackend.repositories;

import com.marti.humanresbackend.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByJobTitleIsNotNull();
}
