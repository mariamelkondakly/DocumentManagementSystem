package com.AtosReady.DocumentManagementSystem.Models;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "documents")
public class Documents {
    @Id
    private ObjectId id;
    private ObjectId dirId;
    private String name;
    private String path;
    private Date  createdAt;
}

