package com.AtosReady.DocumentManagementSystem.Repositories;

import com.AtosReady.DocumentManagementSystem.Models.Documents;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocumentRepo extends MongoRepository<Documents, ObjectId> {

}
