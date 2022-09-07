package com.marti.humanresbackend.services;

import com.marti.humanresbackend.models.entities.CompanyInfo;
import com.marti.humanresbackend.repositories.CompanyInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyInfoService {
    private final CompanyInfoRepository compRep;

    public void createCompanyInfo(String name, String CEOname) {
        compRep.save(new CompanyInfo(name, CEOname));
    }

    public void updateCompanyInfo(String name, String CEOname) {
        CompanyInfo compInfo = compRep.getById(1L);
        compInfo.setCompanyName(name);
        compInfo.setCompanyCEOName(CEOname);
        compRep.save(compInfo);
    }

    public CompanyInfo getCompanyInfo() {
        return compRep.findById(1L).orElse(null);
    }
}
