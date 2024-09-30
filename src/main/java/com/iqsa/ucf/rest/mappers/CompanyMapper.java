package com.iqsa.ucf.rest.mappers;

import com.google.common.base.Strings;
import com.iqsa.ucf.rest.model.entity.CompanyModel;
import com.iqsa.ucf.rest.model.to.CompanyTO;
import com.iqsa.ucf.rest.model.to.UserTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Objects;

import static com.iqsa.ucf.rest.mappers.UserMapper.USER_MAPPER;

@Mapper
public interface CompanyMapper {

    CompanyMapper COMPANY_MAPPER = Mappers.getMapper(CompanyMapper.class);

    @Mapping(target = "users", ignore = true)
    CompanyTO bind(CompanyModel model);

    default CompanyTO bindWithUsers(CompanyModel model) {
        var result = this.bind(model);

        var tempUserList = new ArrayList<UserTO>();

        if (!Objects.isNull(model.getUsers())) {

            model.getUsers().forEach(userModel -> {
                var temp = USER_MAPPER.bind(userModel);
                tempUserList.add(temp);
            });

            result.setUsers(tempUserList);
        }

        return result;
    }

    CompanyModel bind(CompanyTO model);

    default Page<CompanyTO> bindPage(Page<CompanyModel> modelPage) {
        return modelPage.map(this::bindWithUsers);
    }

    default void updateEntity(CompanyModel model, CompanyTO companyTO) {
        if (!Strings.isNullOrEmpty(companyTO.getName())) {
            model.setName(companyTO.getName());
        }
        if (!Strings.isNullOrEmpty(companyTO.getDescription())) {
            model.setDescription(companyTO.getDescription());
        }
    }
}
