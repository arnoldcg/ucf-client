package com.iqsa.ucf.rest.dao;

import com.iqsa.ucf.rest.model.entity.CompanyModel;
import com.iqsa.ucf.rest.model.entity.UserModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserDAO extends CrudRepository<UserModel, Integer>
        , PagingAndSortingRepository<UserModel, Integer> {
}
