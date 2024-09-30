package com.iqsa.ucf.rest.service.impl;

import com.iqsa.ucf.rest.dao.CompanyDAO;
import com.iqsa.ucf.rest.dao.UserDAO;
import com.iqsa.ucf.rest.model.entity.CompanyModel;
import com.iqsa.ucf.rest.model.entity.UserModel;
import com.iqsa.ucf.rest.model.to.CompanyTO;
import com.iqsa.ucf.rest.service.CompanyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.iqsa.ucf.rest.mappers.CompanyMapper.COMPANY_MAPPER;

@Service("CompanyServiceImpl")
public class CompanyServiceImpl implements CompanyService {

    private final CompanyDAO companyDAO;
    private final UserDAO userDAO;

    public CompanyServiceImpl(CompanyDAO companyDAO, UserDAO userDAO) {
        this.companyDAO = companyDAO;
        this.userDAO = userDAO;
    }

    @Override
    public CompanyTO getById(Integer id) {
        var companyById = this.getCompanyById(id);
        return COMPANY_MAPPER.bindWithUsers(companyById);
    }

    @Override
    public Page<CompanyTO> getAll(Pageable pageable) {
        var pageComnpanyId = this.companyDAO.findAll(pageable);
        return COMPANY_MAPPER.bindPage(pageComnpanyId);
    }

    @Override
    public CompanyTO createCompany(CompanyTO companyTO) {
        var companyModel = COMPANY_MAPPER.bind(companyTO);
        companyModel = this.companyDAO.save(companyModel);
        return COMPANY_MAPPER.bindWithUsers(companyModel);
    }

    @Override
    public CompanyTO createCompanyWithUsers(CompanyTO companyTO) {
        final CompanyModel companyModel = COMPANY_MAPPER.bind(companyTO);
        final List<UserModel> users = companyModel.getUsers();
        companyModel.setUsers(null);

        final CompanyModel companyModelResponse = this.companyDAO.save(companyModel);
        users.forEach(u -> u.setCompany(companyModelResponse));
        var usersSaved = this.userDAO.saveAll(users);
        var usersInDbCasted = new ArrayList<UserModel>();
        for (UserModel userModel : usersSaved) {
            usersInDbCasted.add(userModel);
        }

        companyModelResponse.setUsers(usersInDbCasted);
        return COMPANY_MAPPER.bindWithUsers(companyModelResponse);
    }

    @Override
    public CompanyTO updateCompany(CompanyTO companyTO, Integer id) {
        var companyById = this.getCompanyById(id);
        COMPANY_MAPPER.updateEntity(companyById, companyTO);
        companyById = this.companyDAO.save(companyById);
        return COMPANY_MAPPER.bindWithUsers(companyById);
    }

    @Override
    public Boolean deleteCompany(Integer id) {
        var companyById = this.getCompanyById(id);
        if (companyById.getUsers() != null && !companyById.getUsers().isEmpty()) {
            var messageText = "Company with id: %s can not be deleted on the database because there are users registered on this company";
            var message = String.format(messageText, id);
            throw new RuntimeException(message);
        }
        this.companyDAO.deleteById(id);
        return true;
    }

    private CompanyModel getCompanyById(Integer id) {
        var companyOpt = this.companyDAO.findById(id);
        if (companyOpt.isEmpty()) {
            var messageText = "Feature with id: %s not found on database";
            var message = String.format(messageText, id);
            throw new RuntimeException(message);
        }

        return companyOpt.get();
    }
}
