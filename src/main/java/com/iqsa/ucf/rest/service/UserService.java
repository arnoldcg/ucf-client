package com.iqsa.ucf.rest.service;

import com.iqsa.ucf.rest.model.to.CompanyTO;
import com.iqsa.ucf.rest.model.to.UserTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserTO getById(Integer id);

    Page<UserTO> getAll(Pageable pageable);

    UserTO createUser(UserTO userTO);

    UserTO updateUser(UserTO userTO, Integer id);

    Boolean deleteUser(Integer id);
}
