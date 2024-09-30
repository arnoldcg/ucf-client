package com.iqsa.ucf.rest.dao;

import com.iqsa.ucf.rest.model.entity.CompanyModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CompanyDAO extends CrudRepository<CompanyModel, Integer>
        , PagingAndSortingRepository<CompanyModel, Integer> {
}
