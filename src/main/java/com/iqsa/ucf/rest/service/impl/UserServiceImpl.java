package com.iqsa.ucf.rest.service.impl;

import com.iqsa.ucf.rest.dao.CompanyDAO;
import com.iqsa.ucf.rest.dao.UserDAO;
import com.iqsa.ucf.rest.model.entity.CompanyModel;
import com.iqsa.ucf.rest.model.entity.UserModel;
import com.iqsa.ucf.rest.model.to.UserTO;
import com.iqsa.ucf.rest.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.iqsa.ucf.rest.mappers.UserMapper.USER_MAPPER;

@Service("UserServiceImpl")
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    private final CompanyDAO companyDAO;

    public UserServiceImpl(UserDAO userDAO, CompanyDAO companyDAO) {
        this.userDAO = userDAO;
        this.companyDAO = companyDAO;
    }

    @Override
    public UserTO getById(Integer id) {
        var userModel = this.getUserById(id);
        return USER_MAPPER.bindWithCompanyData(userModel);
    }

    @Override
    public Page<UserTO> getAll(Pageable pageable) {
        var pageFeatureModel = this.userDAO.findAll(pageable);
        return USER_MAPPER.bindPage(pageFeatureModel);
    }

    @Override
    public UserTO createUser(UserTO userTO) {
        var featureModel = USER_MAPPER.bind(userTO);
        var companyOpt = this.companyDAO.findById(userTO.getId_company());

        if (companyOpt.isEmpty()) {
            throw new EntityNotFoundException("Company not found");
        }

        featureModel.setCompany(companyOpt.get());
        featureModel = this.userDAO.save(featureModel);
        return USER_MAPPER.bindWithCompanyData(featureModel);
    }

    @Override
    public UserTO updateUser(UserTO userTO, Integer id) {
        var userById = this.getUserById(id);
        CompanyModel newCompany = null;

        if (!Objects.isNull(userTO.getId_company())) {
            var companyOpt = this.companyDAO.findById(userTO.getId_company());

            if (companyOpt.isEmpty()) {
                throw new EntityNotFoundException("Company not found");
            }
            newCompany = companyOpt.get();
        }

        USER_MAPPER.updateUser(userById, userTO, newCompany);
        userById = this.userDAO.save(userById);
        return USER_MAPPER.bindWithCompanyData(userById);
    }

    @Override
    public Boolean deleteUser(Integer id) {
        this.userDAO.deleteById(id);
        return true;
    }

    private UserModel getUserById(Integer id) {
        var userDAOById = this.userDAO.findById(id);
        if (userDAOById.isEmpty()) {
            var messageText = "User with id: %s not found on database";
            var message = String.format(messageText, id);
            throw new RuntimeException(message);
        }

        return userDAOById.get();
    }
}
