package com.marti.humanresbackend.models.entities;

import com.marti.humanresbackend.models.enums.Role;
import com.marti.humanresbackend.models.views.UpdateUserView;
import com.marti.humanresbackend.models.views.UserView;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "user_table")
@Getter
@Setter
@Entity
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique=true, nullable = false)
    private String email;
    private String pass;

    private String fullName;
    private String jobTitle;
    private String workplace;

    private int paidDays;

    private Role role;

    private Long managerId;

    @OneToMany()
    @JoinColumn(name="userId")
    private List<WorkLeave> allWorkleaves = new ArrayList<>();

    public User(String email, String pass, String fullName, String jobTitle, String workplace, int paidDays, Role role, Long managerId) {
        this.email = email;
        this.pass = pass;
        this.fullName = fullName;
        this.jobTitle = jobTitle;
        this.workplace = workplace;
        this.paidDays = paidDays;
        this.role = role;
        this.managerId = managerId;
    }

    public User(UserView uv) {
        this.email = uv.email();
        this.pass = uv.pass();
        this.fullName = uv.fullName();
        this.jobTitle = uv.jobTitle();
        this.workplace = uv.workplace();
        this.paidDays = uv.paidDays();
        this.role = uv.role();
        this.managerId = uv.managerId();
    }

    public static User updateUser(User u, UpdateUserView uuv){
        u.setEmail(uuv.email());
        u.setPass(uuv.pass());
        u.setFullName(uuv.fullName());
        u.setJobTitle(uuv.jobTitle());
        u.setWorkplace(uuv.workplace());
        u.setPaidDays(uuv.paidDays());
        u.setRole(uuv.role());
        u.setManagerId(uuv.managerId());
        return u;
    }
}
