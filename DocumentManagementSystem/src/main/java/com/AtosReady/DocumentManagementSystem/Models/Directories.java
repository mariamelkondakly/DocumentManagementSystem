package com.AtosReady.DocumentManagementSystem.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
    private Long userId;
    private ObjectId workspaceId;
    private ObjectId parentId;
    @TextIndexed
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date createdAt;
    private Set<ObjectId> childrenIds = new HashSet<>();
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date lastAccessedAt;
    private String path;
    private HashMap<String, Documents> documents = new HashMap<>();
    private boolean deleted = false;


    public Directories(Long userId, ObjectId workspace_id, ObjectId parent_id, String name) {
        this.createdAt = new Date();
        this.lastAccessedAt = new Date();
        this.workspaceId = workspace_id;
        this.parentId = parent_id;
        this.name = name;
        this.childrenIds = new HashSet<>();
    }
}
