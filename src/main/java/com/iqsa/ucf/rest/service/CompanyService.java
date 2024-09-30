package com.iqsa.ucf.rest.service;

import com.iqsa.ucf.rest.model.to.CompanyTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyService {
    CompanyTO getById(Integer id);

    Page<CompanyTO> getAll(Pageable pageable);

    CompanyTO createCompany(CompanyTO companyTO);

    CompanyTO createCompanyWithUsers(CompanyTO companyTO);

    CompanyTO updateCompany(CompanyTO companyTO, Integer id);

    Boolean deleteCompany(Integer id);

}
