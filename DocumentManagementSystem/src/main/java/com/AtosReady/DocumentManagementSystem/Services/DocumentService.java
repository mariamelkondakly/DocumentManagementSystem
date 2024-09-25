package com.AtosReady.DocumentManagementSystem.Services;

import com.AtosReady.DocumentManagementSystem.Creators.DocumentCreator;
import com.AtosReady.DocumentManagementSystem.DTO.DirectoryDTO;
import com.AtosReady.DocumentManagementSystem.DTO.DocumentMoveRequest;
import com.AtosReady.DocumentManagementSystem.DTO.SearchResults;
import com.AtosReady.DocumentManagementSystem.DTO.WorkspacesDTO;
import com.AtosReady.DocumentManagementSystem.Exceptions.EmptyFileException;
import com.AtosReady.DocumentManagementSystem.Exceptions.InvalidSignatureException;
import com.AtosReady.DocumentManagementSystem.Exceptions.ResourceExistsException;
import com.AtosReady.DocumentManagementSystem.Exceptions.ResourceNotFoundException;
import com.AtosReady.DocumentManagementSystem.Models.Directories;
import com.AtosReady.DocumentManagementSystem.Models.Documents;
import com.AtosReady.DocumentManagementSystem.Models.Workspaces;
import com.AtosReady.DocumentManagementSystem.Repositories.DocumentsRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
public class DocumentService {

    @Autowired
    protected DirectoriesService directoriesService;

    @Autowired
    protected DocumentsRepo repo;

    @Autowired
    private DocumentCreator creator;

    //DOCUMENT CREATION WHEN UPLOADING
    public ResponseEntity<String> createDocument(String directoryIds, MultipartFile file) throws IOException {
        ObjectId directoryId = new ObjectId(directoryIds);
        if (file.isEmpty()) {
            throw new EmptyFileException("No file sent, file cannot be empty, exception was raised in the createDocument" +
                    "method of the Document service.");
        }
        boolean willReplace;

        Directories parentDirectory =
                directoriesService.repo.findByIdAndUserIdAndDeletedFalse(directoryId, directoriesService.getUserId()).
                        orElseThrow(() -> new ResourceNotFoundException("Directory was not found, exception was thrown in" +
                                "while trying to extract the parent directory in the createDocument method of the " +
                                "DocumentService class"));

        String sanitizedFileName = Objects.requireNonNull(file.getOriginalFilename()).replace(".", "_");

        Optional<Documents> document = repo.findByNameAndParentIdAndUserId(sanitizedFileName, directoriesService.getUserId(), directoryId);
        if (document.isPresent()) {
            if (document.get().isDeleted()) {
                willReplace = true;
                parentDirectory.getDocumentIds().remove(document.get().getId());
                repo.delete(document.get());
            } else {
                throw new ResourceExistsException("A document with the same name and type already exists.exception was thrown in" +
                        "while trying to extract the parent directory in the createDocument method of the DocumentService class");

            }
        } else {
            willReplace = false;
        }

        Documents newDocument = new Documents();
        newDocument.setName(sanitizedFileName);
        newDocument.setPath(parentDirectory.getPath() + "\\" + newDocument.getName());
        newDocument.setType(file.getContentType());
        newDocument.setSize(file.getSize());
        newDocument.setParentId(directoryId);
        newDocument.setUserId(directoriesService.getUserId());
        repo.save(newDocument);
        parentDirectory.getDocumentIds().add(newDocument.getId());
        directoriesService.repo.save(parentDirectory);
        return creator.CreateNewFile(file, parentDirectory.getPath(), willReplace);
    }

    //DOCUMENT RENAME
    public ResponseEntity<String> renameDocument(String documentIds, String oldName, String newName) throws IOException {

        ObjectId documentId = new ObjectId(documentIds);

        if (newName.isEmpty()) {
            throw new InvalidSignatureException("Enter a new name for your file");
        }
        if (oldName.equals(newName)) {
            ResponseEntity.ok("No visible update happened, you entered the same name.");
        }

        Documents document = repo.findByUserIdAndIdAndDeletedFalse(directoriesService.getUserId(), documentId)
                .orElseThrow(() -> new ResourceNotFoundException("couldn't find file, " +
                        "exception was raised in the renameDocument method in the DocumentService class"));

        Directories parentDirectory = directoriesService.repo.findByIdAndUserIdAndDeletedFalse(document.getParentId(), directoriesService.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("couldn't find file's parent directory, " +
                        "exception was raised in the renameDocument method in the DocumentService class"));

        String sanitizedOldName = Objects.requireNonNull(oldName).replace(".", "_");
        String sanitizedNewName = Objects.requireNonNull(newName).replace(".", "_");

        Optional<Documents> existingDocument = repo.findByNameAndParentIdAndUserId(sanitizedNewName, directoriesService.getUserId(), document.getParentId());
        boolean deletedPresent;

        if (existingDocument.isPresent()) {

            if (existingDocument.get().isDeleted()) {
                parentDirectory.getDocumentIds().remove(existingDocument.get().getId());
                directoriesService.repo.save(parentDirectory);
                deletedPresent = true;
                repo.delete(existingDocument.get());
            } else {
                throw new ResourceExistsException("A file with this new name already exists in the directory.");
            }

        } else {
            deletedPresent = false;
        }
        if (getFileExtension(sanitizedOldName).equals(getFileExtension(sanitizedNewName))) {
            document.setName(sanitizedNewName);
            document.setPath(parentDirectory.getPath() + "\\" + sanitizedNewName);
        } else {
            throw new InvalidSignatureException("Extension cannot be changed for the same file");
        }
        document.setLastAccessedAt(new Date());
        repo.save(document);
        String oldPath = parentDirectory.getPath() + "\\" + oldName;
        String newPath = parentDirectory.getPath() + "\\" + newName;

        return creator.renameFile(deletedPresent, oldPath, newPath);
    }

    private String getFileExtension(String fileName) {
        int Index = fileName.lastIndexOf('_'); //the index right before the extension
        if (Index == -1)
            return "";
        else
            return fileName.substring(Index + 1);
    }

    public ResponseEntity<String> moveDocument(DocumentMoveRequest documentMoveRequest) throws IOException {

        Documents document = repo.findByUserIdAndIdAndDeletedFalse(directoriesService.getUserId(), documentMoveRequest.getDocumentId())
                .orElseThrow(() -> new ResourceNotFoundException("The file cannot be found," +
                        " exception was thrown in the moveDocument method in the DocumentService class"));

        Directories oldParentDirectory = directoriesService.repo
                .findByIdAndUserIdAndDeletedFalse(document.getParentId(), directoriesService.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("The directory where the file supposedly is cannot be found," +
                        " exception was thrown in the moveDocument method in the DocumentService class"));

        Directories newParentDirectory = directoriesService.repo
                .findByIdAndUserIdAndDeletedFalse(documentMoveRequest.getNewParentId(), directoriesService.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("The directory where the file should be moved cannot be found," +
                        " exception was thrown in the moveDocument method in the DocumentService class"));

        boolean deletedPresent;

        Optional<Documents> existingDocument = repo.findByNameAndParentIdAndUserId(document.getName(),
                directoriesService.getUserId(), documentMoveRequest.getNewParentId());

        if (existingDocument.isPresent()) {
            if (existingDocument.get().isDeleted()) {
                newParentDirectory.getDocumentIds().remove(existingDocument.get().getId());
                repo.delete(existingDocument.get());
                deletedPresent = true;
            } else {
                throw new ResourceExistsException("A file with the same name already exists in the destination folder" +
                        " exception was thrown in the moveDocument method in the DocumentService class");
            }
        } else {
            deletedPresent = false;
        }

        newParentDirectory.getDocumentIds().add(document.getId());
        oldParentDirectory.getDocumentIds().remove(document.getId());
        document.setParentId(newParentDirectory.getId());
        document.setPath(newParentDirectory.getPath() + "\\" + document.getName());
        document.setLastAccessedAt(new Date());
        repo.save(document);
        directoriesService.repo.save(newParentDirectory);
        directoriesService.repo.save(oldParentDirectory);


        return creator.moveFile(deletedPresent, oldParentDirectory.getPath() + "\\" + document.getName().replace("_", "."),
                newParentDirectory.getPath() + "\\" + document.getName().replace("_", "."));

    }

    public ResponseEntity<String> deleteDocument(String documentIds) throws IOException {
        ObjectId documentId = new ObjectId(documentIds);
        Documents document = repo.findByUserIdAndIdAndDeletedFalse(directoriesService.getUserId(), documentId)
                .orElseThrow(() -> new ResourceNotFoundException("file not found" +
                        "exception was thrown in the deleteDocument method in the DocumentService class"));

        document.setDeleted(true);
        return ResponseEntity.ok("file successfully deleted");
    }

    public ResponseEntity<Resource> previewAndDownloadDocument(String documentIds, String headerType) throws IOException {
        ObjectId documentId = new ObjectId(documentIds);
        Documents document = repo.findByUserIdAndIdAndDeletedFalse(directoriesService.getUserId(), documentId)
                .orElseThrow(() -> new ResourceNotFoundException("file not found." +
                        "exception was thrown in the previewDocument method"));
        document.setLastAccessedAt(new Date());
        repo.save(document);
        return creator.previewAndDownloadDocument(headerType, document);
    }

    public SearchResults searchDocumentsAndWorkspaces(String searchName, int pageNumber, int pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        String sanitisedName = searchName.contains(".") ? searchName.replace(".", "_") : searchName;

        // Search in directories
        Page<Directories> documentResults = directoriesService.repo.findByName(directoriesService.getUserId(), sanitisedName, pageable);
        // Search in workspaces
        Page<Workspaces> workspaceResults = directoriesService.workspaceService.repo.findByUserIdAndNameAndDeletedFalse(
                directoriesService.getUserId(), searchName, pageable);

        Page<DirectoryDTO> documentResultsDTO = documentResults.map(directoriesService.directoriesMapper::directoryToDirectoryDTO);
        Page<WorkspacesDTO> workspaceResultsDTO = workspaceResults.map(directoriesService.workspaceService.workspacesMapper::workspaceWorkspacesDTO);

        // Return combined results in a custom response object
        return new SearchResults(documentResultsDTO, workspaceResultsDTO);
    }

    //TODO: create a whole new function dedicated to searching documents only by name and by tag.
    //TODO: create a function that returns a page of document dto

}
