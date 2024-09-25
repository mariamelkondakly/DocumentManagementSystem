package com.AtosReady.DocumentManagementSystem.Services;

import com.AtosReady.DocumentManagementSystem.Creators.DirectoryCreator;
import com.AtosReady.DocumentManagementSystem.Exceptions.ResourceExistsException;
import com.AtosReady.DocumentManagementSystem.Exceptions.ResourceNotFoundException;
import com.AtosReady.DocumentManagementSystem.Mappers.DirectoriesMapper;
import com.AtosReady.DocumentManagementSystem.Models.Directories;
import com.AtosReady.DocumentManagementSystem.Models.Workspaces;
import com.AtosReady.DocumentManagementSystem.Repositories.DirectoriesRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class DirectoriesService {

    @Autowired
    protected DirectoriesRepo repo;

    @Autowired
    protected WorkspaceService workspaceService;

    @Autowired
    protected DirectoriesMapper directoriesMapper;

    @Autowired
    DirectoryCreator directoryCreator;

    //Poster methods
    public ResponseEntity<HashMap<String, Object>> createSubDirectory(ObjectId parentID, String dirName) {

        if (repo.findByNameAndParentIdAndDeletedFalse(dirName, parentID).isPresent()) {
            throw new ResourceExistsException("Directory with this name already exists. " +
                    "Exception was thrown before creating the new user in the createSubDirectory Method");
        }
        Directories parent = repo.findById(parentID)
                .orElseThrow(() -> new ResourceNotFoundException("parent directory doesn't exist." +
                        "Exception was thrown while trying to get the new parent in the createSubDirectory Method"));

        Optional<Directories> deletedDirectory = repo.findByNameAndParentIdAndDeletedTrue(dirName, parentID);
        deletedDirectory.ifPresent(directory -> repo.delete(directory));

        Directories newDir = new Directories(workspaceService.getUserId(), null, parentID, dirName);

        repo.save(newDir);
        newDir.setPath(directoryCreator.pathSetter(newDir, parent));
        repo.save(newDir);
        parent.getChildrenIds().add(newDir.getId());
        repo.save(parent);

        return directoryCreator.createDirectory(newDir.getPath(), deletedDirectory.isPresent());

    }

    public ResponseEntity<HashMap<String, Object>> createRootDirectory(String workspaceIdS, String dirName) {
        ObjectId workspaceId = new ObjectId(workspaceIdS);
        workspaceService.repo.findById(workspaceId).orElseThrow(() -> new ResourceNotFoundException("Workspace doesn't exist"));
        if (repo.findByNameAndWorkspaceIdAndDeletedFalse(dirName, workspaceId).isPresent()) {
            throw new ResourceExistsException("Directory with this name already exists");
        }
        Workspaces workspace = workspaceService.repo.findById(workspaceId).
                orElseThrow(() -> new ResourceNotFoundException("Workspace doesn't exist."));
        Optional<Directories> deletedDirectory = repo.findByNameAndWorkspaceIdAndDeletedTrue(dirName, workspaceId);
        deletedDirectory.ifPresent(directory -> repo.delete(directory));

        Directories newDir = new Directories();
        newDir.setUserId(workspaceService.getUserId());
        newDir.setWorkspaceId(workspaceId);
        newDir.setName(dirName);
        repo.save(newDir);
        newDir.setPath(directoryCreator.rootPathSetter(newDir, workspace));
        repo.save(newDir);
        workspace.getDirIds().add(newDir.getId());
        workspaceService.repo.save(workspace);
        return directoryCreator.createDirectory(newDir.getPath(), deletedDirectory.isPresent());

    }

    //Getter methods
    public List<Object> getDirsByParentId(String parentIds, int pageNumber, int pageSize) {
        ObjectId parentId = new ObjectId(parentIds);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Directories parent = repo.findByIdAndUserIdAndDeletedFalse(parentId, getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Directory doesn't exist"));
        Page<Directories> directories = repo.findAllByParentIdAndDeletedFalse(parentId, pageable);
        parent.setLastAccessedAt(new Date());
        repo.save(parent);
        List<Object> result = new ArrayList<>();
        result.add(parent.getName());
        result.add(directories.map(directoriesMapper::directoryToDirectoryDTO));
        return result;
    }

    public List<Object> getDirsByWorkspaceId(String workspaceIds, int pageNumber, int pageSize) {
        ObjectId workspaceId = new ObjectId(workspaceIds);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Workspaces workspaces = workspaceService.repo.findByIdAndDeletedFalse(workspaceId)
                .orElseThrow(() -> new ResourceNotFoundException("Workspace doesn't exist"));

        Page<Directories> directories = repo.findAllByWorkspaceIdAndDeletedFalse(workspaceId, pageable);
        List<Object> result = new ArrayList<>();
        result.add(workspaces.getName());
        result.add(directories.map(directoriesMapper::directoryToDirectoryDTO));
        return result;
    }

    public Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getPrincipal();

    }


}

