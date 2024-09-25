package com.AtosReady.DocumentManagementSystem.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "documents")
public class Documents {
    @Id
    private ObjectId id;
    private ObjectId parentId;
    private Long userId;
    private String name;
    private String path;
    private long size;
    private Date createdAt;
    private Date lastAccessedAt;
    private List<String> tags=new ArrayList<>();
    private String type;
    private boolean deleted = false;

    public Documents() {
        createdAt = new Date();
        lastAccessedAt=new Date();
    }
}

