package com.iqsa.ucf.rest.service.impl;

import com.iqsa.ucf.rest.dao.DocumentDAO;
import com.iqsa.ucf.rest.dao.UserDAO;
import com.iqsa.ucf.rest.model.entity.DocumentModel;
import com.iqsa.ucf.rest.model.to.DocumentTO;
import com.iqsa.ucf.rest.service.DocumentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        var featureModel = DOCUMENT_MAPPER.bind(documentTO);
        return DOCUMENT_MAPPER.bind(featureModel);
    }

    @Override
    public DocumentTO updateDocument(DocumentTO featureTO, Integer id) {
        var documentById = this.getDocumentById(id);
        DOCUMENT_MAPPER.updateDocument(documentById, featureTO);
        return DOCUMENT_MAPPER.bind(documentById);
    }

    @Override
    public Boolean deleteDocument(Integer id) {
        this.documentDAO.deleteById(id);
        return true;
    }

    private DocumentModel getDocumentById(Integer id) {
        var featureOpt = this.documentDAO.findById(id);
        if (featureOpt.isEmpty()) {
            var messageText = "User with id: %s not found on database";
            var message = String.format(messageText, id);
            throw new RuntimeException(message);
        }

        return featureOpt.get();
    }
}
