package com.AtosReady.DocumentManagementSystem.Mappers;

import com.AtosReady.DocumentManagementSystem.DTO.WorkspacesDTO;
import com.AtosReady.DocumentManagementSystem.Models.Workspaces;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WorkspacesMapper {

    @Mapping(target = "id", expression = "java(workspace.getId().toString())")
    WorkspacesDTO workspaceWorkspacesDTO(Workspaces workspace);

    @Mapping(target = "id", expression = "java(new org.bson.types.ObjectId(workspace.getId()))")
    Workspaces workspacesDTOToWorkspaces(WorkspacesDTO workspace);
}
