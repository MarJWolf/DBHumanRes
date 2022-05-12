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

    @OneToOne(cascade = CascadeType.DETACH)
    private User userManager;

    @OneToMany(cascade = CascadeType.DETACH)
    @JoinColumn(name="manager_id")
    private List<User> allWorkers = new ArrayList<>();

    public Manager(User u){this.userManager = u;}
}