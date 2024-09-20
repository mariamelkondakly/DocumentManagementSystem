package com.AtosReady.DocumentManagementSystem.Services;

import com.AtosReady.DocumentManagementSystem.Creators.WorkspaceCreator;
import com.AtosReady.DocumentManagementSystem.DTO.WorkspacesDTO;
import com.AtosReady.DocumentManagementSystem.Exceptions.ResourceExistsException;
import com.AtosReady.DocumentManagementSystem.Exceptions.ResourceNotFoundException;
import com.AtosReady.DocumentManagementSystem.Mappers.WorkspacesMapper;
import com.AtosReady.DocumentManagementSystem.Models.Directories;
import com.AtosReady.DocumentManagementSystem.Models.Workspaces;
import com.AtosReady.DocumentManagementSystem.Repositories.WorkspaceRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
public class WorkspaceService {
    @Autowired
    WorkspaceRepo repo;

    @Autowired
    WorkspacesMapper workspacesMapper;

    @Autowired
    private WorkspaceCreator creator;

    //Basic handling of workspaces

    public boolean doesWorkspaceNameExist(Long userId, String name) {
        return repo.findByUserIdAndNameAndDeletedFalse(userId, name).isPresent();
    }

    public Workspaces getWorkspace(ObjectId id) {
        return repo.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found."));
    }

    //CRUD supporting methods

    public ResponseEntity<HashMap<String, Object>> createNewWorkspace(long userId, String name) {
        if (doesWorkspaceNameExist(userId, name)) {
            throw new ResourceExistsException("workspace already exists");
        }
        Optional<Workspaces> deletedWorkspace = repo.findByUserIdAndNameAndDeletedTrue(userId, name);
        deletedWorkspace.ifPresent(workspaces -> repo.delete(workspaces));

        Workspaces newWorkspace = new Workspaces(userId, name);
        repo.save(newWorkspace);
        return creator.createWorkspaceDirectory(Long.toString(userId),
                newWorkspace.getName(),
                deletedWorkspace.isPresent());
    }


    public Page<WorkspacesDTO> getWorkspacesByUserId(long userId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Workspaces> workspaces = repo.findAllByUserIdAndDeletedFalse(userId, pageable);

        return workspaces.map(workspacesMapper::workspaceWorkspacesDTO);
    }

    //Getting the user's id from the token
    public Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getPrincipal();

    }

}
