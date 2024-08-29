package com.AtosReady.DocumentManagementSystem.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.Date;

@Data
@AllArgsConstructor
@Getter
@Setter
public class Documents {
    private ObjectId id;
    private String name;
    private String path;
    private long size;
    private boolean Deleted = false;
    private Date createdAt;

    public Documents() {
        createdAt = new Date();
        id = new ObjectId();
    }
}

