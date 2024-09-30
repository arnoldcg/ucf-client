package com.iqsa.ucf.rest.mappers;

import com.google.common.base.Strings;
import com.iqsa.ucf.rest.model.entity.CompanyModel;
import com.iqsa.ucf.rest.model.entity.DocumentModel;
import com.iqsa.ucf.rest.model.entity.UserModel;
import com.iqsa.ucf.rest.model.to.CompanyTO;
import com.iqsa.ucf.rest.model.to.DocumentTO;
import com.iqsa.ucf.rest.model.to.UserTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.Objects;

@Mapper
public interface DocumentMapper {

    DocumentMapper DOCUMENT_MAPPER = Mappers.getMapper(DocumentMapper.class);

    @Mapping(expression = "java(model.getUser().getId())", target = "id_user")
    DocumentTO bind(DocumentModel model);

    DocumentModel bind(DocumentTO model);

    default Page<DocumentTO> bindPage(Page<DocumentModel> modelPage) {
        return modelPage.map(this::bind);
    }

    default void updateDocument(DocumentModel userModel, DocumentTO documentTO) {
        if (!Strings.isNullOrEmpty(documentTO.getTitle())) {
            userModel.setTitle(documentTO.getTitle());
        }

        if (!Strings.isNullOrEmpty(documentTO.getDescription())) {
            userModel.setDescription(documentTO.getDescription());
        }
    }
}
