package com.AtosReady.DocumentManagementSystem.Mappers;


import com.AtosReady.DocumentManagementSystem.DTO.DirectoryDTO;
import com.AtosReady.DocumentManagementSystem.Models.Directories;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DirectoriesMapper {
    DirectoryDTO directoryToDirectoryDTO(Directories directory);

    Directories directoryDTOToDirectory(DirectoryDTO directory);
}
