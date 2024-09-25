package com.AtosReady.DocumentManagementSystem.Controllers;

import com.AtosReady.DocumentManagementSystem.DTO.WorkspacesDTO;
import com.AtosReady.DocumentManagementSystem.Services.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/workspaces")
public class WorkspaceController {

    @Autowired
    private WorkspaceService workspaceService;

    @PostMapping("/createWorkspace/{name}")
    public ResponseEntity<HashMap<String, Object>> AddWorkspace(@PathVariable("name") String name) {
        return workspaceService.createNewWorkspace(name);
    }


    @GetMapping
    public Page<WorkspacesDTO> getWorkspaces(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return workspaceService.getWorkspacesByUserId(workspaceService.getUserId(), page, size);
    }


}
