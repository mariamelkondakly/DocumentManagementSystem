package com.AtosReady.DocumentManagementSystem.Controllers;

import com.AtosReady.DocumentManagementSystem.DTO.DirectoryDTO;
import com.AtosReady.DocumentManagementSystem.DTO.DirectoryMoveRequest;
import com.AtosReady.DocumentManagementSystem.DTO.DirectoryRenameRequest;
import com.AtosReady.DocumentManagementSystem.Services.DirectoriesService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping("/workspaces")
public class DirectoryController {

    @Autowired
    private DirectoriesService service;

    @PostMapping("/root/{workspace_id}")
    public ResponseEntity<HashMap<String, Object>> createRootDirectory(@PathVariable ObjectId workspace_id,
                                                                       @RequestBody DirectoryDTO dir) {

        return service.createRootDirectory(workspace_id, dir);
    }

    @PostMapping("/{parent_id}")
    public ResponseEntity<HashMap<String, Object>> createSubDirectory(@PathVariable ObjectId parent_id,
                                                                      @RequestBody DirectoryDTO dir) {

        return service.createSubDirectory(parent_id, dir);
    }

    @GetMapping("/{parentId}")
    public Page<DirectoryDTO> getDirectoriesInParents(@PathVariable("parentId") ObjectId parentId,
                                                      @RequestParam(value = "page", defaultValue = "0") int page,
                                                      @RequestParam(value = "size", defaultValue = "10") int size) {
        return service.getDirsByParentId(parentId, page, size);
    }

    @PutMapping("move/{id}")
    public ResponseEntity<String> moveDirectory(@PathVariable("id") ObjectId id,
                                                @RequestBody DirectoryMoveRequest updateRequest) {
        service.MoveDirectory(id, updateRequest);
        return ResponseEntity.ok("Directory updated successfully");
    }

    @PutMapping("rename/{id}")
    public ResponseEntity<String> renameDirectory(@PathVariable("id") ObjectId id,
                                                  @RequestBody DirectoryRenameRequest updateRequest) {
        service.RenameDirectory(id, updateRequest);
        return ResponseEntity.ok("Directory updated successfully");
    }

    @GetMapping("root/{workspaceId}")
    public Page<DirectoryDTO> getDirectoriesInWorkspaces(@PathVariable("workspaceId") ObjectId workspaceId,
                                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                                         @RequestParam(value = "size", defaultValue = "10") int size) {
        return service.getDirsByWorkspaceId(workspaceId, page, size);
    }

    //Delete Endpoints
    @DeleteMapping("/deleteWorkspace/{id}")
    public ResponseEntity<String> deleteWorkspace(@PathVariable("id") ObjectId id) throws IOException {
        service.deleteWorkspace(id);
        return ResponseEntity.ok("Workspace deleted successfully.");
    }

    @DeleteMapping("/deleteDirectory/{id}")
    public ResponseEntity<String> deleteDirectory(@PathVariable("id") ObjectId id) throws IOException {
        service.deleteDirectory(id);
        return ResponseEntity.ok("Workspace deleted successfully.");
    }
}



