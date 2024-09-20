package com.AtosReady.DocumentManagementSystem.Controllers;

import com.AtosReady.DocumentManagementSystem.DTO.DocumentMoveRequest;
import com.AtosReady.DocumentManagementSystem.DTO.SearchResults;
import com.AtosReady.DocumentManagementSystem.DTO.WorkspacesDTO;
import com.AtosReady.DocumentManagementSystem.Models.Directories;
import com.AtosReady.DocumentManagementSystem.Models.Documents;
import com.AtosReady.DocumentManagementSystem.Services.DocumentService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/workspaces/")
public class DocumentController {

    @Autowired
    private DocumentService service;

    @GetMapping("search/{name}")
    public SearchResults search(@PathVariable(value = "name") String searchName,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "size", defaultValue = "10") int size) {
        return service.searchDocumentsAndWorkspaces(searchName, page, size);
    }


    @PostMapping("directories/{parentId}")
    public ResponseEntity<String> uploadDocument(@PathVariable("parentId") ObjectId parentId,
                                                 @RequestParam("file") MultipartFile file) throws IOException {
        return service.createDocument(parentId, file);
    }

    @PutMapping("directories/rename/{parentId}/{oldName}")
    public ResponseEntity<String> renameDocument(@PathVariable("parentId") ObjectId parentId,
                                                 @PathVariable("oldName") String oldName,
                                                 @RequestParam("newName") String newName) throws IOException {
        return service.renameDocument(parentId, oldName, newName);
    }

    @PutMapping("directories/move")
    public ResponseEntity<String> moveDocument(@RequestBody DocumentMoveRequest documentMoveRequest) throws IOException {
        return service.moveDocument(documentMoveRequest);
    }

    @DeleteMapping("directories/{parentId}/{name}")
    public ResponseEntity<String> deleteDocument(@PathVariable("parentId") ObjectId parentId,@PathVariable("name") String name)
            throws IOException {
        return service.deleteDocument(parentId,name);
    }

    @GetMapping("directories/preview/{parentId}/{documentName}")
    public ResponseEntity<Resource> previewDocument(@PathVariable("parentId") ObjectId parentId,@PathVariable("documentName") String documentName)
            throws IOException {
        return service.previewAndDownloadDocument(parentId,documentName,"inline");
    }

    @GetMapping("directories/download/{parentId}/{documentName}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable("parentId") ObjectId parentId,@PathVariable("documentName") String documentName)
            throws IOException {
        return service.previewAndDownloadDocument(parentId,documentName,"attachment");
    }

}
