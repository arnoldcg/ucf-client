package com.iqsa.ucf.rest.mappers;

import com.google.common.base.Strings;
import com.iqsa.ucf.rest.model.entity.CompanyModel;
import com.iqsa.ucf.rest.model.entity.UserModel;
import com.iqsa.ucf.rest.model.to.CompanyTO;
import com.iqsa.ucf.rest.model.to.DocumentTO;
import com.iqsa.ucf.rest.model.to.UserTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Objects;

@Mapper
public interface UserMapper {

    UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);

    @Mapping(expression = "java(model.getCompany().getId())", target = "id_company")
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "documents", ignore = true)
    UserTO bind(UserModel model);

    UserModel bind(UserTO model);

    default UserTO bindWithCompanyData(UserModel model) {
        var result = bind(model);
        var temp = model.getCompany();

        var tempCompanyInfo = CompanyTO.builder()
                .name(temp.getName())
                .description(temp.getDescription())
                .build();

        var documentSummary = new ArrayList<DocumentTO>();

        if(Objects.nonNull(model.getDocuments()) && !model.getDocuments().isEmpty()) {
            model.getDocuments().forEach(d -> {
               var tempDocument = DocumentTO.builder()
                       .id(d.getId())
                       .title(d.getTitle())
                       .description(d.getDescription())
                       .build();

               documentSummary.add(tempDocument);
            });
        }

        result.setCompany(tempCompanyInfo);
        result.setDocuments(documentSummary);
        return result;
    }

    default Page<UserTO> bindPage(Page<UserModel> modelPage) {
        return modelPage.map(this::bindWithCompanyData);
    }

    default void updateUser(UserModel userModel, UserTO userTO, CompanyModel newCompany) {
        if (!Strings.isNullOrEmpty(userTO.getName())) {
            userModel.setName(userTO.getName());
        }

        if (!Strings.isNullOrEmpty(userTO.getPassword())) {
            userModel.setPassword(userTO.getPassword());
        }

        if (!Strings.isNullOrEmpty(userTO.getRole())) {
            userModel.setRole(userTO.getRole());
        }

        if (!Objects.isNull(newCompany)) {
            userModel.setCompany(newCompany);
        }
    }
}
