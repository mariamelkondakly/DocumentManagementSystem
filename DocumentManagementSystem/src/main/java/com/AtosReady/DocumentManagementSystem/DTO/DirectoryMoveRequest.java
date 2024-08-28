package com.AtosReady.DocumentManagementSystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Data
@Getter
@Setter
@AllArgsConstructor
public class DirectoryMoveRequest {
    private ObjectId parentId;
    private boolean isRoot;
    private boolean originalRoot;

}
