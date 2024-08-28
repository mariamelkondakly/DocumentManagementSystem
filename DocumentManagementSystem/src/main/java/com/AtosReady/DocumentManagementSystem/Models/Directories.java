package com.AtosReady.DocumentManagementSystem.Models;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "directories")
public class Directories {
    @Id
    private ObjectId id;
    private ObjectId workspaceId;
    private ObjectId parentId;
    private String name;
    private Date createdAt;
    private Set<ObjectId> childrenIds=new HashSet<>();
    private Date lastAccessedAt;
    private String path;
    private boolean deleted = false;


    public Directories(ObjectId workspace_id, ObjectId parent_id, String name) {
        this.createdAt = new Date();
        this.lastAccessedAt = new Date();
        this.workspaceId = workspace_id;
        this.parentId = parent_id;
        this.name = name;
        this.childrenIds = new HashSet<>();
    }
}
