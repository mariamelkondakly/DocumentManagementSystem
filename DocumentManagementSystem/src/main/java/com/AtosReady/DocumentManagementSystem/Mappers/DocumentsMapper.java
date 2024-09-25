package com.AtosReady.DocumentManagementSystem.Mappers;

import com.AtosReady.DocumentManagementSystem.DTO.DocumentDTO;
import com.AtosReady.DocumentManagementSystem.Models.Documents;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DocumentsMapper {
    @Mapping(target = "id", expression = "java(document.getId().toString())")
    DocumentDTO documentsToDocumentDTO(Documents document);

    @Mapping(target = "id", expression = "java(new org.bson.types.ObjectId(document.getId()))")
    Documents documentDTOToDocuments(DocumentDTO document);
}
