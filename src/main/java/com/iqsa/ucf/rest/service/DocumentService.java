package com.iqsa.ucf.rest.service;

import com.iqsa.ucf.rest.model.to.DocumentTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DocumentService {
    DocumentTO getById(Integer id);

    Page<DocumentTO> getAll(Pageable pageable);

    DocumentTO createDocument(DocumentTO documentTO);

    DocumentTO updateDocument(DocumentTO documentTO, Integer id);

    Boolean deleteDocument(Integer id);
}
