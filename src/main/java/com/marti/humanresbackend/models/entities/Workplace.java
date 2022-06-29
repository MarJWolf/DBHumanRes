package com.marti.humanresbackend.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Table(name = "workplace")
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Workplace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany()
    @JoinColumn(name="workplace_id")
    private List<User> users;

    private String worklplace;

    public Workplace(String name){worklplace = name;}
}
