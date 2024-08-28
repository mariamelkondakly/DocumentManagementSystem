package com.AtosReady.DocumentManagementSystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class DirectoryRenameRequest {
    private String name;
    private boolean isRoot;
}
