package com.marti.humanresbackend.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "company_info")
@Getter
@Setter
@Entity
@NoArgsConstructor
public class CompanyInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;
    private String companyCEOName;

    public CompanyInfo(String name, String CEOname){companyName = name; companyCEOName = CEOname;}
}
