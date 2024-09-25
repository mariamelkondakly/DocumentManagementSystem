package com.AtosReady.DocumentManagementSystem.Controllers;

import com.AtosReady.DocumentManagementSystem.DTO.DocumentMoveRequest;
import com.AtosReady.DocumentManagementSystem.DTO.SearchResults;
import com.AtosReady.DocumentManagementSystem.Services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
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
    public ResponseEntity<String> uploadDocument(@PathVariable("parentId") String parentId,
                                                 @RequestParam("file") MultipartFile file) throws IOException {
        return service.createDocument(parentId, file);
    }

    @PutMapping("directories/rename/{documentId}/{oldName}")
    public ResponseEntity<String> renameDocument(@PathVariable("documentId") String documentId,
                                                 @PathVariable("oldName") String oldName,
                                                 @RequestParam("newName") String newName) throws IOException {
        return service.renameDocument(documentId, oldName, newName);
    }

    @PutMapping("directories/move")
    public ResponseEntity<String> moveDocument(@RequestBody DocumentMoveRequest documentMoveRequest) throws IOException {
        return service.moveDocument(documentMoveRequest);
    }

    @DeleteMapping("directories/{documentId}")
    public ResponseEntity<String> deleteDocument(@PathVariable("documentId") String documentId)
            throws IOException {
        return service.deleteDocument(documentId);
    }

    @GetMapping("directories/preview/{documentId}")
    public ResponseEntity<Resource> previewDocument(@PathVariable("documentId") String documentId)
            throws IOException {
        return service.previewAndDownloadDocument(documentId,"inline");
    }

    @GetMapping("directories/download/{documentId}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable("documentId") String documentId)
            throws IOException {
        return service.previewAndDownloadDocument(documentId,"attachment");
    }

}
