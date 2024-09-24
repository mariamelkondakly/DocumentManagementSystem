package com.AtosReady.DocumentManagementSystem.Services;

import com.AtosReady.DocumentManagementSystem.Creators.DirectoryCreator;
import com.AtosReady.DocumentManagementSystem.DTO.DirectoryDTO;
import com.AtosReady.DocumentManagementSystem.DTO.DirectoryMoveRequest;
import com.AtosReady.DocumentManagementSystem.DTO.DirectoryRenameRequest;
import com.AtosReady.DocumentManagementSystem.Exceptions.ResourceExistsException;
import com.AtosReady.DocumentManagementSystem.Exceptions.ResourceNotFoundException;
import com.AtosReady.DocumentManagementSystem.Mappers.DirectoriesMapper;
import com.AtosReady.DocumentManagementSystem.Models.Directories;
import com.AtosReady.DocumentManagementSystem.Models.Documents;
import com.AtosReady.DocumentManagementSystem.Models.Workspaces;
import com.AtosReady.DocumentManagementSystem.Repositories.DirectoriesRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
    private DirectoryCreator directoryCreator;

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
        ObjectId workspaceId= new ObjectId(workspaceIdS);
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
        ObjectId parentId=new ObjectId(parentIds);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Directories parent = repo.findByIdAndUserIdAndDeletedFalse(parentId, getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Directory doesn't exist"));
        Page<Directories> directories = repo.findAllByParentIdAndDeletedFalse(parentId, pageable);
        parent.setLastAccessedAt(new Date());
        repo.save(parent);
        List<Object> result=new ArrayList<>();
        result.add(parent.getName());
        result.add(directories.map(directoriesMapper::directoryToDirectoryDTO));
        return result;
    }

    public List<Object> getDirsByWorkspaceId(String workspaceIds, int pageNumber, int pageSize) {
        ObjectId workspaceId=new ObjectId(workspaceIds);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Workspaces workspaces = workspaceService.repo.findByIdAndDeletedFalse(workspaceId)
                .orElseThrow(() -> new ResourceNotFoundException("Workspace doesn't exist"));

        Page<Directories> directories = repo.findAllByWorkspaceIdAndDeletedFalse(workspaceId, pageable);
        List<Object> result=new ArrayList<>();
        result.add(workspaces.getName());
        result.add(directories.map(directoriesMapper::directoryToDirectoryDTO));
        return result;
    }


    //Putter methods
    public void RenameDirectory(ObjectId id, DirectoryRenameRequest renameRequest) {
        //get the directory to be updated
        Directories existingDirectory = repo.findByIdAndUserIdAndDeletedFalse(id, getUserId()).orElseThrow(() -> new ResourceNotFoundException("Directory not found"));

        //get the path before changes
        String oldPath = existingDirectory.getPath();

        if (renameRequest.isRoot()) {
            if (repo.findByNameAndWorkspaceIdAndDeletedFalse(renameRequest.getName(),
                    existingDirectory.getParentId()).isPresent()) {
                throw new ResourceExistsException("Directory with this name already exists");
            }
            Optional<Directories> deletedDirectory = repo.findByNameAndWorkspaceIdAndDeletedTrue(renameRequest.getName(),
                    existingDirectory.getParentId());
            if (deletedDirectory.isPresent()) {
                directoryCreator.deletePermanently(deletedDirectory.isPresent(), deletedDirectory.map(Directories::getPath)
                        .orElse(null));
            }
        } else {
            if (repo.findByNameAndParentIdAndDeletedFalse(renameRequest.getName(), existingDirectory.getParentId()).isPresent()) {
                throw new ResourceExistsException("Directory with this name already exists");
            }
            Optional<Directories> deletedDirectory = repo.findByNameAndParentIdAndDeletedTrue(renameRequest.getName(), existingDirectory.getParentId());
            if (deletedDirectory.isPresent()) {
                directoryCreator.deletePermanently(deletedDirectory.isPresent(), deletedDirectory.map(Directories::getPath).orElse(null));
            }
        }

        if (!existingDirectory.getName().equals(renameRequest.getName())) {

            existingDirectory.setName(renameRequest.getName());
            if (renameRequest.isRoot()) {
                Workspaces destinationWorkspace = workspaceService.repo.findByIdAndDeletedFalse(existingDirectory.getWorkspaceId())
                        .orElseThrow(() -> new ResourceNotFoundException("Workspace not found"));
                existingDirectory.setPath(directoryCreator.rootPathSetter(existingDirectory, destinationWorkspace));
                if (!existingDirectory.getChildrenIds().isEmpty()) {
                    recursivelyUpdatePathOfRootChildren((HashSet<ObjectId>) existingDirectory.getChildrenIds(), existingDirectory);
                }
            } else {
                Directories destinationParent = repo.findByIdAndUserIdAndDeletedFalse(existingDirectory.getParentId(), getUserId())
                        .orElseThrow(() -> new ResourceNotFoundException("directory not found"));
                recursivelyUpdateSubPath(existingDirectory, destinationParent);
            }
            directoryCreator.renameDirectory(oldPath, existingDirectory.getPath());

            for (Map.Entry<String, Documents> entry : existingDirectory.getDocuments().entrySet()) {
                entry.getValue().setPath(existingDirectory.getPath() +"\\"+ entry.getValue().getName());
            }

            repo.save(existingDirectory);

        }

    }

    public void MoveDirectory(String idS, DirectoryMoveRequest updateRequest) {
        ObjectId id=new ObjectId(idS);
        //get the directory to be updated
        Directories existingDirectory = repo.findByIdAndUserIdAndDeletedFalse(id, getUserId()).orElseThrow(
                () -> new ResourceNotFoundException("Directory not found, exception was raised in the moveDirectory" +
                        " method while extracting existingDirectory"));

        //get the path before changes
        String oldPath = existingDirectory.getPath();

        //check if the request is about moving the directory
        //if (!existingDirectory.getParentId().equals(updateRequest.getParentId())) {

            //check if the directory was originally a subdirectory
            if (!updateRequest.isOriginalRoot()) {

                //get the source parent
                Directories sourceParent = repo.findByIdAndUserIdAndDeletedFalse(existingDirectory.getParentId(), getUserId())
                        .orElseThrow(() -> new ResourceNotFoundException("Directory not found, exception was raised" +
                                "in the moveDirectory method while extracting sourceParent directory"));

                //check if the subdirectory is becoming root
                if (updateRequest.isRoot()) {

                    //get the new container workspace
                    Workspaces destinationWorkspace = workspaceService.repo.findByIdAndDeletedFalse(updateRequest.getParentId())
                            .orElseThrow(() -> new ResourceNotFoundException("Directory not found, exception was raised" +
                                    "in the moveDirectory method while extracting destinationWorkspace"));

                    //setting a new path,
                    // check if a deleted directory exists with the same name,
                    // if so delete it from database,
                    // then move directory and delete it from folder
                    existingDirectory.setPath(directoryCreator.rootPathSetter(existingDirectory, destinationWorkspace));
                    if (!existingDirectory.getChildrenIds().isEmpty()) {
                        recursivelyUpdatePathOfRootChildren((HashSet<ObjectId>) existingDirectory.getChildrenIds(), existingDirectory);
                    }
                    Optional<Directories> deletedDirectory = repo.findByNameAndWorkspaceIdAndDeletedTrue
                            (existingDirectory.getName(), destinationWorkspace.getId());

                    deletedDirectory.ifPresent(directory -> {
                        destinationWorkspace.getDirIds().remove(directory.getId());
                        repo.delete(directory);
                    });
                    directoryCreator.moveDirectory(oldPath, existingDirectory.getPath(), deletedDirectory.isPresent());

                    //update the ids' list
                    sourceParent.getChildrenIds().remove(id);
                    destinationWorkspace.getDirIds().add(id);

                    repo.save(sourceParent);
                    workspaceService.repo.save(destinationWorkspace);

                    existingDirectory.setWorkspaceId(updateRequest.getParentId());
                    existingDirectory.setParentId(null);
                } else {//the subdirectory is moving from a parent directory to another

                    //get destination directory
                    Directories destinationParent = repo.findByIdAndUserIdAndDeletedFalse(updateRequest.getParentId(), getUserId())
                            .orElseThrow(() -> new ResourceNotFoundException("Directory not found, exception was raised" +
                                    "in the moveDirectory method while extracting destinationParent directory"));

                    recursivelyUpdateSubPath(existingDirectory, destinationParent);
                    Optional<Directories> deletedDirectory = repo.findByNameAndParentIdAndDeletedTrue
                            (existingDirectory.getName(), destinationParent.getId());
                    deletedDirectory.ifPresent(directory -> {
                        destinationParent.getChildrenIds().remove(directory.getId());
                        repo.delete(directory);
                    });
                    directoryCreator.moveDirectory(oldPath, existingDirectory.getPath(), deletedDirectory.isPresent());

                    sourceParent.getChildrenIds().remove(id);
                    destinationParent.getChildrenIds().add(id);

                    repo.save(sourceParent);
                    repo.save(destinationParent);

                    existingDirectory.setParentId(updateRequest.getParentId());
                    existingDirectory.setWorkspaceId(null);
                }
            } else {//the request is about moving from a workspace

                //get the source workspace
                Workspaces sourceWorkspace = workspaceService.repo.findById(existingDirectory.getWorkspaceId()).orElseThrow(() -> new ResourceNotFoundException("Workspace not found"));

                if (updateRequest.isRoot()) {
                    Workspaces destinationWorkspace = workspaceService.repo.
                            findByIdAndDeletedFalse(updateRequest.getParentId())
                            .orElseThrow(() -> new ResourceNotFoundException("Workspace not found, exception was raised" +
                                    "in the moveDirectory method while extracting destinationWorkspace"));

                    existingDirectory.setPath(directoryCreator.rootPathSetter(existingDirectory, destinationWorkspace));
                    if (!existingDirectory.getChildrenIds().isEmpty()) {
                        recursivelyUpdatePathOfRootChildren((HashSet<ObjectId>) existingDirectory.getChildrenIds(), existingDirectory);
                    }
                    Optional<Directories> deletedDirectory = repo.findByNameAndWorkspaceIdAndDeletedTrue(existingDirectory.getName(), destinationWorkspace.getId());
                    deletedDirectory.ifPresent(directory -> {
                        destinationWorkspace.getDirIds().remove(directory.getId());
                        repo.delete(directory);
                    });
                    directoryCreator.moveDirectory(oldPath, existingDirectory.getPath(), deletedDirectory.isPresent());

                    sourceWorkspace.getDirIds().remove(id);
                    destinationWorkspace.getDirIds().add(id);

                    workspaceService.repo.save(sourceWorkspace);
                    workspaceService.repo.save(destinationWorkspace);

                    existingDirectory.setWorkspaceId(updateRequest.getParentId());
                    existingDirectory.setParentId(null);

                } else {
                    Directories destinationParent = repo.findByIdAndUserIdAndDeletedFalse(updateRequest.getParentId(), getUserId())
                            .orElseThrow(() -> new ResourceNotFoundException("Directory not found, exception was raised" +
                                    "in the moveDirectory method while extracting destinationParent"));
                    recursivelyUpdateSubPath(existingDirectory, destinationParent);
                    Optional<Directories> deletedDirectory = repo.findByNameAndParentIdAndDeletedTrue(existingDirectory.getName(), destinationParent.getId());
                    deletedDirectory.ifPresent(directory -> {
                        destinationParent.getChildrenIds().remove(directory.getId());
                        repo.delete(directory);
                    });
                    directoryCreator.moveDirectory(oldPath, existingDirectory.getPath(), deletedDirectory.isPresent());

                    sourceWorkspace.getDirIds().remove(id);
                    destinationParent.getChildrenIds().add(id);

                    workspaceService.repo.save(sourceWorkspace);
                    repo.save(destinationParent);

                    existingDirectory.setParentId(updateRequest.getParentId());
                    existingDirectory.setWorkspaceId(null);
                }

            }
            for (Map.Entry<String, Documents> entry : existingDirectory.getDocuments().entrySet()) {
                entry.getValue().setPath(existingDirectory.getPath() +"\\"+ entry.getValue().getName());
            }
        //}
        existingDirectory.setLastAccessedAt(new Date());
        repo.save(existingDirectory);
    }

    //Deleting methods
    public void deleteDirectory(ObjectId directoryId) throws IOException {
        Directories directory = repo.findByIdAndUserIdAndDeletedFalse(directoryId, getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Directory not found with ID: " + directoryId
                        + "exception was raised in the deleteDirectory method while extracting the directory"));

        directory.setDeleted(true);

        if(!directory.getDocuments().isEmpty()){
            for (Map.Entry<String, Documents> entry : directory.getDocuments().entrySet()) {
                Documents document = entry.getValue();
                document.setDeleted(true);
            }
        }

        if (directory.getChildrenIds() != null) {
            for (ObjectId childId : directory.getChildrenIds()) {
                deleteDirectory(childId);
            }
        }
        repo.save(directory);
    }

    public void deleteWorkspace(ObjectId id) throws IOException {
        Workspaces workspaces = workspaceService.repo.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found. " +
                        "Exception was raised in the deleteWorkspace method while extracting the workspace"));

        workspaces.setDeleted(true);
        for (ObjectId directoryId : workspaces.getDirIds()) {
            deleteDirectory(directoryId);
        }
        workspaceService.repo.save(workspaces);

    }

    //updating utilities
    @Transactional
    public void recursivelyUpdateSubPath(Directories existingDirectory, Directories destinationParent) {
        existingDirectory.setPath(directoryCreator.pathSetter(existingDirectory, destinationParent));
        repo.save(existingDirectory);
        if (!existingDirectory.getChildrenIds().isEmpty()) {
            for (ObjectId childId : existingDirectory.getChildrenIds()) {
                Directories child = repo.findByIdAndUserIdAndDeletedFalse(childId, getUserId())
                        .orElseThrow(
                                () -> new ResourceNotFoundException("Directory with id: " + childId + "was not found." +
                                        " Exception was thrown in recursivelyUpdatePath"));
                recursivelyUpdateSubPath(child, existingDirectory);
            }
        }
    }

    public void recursivelyUpdatePathOfRootChildren(HashSet<ObjectId> children, Directories destinationParent) {
        for (ObjectId childId : children) {
            Directories child = repo.findByIdAndUserIdAndDeletedFalse(childId, getUserId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Directory with id: " + childId + "was not found." +
                                    " Exception was thrown in recursivelyUpdatePathOfRootChildren"));
            recursivelyUpdateSubPath(child, destinationParent);
        }
    }

    public Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getPrincipal();

    }


}

