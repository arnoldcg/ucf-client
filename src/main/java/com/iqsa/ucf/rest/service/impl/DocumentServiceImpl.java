package com.iqsa.ucf.rest.service.impl;

import com.iqsa.ucf.rest.dao.DocumentDAO;
import com.iqsa.ucf.rest.dao.UserDAO;
import com.iqsa.ucf.rest.model.entity.DocumentModel;
import com.iqsa.ucf.rest.model.entity.UserModel;
import com.iqsa.ucf.rest.model.to.DocumentTO;
import com.iqsa.ucf.rest.service.DocumentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.iqsa.ucf.rest.mappers.DocumentMapper.DOCUMENT_MAPPER;

@Service("DocumentServiceImpl")
public class DocumentServiceImpl implements DocumentService {

    private final UserDAO userDAO;

    private final DocumentDAO documentDAO;

    public DocumentServiceImpl(UserDAO userDAO,
                               DocumentDAO documentDAO) {
        this.userDAO = userDAO;
        this.documentDAO = documentDAO;
    }

    @Override
    public DocumentTO getById(Integer id) {
        var documentById = this.getDocumentById(id);
        return DOCUMENT_MAPPER.bind(documentById);
    }

    @Override
    public Page<DocumentTO> getAll(Pageable pageable) {
        var pageFeatureModel = this.documentDAO.findAll(pageable);
        return DOCUMENT_MAPPER.bindPage(pageFeatureModel);
    }

    @Override
    public DocumentTO createDocument(DocumentTO documentTO) {

        UserModel owner = null;
        if (!Objects.isNull(documentTO.getId_user())) {
            var userOpt = this.userDAO.findById(documentTO.getId_user());
            if (userOpt.isEmpty()) {
                var messageText = "User with id: %s not found on database";
                var message = String.format(messageText, documentTO.getId_user());
                throw new RuntimeException(message);
            }

            owner = userOpt.get();
        } else {
            var messageText = "User not specified for this document. An owner need to be defined";
            throw new RuntimeException(messageText);
        }

        var documentModel = DOCUMENT_MAPPER.bind(documentTO);
        documentModel.setUser(owner);
        documentModel = this.documentDAO.save(documentModel);
        return DOCUMENT_MAPPER.bind(documentModel);
    }

    @Override
    public DocumentTO updateDocument(DocumentTO documentTO, Integer id) {
        var documentModel = this.getDocumentById(id);
        DOCUMENT_MAPPER.updateDocument(documentModel, documentTO);
        this.documentDAO.save(documentModel);
        return DOCUMENT_MAPPER.bind(documentModel);
    }

    @Override
    public Boolean deleteDocument(Integer id) {
        this.documentDAO.deleteById(id);
        return true;
    }

    private DocumentModel getDocumentById(Integer id) {
        var featureOpt = this.documentDAO.findById(id);
        if (featureOpt.isEmpty()) {
            var messageText = "Document with id: %s not found on database";
            var message = String.format(messageText, id);
            throw new RuntimeException(message);
        }

        return featureOpt.get();
    }
}
