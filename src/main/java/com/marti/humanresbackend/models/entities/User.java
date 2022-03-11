package com.marti.humanresbackend.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "user_table")
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String pass;

    private String fullName;
    private String jobTitle;

    private int paidDays;
    private int unpaidDays;

    public User(String email, String pass, String fullName, String jobTitle) {
        this.email = email;
        this.pass = pass;
        this.fullName = fullName;
        this.jobTitle = jobTitle;
    }

    //workplace
    //role (user/hr)

}
