package com.AtosReady.DocumentManagementSystem.Models;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "workspaces")
public class Workspaces {
    @Id
    private ObjectId id;
    private long userId;
    private String name;
    private Set<ObjectId> dirIds;
    private boolean deleted = false;


    public Workspaces(long userId, String name) {
        this.userId = userId;
        this.name = name;
        this.dirIds = new HashSet<>();
    }

}
