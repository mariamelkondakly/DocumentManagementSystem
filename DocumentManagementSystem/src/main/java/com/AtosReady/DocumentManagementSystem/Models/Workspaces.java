package com.AtosReady.DocumentManagementSystem.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Getter
@Setter
@AllArgsConstructor
@Document(collection = "workspaces")
public class Workspaces {
    @Id
    private ObjectId id;
    private long userId;
    private String name;
    private Set<ObjectId> dirIds;
    private boolean deleted = false;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date createdAt;

    public Workspaces(){
        createdAt=new Date();
    }
    public Workspaces(long userId, String name) {
        this();
        this.userId = userId;
        this.name = name;
        this.dirIds = new HashSet<>();
    }

}
