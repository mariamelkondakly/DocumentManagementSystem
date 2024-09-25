package com.AtosReady.DocumentManagementSystem.Repositories;


import com.AtosReady.DocumentManagementSystem.Models.Documents;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.ArrayList;
import java.util.Optional;

public interface DocumentsRepo extends MongoRepository<Documents, ObjectId> {
    Page<Documents> findAllByParentIdAndUserIdAndDeletedFalse(ObjectId parentId, Long userId, Pageable pageable);

    Optional<Documents> findByUserIdAndIdAndDeletedFalse(Long userId, ObjectId Id);

    Optional<Documents> findByNameAndParentIdAndUserId(String name, Long userId, ObjectId parentId);

    ArrayList<Documents> findAllByParentIdAndUserIdAndDeletedFalse(ObjectId parentId, long userId);

    @Query("{ 'userId': ?0, 'name': { '$regex': ?1, '$options': 'i' }, 'deleted': false } ")
    Page<Documents> findByName(Long userId, String searchName, Pageable pageable);


}
