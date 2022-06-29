package com.marti.humanresbackend.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Table(name = "job_title")
@Getter
@Setter
@Entity
@NoArgsConstructor
public class JobTitle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany()
    @JoinColumn(name="job_title_id")
    private List<User> users;

    private String jobTitle;


    public JobTitle(String name){jobTitle = name;}
}
