package com.AtosReady.DocumentManagementSystem.Mappers;

import com.AtosReady.DocumentManagementSystem.DTO.WorkspacesDTO;
import com.AtosReady.DocumentManagementSystem.Models.Workspaces;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkspacesMapper {
    WorkspacesDTO workspaceWorkspacesDTO(Workspaces workspace);
    Workspaces workspacesDTOToWorkspaces(WorkspacesDTO workspace);
}
