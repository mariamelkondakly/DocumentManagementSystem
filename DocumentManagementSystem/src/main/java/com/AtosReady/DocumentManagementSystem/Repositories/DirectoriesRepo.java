package com.AtosReady.DocumentManagementSystem.Repositories;

import com.AtosReady.DocumentManagementSystem.Models.Directories;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public interface DirectoriesRepo extends MongoRepository<Directories, ObjectId> {
    Optional<Directories> findByNameAndParentIdAndDeletedFalse(String name, ObjectId parentId);
    Page<Directories> findAllByWorkspaceIdAndDeletedFalse(ObjectId workspaceId, Pageable pageable);
    Page<Directories> findAllByParentIdAndDeletedFalse(ObjectId parentId, Pageable pageable);
    Optional<Directories> findByNameAndWorkspaceIdAndDeletedFalse(String name, ObjectId workspaceId);
    Optional<Directories> findByIdAndDeletedFalse(ObjectId parentId);
    HashSet<Directories> findAllByIdAndDeletedFalse(Set<ObjectId> dirIds);
    Optional<Directories> findByNameAndWorkspaceIdAndDeletedTrue(String name, ObjectId id);
    Optional<Directories> findByNameAndParentIdAndDeletedTrue(String name, ObjectId parentID);
}
