package com.AtosReady.DocumentManagementSystem.Controllers;

import com.AtosReady.DocumentManagementSystem.DTO.DocumentMoveRequest;
import com.AtosReady.DocumentManagementSystem.Services.DocumentService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/workspaces/directories/")
public class DocumentController {

    @Autowired
    private DocumentService service;

    @PostMapping("{parentId}")
    public ResponseEntity<String> uploadDocument(@PathVariable("parentId") ObjectId parentId,
                                                 @RequestParam("file") MultipartFile file) throws IOException {
        return service.createDocument(parentId, file);
    }

    @PutMapping("rename/{parentId}/{oldName}")
    public ResponseEntity<String> renameDocument(@PathVariable("parentId") ObjectId parentId,
                                                 @PathVariable("oldName") String oldName,
                                                 @RequestParam("newName") String newName) throws IOException {
        return service.renameDocument(parentId, oldName, newName);
    }

    @PutMapping("move")
    public ResponseEntity<String> moveDocument(@RequestBody DocumentMoveRequest documentMoveRequest) throws IOException {
        return service.moveDocument(documentMoveRequest);
    }

    @DeleteMapping("{parentId}/{name}")
    public ResponseEntity<String> deleteDocument(@PathVariable("parentId") ObjectId parentId,@PathVariable("name") String name) throws IOException {
        return service.deleteDocument(parentId,name);
    }

}
