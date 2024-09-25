package com.AtosReady.DocumentManagementSystem.Controllers;

import com.AtosReady.DocumentManagementSystem.DTO.DirectoryMoveRequest;
import com.AtosReady.DocumentManagementSystem.DTO.DirectoryRenameRequest;
import com.AtosReady.DocumentManagementSystem.Services.DirectoriesService;
import com.AtosReady.DocumentManagementSystem.Services.DirectoryDocumentService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/workspaces")
public class DirectoryController {

    @Autowired
    private DirectoriesService service;

    @Autowired
    private DirectoryDocumentService directoryDocumentService;

    //Post Endpoints
    @PostMapping("/root/{workspace_id}/{name}")
    public ResponseEntity<HashMap<String, Object>> createRootDirectory(@PathVariable String workspace_id,
                                                                       @PathVariable String name) {

        return service.createRootDirectory(workspace_id, name);
    }

    @PostMapping("/{parent_id}/{dirName}")
    public ResponseEntity<HashMap<String, Object>> createSubDirectory(@PathVariable ObjectId parent_id,
                                                                      @PathVariable String dirName) {

        return service.createSubDirectory(parent_id, dirName);
    }

    //Get Endpoints
    @GetMapping("/{parentId}")
    public List<Object> getDirectoriesInParents(@PathVariable("parentId") String parentId,
                                                @RequestParam(value = "page", defaultValue = "0") int page,
                                                @RequestParam(value = "size", defaultValue = "10") int size) {
        return service.getDirsByParentId(parentId, page, size);
    }

    @GetMapping("/root/{workspaceId}")
    public List<Object> getDirectoriesInWorkspaces(@PathVariable("workspaceId") String workspaceId,
                                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                                   @RequestParam(value = "size", defaultValue = "10") int size) {
        return service.getDirsByWorkspaceId(workspaceId, page, size);
    }

    //Put Endpoints
    @PutMapping("/move/{id}")
    public ResponseEntity<String> moveDirectory(@PathVariable("id") String id,
                                                @RequestBody DirectoryMoveRequest updateRequest) {
        directoryDocumentService.MoveDirectory(id, updateRequest);
        return ResponseEntity.ok("Directory updated successfully");
    }

    @PutMapping("/rename/{id}")
    public ResponseEntity<String> renameDirectory(@PathVariable("id") String id,
                                                  @RequestBody DirectoryRenameRequest updateRequest) {
        directoryDocumentService.RenameDirectory(id, updateRequest);
        return ResponseEntity.ok("Directory updated successfully");
    }


    //Delete Endpoints
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWorkspace(@PathVariable("id") String id) throws IOException {
        directoryDocumentService.deleteWorkspace(id);
        return ResponseEntity.ok("Workspace deleted successfully.");
    }

    @DeleteMapping("/deleteDirectory/{id}")
    public ResponseEntity<String> deleteDirectory(@PathVariable("id") String id) throws IOException {
        directoryDocumentService.deleteDirectory(new ObjectId(id));
        return ResponseEntity.ok("Workspace deleted successfully.");
    }
}



