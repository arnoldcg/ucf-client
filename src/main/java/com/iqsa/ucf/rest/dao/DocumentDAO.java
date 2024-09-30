package com.iqsa.ucf.rest.dao;

import com.iqsa.ucf.rest.model.entity.DocumentModel;
import com.iqsa.ucf.rest.model.entity.UserModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DocumentDAO extends CrudRepository<DocumentModel, Integer>
        , PagingAndSortingRepository<DocumentModel, Integer> {
}
