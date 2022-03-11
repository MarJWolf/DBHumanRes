package com.marti.humanresbackend.models.entities;

import com.marti.humanresbackend.models.Status;
import com.marti.humanresbackend.models.Type;
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
    private Long userId;
    private Type type;
    private Date startDate;
    private Date endDate;
    private Status status;

}
