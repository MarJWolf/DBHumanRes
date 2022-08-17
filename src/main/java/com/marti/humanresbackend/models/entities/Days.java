package com.marti.humanresbackend.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "days")
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Days implements Comparable<Days>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userDaysId;

    private int days;
    private int year;
    private boolean use;

    public Days(int cDays, Long userId, int year,boolean canUse){
        this.days = cDays;
        this.userDaysId = userId;
        this.year = year;
        this.use = canUse;
    }

    public Days(Long userId){
        this.days = 0;
        this.year = 0;
        this.use = false;
        this.userDaysId = userId;
    }

    @Override
    public int compareTo(Days d) {
        if (getYear() != 0 || d.getYear() != 0) {
            if(getYear()<d.getYear()){
                return -1;
            }else{
                return 1;
            }
        }
        return 0;
    }
}
