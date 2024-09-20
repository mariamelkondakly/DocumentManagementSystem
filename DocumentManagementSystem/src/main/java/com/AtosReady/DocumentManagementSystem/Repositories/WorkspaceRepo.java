package com.AtosReady.DocumentManagementSystem.Repositories;

import com.AtosReady.DocumentManagementSystem.Models.Directories;
import com.AtosReady.DocumentManagementSystem.Models.Workspaces;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface WorkspaceRepo extends MongoRepository<Workspaces, ObjectId> {
    Page<Workspaces> findAllByUserIdAndDeletedFalse(long userId, Pageable pageable);

    Optional<Object> findByUserIdAndNameAndDeletedFalse(Long userId, String name);

    Optional<Workspaces> findByIdAndDeletedFalse(ObjectId id);

    Optional<Workspaces> findByUserIdAndNameAndDeletedTrue(long userId, String name);

    void deleteByUserIdAndNameAndDeletedTrue(long userId, String name);

    @Query("{ 'userId': ?0, 'name': { '$regex': ?1, '$options': 'i' }, 'deleted': false }")
    Page<Workspaces> findByUserIdAndNameAndDeletedFalse(Long userId, String name, Pageable pageable);
}