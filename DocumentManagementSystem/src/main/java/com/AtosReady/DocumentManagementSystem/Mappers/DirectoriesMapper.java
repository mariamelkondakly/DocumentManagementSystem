package com.AtosReady.DocumentManagementSystem.Mappers;


import com.AtosReady.DocumentManagementSystem.DTO.DirectoryDTO;
import com.AtosReady.DocumentManagementSystem.Models.Directories;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DirectoriesMapper {

    @Mapping(target = "id", expression = "java(directory.getId().toString())")
    DirectoryDTO directoryToDirectoryDTO(Directories directory);

    @Mapping(target = "id", expression = "java(new org.bson.types.ObjectId(directory.getId()))")
    Directories directoryDTOToDirectory(DirectoryDTO directory);
}
