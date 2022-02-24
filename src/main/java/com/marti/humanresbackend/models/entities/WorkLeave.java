package com.marti.humanresbackend.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Table(name = "workleave")
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class WorkLeave {
    @Id
    private Long id;
    private int type;
    private Date startDate;
    private Date endDate;
    private boolean status;

    // TODO: 2/24/2022 : tipa na otpuska da se obraboti i preceni kakvo suvpada i vruzkata mejdu tablicite
}
