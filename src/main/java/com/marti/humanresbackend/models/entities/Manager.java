package com.marti.humanresbackend.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Table(name = "manager")
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Manager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User manager;

    @OneToMany()
    @JoinColumn(name="manager_id")
    private List<User> allWorkers = new ArrayList<>();
}
