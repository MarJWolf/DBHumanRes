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
    @Column(unique = true, nullable = false)
    private String email;
    private String pass;
    private String fullName;

    @Column(name = "job_title_id")
    private Long jobTitleId;

    @Column(name = "workplace_id")
    private Long workplaceId;
    //to remove
    private int contractPaidDays;

    private Role role;

    private Long managerId;

    @OneToMany()
    @JoinColumn(name = "userId")
    private List<WorkLeave> allWorkleaves = new ArrayList<>();

    @OneToMany()
    @JoinColumn(name = "userDaysId")
    private List<Days> allDays = new ArrayList<>();

    public User(String email, String pass, String fullName, Long jobTitle, Long workplaceId, int contractPaidDays, Role role, Long managerId) {
        this.email = email;
        this.pass = pass;
        this.fullName = fullName;
        this.jobTitleId = jobTitle;
        this.workplaceId = workplaceId;
        this.contractPaidDays = contractPaidDays;
        this.role = role;
        this.managerId = managerId;
    }

    public User(UserView uv) {
        this.email = uv.email();
        this.pass = uv.pass();
        this.fullName = uv.fullName();
        this.jobTitleId = uv.jobTitleId();
        this.workplaceId = uv.workplaceId();
        this.contractPaidDays = uv.contractPaidDays();
        this.role = uv.role();
        this.managerId = uv.managerId();
    }

    public static User updateUser(User u, UpdateUserView uuv) {
        u.setEmail(uuv.email());
        u.setPass(uuv.pass());
        u.setFullName(uuv.fullName());
        u.setJobTitleId(uuv.jobTitleId());
        u.setWorkplaceId(uuv.workplaceId());
        u.setContractPaidDays(uuv.contractPaidDays());
        u.setRole(uuv.role());
        u.setManagerId(uuv.managerId());
        return u;
    }

    public void updateDays(int year) {
        List<Days> list = new ArrayList<>(this.allDays);
        this.allDays.clear();
        for (Days days : list) {
            if (days.getYear() + 2 <= year)
                days.setUse(false);
            this.allDays.add(days);
        }
    }
}
