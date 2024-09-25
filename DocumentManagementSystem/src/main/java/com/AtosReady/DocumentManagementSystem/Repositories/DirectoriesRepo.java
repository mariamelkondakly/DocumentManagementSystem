package com.AtosReady.DocumentManagementSystem.Repositories;

import com.AtosReady.DocumentManagementSystem.Models.Directories;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface DirectoriesRepo extends MongoRepository<Directories, ObjectId> {
    Optional<Directories> findByNameAndParentId(String name, ObjectId parentId);

    Page<Directories> findAllByWorkspaceIdAndDeletedFalse(ObjectId workspaceId, Pageable pageable);

    Page<Directories> findAllByParentIdAndDeletedFalse(ObjectId parentId, Pageable pageable);

    Optional<Directories> findByNameAndWorkspaceId(String name, ObjectId workspaceId);

    Optional<Directories> findByIdAndUserIdAndDeletedFalse(ObjectId parentId, long userId);

    Optional<Directories> findByNameAndWorkspaceIdAndDeletedTrue(String name, ObjectId id);

    Optional<Directories> findByNameAndParentIdAndDeletedTrue(String name, ObjectId parentID);

    Optional<Directories> findByNameAndWorkspaceIdAndDeletedFalse(String name, ObjectId workspaceId);

    Optional<Directories> findByNameAndParentIdAndDeletedFalse(String dirName, ObjectId parentID);


    //    @Query("{ '$text': { '$search': ?0 } }")
//    @Query("{ 'documents.name' :  }")
//    @Query("{ 'documents.$*.name' : ?0 }")
    @Query("{ 'userId': ?0, 'name': { '$regex': ?1, '$options': 'i' }, 'deleted': false } ")
    Page<Directories> findByName(Long userId, String searchName, Pageable pageable);

}
