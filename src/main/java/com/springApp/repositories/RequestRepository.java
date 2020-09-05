package com.springApp.repositories;

import com.springApp.entities.RequestEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RequestRepository extends PagingAndSortingRepository<RequestEntity, Integer> {
    public Optional<RequestEntity> findFirstByStatus(String status);
}
