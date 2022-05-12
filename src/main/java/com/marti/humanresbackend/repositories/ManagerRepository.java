package com.marti.humanresbackend.repositories;

import com.marti.humanresbackend.models.entities.Manager;
import com.marti.humanresbackend.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {

    Manager findManagerByUserManager(User u);
}
