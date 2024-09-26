package com.AtosReady.DocumentManagementSystem.Repositories;

import com.AtosReady.DocumentManagementSystem.Models.Documents;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface DocumentsRepo extends MongoRepository<Documents, ObjectId> {

    Page<Documents> findAllByParentIdAndUserIdAndDeletedFalse(ObjectId parentId, Long userId, Pageable pageable);

    Optional<Documents> findByUserIdAndIdAndDeletedFalse(Long userId, ObjectId Id);

    Optional<Documents> findByNameAndParentIdAndUserId(String name, Long userId, ObjectId parentId);

    ArrayList<Documents> findAllByParentIdAndUserIdAndDeletedFalse(ObjectId parentId, long userId);

    @Query("{ 'userId': ?0, 'name': { '$regex': ?1, '$options': 'i' }, 'deleted': false } ")
    Page<Documents> findByName(Long userId, String searchName, Pageable pageable);

    // Find documents by multiple tags (any matching tags)
    @Query("{ 'userId': ?0, 'tags': { '$in': [ ?1 ] }, 'deleted': false }")
    Page<Documents> findByTag(Long userId, String tag, Pageable pageable);


    // Find documents by type with case-insensitive match
    @Query("{ 'userId': ?0, 'type': { '$regex': ?1, '$options': 'i' }, 'deleted': false }")
    Page<Documents> findByTypeIgnoreCase(Long userId, String type, Pageable pageable);

}
