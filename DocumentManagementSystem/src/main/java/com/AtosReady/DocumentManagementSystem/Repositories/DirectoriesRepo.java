package com.AtosReady.DocumentManagementSystem.Repositories;

import com.AtosReady.DocumentManagementSystem.Models.Directories;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DirectoriesRepo extends MongoRepository<Directories, ObjectId> {
    Optional<Directories> findByNameAndParentIdAndDeletedFalse(String name, ObjectId parentId);

    Page<Directories> findAllByWorkspaceIdAndDeletedFalse(ObjectId workspaceId, Pageable pageable);

    Page<Directories> findAllByParentIdAndDeletedFalse(ObjectId parentId, Pageable pageable);

    Optional<Directories> findByNameAndWorkspaceIdAndDeletedFalse(String name, ObjectId workspaceId);

    Optional<Directories> findByIdAndUserIdAndDeletedFalse(ObjectId parentId, long userId);

    Optional<Directories> findByNameAndWorkspaceIdAndDeletedTrue(String name, ObjectId id);

    Optional<Directories> findByNameAndParentIdAndDeletedTrue(String name, ObjectId parentID);
}
