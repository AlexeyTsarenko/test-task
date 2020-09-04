package com.springApp.repositories;

import com.springApp.entities.RequestEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends PagingAndSortingRepository<RequestEntity, Integer> {}
