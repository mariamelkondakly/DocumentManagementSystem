package com.AtosReady.DocumentManagementSystem.Services;

import com.AtosReady.DocumentManagementSystem.DTO.DirectoryMoveRequest;
import com.AtosReady.DocumentManagementSystem.DTO.DirectoryRenameRequest;
import com.AtosReady.DocumentManagementSystem.Exceptions.ResourceExistsException;
import com.AtosReady.DocumentManagementSystem.Exceptions.ResourceNotFoundException;
import com.AtosReady.DocumentManagementSystem.Models.Directories;
import com.AtosReady.DocumentManagementSystem.Models.Documents;
import com.AtosReady.DocumentManagementSystem.Models.Workspaces;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class DirectoryDocumentService {

    @Autowired
    DocumentService docService;

    //Putter methods

    //renaming methods
    private void dealWithDirectoryWihRepeatedName(Boolean isRoot, String name, ObjectId parentId) {
        Optional<Directories> existingDirectory;

        if (isRoot) {
            Workspaces workspace = docService.directoriesService.workspaceService.repo.findByIdAndDeletedFalse(parentId)
                    .orElseThrow(() -> new ResourceNotFoundException("workspace not found" +
                            "exception was thrown in the dealing with deleted duplicates function"));
            existingDirectory = docService.directoriesService.repo.findByNameAndWorkspaceId(name, parentId);
            if (existingDirectory.isPresent()) {
                if (existingDirectory.get().isDeleted()) {
                    docService.directoriesService.directoryCreator.deletePermanently(true, existingDirectory.get().getPath());
                    workspace.getDirIds().remove(existingDirectory.get().getId());
                    docService.directoriesService.repo.delete(existingDirectory.get());
                } else {
                    throw new ResourceExistsException("Directory with this name already exists");
                }
            }
        } else {
            Directories parentDir = docService.directoriesService.repo.findByIdAndUserIdAndDeletedFalse(parentId, docService.directoriesService.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("directory not found"));
            existingDirectory = docService.directoriesService.repo.findByNameAndParentId(name, parentId);
            if (existingDirectory.isPresent()) {
                if (existingDirectory.get().isDeleted()) {
                    docService.directoriesService.directoryCreator.deletePermanently(true, existingDirectory.get().getPath());
                    parentDir.getChildrenIds().remove(existingDirectory.get().getId());
                    docService.directoriesService.repo.delete(existingDirectory.get());
                } else {
                    throw new ResourceExistsException("Directory with this name already exists");
                }
            }
        }

    }

    public void RenameDirectory(String ids, DirectoryRenameRequest renameRequest) {
        //get the directory to be updated
        ObjectId id = new ObjectId(ids);
        Directories directory = docService.directoriesService.repo
                .findByIdAndUserIdAndDeletedFalse(id, docService.directoriesService.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Directory not found"));

        dealWithDirectoryWihRepeatedName(renameRequest.isRoot(), renameRequest.getName(), directory.getParentId());

        //get the path before changes
        String oldPath = directory.getPath();

        if (!directory.getName().equals(renameRequest.getName())) {

            directory.setName(renameRequest.getName());
            if (renameRequest.isRoot()) {
                Workspaces parentWorkspace = docService.directoriesService.workspaceService.repo.findByIdAndDeletedFalse(directory.getWorkspaceId())
                        .orElseThrow(() -> new ResourceNotFoundException("Workspace not found"));
                directory.setPath(docService.directoriesService.directoryCreator.rootPathSetter(directory, parentWorkspace));
                if (!directory.getChildrenIds().isEmpty()) {
                    recursivelyUpdatePathOfRootChildren((HashSet<ObjectId>) directory.getChildrenIds(), directory);
                }
            } else {
                Directories Parent = docService.directoriesService
                        .repo.findByIdAndUserIdAndDeletedFalse(directory.getParentId(), docService.directoriesService.getUserId())
                        .orElseThrow(() -> new ResourceNotFoundException("directory not found"));
                recursivelyUpdateSubPath(directory, Parent);
            }
            docService.directoriesService.directoryCreator.renameDirectory(oldPath, directory.getPath());
            if (!directory.getDocumentIds().isEmpty()) {
                List<Documents> documents = docService.repo.findAllByParentIdAndUserIdAndDeletedFalse(directory.getId(), docService.directoriesService.getUserId());
                for (Documents document : documents) {
                    document.setPath(directory.getPath() + "\\" + document.getName());
                    docService.repo.save(document);
                }
            }
            docService.directoriesService.repo.save(directory);

        }

    }

    //move functions

    public void MoveDirectory(String idS, DirectoryMoveRequest moveRequest) {
        ObjectId id = new ObjectId(idS);

        Directories movedDirectory = docService.directoriesService.repo.findByIdAndUserIdAndDeletedFalse(id, docService.directoriesService.getUserId()).orElseThrow(
                () -> new ResourceNotFoundException("Directory not found, exception was raised in the moveDirectory" +
                        " method while extracting movedDirectory"));

        String oldPath = movedDirectory.getPath();

        if (moveRequest.isOriginalRoot()) { //movedDirectory's parent is a workspace

            Workspaces sourceWorkspace = docService.directoriesService.workspaceService.repo.findById(movedDirectory.getWorkspaceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Workspace not found, exception was thrown " +
                            "while extracting the sourceWorkspace"));

            if (moveRequest.isRoot()) { //movedDirectory's parent will still be a workspace
                moveToRoot(moveRequest.getParentId(), movedDirectory, oldPath);
            } else {
                moveToSub(moveRequest.getParentId(), movedDirectory, oldPath);
            }

            sourceWorkspace.getDirIds().remove(id);
            docService.directoriesService.workspaceService.repo.save(sourceWorkspace);

        }
        else { //movedDirectory's parent is another directory

            Directories sourceParent = docService.directoriesService.
                    repo.findByIdAndUserIdAndDeletedFalse(movedDirectory.getParentId(), docService.directoriesService.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Directory not found, exception was raised" +
                            "in the moveDirectory method while extracting sourceParent directory"));

            if (moveRequest.isRoot()) {
                moveToRoot(moveRequest.getParentId(), movedDirectory, oldPath);
            } else {//the subdirectory is moving from a parent directory to another
                moveToSub(moveRequest.getParentId(), movedDirectory, oldPath);
            }

            sourceParent.getChildrenIds().remove(id);
            docService.directoriesService.repo.save(sourceParent);
        }

        if (!movedDirectory.getDocumentIds().isEmpty()) {
            List<Documents> documents = docService.repo.findAllByParentIdAndUserIdAndDeletedFalse(movedDirectory.getId(), docService.directoriesService.getUserId());
            for (Documents document : documents) {
                document.setPath(movedDirectory.getPath() + "\\" + document.getName());
                docService.repo.save(document);
            }
        }
        movedDirectory.setLastAccessedAt(new Date());
        docService.directoriesService.repo.save(movedDirectory);
    }

    public void moveToRoot(ObjectId parentId, Directories movedDirectory, String oldPath) {
        //get the new container workspace
        Workspaces destinationWorkspace = docService.directoriesService.workspaceService.repo.findByIdAndDeletedFalse(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("workspace not found, exception was raised" +
                        "in the moveDirectory method while extracting destinationWorkspace"));

        dealWithDirectoryWihRepeatedName(true, movedDirectory.getName(), parentId);

        movedDirectory.setPath(docService.directoriesService.directoryCreator.rootPathSetter(movedDirectory, destinationWorkspace));
        if (!movedDirectory.getChildrenIds().isEmpty()) {
            recursivelyUpdatePathOfRootChildren((HashSet<ObjectId>) movedDirectory.getChildrenIds(), movedDirectory);
        }

        docService.directoriesService.directoryCreator.moveDirectory(oldPath, movedDirectory.getPath());

        //update the ids' list

        destinationWorkspace.getDirIds().add(movedDirectory.getId());

        docService.directoriesService.workspaceService.repo.save(destinationWorkspace);

        movedDirectory.setWorkspaceId(parentId);
        movedDirectory.setParentId(null);
    }

    public void moveToSub(ObjectId parentId, Directories movedDirectory, String oldPath) {
        Directories destinationParent = docService.directoriesService.repo.findByIdAndUserIdAndDeletedFalse(parentId, docService.directoriesService.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Directory not found, exception was raised" +
                        "in the moveDirectory method while extracting destinationParent directory"));

        dealWithDirectoryWihRepeatedName(false, movedDirectory.getName(), parentId);

        recursivelyUpdateSubPath(movedDirectory, destinationParent);

        docService.directoriesService.directoryCreator.moveDirectory(oldPath, movedDirectory.getPath());

        destinationParent.getChildrenIds().add(movedDirectory.getId());


        docService.directoriesService.repo.save(destinationParent);

        movedDirectory.setParentId(parentId);
        movedDirectory.setWorkspaceId(null);
    }

    //Deleting methods

    public void deleteDirectory(ObjectId directoryId) throws IOException {
        Directories directory = docService.directoriesService.repo.findByIdAndUserIdAndDeletedFalse(directoryId, docService.directoriesService.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Directory not found with ID: " + directoryId
                        + "exception was raised in the deleteDirectory method while extracting the directory"));

        directory.setDeleted(true);

        if (!directory.getDocumentIds().isEmpty()) {
            List<Documents> documents = docService.repo.findAllByParentIdAndUserIdAndDeletedFalse(directory.getId(), docService.directoriesService.getUserId());
            for (Documents document : documents) {
                document.setDeleted(true);
                docService.repo.save(document);
            }
        }

        if (!directory.getChildrenIds().isEmpty()) {
            for (ObjectId childId : directory.getChildrenIds()) {
                deleteDirectory(childId);
            }
        }
        docService.directoriesService.repo.save(directory);
    }

    public void deleteWorkspace(String ids) throws IOException {
        ObjectId id = new ObjectId(ids);
        Workspaces workspaces = docService.directoriesService.workspaceService.repo.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found. " +
                        "Exception was raised in the deleteWorkspace method while extracting the workspace"));

        workspaces.setDeleted(true);
        docService.directoriesService.workspaceService.repo.save(workspaces);

        for (ObjectId directoryId : workspaces.getDirIds()) {
            deleteDirectory(directoryId);
        }

    }


    //updating utilities
    @Transactional
    public void recursivelyUpdateSubPath(Directories existingDirectory, Directories destinationParent) {
        existingDirectory.setPath(docService.directoriesService.directoryCreator.pathSetter(existingDirectory, destinationParent));
        docService.directoriesService.repo.save(existingDirectory);
        if (!existingDirectory.getChildrenIds().isEmpty()) {
            for (ObjectId childId : existingDirectory.getChildrenIds()) {
                Directories child = docService.directoriesService.repo.findByIdAndUserIdAndDeletedFalse(childId, docService.directoriesService.getUserId())
                        .orElseThrow(
                                () -> new ResourceNotFoundException("Directory with id: " + childId + "was not found." +
                                        " Exception was thrown in recursivelyUpdatePath"));
                recursivelyUpdateSubPath(child, existingDirectory);
            }
        }
    }

    public void recursivelyUpdatePathOfRootChildren(HashSet<ObjectId> children, Directories destinationParent) {
        for (ObjectId childId : children) {
            Directories child = docService.directoriesService.repo.findByIdAndUserIdAndDeletedFalse(childId, docService.directoriesService.getUserId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Directory with id: " + childId + "was not found." +
                                    " Exception was thrown in recursivelyUpdatePathOfRootChildren"));
            recursivelyUpdateSubPath(child, destinationParent);
        }
    }

    //restoring methods
    public void restoreDirectory(ObjectId directoryId) throws IOException {
        Directories directory = docService.directoriesService.repo.findByIdAndUserIdAndDeletedTrue(directoryId, docService.directoriesService.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Directory not found with ID: " + directoryId
                        + "exception was raised in the deleteDirectory method while extracting the directory"));

        directory.setDeleted(false);

        if (!directory.getDocumentIds().isEmpty()) {
            List<Documents> documents = docService.repo.findAllByParentIdAndUserIdAndDeletedFalse(directory.getId(), docService.directoriesService.getUserId());
            for (Documents document : documents) {
                document.setDeleted(false);
                docService.repo.save(document);
            }
        }

        if (!directory.getChildrenIds().isEmpty()) {
            for (ObjectId childId : directory.getChildrenIds()) {
                restoreDirectory(childId);
            }
        }
        docService.directoriesService.repo.save(directory);
    }

    public void restoreWorkspace(String ids) throws IOException {
        ObjectId id = new ObjectId(ids);
        Workspaces workspaces = docService.directoriesService.workspaceService.repo.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found. " +
                        "Exception was raised in the deleteWorkspace method while extracting the workspace"));

        workspaces.setDeleted(false);
        docService.directoriesService.workspaceService.repo.save(workspaces);

        for (ObjectId directoryId : workspaces.getDirIds()) {
            deleteDirectory(directoryId);
        }

    }
}
