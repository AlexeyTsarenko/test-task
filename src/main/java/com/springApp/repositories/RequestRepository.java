package com.springApp.repositories;

import com.springApp.entities.RequestEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends PagingAndSortingRepository<RequestEntity, Integer> {
    Optional<RequestEntity> findFirstByStatusOrStatus(String status1,String status2);
    List<RequestEntity> findAllByClient(int id);
    Optional<RequestEntity> findFirstByTicket(int id);
    List<RequestEntity> findAllByStatus(String status);

}
